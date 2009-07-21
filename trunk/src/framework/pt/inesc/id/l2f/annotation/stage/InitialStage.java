package pt.inesc.id.l2f.annotation.stage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import pt.inesc.id.l2f.annotation.unit.FinalProcessUnit;
import pt.inesc.id.l2f.annotation.unit.ProcessUnit;

public class InitialStage extends Thread {
	private BlockingQueue<ProcessUnit> _in;

	private BlockingQueue<ProcessUnit> _out;

	public InitialStage(BlockingQueue<ProcessUnit> out) {
		_out = out;

		_in = new LinkedBlockingQueue<ProcessUnit>();
	}

	public void input(ProcessUnit input) {
		try {
			_in.put(input);
		} catch (InterruptedException e) {
			// TODO lançar uma excepção
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			while (true) {
				ProcessUnit s = _in.take();
				
				// copy element to consumers
				_out.put(s);
				
				// stop if finds a null value
				if (s instanceof FinalProcessUnit) {
					break;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			_in.put(new FinalProcessUnit());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
