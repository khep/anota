package pt.inesc.id.l2f.annotation.tools.pt;

import java.util.StringTokenizer;

import pt.inesc.id.l2f.annotation.document.laf.Segment;
import pt.inesc.id.l2f.annotation.document.laf.Segmentation;
import pt.inesc.id.l2f.annotation.tool.Tokenizer;
import pt.inesc.id.l2f.annotation.tool.execution.JavaExecutionMode;

/**
 * 
 * 
 * @author Tiago Luis
 *
 */
public class SpaceTokenizer extends Tokenizer {
	long i = 0;
	
	public SpaceTokenizer() {
		super(new JavaExecutionMode());
	}

	@Override
	protected Segmentation tokenize(String input, String id) {
		Segmentation segmentation = new Segmentation();
		
		int offset = 0;

		StringTokenizer words = new StringTokenizer(input);

		while (words.hasMoreElements()) {
			String word = words.nextToken();
			
			String xpointer = "xpointer(id('" + id + "')";
			
			String from = new String();
			String to = new String();
			
			from = xpointer + "/point()[" + Integer.toString(input.indexOf(word, offset)) + "])";
			to = xpointer + "/point()[" + Integer.toString(input.indexOf(word, offset) + word.length()) + "])";
			
			segmentation.addSegment(new Segment("t" + i++, from, to, word));
			
			offset += word.length() + 1;
		}
		
		return segmentation;
	}

	public void close() {
		
	}
	
	@Override
	public void start() {}
}
