package pt.inesc.id.l2f.annotation.tools.jp;

import java.util.List;

import pt.inesc.id.l2f.annotation.document.laf.MorphoSyntacticAnnotation;
import pt.inesc.id.l2f.annotation.document.laf.Segment;
import pt.inesc.id.l2f.annotation.document.laf.Segmentation;
import pt.inesc.id.l2f.annotation.tool.Classifier;

/**
 * 
 * 
 * @author Tiago Luis
 *
 */
public class JapanesePOSTagger extends Classifier {
	// japanese POSTagger command
	private static final String[] COMMAND = {"/usr/bin/chasen",  "-F",  "%m\t%ps\t%pe\t%P-\t%B-\n"};
	// japanese POSTagger environment variables
	private static final String[][] ENVIRONMENT = {};
	// japanese POSTagger charset
	private static final String CHARSET = "EUC-JP";

	public JapanesePOSTagger() {
		super(new JapaneseExecutionMode(COMMAND, ENVIRONMENT, CHARSET));

		// HACK: Cannot refer to 'this' while explicitly invoking a constructor
		_mode.setTool(this);
	}

	@Override
	public void start() {
		_mode.init();

		_mode.start();
	}

	@Override
	public void close() {
		_mode.close();
	}

	@Override
	public void tagg(MorphoSyntacticAnnotation annotation, List<String> input, List<String> output) {
		Segmentation segmentation = new Segmentation();
		
		for (int i = 0; i < output.size(); i++) {
			String line = output.get(i);
			
			// get token
			String[] elements = line.split("\\s+");
			
			segmentation.addSegment(new Segment("t" + i, elements[1], elements[2], elements[0]));
		}
		
		this.addSegmentation(segmentation);
	}
}