package pt.inesc.id.l2f.annotation.unit;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import pt.inesc.id.l2f.annotation.document.DocumentElement;
import pt.inesc.id.l2f.annotation.document.util.ListWritable;
import pt.inesc.id.l2f.annotation.document.xml.XMLReader;
import pt.inesc.id.l2f.annotation.document.xml.XMLWriter;

public class Dependencies implements DocumentElement, Writable {
	// list annotation document dependencies
	private ListWritable<Text> _dependencies;
	
	public Dependencies() {
		_dependencies = new ListWritable<Text>();
	}

	public void readFields(DataInput in) throws IOException {
		_dependencies = new ListWritable<Text>();
		_dependencies.readFields(in);
	}

	public void write(DataOutput out) throws IOException {
		_dependencies.write(out);
	}

	/**
	 * 
	 * 
	 * @return the dependencies
	 */
	public List<String> getDependencies() {
		List<String> dependencies = new ArrayList<String>();
		
		for (Text dependency : _dependencies) {
			dependencies.add(dependency.toString());
		}
		
		return dependencies;
	}
	
	/**
	 * 
	 * 
	 * @param id
	 */
	public void addDependency(String id) {
		_dependencies.add(new Text(id));
	}

	public void readFrom(XMLReader xmlr) {
		int event = -1;
		
		while (true) {
			event = xmlr.next();
			
			if (xmlr.isElementEnd(event, "dependencies")) {
				break;
			}
			
			if (xmlr.isElementStart(event)) {
				String name = xmlr.getElementName();
				
				if (name.equals("annotation")) {
					Map<String, String> feature = xmlr.getAttributes();

					_dependencies.add(new Text(feature.get("id")));
				}
			}
		}
	}

	public void writeTo(XMLWriter xmlw) {
		xmlw.writeStartElement("dependencies");
		
		for (Text dependency : _dependencies) {
			xmlw.writeStartElement("annotation");
			
			xmlw.writeAttribute("id", dependency.toString());
			
			xmlw.writeEndElement();
		}
		
		xmlw.writeEndElement();
	}
}
