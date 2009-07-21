package pt.inesc.id.l2f.annotation.tool.execution;

import pt.inesc.id.l2f.annotation.tool.Tool;
import pt.inesc.id.l2f.annotation.unit.InputDocumentProcessUnit;
import pt.inesc.id.l2f.annotation.unit.LinguisticAnnotationProcessUnit;

public abstract class JNIExecutionMode extends ToolExecutionMode {
	
	public JNIExecutionMode() {}
	
	@Override
	public void start() {}
	
	@Override
	public void input(Tool tool, InputDocumentProcessUnit unit) {
		unit.execute(tool, this.getOutput(unit));
	}

	@Override
	public void input(Tool tool, LinguisticAnnotationProcessUnit unit) {
		unit.execute(tool, this.getOutput(unit));
	}

	public abstract ToolExecutionModeUnit getOutput(InputDocumentProcessUnit unit);

	public abstract ToolExecutionModeUnit getOutput(LinguisticAnnotationProcessUnit unit);

	@Override
	public void close() {}
}
