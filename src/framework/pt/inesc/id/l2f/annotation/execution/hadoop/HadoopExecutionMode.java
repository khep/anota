package pt.inesc.id.l2f.annotation.execution.hadoop;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;

import pt.inesc.id.l2f.annotation.document.laf.LinguisticAnnotationDocument;
import pt.inesc.id.l2f.annotation.execution.Directory;
import pt.inesc.id.l2f.annotation.execution.File;
import pt.inesc.id.l2f.annotation.execution.LafFile;
import pt.inesc.id.l2f.annotation.execution.ParallelExecutionMode;
import pt.inesc.id.l2f.annotation.execution.Path;
import pt.inesc.id.l2f.annotation.execution.RawFile;
import pt.inesc.id.l2f.annotation.execution.TeiFile;
import pt.inesc.id.l2f.annotation.execution.Text;
import pt.inesc.id.l2f.annotation.input.InputDocument;
import pt.inesc.id.l2f.annotation.stage.Stage;
import pt.inesc.id.l2f.annotation.tool.Tool;
import pt.inesc.id.l2f.annotation.unit.LinguisticAnnotationProcessUnit;

public class HadoopExecutionMode extends ParallelExecutionMode {
	// ...
	private Configuration _conf;

	public HadoopExecutionMode(Stage ... stages) {
		super(stages);

		_conf = new Configuration();
	}

	public HadoopExecutionMode(Tool ... tools) {
		super(tools);

		_conf = new Configuration();
	}

	@Override
	public void start() {
//		_annotation.start();
	}

	@Override
	public void close() {
//		_annotation.close();
	}

	@Override
	public void annotateText(Text input, Path output) {
		
	}

	public void setMappers(int mappers) {
		_conf.setInt("split.framework.mappers", mappers);
	}

	public void setReducers(int reducers) {
		_conf.setInt("split.framework.reducers", reducers);
	}
	
	public void compressMapOutput() {
		_conf.setBoolean("split.framework.compress.map", true);
	}
	
	public void compressFinalOutput() {
		_conf.setBoolean("split.framework.compress.output", true);
	}
	
	public void addInputPath(String path) {
		String previous = _conf.get("split.framework.input", "");
		
		// add path
		_conf.set("split.framework.input", path + "\n" + previous);
	}
	
	public void addOutputPath(String path) {
		_conf.set("split.framework.output", path);
	}

	@Override
	public void annotateText(List<Text> input, Path output) {}

	@Override
	public void annotateFile(File input, Path output) {
		List<File> file = new ArrayList<File>();
		file.add(input);
		
		this.annotateFile(file, output);
	}
	

	@Override
	public void annotateFile(List<File> input, Path output) {
		
		try {
			File last = null;
			
			for (File file : input) {
				this.addInputPath(file.getPath());
				
				last = file;
			}
			
			this.addOutputPath(output.getPath());
			
			if (last instanceof RawFile) {
				_conf.set("split.framework.input.type", "raw");
			} else if (last instanceof TeiFile) {
				_conf.set("split.framework.input.type", "tei");
			} else if (last instanceof LafFile) {
				_conf.set("split.framework.input.type", "laf");
			}
			
			String tools = new String();

			for (Stage stage : _stages) {
				tools += stage.getTool().getClass().getCanonicalName() + " ";
			}

			// remove last character (blank space)
			tools = tools.substring(0, tools.length() - " ".length());

			_conf.set("split.framework.tools", tools);

			ToolRunner.run(_conf, new ParallelAnnotation(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void annotateText(Directory input, Path output) {

		try {
			this.addInputPath(input.getPath());
			this.addOutputPath(output.getPath());

			String tools = new String();

			for (Stage stage : _stages) {
				tools += stage.getTool().getClass().getCanonicalName() + " ";
			}

			// remove last character (blank space)
			tools = tools.substring(0, tools.length() - " ".length());

			_conf.set("split.framework.tools", tools);

			ToolRunner.run(_conf, new ParallelAnnotation(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void annotate(List<LinguisticAnnotationDocument> annotations, Path output) {

	}

	@Override
	public void annotateInputDocuments(List<InputDocument> documents, Path output) {
//		Stage first = _stages.getFirst();
//
//		for (InputDocument doc : documents) {
//			first.process(new InputDocumentProcessUnit(doc));
//		}
	}

	@Override
	public void annotate(LinguisticAnnotationProcessUnit annotation, Path output) {
		
	}
}
