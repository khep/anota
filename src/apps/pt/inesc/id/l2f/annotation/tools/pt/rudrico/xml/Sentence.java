package pt.inesc.id.l2f.annotation.tools.pt.rudrico.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pt.inesc.id.l2f.annotation.document.DocumentElement;
import pt.inesc.id.l2f.annotation.document.xml.XMLReader;
import pt.inesc.id.l2f.annotation.document.xml.XMLWriter;

public class Sentence implements DocumentElement {
	private List<Word> _words;

	public Sentence() {
		_words = new ArrayList<Word>();
	}

	public Sentence(List<Word> words) {
		_words = words;
	}

	public Sentence(Sentence sentence) {
		if (sentence.getWords() != null) {
			_words = new ArrayList<Word>(sentence.getWords());
		}
	}

	/**
	 * @return the words
	 */
	public List<Word> getWords() {
		return Collections.unmodifiableList(_words);
	}

	/**
	 * @param word the word to add
	 */
	public void addWord(Word word) {
		_words.add(word);
	}

	public void readFrom(XMLReader xmlr) {
		int event = -1;

		while (true) {
			event = xmlr.next();

			if (xmlr.isElementEnd(event, "sentence")) {
				break;
			}

			if (xmlr.isElementStart(event)) {
				String name = xmlr.getElementName();

				if (name.equals("word")) {
					Word word = new Word();
					
					word.setName(xmlr.getAttributes().get("name"));
					
					word.readFrom(xmlr);
					
					_words.add(word);
				}
			}
		}
	}

	public void writeTo(XMLWriter xmlw) {
		xmlw.writeStartElement("sentence");

		for (Word word : _words) {
			word.writeTo(xmlw);
		}

		xmlw.writeEndElement();
	}
}
