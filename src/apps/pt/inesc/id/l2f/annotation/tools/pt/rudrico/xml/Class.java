package pt.inesc.id.l2f.annotation.tools.pt.rudrico.xml;

import java.util.ArrayList;
import java.util.List;

import pt.inesc.id.l2f.annotation.document.DocumentElement;
import pt.inesc.id.l2f.annotation.document.xml.XMLReader;
import pt.inesc.id.l2f.annotation.document.xml.XMLWriter;

public class Class implements DocumentElement {
	private String _root;
	
	private List<Id> _ids;
	
	public Class() {
		_ids = new ArrayList<Id>();
	}
	
	public Class(String root, List<Id> ids) {
		_root = root;
		_ids = ids;
	}
	
	public Class(Class c) {
		if (c.getRoot() != null) {
			_root = new String(c.getRoot());
		}
		
		if (c.getIds() != null) {
			_ids = new ArrayList<Id>(c.getIds());
		}
	}

	/**
	 * @return the root
	 */
	public String getRoot() {
		return _root;
	}

	/**
	 * @param root the root to set
	 */
	public void setRoot(String root) {
		_root = root;
	}

	/**
	 * @return the ids
	 */
	public List<Id> getIds() {
		return _ids;
	}

	/**
	 * @param id the id to add
	 */
	public void addId(Id id) {
		_ids.add(id);
	}

	public void readFrom(XMLReader xmlr) {
		int event = -1;

		while (true) {
			event = xmlr.next();

			if (xmlr.isElementEnd(event, "class")) {
				break;
			}

			if (xmlr.isElementStart(event)) {
				String name = xmlr.getElementName();

				if (name.equals("id")) {
					Id id = new Id();
					
					id.readFrom(xmlr);
					
					_ids.add(id);
				}
			}
		}
	}

	public void writeTo(XMLWriter xmlw) {
		xmlw.writeStartElement("class");

		// write root
		xmlw.writeAttribute("root", _root);

		// write ids
		for (Id id : _ids) {
			id.writeTo(xmlw);
		}

		// write class end element
		xmlw.writeEndElement();
	}
}
