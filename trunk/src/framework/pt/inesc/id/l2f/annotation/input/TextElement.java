package pt.inesc.id.l2f.annotation.input;

public class TextElement {
	// ...
	private String _text;
	
	public TextElement(String text) {
		_text = text;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return _text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		_text = text;
	}
}
