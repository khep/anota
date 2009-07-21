package pt.inesc.id.l2f.annotation.execution.hadoop;

import java.io.IOException;

import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.SequenceFileOutputFormat;
import org.apache.hadoop.mapred.TextInputFormat;

import pt.inesc.id.l2f.annotation.execution.hadoop.laf.LafMapClass;
import pt.inesc.id.l2f.annotation.execution.hadoop.laf.LafReduceClass;
import pt.inesc.id.l2f.annotation.execution.hadoop.raw.RawMapClass;
import pt.inesc.id.l2f.annotation.execution.hadoop.raw.RawReduceClass;
import pt.inesc.id.l2f.annotation.execution.hadoop.tei.TeiMapClass;
import pt.inesc.id.l2f.annotation.execution.hadoop.tei.TeiReduceClass;
import pt.inesc.id.l2f.annotation.unit.LinguisticAnnotationProcessUnit;

/**
 * 
 * 
 * @author Tiago Luis
 */
public class ParallelAnnotation extends Configured implements org.apache.hadoop.util.Tool {

	public abstract static class Map extends MapReduceBase implements Mapper<WritableComparable, Writable, WritableComparable, Writable> {
		// ...
		private boolean _hasCollector = false;
		
		public void configure(JobConf job) {
			this.start(job);
		}

		public void map(WritableComparable key, Writable value, OutputCollector<WritableComparable, Writable> output, Reporter reporter) throws IOException {

			// store output collector and reporter
			if (!_hasCollector) {
				this.setOutputCollector(output);
				this.setReporter(reporter);

				_hasCollector = true;
			}
			
			this.process(key, value, output, reporter);
		}

		public void close() throws IOException {
			this.stop();
		}

		/**
		 * 
		 * 
		 * @param job
		 */
		public abstract void start(JobConf job);
		
		/**
		 * 
		 * 
		 * @param key
		 * @param value
		 * @param output
		 * @param reporter
		 * @throws IOException
		 */
		public abstract void process(WritableComparable key, Writable value, OutputCollector<WritableComparable, Writable> output, Reporter reporter) throws IOException;
		
		/**
		 * 
		 * 
		 */
		public abstract void stop();
		
		/**
		 * 
		 * 
		 * @param output
		 */
		public abstract void setOutputCollector(OutputCollector<WritableComparable, Writable> output);
		
		/**
		 * 
		 * 
		 * @param reporter
		 */
		public abstract void setReporter(Reporter reporter);
	}

	// -----------------------------------------------------------------

	public abstract static class Reduce extends MapReduceBase implements Reducer<WritableComparable, Writable, WritableComparable, Writable> {
		// ...
		private boolean _hasCollector = false;
		
		public void configure(JobConf job) {
			this.start(job);
		}
		
		public void reduce(WritableComparable key, Iterator<Writable> values, OutputCollector<WritableComparable, Writable> output, Reporter reporter) throws IOException {
			
			// store output collector and reporter
			if (!_hasCollector) {
				this.setOutputCollector(output);
				this.setReporter(reporter);

				_hasCollector = true;
			}
			
			this.process(key, values, output, reporter);
		}
		
		public void close() throws IOException {
			this.stop();
		}
		
		/**
		 * 
		 * 
		 * @param job
		 */
		public abstract void start(JobConf job);
		
		/**
		 * 
		 * 
		 * @param key
		 * @param value
		 * @param output
		 * @param reporter
		 * @throws IOException
		 */
		public abstract void process(WritableComparable key, Iterator<Writable> values, OutputCollector<WritableComparable, Writable> output, Reporter reporter) throws IOException;
		
		/**
		 * 
		 * 
		 */
		public abstract void stop();
		
		/**
		 * 
		 * 
		 * @param output
		 */
		public abstract void setOutputCollector(OutputCollector<WritableComparable, Writable> output);
		
		/**
		 * 
		 * 
		 * @param reporter
		 */
		public abstract void setReporter(Reporter reporter);
	}
	
	// -----------------------------------------------------------------

	/**
	 * The main driver for ...... map/reduce program. Invoke this method to
	 * submit the map/reduce job.
	 * 
	 * @throws IOException
	 *             in case of communication problems with the job tracker.
	 */
	public int run(String[] args) throws Exception {
		Configuration conf = super.getConf();
		JobConf job = new JobConf(conf, ParallelAnnotation.class);

		job.setJobName("SPLiT");
		job.setOutputFormat(ParallelAnnotationOutputFormat.class);
		job.setOutputValueClass(LinguisticAnnotationProcessUnit.class);
		
		String type = conf.get("split.framework.input.type", "raw");
		
		if (type.equals("raw")) {
			job.setInputFormat(TextInputFormat.class);
			
			job.setMapperClass(RawMapClass.class);
//			job.setCombinerClass(RawReduceClass.class);
			job.setReducerClass(RawReduceClass.class);
			
			job.setOutputKeyClass(Text.class);
		} else if (type.equals("tei")) {
			job.set("xmlinput.start", "<p ");
			job.set("xmlinput.end", "</p>");
			
			job.setInputFormat(XmlInputFormat.class);
			
			job.setMapperClass(TeiMapClass.class);
//			job.setCombinerClass(TeiReduceClass.class);
			job.setReducerClass(TeiReduceClass.class);
			
			job.setOutputKeyClass(Text.class);
		} else if (type.equals("laf")) {
			job.set("xmlinput.start", "<unit ");
			job.set("xmlinput.end", "</unit>");
			
			job.setInputFormat(XmlInputFormat.class);
			
			job.setMapperClass(LafMapClass.class);
//			job.setCombinerClass(LafReduceClass.class);
			job.setReducerClass(LafReduceClass.class);
			
			job.setOutputKeyClass(Text.class);
		}
		
		job.setNumMapTasks(conf.getInt("split.framework.mappers", 2));
		job.setNumReduceTasks(conf.getInt("split.framework.reducers", 1));
		
		// add input paths
		String[] inputs = conf.get("split.framework.input", "input").split("\n");
		
		for (String input : inputs) {
			if (input.equals("")) {
				continue;
			}
			
			FileInputFormat.addInputPath(job, new Path(input));
		}
		
		FileOutputFormat.setOutputPath(job, new Path(conf.get("split.framework.output", "output")));

		if (conf.getBoolean("split.framework.compress.map", false)) {
			// FIXME: isto não está a funcionar
			job.setCompressMapOutput(true);
			job.setMapOutputCompressorClass(GzipCodec.class);
		}
		
		if (conf.getBoolean("split.framework.compress.output", false)) {
			FileOutputFormat.setCompressOutput(job, true);
			FileOutputFormat.setOutputCompressorClass(job, GzipCodec.class);
		}
		
		// uncomment to run locally in a single process
		// conf.set("mapred.job.tracker", "local");

		JobClient.runJob(job);

		return 0;
	}
}
