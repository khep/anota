package pt.inesc.id.l2f.annotation.execution.hadoop.laf;

import java.io.IOException;
import java.io.StringReader;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import pt.inesc.id.l2f.annotation.document.xml.XMLReader;
import pt.inesc.id.l2f.annotation.execution.hadoop.ParallelAnnotation.Map;
import pt.inesc.id.l2f.annotation.unit.LinguisticAnnotationProcessUnit;

public class LafMapClass extends Map {
	
	@Override
	public void start(JobConf job) {}
	
	@Override
	public void process(WritableComparable key, Writable value, OutputCollector<WritableComparable, Writable> output, Reporter reporter) throws IOException {
		String input = ((Text) value).toString();
		
		XMLReader xmlr = new XMLReader(new StringReader(input));
		LinguisticAnnotationProcessUnit unit = this.readLinguisticAnnotationUnit(xmlr);
		
		System.out.println("MAP (id): " + unit.getId());
		System.out.println("MAP (stage): " + unit.getStageNumber());
		System.out.println("MAP (annotation id): " + unit.getAnnotationId());
		
		output.collect(new Text(unit.getAnnotationId()), unit);
	}
	
	@Override
	public void stop() {}

	@Override
	public void setOutputCollector(OutputCollector<WritableComparable, Writable> output) {}

	@Override
	public void setReporter(Reporter reporter) {}
	
	// TODO: remover
	private LinguisticAnnotationProcessUnit readLinguisticAnnotationUnit(XMLReader xmlr) {
		int event = -1;

		while (true) {
			event = xmlr.next();

			if (xmlr.isDocumentEnd(event)) {
				break;
			}

			if (xmlr.isElementStart(event)) {
				String name = xmlr.getElementName();

				if (name.equals("unit")) {
					java.util.Map<String, String> feature = xmlr.getAttributes();
					
					LinguisticAnnotationProcessUnit unit = new LinguisticAnnotationProcessUnit(feature.get("id"), feature.get("annotation"), Integer.valueOf(feature.get("stage")));
					unit.readFrom(xmlr);

					return unit;
				}
			}
		}
		
		return null;
	}
}
