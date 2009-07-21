package pt.inesc.id.l2f.annotation.execution.hadoop.tei;

import java.io.IOException;
import java.io.StringReader;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import pt.inesc.id.l2f.annotation.document.xml.XMLReader;
import pt.inesc.id.l2f.annotation.execution.LocalExecutionMode;
import pt.inesc.id.l2f.annotation.execution.hadoop.HadoopFinalStage;
import pt.inesc.id.l2f.annotation.execution.hadoop.ParallelAnnotation.Map;
import pt.inesc.id.l2f.annotation.tool.Tool;

public class TeiMapClass extends Map {

	// these are just for testing counters
	private enum Counter {
		SKIPPED
	}

	public TeiMapClass() {}
	
	// ...
	private LocalExecutionMode _local;
	// ...
	private long _maximum;
	
	@Override
	public void start(JobConf job) {
		// get maximum
		_maximum = job.getInt("split.framework.maximum", -1);
		
		try {
			// get tools
			String[] toolsProperty = job.get("split.framework.tools").split(" ");

			Tool[] tools = new Tool[toolsProperty.length];

			for (int i = 0; i < toolsProperty.length; i++) {
				tools[i] = (Tool) job.getClassLoader().loadClass(toolsProperty[i]).newInstance();
			}

			_local = new LocalExecutionMode(tools);

			_local.addFinalStage(new HadoopFinalStage());

			_local.start();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void process(WritableComparable key, Writable value, OutputCollector<WritableComparable, Writable> output, Reporter reporter) throws IOException {
		String input = ((Text) value).toString();
		
		XMLReader xmlr = new XMLReader(new StringReader(input));
		String characters = this.getCharacters(xmlr);
		
		if (_maximum != -1) {
			if (input.length() < _maximum) {
				_local.annotateText(new pt.inesc.id.l2f.annotation.execution.Text(characters), null);
			} else {
				reporter.incrCounter(Counter.SKIPPED, 1);
			}
		} else {
			_local.annotateText(new pt.inesc.id.l2f.annotation.execution.Text(characters), null);
		}
	}
	
	@Override
	public void stop() {
		_local.close();
	}

	@Override
	public void setOutputCollector(OutputCollector<WritableComparable, Writable> output) {
		((HadoopFinalStage) _local.getFinalStage()).setOutputCollector(output);
	}

	@Override
	public void setReporter(Reporter reporter) {
		((HadoopFinalStage) _local.getFinalStage()).setReporter(reporter);
	}
	
	private String getCharacters(XMLReader xmlr) {
		int event = -1;
		
		while (true) {
			event = xmlr.next();
			
			if (xmlr.isElementEnd(event, "p")) {
				break;
			}
			
			if (xmlr.isCharacters(event)) {
				return xmlr.getcharacters();
			}
		}
		
		return null;
	}
}
