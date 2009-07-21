package pt.inesc.id.l2f.annotation.tools.pt.palavroso;

import pt.inesc.id.l2f.annotation.tools.pt.MorphologicalUnit;

public abstract class SegmentVisitor {

	public abstract void visitPalavrosoSegment(PalavrosoSegment palavrosoSegment);

	public abstract void visitToken(MorphologicalUnit token);

}
