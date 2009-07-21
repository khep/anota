package pt.inesc.id.l2f.annotation.tools.pt.rudrico.xml;

import java.util.Map;

import pt.inesc.id.l2f.annotation.document.DocumentElement;
import pt.inesc.id.l2f.annotation.document.xml.XMLReader;
import pt.inesc.id.l2f.annotation.document.xml.XMLWriter;

public class Id implements DocumentElement {
	private String _atrib;
	
	private String _value;
	
	public Id() {}
	
	public Id(String atrib, String value) {
		_atrib = atrib;
		_value = value;
	}
	
	public Id(Id id) {
		if (id.getAtrib() != null) {
			_atrib = id.getAtrib();
		}
		
		if (id.getValue() != null) {
			_value = id.getValue();
		}
	}

	/**
	 * @return the atrib
	 */
	public String getAtrib() {
		return _atrib;
	}

	/**
	 * @param atrib the atrib to set
	 */
	public void setAtrib(String atrib) {
		_atrib = atrib;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return _value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		_value = value;
	}

	public void readFrom(XMLReader xmlr) {
		Map<String, String> attributes = xmlr.getAttributes();
		
		_atrib = attributes.get("atrib");
		_value = attributes.get("value");
	}

	public void writeTo(XMLWriter xmlw) {
		xmlw.writeStartElement("id");

		xmlw.writeAttribute("atrib", _atrib);
		xmlw.writeAttribute("value", _value);

		// write id end element
		xmlw.writeEndElement();
	}
}
