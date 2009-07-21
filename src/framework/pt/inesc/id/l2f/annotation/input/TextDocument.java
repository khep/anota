package pt.inesc.id.l2f.annotation.input;

import java.util.ArrayList;
import java.util.List;

public class TextDocument extends InputDocument {

	// ...
	private List<TextElement> _nodes;
	
	// ...
	private int _current = 0;
	
	public TextDocument() {
		_nodes = new ArrayList<TextElement>();
	}
	
	/**
	 * 
	 * 
	 * @param text
	 */
	public TextDocument(List<String> text) {
		this();
		
		for (String t : text) {
			_nodes.add(new TextElement(t));
		}
	}
	
	public TextDocument(String text) {
		this();
		
		_nodes.add(new TextElement(text));
	}
	
	@Override
	public TextElement next() {
		
		if (_current == _nodes.size()) {
			_current = 0;
			
			return null;
		}
		
		return _nodes.get(_current++);
	}
}
