package pt.inesc.id.l2f.annotation.document.laf;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import pt.inesc.id.l2f.annotation.document.DocumentElement;
import pt.inesc.id.l2f.annotation.document.util.ListWritable;
import pt.inesc.id.l2f.annotation.document.xml.XMLReader;
import pt.inesc.id.l2f.annotation.document.xml.XMLWriter;

public class Classification implements DocumentElement, Writable {
	private ListWritable<Text> _segments;

	private ListWritable<FeatureStructure> _fs;

	public Classification() {
		_segments = new ListWritable<Text>();
		_fs = new ListWritable<FeatureStructure>();
	}

	public Classification(FeatureStructure fs) {
		this();

		// add feature structure
		_fs.add(fs);
	}

	public Classification(List<FeatureStructure> fs) {
		this();

		// add feature structure list
		_fs.addAll(fs);
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public List<FeatureStructure> getFeatureStructures() {
		return _fs;
	}

	/**
	 * 
	 * 
	 * @return
	 * @throws NoSuchElementException
	 */
	public FeatureStructure getFirstFeatureStructure() throws NoSuchElementException {
		return _fs.get(0);
	}

	/**
	 * 
	 * 
	 * @param fs
	 */
	public void addFeatureStructure(FeatureStructure fs) {
		_fs.add(fs);
	}

//	/**
//	 * 
//	 * 
//	 * @param fs
//	 */
//	public void setFeatureStructures(List<FeatureStructure> fs) {
//		_fs = fs;
//	}

	/**
	 * 
	 * 
	 * @return the segments
	 */
	public List<String> getSegments() {
		List<String> segments = new ArrayList<String>();
		
		for (Text segment : _segments) {
			segments.add(segment.toString());
		}
		
		return segments;
	}

	/**
	 * 
	 * 
	 * @param segment
	 */
	public void addSegment(String segment) {
		_segments.add(new Text(segment));
	}

	/**
	 * 
	 * 
	 * @param segments the segments to set
	 */
	public void setSegments(List<String> segments) {
		_segments = new ListWritable<Text>();
		
		for (String segment : segments) {
			_segments.add(new Text(segment));
		}
	}
	
	public void writeTo(XMLWriter xmlw) {
		xmlw.writeStartElement("node");

		// create edgesTo string from segment id's
		String edgesTo = "";

		for (Text segment : _segments) {
			edgesTo += segment + " ";
		}

		if (edgesTo.length() > 0) {
			// remove last character (blank space)
			edgesTo = edgesTo.substring(0, edgesTo.length() - 1);
		}

		xmlw.writeAttribute("edgesTo", edgesTo);

		for (FeatureStructure fs : _fs) {
			fs.writeTo(xmlw);
		}

		xmlw.writeEndElement();
	}

	public void readFrom(XMLReader xmlr) {
		int event = -1;
		
		while (true) {
			event = xmlr.next();
			
			if (xmlr.isElementEnd(event, "node")) {
				break;
			}
			
			if (xmlr.isElementStart(event)) {
				String name = xmlr.getElementName();
				
				if (name.equals("fs")) {
					FeatureStructure fs = new FeatureStructure();

					fs.readFrom(xmlr);

					_fs.add(fs);
				}
			}
		}
	}

	public void readFields(DataInput in) throws IOException {
		_segments = new ListWritable<Text>();
		_segments.readFields(in);
		
		_fs = new ListWritable<FeatureStructure>();
		_fs.readFields(in);
	}

	public void write(DataOutput out) throws IOException {
		_segments.write(out);
		_fs.write(out);
	}
}
