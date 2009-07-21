package pt.inesc.id.l2f.annotation.unit;

import org.apache.hadoop.io.Writable;

import pt.inesc.id.l2f.annotation.document.xml.XMLReader;
import pt.inesc.id.l2f.annotation.document.xml.XMLWriter;
import pt.inesc.id.l2f.annotation.tool.Tool;
import pt.inesc.id.l2f.annotation.tool.ToolVisitor;
import pt.inesc.id.l2f.annotation.tool.execution.ToolExecutionModeUnit;

/**
 * 
 * 
 * @author Tiago Luis
 *
 */
public abstract class ProcessUnit implements Writable {
	// true if this is the last unit
	private boolean _last;
	
	public ProcessUnit() {
		_last = false;
	}
	
	public ProcessUnit(boolean last) {
		_last = last;
	}
	
	/**
	 * 
	 * 
	 * @param visitor
	 * @return
	 */
	public abstract void accept(ToolVisitor visitor);
	
	/**
	 * 
	 * 
	 * @param visitor
	 */
	public abstract void execute(Tool tool, ToolExecutionModeUnit unit);
	
	/**
	 * 
	 * 
	 * @param xmlw
	 */
	public abstract void writeTo(XMLWriter xmlw);
	
	/**
	 * 
	 * 
	 * @param xmlr
	 */
	public abstract void readFrom(XMLReader xmlr);
	
	/**
	 * 
	 * 
	 * @return true if this unit is the last one
	 */
	public boolean isLast() {
		return _last;
	}

	/**
	 *  
	 * 
	 */
	public void setLast() {
		_last = true;
	}
}
