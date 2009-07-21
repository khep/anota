package pt.inesc.id.l2f.annotation.execution;

import pt.inesc.id.l2f.annotation.stage.Stage;
import pt.inesc.id.l2f.annotation.tool.Tool;

public abstract class ParallelExecutionMode extends ExecutionMode {
	
	public ParallelExecutionMode(Stage ... stages) {
		super(stages);
	}
	
	public ParallelExecutionMode(Tool ... tools) {
		super(tools);
	}
}
