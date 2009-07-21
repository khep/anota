package pt.inesc.id.l2f.annotation.document;

import pt.inesc.id.l2f.annotation.document.xml.XMLReader;
import pt.inesc.id.l2f.annotation.document.xml.XMLWriter;

public interface DocumentElement {

	/**
	 * 
	 * 
	 * @param xmlw
	 */
	public void writeTo(XMLWriter xmlw);
	
	/**
	 * 
	 * 
	 * @param xmlr
	 */
	public void readFrom(XMLReader xmlr);
}
