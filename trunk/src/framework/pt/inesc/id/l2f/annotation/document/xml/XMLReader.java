package pt.inesc.id.l2f.annotation.document.xml;

import java.io.InputStream;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class XMLReader {
	// ...
	private static XMLInputFactory _xmlif;
	// ...
	private XMLStreamReader _xmlr;
	
	static {
		_xmlif = XMLInputFactory.newInstance();
	}

	public XMLReader(Reader reader) {
		try {
			_xmlr = _xmlif.createXMLStreamReader(reader);
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	public XMLReader(InputStream input) {
		try {
			_xmlr = _xmlif.createXMLStreamReader(input);
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
	
	public XMLReader(InputStream input, String encoding) {
		try {
			_xmlr = _xmlif.createXMLStreamReader(input, encoding);
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public boolean hasNext() {
		
		try {
			return _xmlr.hasNext();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public int next() {
		
		try {
			return _xmlr.next();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public Map<String, String> getAttributes() {
		Map<String, String> attributes = new HashMap<String, String>();

		for (int i = 0; i < _xmlr.getAttributeCount(); i++) {
			String name = _xmlr.getAttributeName(i).toString();
			String value = _xmlr.getAttributeValue(i);

			attributes.put(name, value);
		}
		
		return Collections.unmodifiableMap(attributes);
	}
	
	public String getcharacters() {
		int start = _xmlr.getTextStart();
        int length = _xmlr.getTextLength();

        return new String(_xmlr.getTextCharacters(), start, length);
	}
	
	/**
	 * 
	 * 
	 * @param event
	 * @param name element name
	 * @return
	 */
	public boolean isElementEnd(int event, String name) {
		return event == XMLStreamConstants.END_ELEMENT && name.equals(_xmlr.getLocalName().toString());
	}
	
	/**
	 * 
	 * 
	 * @param event
	 * @return
	 */
	public boolean isCharacters(int event) {
		return event == XMLStreamConstants.CHARACTERS;
	}
	
	/**
	 * 
	 * 
	 * @param event
	 * @return
	 */
	public boolean isElementStart(int event) {
		return event == XMLStreamConstants.START_ELEMENT;
	}
	
	/**
	 * 
	 * 
	 * @param event
	 * @return
	 */
	public boolean isDocumentEnd(int event) {
		return event == XMLStreamConstants.END_DOCUMENT;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public String getElementName() {
		return _xmlr.getLocalName().toString();
	}
	
//	public void next() {
//		Element current = _current;
//		
//		// get next element
//		_current = this.getNextElement();
//		
//		return current;
//	}
	
//	private Element getNextElement() {
//		Element element = null;
//		
//		try {
//			while (_xmlr.hasNext()) {
//				_xmlr.next();
//
//				if (_xmlr.getEventType() == XMLStreamConstants.START_ELEMENT) {
//					element = new Element(_xmlr.getLocalName());
//					
//					System.out.println("DEBUG: " + element.getName());
//					
//					// get element attributes
//					for (int i = 0; i < _xmlr.getAttributeCount(); i++) {
//						String name = _xmlr.getAttributeName(i).toString();
//						String value = _xmlr.getAttributeValue(i);
//
//						element.addAttribute(name, value);
//					}
//					
//					break;
//				}
//			}
//		} catch (XMLStreamException e) {
//			e.printStackTrace();
//		}
//		
//		return element;
//	}
//	
//	public Element getCurrentElement() {
//		return _current;
//	}
	
//	public Map<String, String> getCurrentElementAttributes() {
//		Map<String, String> attributes = new HashMap<String, String>();
//		
//		for (int i = 0; i < _xmlr.getAttributeCount(); i++) {
//			QName name = _xmlr.getAttributeName(i);
//			String value = _xmlr.getAttributeValue(i);
//			
//			attributes.put(name.toString(), value);
//		}
//		
//		return attributes;
//	}
}
