package pt.inesc.id.l2f.annotation.tool.execution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import pt.inesc.id.l2f.annotation.input.TextDocument;
import pt.inesc.id.l2f.annotation.input.TextElement;
import pt.inesc.id.l2f.annotation.tool.Tool;
import pt.inesc.id.l2f.annotation.unit.InputDocumentProcessUnit;
import pt.inesc.id.l2f.annotation.unit.LinguisticAnnotationProcessUnit;

/**
 * 
 * 
 * @author Tiago Luis
 *
 */
public abstract class ExternalProcessExecutionMode extends ToolExecutionMode {
	// ...
	private ProcessBuilder _processBuilder;
	// ...
	protected Thread _outputCollectorThread;
	// ...
	protected OutputStreamWriter _osr;
	// ...
	protected Process _process;
	// ...
	protected String[] _command;
	// ...
	protected String[][] _environment;
	// ...
	protected String _charset;
	// ...
	protected BlockingQueue<ToolExecutionModeUnit> _in = new LinkedBlockingQueue<ToolExecutionModeUnit>();

	public ExternalProcessExecutionMode(String[] command, String[][] environment, String charset) {
		_command = command;
		_environment = environment;
		_charset = charset;
	}

	/**
	 * 
	 * 
	 */
	public void init() {
		// create process command with command
		_processBuilder = new ProcessBuilder(Arrays.asList(_command));

		if (_environment != null) {
			// set environment
			for (String[] pair : _environment) {
				String key = pair[0];
				String value = pair[1];

				_processBuilder.environment().put(key, value);
			}
		}
	}

	/**
	 * 
	 * 
	 */
	public void start() {
		try {
			_process = _processBuilder.start();

			// get process output stream writer
			_osr = new OutputStreamWriter(_process.getOutputStream(), _charset);

			// start output collector
			_outputCollectorThread = new Thread(this);

			_outputCollectorThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		
		try {
			// TODO: ver isto
			BufferedReader br = new BufferedReader(new InputStreamReader(_process.getInputStream(), _charset));
			InputStream is = _process.getInputStream();
			
			while (true) {
				ToolExecutionModeUnit unit = _in.take();

				// stop if it finds final unit
				if (unit.isLast()) {
					break;
				}
				
				// get process output
				this.setOutput(unit, is, br);
				
				unit.getUnit().execute(_tool, unit);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void input(Tool tool, InputDocumentProcessUnit unit) {
		
		try {
			TextElement node = null;
			
			while ((node = unit.getInputDocument().next()) != null) {
				List<String> list = new ArrayList<String>();
				
				list.add(node.getText());
				
				// TODO: parameterizar
				for (int i = 0; i < 0; i++) {
					node = unit.getInputDocument().next();
					
					if (node != null) {
						list.add(node.getText());
					} else {
						break;
					}
				}
				
				TextDocument doc = new TextDocument(list);
				
				ToolExecutionModeUnit eunit = this.setInput(new InputDocumentProcessUnit(doc));
			
				for (String input : eunit.getInput()) {
					_osr.write(input);
				}
			
				_in.put(eunit);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void input(Tool tool, LinguisticAnnotationProcessUnit unit) {
		
		try {
			ToolExecutionModeUnit eunit = this.setInput(unit);
			
			for (String input : eunit.getInput()) {
				_osr.write(input);
			}
			
			_in.put(eunit);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 
	 * @param unit
	 * @return
	 */
	public abstract ToolExecutionModeUnit setInput(InputDocumentProcessUnit unit);

	/**
	 * 
	 * 
	 * @param unit
	 * @return
	 */
	public abstract ToolExecutionModeUnit setInput(LinguisticAnnotationProcessUnit unit);

	/**
	 * 
	 * 
	 * @param unit
	 * @param is
	 * @param reader
	 * @return
	 */
	public abstract ToolExecutionModeUnit setOutput(ToolExecutionModeUnit unit, InputStream is, Reader reader);
	
	/**
	 * 
	 * 
	 */
	public void close() {
		try {
			_osr.close();

			_in.put(new ToolExecutionModeUnit(true));

			_outputCollectorThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public boolean isRunning() {

		try {
			// try to get process exit value
			_process.exitValue();

			// the process is running
			return false;
		} catch (IllegalThreadStateException itse) {
			return true;
		}
	}


	/**
	 * @return the process
	 */
	public Process getProcess() {
		return _process;
	}
}
