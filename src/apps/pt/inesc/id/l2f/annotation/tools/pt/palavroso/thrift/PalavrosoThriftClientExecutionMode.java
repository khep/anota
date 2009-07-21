package pt.inesc.id.l2f.annotation.tools.pt.palavroso.thrift;

import java.util.ArrayList;
import java.util.List;

import com.facebook.thrift.TException;

import pt.inesc.id.l2f.annotation.tool.execution.ThriftExecutionMode;
import pt.inesc.id.l2f.annotation.tool.execution.ToolExecutionModeUnit;
import pt.inesc.id.l2f.annotation.tools.pt.palavroso.thrift.gen.PalavrosoServer;
import pt.inesc.id.l2f.annotation.tools.pt.palavroso.thrift.gen.PalavrosoServer.Client;
import pt.inesc.id.l2f.annotation.unit.InputDocumentProcessUnit;
import pt.inesc.id.l2f.annotation.unit.LinguisticAnnotationProcessUnit;
import pt.inesc.id.l2f.annotation.input.InputDocument;
import pt.inesc.id.l2f.annotation.input.TextElement;

public class PalavrosoThriftClientExecutionMode extends ThriftExecutionMode {
	// ...
	private static final String COMMAND = "/usr/share/split/palavroso/thrift/bin/server";
	// ...
	private static final String[][] ENVIRONMENT = {};
	// ...
	private Client _client;

	public PalavrosoThriftClientExecutionMode(String hostname, int port) {
		super(hostname, port);
	}

	public PalavrosoThriftClientExecutionMode(int port) {
		super(new String[] { COMMAND, String.valueOf(port) }, port, ENVIRONMENT);
	}

	@Override
	public void createClient() {
		_client = new PalavrosoServer.Client(_protocol);
	}
	
	@Override
	public ToolExecutionModeUnit getOutput(InputDocumentProcessUnit unit) {
		List<String> input = new ArrayList<String>();
		List<String> output = new ArrayList<String>();

		InputDocument document = unit.getInputDocument();
		TextElement node = null;

		while ((node = document.next()) != null) {
			try {
				String o = _client.process(node.getText());
				output.add(o);
			} catch (TException e) {
				e.printStackTrace();
			}
		}

		ToolExecutionModeUnit eunit = new ToolExecutionModeUnit(input, unit);
		eunit.setOutput(output);

		return eunit;
	}

	@Override
	public ToolExecutionModeUnit getOutput(LinguisticAnnotationProcessUnit unit) {
//		List<String> input = new ArrayList<String>();
//		List<Token> tokens = new ArrayList<Token>();
//		List<String> output = new ArrayList<String>();
//
//		for (Token token : unit.get_odoc().getTokens()) {
//			input.add(token.getWord() + "\n");
//
//			tokens.add(token);
//		}
//
//		ToolExecutionModeUnit eunit = new PalavrosoExecutionModeUnit(input, unit, tokens);
//
//		try {
//			String i = "";
//			for (String s : eunit.getInput()) {
//				i += s + " ";
//			}
//
//			String o = _client.process(i);
//
//			String[] lines = o.split("\n");
//
//			for (String line : lines) {
//				output.add(line);
//			}
//		} catch (TException e) {
//			e.printStackTrace();
//		}
//
//		eunit.setOutput(output);
//
//		return eunit;
		
		return null;
	}
}
