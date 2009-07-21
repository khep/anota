package pt.inesc.id.l2f.annotation.tool;

import pt.inesc.id.l2f.annotation.unit.InputDocumentProcessUnit;
import pt.inesc.id.l2f.annotation.unit.LinguisticAnnotationProcessUnit;

/**
 * 
 * 
 * @author Tiago Luis
 *
 */
public interface ToolVisitor {
	
	/**
	 * 
	 */
	public abstract void start();
	
	/**
	 * 
	 * 
	 * @param unit
	 * @return
	 */
	public void visit(InputDocumentProcessUnit unit);
	
	/**
	 * 
	 * 
	 * @param unit
	 * @return
	 */
	public void visit(LinguisticAnnotationProcessUnit unit);
	
	/**
	 * 
	 */
	public abstract void close();
}
