package pt.inesc.id.l2f.annotation.tools.pt.palavroso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import java.util.ArrayList;
import java.util.List;

import pt.inesc.id.l2f.annotation.input.InputDocument;
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
public class PalavrosoExecutionMode extends ExternalProcessExecutionMode {

	public PalavrosoExecutionMode(String[] command, String[][] environment, String charset) {
		super(command, environment, charset);
	}
	
	@Override
	public ToolExecutionModeUnit setInput(InputDocumentProcessUnit unit) {
		List<String> input = new ArrayList<String>();
		
		InputDocument document = unit.getInputDocument();
		
		TextElement node;
		
		while ((node = document.next()) != null) {
			input.add(node.getText());
		}
		
		input.add("\nKKAAPPUUTT\n");
		
		return new ToolExecutionModeUnit(input, unit);
	}

	@Override
	public ToolExecutionModeUnit setInput(LinguisticAnnotationProcessUnit unit) {
		// TODO: lançar excepção
		return null;
	}
	
	@Override
	public ToolExecutionModeUnit setOutput(ToolExecutionModeUnit unit, InputStream is, Reader reader) {
		List<String> output = new ArrayList<String>();
		BufferedReader br = (BufferedReader) reader;
		
		try {
			String line = "";
				
			while (true) {
				line = br.readLine();
				
				if (line == null || line.startsWith("KKAAPPUUTT")) {
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
