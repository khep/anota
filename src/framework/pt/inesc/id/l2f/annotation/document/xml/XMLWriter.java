package pt.inesc.id.l2f.annotation.document.xml;

import java.io.OutputStream;
import java.io.Writer;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class XMLWriter {
	private static XMLOutputFactory _xmlof;
	
	private XMLStreamWriter _xmlw;

	static {
		_xmlof = XMLOutputFactory.newInstance();
	}

	public XMLWriter(Writer writer) {
		try {
			_xmlw = _xmlof.createXMLStreamWriter(writer);
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
	
	public XMLWriter(OutputStream output) {
		try {
			_xmlw = _xmlof.createXMLStreamWriter(output);
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	public void writeAttribute(String localName, String value) {
		try {
			_xmlw.writeAttribute(localName, value);
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
	
	public void writeStartElement(String localName) {
		try {
			_xmlw.writeStartElement(localName);
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
	
	public void writeEndElement() {
		try {
			_xmlw.writeEndElement();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
	
	public void writeCharacters(String text) {
		try {
			_xmlw.writeCharacters(text);
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
	
	public void writeStartDocument(String encoding, String version) {
		try {
			_xmlw.writeStartDocument(encoding, version);
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
	
	public void writeDTD(String dtd) {
		try {
			_xmlw.writeDTD(dtd);
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
	
	public void writeEndDocument() {
		try {
			_xmlw.writeEndDocument();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
}
