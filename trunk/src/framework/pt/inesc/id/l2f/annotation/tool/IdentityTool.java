package pt.inesc.id.l2f.annotation.tool;

import pt.inesc.id.l2f.annotation.tool.execution.JavaExecutionMode;
import pt.inesc.id.l2f.annotation.tool.execution.ToolExecutionModeUnit;
import pt.inesc.id.l2f.annotation.unit.InputDocumentProcessUnit;
import pt.inesc.id.l2f.annotation.unit.LinguisticAnnotationProcessUnit;

public class IdentityTool extends Tool {

	public IdentityTool() {
		super(new JavaExecutionMode());
	}

	@Override
	public void start() {

	}
	
	public void visit(InputDocumentProcessUnit unit) {
		_mode.input(this, unit);
	}

	public void visit(LinguisticAnnotationProcessUnit unit) {
		_mode.input(this, unit);
	}

//	public void visit(SyntacticProcessUnit unit) {
//		_mode.input(this, unit);
//	}
	
	@Override
	public void process(InputDocumentProcessUnit unit, ToolExecutionModeUnit result) {}

	@Override
	public void process(LinguisticAnnotationProcessUnit unit, ToolExecutionModeUnit result) {}

//	@Override
//	public void process(SyntacticProcessUnit unit, ToolExecutionModeUnit result) {}
	
	@Override
	public void close() {}
}
