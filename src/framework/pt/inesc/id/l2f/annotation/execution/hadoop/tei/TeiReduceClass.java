package pt.inesc.id.l2f.annotation.execution.hadoop.tei;

import java.io.IOException;

import java.util.Iterator;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import pt.inesc.id.l2f.annotation.execution.hadoop.ParallelAnnotation.Reduce;

public class TeiReduceClass extends Reduce {

	public TeiReduceClass() {}
	
	@Override
	public void start(JobConf job) {}
	
	@Override
	public void process(WritableComparable key, Iterator<Writable> values, OutputCollector<WritableComparable, Writable> output, Reporter reporter) throws IOException {
		
		while (values.hasNext()) {
			output.collect(key, values.next());
		}
	}

	@Override
	public void setOutputCollector(OutputCollector<WritableComparable, Writable> output) {}

	@Override
	public void setReporter(Reporter reporter) {}

	@Override
	public void stop() {}
}

