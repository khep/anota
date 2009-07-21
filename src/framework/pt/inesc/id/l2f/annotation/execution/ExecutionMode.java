package pt.inesc.id.l2f.annotation.execution;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import pt.inesc.id.l2f.annotation.document.laf.LinguisticAnnotationDocument;
import pt.inesc.id.l2f.annotation.input.InputDocument;
import pt.inesc.id.l2f.annotation.stage.Stage;
import pt.inesc.id.l2f.annotation.tool.Tool;
import pt.inesc.id.l2f.annotation.unit.FinalProcessUnit;
import pt.inesc.id.l2f.annotation.unit.LinguisticAnnotationProcessUnit;

public abstract class ExecutionMode {
	protected LinkedList<Stage> _stages;
	
	protected LinkedList<Thread> _threads;
	
	public ExecutionMode() {
		_stages = new LinkedList<Stage>();
		_threads = new LinkedList<Thread>();
	}
	
	public ExecutionMode(Stage ... stages) {
		this();
		
		for (Stage stage : stages) {
			_stages.add(stage);
			_threads.add(new Thread(stage));
		}
	}
	
	public ExecutionMode(List<Stage> stages) {
		this();
		
		for (Stage stage : stages) {
			_stages.add(stage);
			_threads.add(new Thread(stage));
		}
	}
	
	public ExecutionMode(Tool  ... tools) {
		this();
		
		for (Tool tool : tools) {
			Stage stage = new Stage(tool);
			
			_stages.add(stage);
			_threads.add(new Thread(stage));
		}
	}
	
	/**
	 * 
	 * 
	 */
	public void start() {
		
		// verify number of stages
		if (_stages.size() < 1) {
			System.err.println("...");
			return;
		}

		for (int i = 0; i < _stages.size() - 1; i++) {
			Stage current = _stages.get(i);
			Stage next = _stages.get(i + 1);

			// link stages
			next.setInputQueue(current.getOutputQueue());
		}

		for (Stage stage : _stages) {
			stage.getTool().init();
			stage.getTool().start();
		}

		for (Thread thread : _threads) {
			thread.start();
		}
	}
	
	// TODO: dar um nome melhor a Text
	public abstract void annotateText(Text input, Path output);
	
	public abstract void annotateText(List<Text> input, Path output);
	
	public abstract void annotateFile(File input, Path output);
	
	public abstract void annotateFile(List<File> input, Path output);
	
	// TODO: mudar nome
	public abstract void annotateText(Directory input, Path output);
	
	public abstract void annotate(List<LinguisticAnnotationDocument> annotations, Path output);
	
	public abstract void annotate(LinguisticAnnotationProcessUnit annotation, Path output);
	
	public abstract void annotateInputDocuments(List<InputDocument> documents, Path output);
	
	/**
	 * 
	 * 
	 */
	public void close() {
		
		try {
			Stage first = _stages.getFirst();

			// add final unit
			first.getInputQueue().put(new FinalProcessUnit());

			for (Thread thread : _threads) {
				thread.join();
			}

			for (Stage stage : _stages) {
				stage.getTool().close();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the stages
	 */
	public List<Stage> getStages() {
		return Collections.unmodifiableList(_stages);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public Stage getFirstStage() {
		return _stages.getFirst();
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public Stage getFinalStage() {
		return _stages.getLast();
	}

	/**
	 * @param stages the stages to set
	 */
	public void setStages(LinkedList<Stage> stages) {
		_stages = stages;
	}
	
	public void addFinalStage(Stage stage) {
		Stage last = _stages.getLast();
		
		stage.setInputQueue(last.getOutputQueue());
		
		_stages.addLast(stage);
		_threads.addLast(new Thread(stage));
	}

	/**
	 * @return the threads
	 */
	public List<Thread> getThreads() {
		return Collections.unmodifiableList(_threads);
	}

	/**
	 * @param threads the threads to set
	 */
	public void setThreads(LinkedList<Thread> threads) {
		_threads = threads;
	}
}
