package pt.inesc.id.l2f.annotation.document.laf;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import java.util.List;

import org.apache.hadoop.io.Writable;

import pt.inesc.id.l2f.annotation.document.DocumentElement;
import pt.inesc.id.l2f.annotation.document.util.ListWritable;
import pt.inesc.id.l2f.annotation.document.xml.XMLReader;
import pt.inesc.id.l2f.annotation.document.xml.XMLWriter;

public class MorphoSyntacticAnnotation implements DocumentElement, Writable {
	private ListWritable<Classification> _classifications;

	public MorphoSyntacticAnnotation() {
		_classifications = new ListWritable<Classification>();
	}

	public List<Classification> getClassifications() {
		return _classifications;
	}

	public void addClassification(Classification classification) {
		_classifications.add(classification);
	}

	public void setClassifications(List<Classification> classifications) {
		_classifications = new ListWritable<Classification>();
		
		for (Classification classification : classifications) {
			_classifications.add(classification);
		}
	}

	public void writeTo(XMLWriter xmlw) {
		xmlw.writeStartElement("nodeSet");

		for (Classification classification : _classifications) {
			classification.writeTo(xmlw);
		}

		xmlw.writeEndElement();
	}

	public void readFrom(XMLReader xmlr) {
		
int event = -1;
		
		while (true) {
			event = xmlr.next();
			
			if (xmlr.isElementEnd(event, "nodeSet")) {
				break;
			}
			
			if (xmlr.isElementStart(event)) {
				String name = xmlr.getElementName();
				
				if (name.equals("node")) {
					Classification classification = new Classification();

					// get segments (edgesTo) attribute
					String attribute = xmlr.getAttributes().get("edgesTo");
					
					// parse attribute
					String[] segments = attribute.split(" ");
					for (String segment : segments) {
						classification.addSegment(segment);
					}
					
					classification.readFrom(xmlr);

					_classifications.add(classification);
				}
			}
		}
	}

	public void readFields(DataInput in) throws IOException {
		_classifications = new ListWritable<Classification>();
		_classifications.readFields(in);
	}

	public void write(DataOutput out) throws IOException {
		_classifications.write(out);
	}
}
