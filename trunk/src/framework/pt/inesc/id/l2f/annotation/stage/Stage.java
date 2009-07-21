package pt.inesc.id.l2f.annotation.stage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import pt.inesc.id.l2f.annotation.tool.Tool;
import pt.inesc.id.l2f.annotation.unit.ProcessUnit;

/**
 * 
 * 
 * @author Tiago Luis
 *
 */
public  class Stage implements Runnable {
	// ...
	protected Tool _tool;
	// ...
	protected BlockingQueue<ProcessUnit> _input;
	// ...
	protected BlockingQueue<ProcessUnit> _output;

	public Stage(Tool tool) {
		_tool = tool;

		_input = new LinkedBlockingQueue<ProcessUnit>(100);
		_output = new LinkedBlockingQueue<ProcessUnit>(100);

		// set current stage
		_tool.setStage(this);
	}

	public Stage(BlockingQueue<ProcessUnit> input, BlockingQueue<ProcessUnit> output, Tool tool) {
		_input = input;
		_output = output;
		_tool = tool;

		// set current stage
		_tool.setStage(this);
	}

	/**
	 * 
	 */
	public void configure() {}

	public void run() {

		try {
			while (true) {
				ProcessUnit unit = _input.take();

				// stop if it finds a null value
				if (unit.isLast()) {

					_tool.close();

					_output.put(unit);

					break;
				}

				// process unit
				unit.accept(_tool);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Passes the unit (to be processed) to input queue.
	 * 
	 * @param unit
	 */
	public void process(ProcessUnit unit) {

		try {
			_input.put(unit);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Passes the processed unit to next stage.
	 * 
	 * @param unit the unit to pass to next stage (after being processed)
	 * @throws InterruptedException
	 */
	public void collect(ProcessUnit unit) {
		try {
			_output.put(unit);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	public void close() {}

	/**
	 * 
	 * @return the tool
	 */
	public Tool getTool() {
		return _tool;
	}

	/**
	 * 
	 * @param tool the tool to set
	 */
	public void setTool(Tool tool) {
		_tool = tool;
	}

	/**
	 * 
	 * @return the input queue
	 */
	public BlockingQueue<ProcessUnit> getInputQueue() {
		return _input;
	}

	/**
	 * 
	 * @param input the input queue to set
	 */
	public void setInputQueue(BlockingQueue<ProcessUnit> input) {
		_input = input;
	}

	/**
	 * 
	 * @return the output queue
	 */
	public BlockingQueue<ProcessUnit> getOutputQueue() {
		return _output;
	}

	/**
	 * 
	 * @param output the output queue to set
	 */
	public void setOutputQueue(BlockingQueue<ProcessUnit> output) {
		_output = output;
	}
}
