package pt.inesc.id.l2f.annotation.execution.hadoop.laf;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import pt.inesc.id.l2f.annotation.document.laf.LinguisticAnnotationDocument;
import pt.inesc.id.l2f.annotation.execution.LocalExecutionMode;
import pt.inesc.id.l2f.annotation.execution.hadoop.HadoopFinalStage;
import pt.inesc.id.l2f.annotation.execution.hadoop.ParallelAnnotation.Reduce;
import pt.inesc.id.l2f.annotation.tool.Tool;
import pt.inesc.id.l2f.annotation.unit.Dependencies;
import pt.inesc.id.l2f.annotation.unit.LinguisticAnnotationProcessUnit;

public class LafReduceClass extends Reduce {
	// ...
	private LocalExecutionMode _local;
	
	@Override
	public void start(JobConf job) {
		
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
	public void process(WritableComparable key, Iterator<Writable> values, OutputCollector<WritableComparable, Writable> output, Reporter reporter) throws IOException {
		List<LinguisticAnnotationProcessUnit> units = new ArrayList<LinguisticAnnotationProcessUnit>();
		
		// sort linguistic annotations
		while (values.hasNext()) {
			units.add((LinguisticAnnotationProcessUnit) values.next());
		}
		
		Collections.sort(units);
		
		// ....
		LinguisticAnnotationDocument annotation = new LinguisticAnnotationDocument();

		// get last linguistic annotation
		LinguisticAnnotationProcessUnit last = null;
		
		// store ....
		Dependencies dep = new Dependencies();
		
		for (LinguisticAnnotationProcessUnit unit : units) {
			annotation.merge(unit.getDocument());
			
			dep.addDependency(unit.getId());
			
			last = unit;
		}
		
		_local.annotate(new LinguisticAnnotationProcessUnit(annotation, new LinguisticAnnotationDocument(), dep, last.getAnnotationId(), last.getStageNumber() + 1), null);
	}

	@Override
	public void setOutputCollector(OutputCollector<WritableComparable, Writable> output) {
		((HadoopFinalStage) _local.getFinalStage()).setOutputCollector(output);
	}

	@Override
	public void setReporter(Reporter reporter) {
		((HadoopFinalStage) _local.getFinalStage()).setReporter(reporter);
	}

	@Override
	public void stop() {
		_local.close();
	}
}
