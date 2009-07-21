package pt.inesc.id.l2f.annotation.tools.pt.rudrico.xml;

import java.util.ArrayList;
import java.util.List;

import pt.inesc.id.l2f.annotation.document.DocumentElement;
import pt.inesc.id.l2f.annotation.document.xml.XMLReader;
import pt.inesc.id.l2f.annotation.document.xml.XMLWriter;

public class Word implements DocumentElement {
	private String _name;

	private List<Class> _classes;

	public Word() {
		_classes = new ArrayList<Class>();
	}
	
	public Word(String name) {
		this();
		
		_name = name;
	}

	public Word(String name, List<Class> classes) {
		_name = name;
		_classes = classes;
	}

	public Word(Word word) {
		if (word.getName() != null) {
			_name = new String(word.getName());
		}

		if (word.getName() != null) {
			_classes = new ArrayList<Class>(word.getClasses());
		}
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return _name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		_name = name;
	}

	/**
	 * @return the classes
	 */
	public List<Class> getClasses() {
		return _classes;
	}

	/**
	 * 
	 * @param c the class to add
	 */
	public void addClass(Class c) {
		_classes.add(c);
	}

	public void readFrom(XMLReader xmlr) {
		int event = -1;
		
		while (true) {
			event = xmlr.next();
			
			if (xmlr.isElementEnd(event, "word")) {
				break;
			}
			
			if (xmlr.isElementStart(event)) {
				String name = xmlr.getElementName();
				
				if (name.equals("class")) {
					Class c = new Class();

					c.setRoot(xmlr.getAttributes().get("root"));
					
					c.readFrom(xmlr);

					_classes.add(c);
				}
			}
		}
	}

	public void writeTo(XMLWriter xmlw) {
		xmlw.writeStartElement("word");

		xmlw.writeAttribute("name", _name);

		for (Class c : _classes) {
			c.writeTo(xmlw);
		}
		
		xmlw.writeEndElement();
	}
}
