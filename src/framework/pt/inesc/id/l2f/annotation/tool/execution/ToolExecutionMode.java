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
public abstract class ToolExecutionMode extends Thread {
	// ...
	protected Tool _tool;
	
	public ToolExecutionMode() {}
	
	public ToolExecutionMode(Tool tool) {
		_tool = tool;
	}
	
	/**
	 * 
	 * 
	 * @param tool
	 * @param unit
	 */
	public abstract void input(Tool tool, InputDocumentProcessUnit unit);
	
	/**
	 * 
	 * 
	 * @param tool
	 * @param unit
	 */
	public abstract void input(Tool tool, LinguisticAnnotationProcessUnit unit);
	
//	/**
//	 * 
//	 * 
//	 * @param tool
//	 * @param unit
//	 */
//	public abstract void input(Tool tool, SyntacticProcessUnit unit);
	
	/**
	 * 
	 * 
	 */
	public abstract void init();
	
	/**
	 * 
	 * 
	 */
	public abstract void start();
	
	/**
	 * 
	 * 
	 */
	public abstract void close();

	/**
	 * 
	 * 
	 * @return
	 */
	public Tool getTool() {
		return _tool;
	}

	/**
	 * 
	 * 
	 * @param tool
	 */
	public void setTool(Tool tool) {
		_tool = tool;
	}
}
