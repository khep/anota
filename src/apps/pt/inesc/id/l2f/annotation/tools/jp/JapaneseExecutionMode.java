package pt.inesc.id.l2f.annotation.tools.jp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import pt.inesc.id.l2f.annotation.input.TextElement;
import pt.inesc.id.l2f.annotation.tool.execution.ToolExecutionModeUnit;
import pt.inesc.id.l2f.annotation.tool.execution.ExternalProcessExecutionMode;
import pt.inesc.id.l2f.annotation.unit.InputDocumentProcessUnit;
import pt.inesc.id.l2f.annotation.unit.LinguisticAnnotationProcessUnit;

/**
 * 
 * 
 * @author Tiago Luis
 *
 */
public class JapaneseExecutionMode extends ExternalProcessExecutionMode {

	public JapaneseExecutionMode(String[] command, String[][] environment, String charset) {
		super(command, environment, charset);
	}

	@Override
	public ToolExecutionModeUnit setInput(InputDocumentProcessUnit unit) {
		List<String> input = new ArrayList<String>();

		TextElement node = null;

		while ((node = unit.getInputDocument().next()) != null) {
//			String id = "none";
			String text = node.getText();

			if (text.matches("\\s+")) {
				continue;
			}

			input.add(text);
		}

		return new ToolExecutionModeUnit(input, unit);
	}

	@Override
	public ToolExecutionModeUnit setInput(LinguisticAnnotationProcessUnit unit) {
		return null;
	}

//	@Override
//	public ToolExecutionModeUnit setInput(SyntacticProcessUnit unit) {
//		return null;
//	}

	@Override
	public ToolExecutionModeUnit setOutput(ToolExecutionModeUnit unit, InputStream is, Reader reader) {
		BufferedReader br = new BufferedReader(reader);
		List<String> output = new ArrayList<String>();

		try {
			String line = new String();

			while (true) {
				line = br.readLine();

				if (line.equals("EOS")) {
					break;
				}

				output.add(line);
			}

			unit.setOutput(output);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return unit;
	}
}
