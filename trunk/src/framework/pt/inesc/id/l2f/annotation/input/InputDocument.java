package pt.inesc.id.l2f.annotation.input;

public abstract class InputDocument {
	
	/**
	 * 
	 * 
	 * @return (null quando não houver mais unidades...)
	 */
	public abstract TextElement next();
}
