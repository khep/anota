package pt.inesc.id.l2f.annotation.tool.execution;

import java.io.IOException;

import java.net.ServerSocket;

import java.util.Arrays;

import com.facebook.thrift.transport.TSocket;
import com.facebook.thrift.transport.TTransport;
import com.facebook.thrift.transport.TTransportException;
import com.facebook.thrift.protocol.TBinaryProtocol;
import com.facebook.thrift.protocol.TProtocol;

import pt.inesc.id.l2f.annotation.tool.Tool;
import pt.inesc.id.l2f.annotation.unit.InputDocumentProcessUnit;
import pt.inesc.id.l2f.annotation.unit.LinguisticAnnotationProcessUnit;

public abstract class ThriftExecutionMode extends ToolExecutionMode {
	// ...
	protected TTransport _transport;
	// ...
	protected TProtocol _protocol;
	// ...
	protected int _port;
	// ...
	protected String _hostname;
	// ...
	protected ProcessBuilder _processBuilder;
	// ...
	protected Process _process;
	// ...
	protected String[] _command;
	// ...
	protected String[][] _environment;
	
	public ThriftExecutionMode(String hostname, int port) {
		_hostname = hostname;
		_port = port;
	}
	
	public ThriftExecutionMode(String[] command, int port, String[][] environment) {
		_hostname = "localhost";
		_port = port;
		_command = command;
		_environment = environment;
	}
	
	public ThriftExecutionMode(Tool tool, String hostname, int port) {
		super(tool);
	
		_hostname = hostname;
		_port = port;
	}
	
	public abstract void createClient();
	
	@Override
	public void init() {
		
		if (_command != null) {
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
	}
	
	@Override
	@SuppressWarnings("static-access")
	public void start() {
		
		if (_command != null) {
			try {
				_process = _processBuilder.start();
				
				
				
//				Thread t = new Thread(this);
//				t.start();
//				
				
				
				// wait for server initialization
				Thread.currentThread().sleep(2000);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		try {
			_transport = new TSocket(_hostname, _port);
			_protocol = new TBinaryProtocol(_transport);
			_transport.open();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
		
		this.createClient();
	}
	
//	@Override
//	public void run() {
//		BufferedReader br1 = new BufferedReader(new InputStreamReader(_process.getErrorStream()));
//		BufferedReader br2 = new BufferedReader(new InputStreamReader(_process.getInputStream()));
//		
//		String line = "";
//		
//		try {
//			while ((line = br1.readLine()) != null) {
//				System.out.println("DEBUG (Error): " + line);
//			}
//			
////			while ((line = br2.readLine()) != null) {
////				System.out.println("DEBUG (Input): " + line);
////			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	@Override
	public void input(Tool tool, InputDocumentProcessUnit unit) {
		unit.execute(tool, this.getOutput(unit));
	}

	@Override
	public void input(Tool tool, LinguisticAnnotationProcessUnit unit) {
		unit.execute(tool, this.getOutput(unit));
	}

//	@Override
//	public void input(Tool tool, SyntacticProcessUnit unit) {
//		unit.execute(tool, this.getOutput(unit));
//	}
	
	public abstract ToolExecutionModeUnit getOutput(InputDocumentProcessUnit unit);

	public abstract ToolExecutionModeUnit getOutput(LinguisticAnnotationProcessUnit unit);

//	public abstract ToolExecutionModeUnit getOutput(SyntacticProcessUnit unit);

	@Override
	public void close() {
		_transport.close();
		
		if (_process != null) {
			_process.destroy();
		}
	}

	/**
	 * @return the transport
	 */
	public TTransport getTransport() {
		return _transport;
	}

	/**
	 * @return the protocol
	 */
	public TProtocol getProtocol() {
		return _protocol;
	}
	
	public static int findFreePort() {
		int port = -1;
		
		try {
			ServerSocket server; server = new ServerSocket(0);
			
			port = server.getLocalPort();
			
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return port;
	}
}
