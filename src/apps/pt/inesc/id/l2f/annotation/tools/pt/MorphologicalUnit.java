package pt.inesc.id.l2f.annotation.tools.pt;

import java.util.ArrayList;
import java.util.List;

import pt.inesc.id.l2f.annotation.tools.pt.palavroso.SegmentVisitor;

public abstract class MorphologicalUnit {
	
	private List<Lemma> _lemmas;

	private String _form;

	public void setForm(String form) {
		_form = form;
		
		_lemmas = new ArrayList<Lemma>();
	}

	/**
	 * @return the lemmas
	 */
	public List<Lemma> getLemmas() {
		return _lemmas;
	}

	/**
	 * @param lemmas the lemmas to set
	 */
	public void setLemmas(List<Lemma> lemmas) {
		_lemmas = lemmas;
	}

	/**
	 * @return the form
	 */
	public String getForm() {
		return _form;
	}

	public void accept(SegmentVisitor visitor) {
		visitor.visitToken(this);
	}

}
