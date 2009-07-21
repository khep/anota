package pt.inesc.id.l2f.annotation.execution.hadoop;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputFormatBase;
import org.apache.hadoop.mapred.RecordWriter;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.util.Progressable;
import org.apache.hadoop.util.ReflectionUtils;

import pt.inesc.id.l2f.annotation.document.xml.XMLWriter;
import pt.inesc.id.l2f.annotation.unit.ProcessUnit;

public class ParallelAnnotationOutputFormat extends OutputFormatBase<WritableComparable, ProcessUnit> {
	
	public RecordWriter<WritableComparable, ProcessUnit> getRecordWriter(FileSystem ignored, JobConf job, String name, Progressable progress) throws IOException {
		Path dir = job.getOutputPath();
	    FileSystem fs = dir.getFileSystem(job);
		
		boolean isCompressed = getCompressOutput(job);
		
		if (!isCompressed) {
			FSDataOutputStream out = fs.create(new Path(dir, name), progress);
			final OutputStreamWriter osr = new OutputStreamWriter(out);
			final XMLWriter xmlw = new XMLWriter(osr);
			
			// write document start
			xmlw.writeStartDocument("UTF-8", "1.0");

			// write root element start
			xmlw.writeStartElement("units");
			
			return new RecordWriter<WritableComparable, ProcessUnit>() {

				public void write(WritableComparable key, ProcessUnit value) throws IOException {
					if (key != null) {
						if (value != null) {
							value.writeTo(xmlw);
						}
					}
				}

				public void close(Reporter reporter) throws IOException {
					// write root element end
					xmlw.writeEndDocument();
					
					// write document end
					xmlw.writeEndDocument();
					
					// close output stream
					osr.close();
				}
			};
		} else {
			Class<? extends CompressionCodec> codecClass = getOutputCompressorClass(job, GzipCodec.class);
			
			// create the named codec
			CompressionCodec codec = (CompressionCodec)
			ReflectionUtils.newInstance(codecClass, job);
			
			// build the filename including the extension
			Path filename = new Path(dir, name + codec.getDefaultExtension());
			FSDataOutputStream fileOut = fs.create(filename, progress);
			
			DataOutputStream out = new DataOutputStream(codec.createOutputStream(fileOut));
			final OutputStreamWriter osr = new OutputStreamWriter(out);
			final XMLWriter xmlw = new XMLWriter(osr);
			
			// write document start
			xmlw.writeStartDocument("UTF-8", "1.0");

			// write root element start
			xmlw.writeStartElement("units");
			
			return new RecordWriter<WritableComparable, ProcessUnit>() {

				public void write(WritableComparable key, ProcessUnit value) throws IOException {
					if (key != null) {
						if (value != null) {
							value.writeTo(xmlw);
						}
					}
				}

				public void close(Reporter reporter) throws IOException {
					// write root element end
					xmlw.writeEndDocument();
					
					// write document end
					xmlw.writeEndDocument();
					
					// close output stream
					osr.close();
				}
			};
		}
	}
}

