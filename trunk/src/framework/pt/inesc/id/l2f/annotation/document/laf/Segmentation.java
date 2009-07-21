package pt.inesc.id.l2f.annotation.document.laf;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import pt.inesc.id.l2f.annotation.document.util.ListWritable;
import pt.inesc.id.l2f.annotation.document.util.MapWritable;
import pt.inesc.id.l2f.annotation.document.xml.XMLReader;
import pt.inesc.id.l2f.annotation.document.xml.XMLWriter;
import pt.inesc.id.l2f.annotation.document.DocumentElement;

public class Segmentation implements DocumentElement, Writable {
	// ...
	private ListWritable<Segment> _segmentsList;
	// ...
	private MapWritable<Text, Segment> _segmentsMap;
	
	public Segmentation() {
		_segmentsList = new ListWritable<Segment>();
		_segmentsMap = new MapWritable<Text, Segment>();
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public List<Segment> getSegments() {
		return _segmentsList;
	}
	
	/**
	 * 
	 * 
	 * @param id
	 * @return
	 */
	public Segment getSegment(String id) {
		return (Segment) _segmentsMap.get(new Text(id));
	}

	/**
	 * 
	 * 
	 * @param segment
	 */
	public void addSegment(Segment segment) {
		_segmentsList.add(segment);
		
		_segmentsMap.put(new Text(segment.getId()), segment);
	}

	public void writeTo(XMLWriter xmlw) {
		xmlw.writeStartElement("edgeSet");

		for (Segment segment : _segmentsList) {
			segment.writeTo(xmlw);
		}

		xmlw.writeEndElement();
	}

	public void readFrom(XMLReader xmlr) {
		int event = -1;
		
		while (true) {
			event = xmlr.next();
			
			if (xmlr.isElementEnd(event, "edgeSet")) {
				break;
			}
			
			if (xmlr.isElementStart(event)) {
				String name = xmlr.getElementName();
				
				if (name.equals("edge")) {
					Segment segment = new Segment();
					
					segment.readFrom(xmlr);
					
					_segmentsList.add(segment);
					_segmentsMap.put(new Text(segment.getId()), segment);
				}
			}
		}
	}

	public void readFields(DataInput in) throws IOException {
		_segmentsList = new ListWritable<Segment>();
		_segmentsList.readFields(in);
		
		_segmentsMap = new MapWritable<Text, Segment>();
		_segmentsMap.readFields(in);
	}

	public void write(DataOutput out) throws IOException {
		_segmentsList.write(out);
		_segmentsMap.write(out);
	}
}
