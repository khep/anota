package pt.inesc.id.l2f.annotation.tool.execution;

import pt.inesc.id.l2f.annotation.tool.Tool;
import pt.inesc.id.l2f.annotation.unit.InputDocumentProcessUnit;
import pt.inesc.id.l2f.annotation.unit.LinguisticAnnotationProcessUnit;

/**
 * 
 * 
 * @author Tiago Luis
 *
 */
public class JavaExecutionMode extends ToolExecutionMode {

	public JavaExecutionMode() {}
	
	public JavaExecutionMode(Tool tool) {
		super(tool);
	}

	@Override
	public void init() {}

	@Override
	public void start() {}
	
	@Override
	public void input(Tool tool, InputDocumentProcessUnit unit) {
		unit.execute(tool, null);
	}

	@Override
	public void input(Tool tool, LinguisticAnnotationProcessUnit unit) {
		unit.execute(tool, null);
	}
	
	@Override
	public void close() {}
}
