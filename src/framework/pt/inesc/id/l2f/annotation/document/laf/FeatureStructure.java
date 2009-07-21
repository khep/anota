package pt.inesc.id.l2f.annotation.document.laf;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import pt.inesc.id.l2f.annotation.document.DocumentElement;
import pt.inesc.id.l2f.annotation.document.util.MapWritable;
import pt.inesc.id.l2f.annotation.document.xml.XMLReader;
import pt.inesc.id.l2f.annotation.document.xml.XMLWriter;

public class FeatureStructure implements DocumentElement, Writable {
	private MapWritable<Text, Feature> _features;

	public FeatureStructure() {
		_features = new MapWritable<Text, Feature>();
	}
	
	/**
	 * 
	 * @return
	 */
	public Map<Text, Feature> getFeatures() {
		return Collections.unmodifiableMap(_features);
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public Feature getFeature(String name) {
		return _features.get(new Text(name));
	}

	/**
	 * 
	 * @param name
	 * @param feature
	 */
	public void addFeature(String name, Feature feature) {
		_features.put(new Text(name), feature);
	}

	public void writeTo(XMLWriter xmlw) {
		xmlw.writeStartElement("fs");

		// write features
		for (Feature feature : _features.values()) {
			feature.writeTo(xmlw);
		}

		xmlw.writeEndElement();
	}

	public void readFrom(XMLReader xmlr) {
		int event = -1;
		
		while (true) {
			event = xmlr.next();
			
			if (xmlr.isElementEnd(event, "fs")) {
				break;
			}
			
			if (xmlr.isElementStart(event)) {
				String name = xmlr.getElementName();
				
				if (name.equals("f")) {
					Feature f = new Feature();
					f.readFrom(xmlr);

					_features.put(new Text(f.getName()), f);
				}
			}
		}
	}

	public void readFields(DataInput in) throws IOException {
		_features = new MapWritable<Text, Feature>();
		_features.readFields(in);
	}

	public void write(DataOutput out) throws IOException {
		_features.write(out);
	}
}
