package pt.inesc.id.l2f.annotation.tool.execution;

import java.util.List;

import pt.inesc.id.l2f.annotation.unit.ProcessUnit;

/**
 * 
 * 
 * @author Tiago Luis
 *
 */
public class ToolExecutionModeUnit extends Thread {
	// ...
	protected List<String> _input;
	// ...
	protected List<String> _output;
	// ...
	protected ProcessUnit _unit;
	// ...
	private boolean _last;
	
	public ToolExecutionModeUnit(boolean last) {
		_last = last;
	}
	
	public ToolExecutionModeUnit(List<String> input, ProcessUnit unit) {
		_input = input;
		_unit = unit;
		_last = false;
	}
	
	/**
	 * @return the input
	 */
	public List<String> getInput() {
		return _input;
	}

	/**
	 * @param input the input to set
	 */
	public void setInput(List<String> input) {
		_input = input;
	}

	/**
	 * @return the unit
	 */
	public ProcessUnit getUnit() {
		return _unit;
	}

	/**
	 * 
	 * 
	 * @param unit the unit to set
	 */
	public void setUnit(ProcessUnit unit) {
		_unit = unit;
	}

	/**
	 * 
	 * 
	 * @return the output
	 */
	public List<String> getOutput() {
		return _output;
	}

	/**
	 * 
	 * 
	 * @param output the output to set
	 */
	public void setOutput(List<String> output) {
		_output = output;
	}

	/**
	 * 
	 * @return true if it is the last unit
	 */
	public boolean isLast() {
		return _last;
	}

	/**
	 * 
	 * @param last the boolean value to set
	 */
	public void setLast(boolean last) {
		_last = last;
	}
}
