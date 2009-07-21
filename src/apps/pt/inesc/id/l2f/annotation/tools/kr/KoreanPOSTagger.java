package pt.inesc.id.l2f.annotation.tools.kr;

import java.util.List;

import pt.inesc.id.l2f.annotation.document.laf.MorphoSyntacticAnnotation;
import pt.inesc.id.l2f.annotation.tool.Classifier;

/**
 * 
 * 
 * 
 * @author Tiago Luis
 *
 */
public class KoreanPOSTagger extends Classifier {
	// korean POSTagger command
	private static final String[] COMMAND = {"/usr/bin/chasen"};
	// korean POSTagger environment variables
	private static final String[][] ENVIRONMENT = {};
	// korean POSTagger charset
	private static final String CHARSET = "EUC-JP";
	
	public KoreanPOSTagger() {
		super(new KoreanExecutionMode(COMMAND, ENVIRONMENT, CHARSET));
		
		// HACK: Cannot refer to 'this' while explicitly invoking a constructor
		_mode.setTool(this);
	}
	
	@Override
	public void start() {
//		_mode = new JapaneseMultiThreadedExecutionMode(this, "EUC-JP", builder, null, out);
		
		_mode.init();
		
		_mode.start();
	}

	@Override
	public void close() {
		_mode.close();
	}

	@Override
	public void tagg(MorphoSyntacticAnnotation annotation, List<String> input, List<String> output) {}
}
