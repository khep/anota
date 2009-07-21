package pt.inesc.id.l2f.annotation.tools.pt.rudrico.jni;

import java.io.StringWriter;

import java.util.ArrayList;
import java.util.List;

import pt.inesc.id.l2f.annotation.document.laf.Classification;
import pt.inesc.id.l2f.annotation.document.laf.Feature;
import pt.inesc.id.l2f.annotation.document.laf.FeatureStructure;
import pt.inesc.id.l2f.annotation.document.laf.Segment;
import pt.inesc.id.l2f.annotation.document.xml.XMLWriter;
import pt.inesc.id.l2f.annotation.tool.execution.JNIExecutionMode;
import pt.inesc.id.l2f.annotation.tool.execution.ToolExecutionModeUnit;
import pt.inesc.id.l2f.annotation.tools.pt.rudrico.xml.AnnotationDocument;
import pt.inesc.id.l2f.annotation.tools.pt.rudrico.xml.Class;
import pt.inesc.id.l2f.annotation.tools.pt.rudrico.xml.Id;
import pt.inesc.id.l2f.annotation.tools.pt.rudrico.xml.Sentence;
import pt.inesc.id.l2f.annotation.tools.pt.rudrico.xml.Word;
import pt.inesc.id.l2f.annotation.unit.InputDocumentProcessUnit;
import pt.inesc.id.l2f.annotation.unit.LinguisticAnnotationProcessUnit;

public class RudricoJNIExecutionMode extends JNIExecutionMode {
	private native void initialize();
	private native String process(String input);
//	private native void release();
	
	static {
        System.loadLibrary("RudricoJNI");
	}
	
	public long _l;

	public RudricoJNIExecutionMode() {}
	
	@Override
	public void init() {
		// initialize
		this.initialize();
	}

	@Override
	public ToolExecutionModeUnit getOutput(InputDocumentProcessUnit unit) {
		return null;
	}

	@Override
	public ToolExecutionModeUnit getOutput(LinguisticAnnotationProcessUnit unit) {
		List<String> output = new ArrayList<String>();

		AnnotationDocument document = new AnnotationDocument();
		Sentence sentence = new Sentence();

		for (Classification classification : unit.getDocument().getLastMorphoSyntacticAnnotation().getClassifications()) {
			Segment segment = unit.getDocument().getSegment(classification.getSegments().get(0));
			Word word = new Word(segment.getWord());

			// TODO: update ao modo de execução command line
			for (FeatureStructure fs : classification.getFeatureStructures()) {
				Class c = new Class();

				for (Feature f : fs.getFeatures().values()) {

					if (f.getName().equals("POS")) {
						continue;
					}

					if (f.getName().equals("LEMMA")) {
						c.setRoot(f.getValue());
						continue;
					}

					c.addId(new Id(f.getName(), f.getValue()));
				}

				c.addId(new Id("LOW", segment.getFrom()));
				c.addId(new Id("HIG", segment.getTo()));

				if (segment.getWord().matches("^[A-ZÀÁÉÍÓÚÇÂÊÔÛÃÕ].*")) {
					c.addId(new Id("UPC", "true"));
				} else {
					c.addId(new Id("UPC", "false"));
				}

				word.addClass(c);
			}

			sentence.addWord(word);
		}

		document.addSentence(sentence);

		StringWriter w = new StringWriter();
		XMLWriter xmlw = new XMLWriter(w);
		document.writeTo(xmlw);

		List<String> input = new ArrayList<String>();
		input.add(w.getBuffer().toString());

		ToolExecutionModeUnit eunit = new ToolExecutionModeUnit(input, unit);

		for (String s : eunit.getInput()) {
			String o = this.process(s);

			output.add(o);
		}

		eunit.setOutput(output);

		return eunit;
	}
	
	@Override
	public void close() {
		super.close();
		
		// free resources
//		this.release();
	}
}
