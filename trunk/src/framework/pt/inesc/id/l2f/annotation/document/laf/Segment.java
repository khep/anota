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

public class Segment implements DocumentElement, Writable {
	// ...
	private Text _id;
	// ...
	private Text _from;
	// ...
	private Text _to;
	// ...
	private Text _word;
	
	public Segment() {}
	
	public Segment(String id, String from, String to, String word) {
		_id = new Text(id);
		_from = new Text(from);
		_to = new Text(to);
		_word = new Text(word);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getId() {
		return _id.toString();
	}
	
	/**
	 * 
	 * 
	 * @param id
	 */
	public void setId(String id) {
		_id = new Text(id);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getFrom() {
		return _from.toString();
	}
	
	/**
	 * 
	 * 
	 * @param from
	 */
	public void setFrom(String from) {
		_from = new Text(from);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getTo() {
		return _to.toString();
	}
	
	/**
	 * 
	 * 
	 * @param to
	 */
	public void setTo(String to) {
		_to = new Text(to);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getWord() {
		return _word.toString();
	}
	
	/**
	 * 
	 * 
	 * @param word
	 */
	public void setWord(String word) {
		_word = new Text(word);
	}

	public void writeTo(XMLWriter xmlw) {
		xmlw.writeStartElement("edge");

		xmlw.writeAttribute("id", _id.toString());
		xmlw.writeAttribute("from", _from.toString());
		xmlw.writeAttribute("to", _to.toString());
		xmlw.writeAttribute("word", _word.toString());

		xmlw.writeEndElement();
	}

	public void readFrom(XMLReader xmlr) {
		Map<String, String> attributes = xmlr.getAttributes();
		
		_id = new Text(attributes.get("id"));
		_from = new Text(attributes.get("from"));
		_to = new Text(attributes.get("to"));
		_word = new Text(attributes.get("word"));
	}

	public void readFields(DataInput in) throws IOException {
		_id = new Text();
		_id.readFields(in);
		
		_from = new Text();
		_from.readFields(in);
		
		_to = new Text();
		_to.readFields(in);
		
		_word = new Text();
		_word.readFields(in);
	}

	public void write(DataOutput out) throws IOException {
		_id.write(out);
		_from.write(out);
		_to.write(out);
		_word.write(out);
	}
}
