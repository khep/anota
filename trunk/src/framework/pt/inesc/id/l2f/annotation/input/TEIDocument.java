package pt.inesc.id.l2f.annotation.input;

import java.io.FileNotFoundException;
import java.io.FileReader;

import pt.inesc.id.l2f.annotation.document.xml.XMLReader;

public class TEIDocument extends InputDocument {
	// ...
	private XMLReader _xmlr;
	
	public TEIDocument(String filename) throws FileNotFoundException {
		_xmlr = new XMLReader(new FileReader(filename));
	}
	
	@Override
	public TextElement next() {
		int event = -1;
		
		while (true) {
			event = _xmlr.next();
			
			if (_xmlr.isDocumentEnd(event)) {
				break;
			}
			
			if (_xmlr.isCharacters(event)) {
				String text = _xmlr.getcharacters();
				
				if (text.matches("\\s+")) {
					continue;
				}
				
				return new TextElement(text);
			}
		}
		
		return null;
	}

}
