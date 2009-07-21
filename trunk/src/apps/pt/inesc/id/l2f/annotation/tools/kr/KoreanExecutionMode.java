package pt.inesc.id.l2f.annotation.tools.kr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import pt.inesc.id.l2f.annotation.tool.execution.ToolExecutionModeUnit;
import pt.inesc.id.l2f.annotation.tool.execution.ExternalProcessExecutionMode;
import pt.inesc.id.l2f.annotation.unit.InputDocumentProcessUnit;
import pt.inesc.id.l2f.annotation.unit.LinguisticAnnotationProcessUnit;

/**
 * 
 * 
 * @author Tiago Luis
 *
 * @param <Unit>
 */
public class KoreanExecutionMode extends ExternalProcessExecutionMode {

	public KoreanExecutionMode(String[] command, String[][] environment, String charset) {
		super(command, environment, charset);
	}
	
	public void process(BufferedReader br, ToolExecutionModeUnit unit) {
		
		try {
			System.out.println("DEBUG: " + br.readLine() + " " + unit.getInput().get(0));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ToolExecutionModeUnit setInput(InputDocumentProcessUnit unit) {
		return null;
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
		return null;
	}
}
