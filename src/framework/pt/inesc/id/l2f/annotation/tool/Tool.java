package pt.inesc.id.l2f.annotation.tool;

import pt.inesc.id.l2f.annotation.tool.execution.ToolExecutionMode;
import pt.inesc.id.l2f.annotation.tool.execution.ToolExecutionModeUnit;
import pt.inesc.id.l2f.annotation.unit.InputDocumentProcessUnit;
import pt.inesc.id.l2f.annotation.unit.LinguisticAnnotationProcessUnit;
import pt.inesc.id.l2f.annotation.stage.Stage;

/**
 * 
 * 
 * @author Tiago Luis
 *
 */
public abstract class Tool implements ToolVisitor {
	// ...
	protected Stage _stage;
	// ...
	protected ToolExecutionMode _mode;
	
	public Tool(ToolExecutionMode mode) {
		_mode = mode;
	}
	
	/**
	 * 
	 * 
	 * @param unit
	 * @param result
	 */
	public abstract void process(InputDocumentProcessUnit unit, ToolExecutionModeUnit result);
	
	/**
	 * 
	 * 
	 * @param unit
	 * @param result
	 */
	public abstract void process(LinguisticAnnotationProcessUnit unit, ToolExecutionModeUnit result);
	
	/**
	 * 
	 */
	public void init() {
		_mode.init();
	}
	
	/**
	 * 
	 */
	public void start() {
		_mode.close();
	}
	
	/**
	 * 
	 */
	public void close() {
		_mode.close();
	}
	
	/**
	 * 
	 * 
	 * @return the stage
	 */
	public Stage getStage() {
		return _stage;
	}

	/**
	 * 
	 * 
	 * @param stage the stage to set
	 */
	public void setStage(Stage stage) {
		_stage = stage;
	}

	/**
	 * 
	 * 
	 * @return execution mode of tool
	 */
	public ToolExecutionMode getExecutionMode() {
		return _mode;
	}

	/**
	 * 
	 * 
	 * @param mode execution mode of tool
	 */
	public void setExecutionMode(ToolExecutionMode mode) {
		_mode = mode;
	}
}
