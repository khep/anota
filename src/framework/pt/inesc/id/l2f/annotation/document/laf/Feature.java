package pt.inesc.id.l2f.annotation.document.laf;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import pt.inesc.id.l2f.annotation.document.DocumentElement;
import pt.inesc.id.l2f.annotation.document.xml.XMLReader;
import pt.inesc.id.l2f.annotation.document.xml.XMLWriter;

public class Feature implements DocumentElement, Writable {
	// ...
	private Text _name;
	// ...
	private Text _value;

	public Feature() {}

	public Feature(String name, String value) {
		_name = new Text(name);
		_value = new Text(value);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return _name.toString();
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		_name = new Text(name);
	}

	/**
	 * 
	 * @return
	 */
	public String getValue() {
		return _value.toString();
	}

	/**
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		_value = new Text(value);
	}

	public void writeTo(XMLWriter xmlw) {
		xmlw.writeStartElement("f");

		xmlw.writeAttribute("name", _name.toString());
		xmlw.writeAttribute("value", _value.toString());

		xmlw.writeEndElement();
	}

	public void readFrom(XMLReader xmlr) {
		Map<String, String> feature = xmlr.getAttributes();

		_name = new Text(feature.get("name"));
		_value = new Text(feature.get("value"));
	}

	public void readFields(DataInput in) throws IOException {
		_name = new Text();
		_name.readFields(in);
		
		_value = new Text();
		_value.readFields(in);
	}

	public void write(DataOutput out) throws IOException {
		_name.write(out);
		_value.write(out);
	}
}
