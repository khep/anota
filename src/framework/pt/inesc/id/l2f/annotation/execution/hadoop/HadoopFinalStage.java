package pt.inesc.id.l2f.annotation.execution.hadoop;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import pt.inesc.id.l2f.annotation.stage.Stage;
import pt.inesc.id.l2f.annotation.tool.IdentityTool;
import pt.inesc.id.l2f.annotation.unit.FinalProcessUnit;
import pt.inesc.id.l2f.annotation.unit.ProcessUnit;

public class HadoopFinalStage extends Stage {
	private Reporter _reporter;
	
	private OutputCollector<WritableComparable, Writable> _output;

	public HadoopFinalStage() {
		super(new IdentityTool());
	}

	public void run() {
		long i = 0;
		
		try {
			while (true) {
				ProcessUnit unit = _input.take();
				
				if (unit instanceof FinalProcessUnit) {
					break;
				}
				
//				LinguisticAnnotationProcessUnit lunit = (LinguisticAnnotationProcessUnit) unit;
				
//				String output = "";
//				
//				int size = lunit.getOutputDocumentMorphoSyntacticAnnotations().size();
//				MorphoSyntacticAnnotation lastAnnotation = lunit.getOutputDocumentMorphoSyntacticAnnotations().get(size - 1);
//				
//				for (Classification classification : lastAnnotation.getClassifications()) {
//					output += lunit.getDocument().getSegment(classification.getSegments().get(0)).getWord() + " (" + classification.getFeatureStructures().getLast().getFeature("CAT").getValue() + ") ";
//				}
//				
//				_output.collect(new LongWritable(i++), new Text(output));
				
				
				_output.collect(new Text(Long.toString(i++)), unit);
				
				
//				StringWriter sw = new StringWriter();
//				document.writeTo(sw);
//				_output.collect(new LongWritable(i++), new Text(sw.toString()));
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 
	 * @return the reporter
	 */
	public Reporter getReporter() {
		return _reporter;
	}

	/**
	 * 
	 * 
	 * @param reporter the reporter to set
	 */
	public void setReporter(Reporter reporter) {
		_reporter = reporter;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public boolean hasReporter() {
		return _reporter != null;
	}

	/**
	 * 
	 * 
	 * @return the output collector
	 */
	public OutputCollector<WritableComparable, Writable> getOutputCollector() {
		return _output;
	}

	/**
	 * @param output the output collector to set
	 */
	public void setOutputCollector(OutputCollector<WritableComparable, Writable> output) {
		_output = output;
	}
}
