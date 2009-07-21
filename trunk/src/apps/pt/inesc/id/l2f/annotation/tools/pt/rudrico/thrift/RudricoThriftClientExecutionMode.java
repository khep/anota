package pt.inesc.id.l2f.annotation.tools.pt.rudrico.thrift;

import java.io.StringWriter;

import java.util.ArrayList;
import java.util.List;

import com.facebook.thrift.TException;

import pt.inesc.id.l2f.annotation.tool.execution.ThriftExecutionMode;
import pt.inesc.id.l2f.annotation.tool.execution.ToolExecutionModeUnit;
import pt.inesc.id.l2f.annotation.tools.pt.rudrico.thrift.gen.RudricoServer;
import pt.inesc.id.l2f.annotation.tools.pt.rudrico.thrift.gen.RudricoServer.Client;
import pt.inesc.id.l2f.annotation.tools.pt.rudrico.xml.AnnotationDocument;
import pt.inesc.id.l2f.annotation.tools.pt.rudrico.xml.Class;
import pt.inesc.id.l2f.annotation.tools.pt.rudrico.xml.Id;
import pt.inesc.id.l2f.annotation.tools.pt.rudrico.xml.Sentence;
import pt.inesc.id.l2f.annotation.tools.pt.rudrico.xml.Word;
import pt.inesc.id.l2f.annotation.unit.InputDocumentProcessUnit;
import pt.inesc.id.l2f.annotation.unit.LinguisticAnnotationProcessUnit;
import pt.inesc.id.l2f.annotation.document.laf.Classification;
import pt.inesc.id.l2f.annotation.document.laf.Feature;
import pt.inesc.id.l2f.annotation.document.laf.FeatureStructure;
import pt.inesc.id.l2f.annotation.document.laf.Segment;
import pt.inesc.id.l2f.annotation.document.xml.XMLWriter;

public class RudricoThriftClientExecutionMode extends ThriftExecutionMode {
	// ...
	private static final String COMMAND = "/usr/share/split/rudrico/thrift/bin/server";
	// ...
	private static final String[][] ENVIRONMENT = {};
	// ...
	private Client _client;
	
	public RudricoThriftClientExecutionMode(int port) {
		super(new String[] { COMMAND, String.valueOf(port) }, port, ENVIRONMENT);
	}
	
	public RudricoThriftClientExecutionMode(String hostname, int port) {
		super(hostname, port);
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
		
		try {
			for (String s : eunit.getInput()) {
				String o = _client.process(s);
				
				output.add(o);
			}
		} catch (TException e) {
			e.printStackTrace();
		}
	
		eunit.setOutput(output);
		
		return eunit;
	}

	@Override
	public void createClient() {
		_client = new RudricoServer.Client(_protocol);
//		
//		Thread t = new Thread(this);
//		t.start();
	}
}
