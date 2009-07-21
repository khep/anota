package pt.inesc.id.l2f.annotation.tools.pt.rudrico.xml;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;

import pt.inesc.id.l2f.annotation.document.Document;
import pt.inesc.id.l2f.annotation.document.xml.XMLReader;
import pt.inesc.id.l2f.annotation.document.xml.XMLWriter;

public class AnnotationDocument implements Document {
	// ...
	private List<Sentence> _sentences;
	
	public AnnotationDocument() {
		_sentences = new ArrayList<Sentence>();
	}
	
	/**
	 * 
	 * 
	 * @param is
	 * 
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public void readFrom(InputStream is) {
		XMLReader xmlr = new XMLReader(new DataInputStream(is), "UTF-8");
		this.readFrom(xmlr);
		
//		AnnotationXMLReader xmlr = new AnnotationXMLReader(this);
//		
//		InputSource s = new InputSource(is);
//		
//		s.setEncoding("ISO-8859-1");
//		
//		xmlr.process(s);
	}
	
	
	/**
	 * 
	 * 
	 * @param sentence
	 */
	public void addSentence(Sentence sentence) {
		_sentences.add(sentence);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public List<Sentence> getSentences() {
		return _sentences;
	}

//	public void readFrom(Reader reader) {
//		XMLReader xmlr = new XMLReader(reader);
//		this.readFrom(xmlr);
//	}
	
	/**
	 * 
	 * 
	 * @param xmlr
	 */
	public void readFrom(XMLReader xmlr) {
		int event = -1;
		
		while (true) {
			event = xmlr.next();
			
			if (xmlr.isDocumentEnd(event)) {
				break;
			}
			
			if (xmlr.isElementStart(event)) {
				String name = xmlr.getElementName();
				
				if (name.equals("sentence")) {
					Sentence sentence = new Sentence();
					
					sentence.readFrom(xmlr);
					
					_sentences.add(sentence);
				}
			}
		}
	}
	
//	/**
//	 * 
//	 * 
//	 * @param writer
//	 * @param include
//	 */
//	public void writeTo(Writer writer) {
//		XMLWriter xmlw = new XMLWriter(writer);
//		this.writeTo(xmlw);
//	}

	public void writeTo(XMLWriter xmlw) {
		// write XML prologue
		xmlw.writeStartDocument("UTF-8", "1.0");
		
		xmlw.writeDTD("<!DOCTYPE l2f_annotation SYSTEM \"/afs/l2f/home/tmcl/rudrico/l2f_annotation.dtd\">");

		// write root element
		xmlw.writeStartElement("l2f_annotation");
		
		// write sentences
		for (Sentence sentence : _sentences) {
			sentence.writeTo(xmlw);
		}
		
		xmlw.writeEndElement();

		// write document end (closes all open structures)
		xmlw.writeEndDocument();
	}
}
