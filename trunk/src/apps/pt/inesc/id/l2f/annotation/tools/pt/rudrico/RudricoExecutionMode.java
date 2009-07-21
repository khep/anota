package pt.inesc.id.l2f.annotation.tools.pt.rudrico;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;

import java.util.ArrayList;
import java.util.List;

import pt.inesc.id.l2f.annotation.document.laf.Classification;
import pt.inesc.id.l2f.annotation.document.laf.Feature;
import pt.inesc.id.l2f.annotation.document.laf.FeatureStructure;
import pt.inesc.id.l2f.annotation.document.laf.Segment;
import pt.inesc.id.l2f.annotation.document.xml.XMLWriter;
import pt.inesc.id.l2f.annotation.tool.execution.ToolExecutionModeUnit;
import pt.inesc.id.l2f.annotation.tool.execution.ExternalProcessExecutionMode;
import pt.inesc.id.l2f.annotation.tools.pt.rudrico.xml.AnnotationDocument;
import pt.inesc.id.l2f.annotation.tools.pt.rudrico.xml.Id;
import pt.inesc.id.l2f.annotation.tools.pt.rudrico.xml.Sentence;
import pt.inesc.id.l2f.annotation.tools.pt.rudrico.xml.Word;
import pt.inesc.id.l2f.annotation.tools.pt.rudrico.xml.Class;
import pt.inesc.id.l2f.annotation.unit.InputDocumentProcessUnit;
import pt.inesc.id.l2f.annotation.unit.LinguisticAnnotationProcessUnit;

/**
 * 
 * 
 * @author Tiago Luis
 *
 * @param <Unit>
 */
public class RudricoExecutionMode extends ExternalProcessExecutionMode {

	public RudricoExecutionMode(String[] command, String[][] environment, String charset) {
		super(command, environment, charset);
	}

	@Override
	public ToolExecutionModeUnit setInput(InputDocumentProcessUnit unit) {
		return null;
	}

	@Override
	public ToolExecutionModeUnit setInput(LinguisticAnnotationProcessUnit unit) {
		AnnotationDocument document = new AnnotationDocument();
		Sentence sentence = new Sentence();

//		List<WordFormAlternatives> wfAlternatives = unit.getDocument().getWordFormalternatives();

		for (Classification classification : unit.getDocument().getLastMorphoSyntacticAnnotation().getClassifications()) {
			Segment segment = unit.getDocument().getSegment(classification.getSegments().get(0));
			Word word = new Word(segment.getWord());

			for (FeatureStructure fs : classification.getFeatureStructures()) {
				Class c = new Class();

//				FeatureStructure fs = wordForm.getFeatureStructure();
				for (Feature f : fs.getFeatures().values()) {
					c.setRoot(segment.getWord().toLowerCase());

					c.addId(new Id(f.getName(), f.getValue()));
				}

				c.addId(new Id("LOW", segment.getFrom()));
				c.addId(new Id("HIG", segment.getTo()));

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

		return new ToolExecutionModeUnit(input, unit);
	}

//	@Override
//	public ToolExecutionModeUnit setInput(SyntacticProcessUnit unit) {
//	return null;
//	}

	@Override
	public ToolExecutionModeUnit setOutput(ToolExecutionModeUnit unit, InputStream is, Reader reader) {

		AnnotationDocument document = new AnnotationDocument();
		document.readFrom(is);

//		AnnotationXMLReader r = new AnnotationXMLReader(document);

//		InputSource inputSource = new InputSource(reader);
//		inputSource.setEncoding(_charset);

//		r.process(inputSource);

		StringWriter w = new StringWriter();
		XMLWriter xmlw = new XMLWriter(w);
		document.writeTo(xmlw);

		List<String> output = new ArrayList<String>();
		output.add(w.getBuffer().toString());

		unit.setOutput(output);

		return unit;
	}
}
