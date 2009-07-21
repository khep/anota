package pt.inesc.id.l2f.annotation.document.util;

public class Pair<F, S> {
	// ...
	private F _first;
	// ...
	private S _second;
	
	public Pair(F first, S second) {
		_first = first;
		_second = second;
	}

	/**
	 * 
	 * 
	 * @return the first
	 */
	public F getFirst() {
		return _first;
	}

	/**
	 * 
	 * 
	 * @return the second
	 */
	public S getSecond() {
		return _second;
	}
	
	
}
