package pt.inesc.id.l2f.annotation.tools.pt.rudrico;

import java.io.ByteArrayInputStream;

import java.util.List;

import pt.inesc.id.l2f.annotation.document.laf.Classification;
import pt.inesc.id.l2f.annotation.document.laf.Feature;
import pt.inesc.id.l2f.annotation.document.laf.FeatureStructure;
import pt.inesc.id.l2f.annotation.document.laf.MorphoSyntacticAnnotation;
import pt.inesc.id.l2f.annotation.document.laf.Segment;
import pt.inesc.id.l2f.annotation.document.laf.Segmentation;
import pt.inesc.id.l2f.annotation.tools.pt.rudrico.jni.RudricoJNIExecutionMode;
import pt.inesc.id.l2f.annotation.tools.pt.rudrico.xml.AnnotationDocument;
import pt.inesc.id.l2f.annotation.tools.pt.rudrico.xml.Class;
import pt.inesc.id.l2f.annotation.tools.pt.rudrico.xml.Id;
import pt.inesc.id.l2f.annotation.tools.pt.rudrico.xml.Sentence;
import pt.inesc.id.l2f.annotation.tools.pt.rudrico.xml.Word;
import pt.inesc.id.l2f.annotation.tool.Classifier;

/**
 * 
 * 
 * @author Tiago Luis
 *
 */
public class Rudrico extends Classifier {
	// rudrico command
//	private static final String[] COMMAND = {"/usr/bin/RuDriCo", "-R", "/usr/share/split/rudrico/rules/rules.pl"};
	// rudrico environment variables
//	private static final String[][] ENVIRONMENT = {};
	// rudrico charset
//	private static final String CHARSET = "ISO-8859-1";

	public Rudrico() {
//		super(new RudricoThriftClientExecutionMode("wc09", 9091));
//		super(new RudricoThriftClientExecutionMode(ThriftExecutionMode.findFreePort()));
		super(new RudricoJNIExecutionMode());

		// HACK: Cannot refer to 'this' while explicitly invoking a constructor
		_mode.setTool(this);
	}

	@Override
	public void start() {
		_mode.start();
	}

	@Override
	public void close() {
		_mode.close();
	}

	@Override
	public void tagg(MorphoSyntacticAnnotation annotation, List<String> input, List<String> output) {
		Segmentation segmentation = new Segmentation();
		MorphoSyntacticAnnotation morphoSyntacticAnnotation = new MorphoSyntacticAnnotation();

		int id = 0;
		
		// get rudrico output document
		for (String string : output) {
			AnnotationDocument document = new AnnotationDocument();
			document.readFrom(new ByteArrayInputStream(string.getBytes()));

			for (Sentence sentence : document.getSentences()) {
				for (Word word : sentence.getWords()) {
					Segment segment = new Segment();
					segment.setWord(word.getName());

					// TODO: fix this
					segment.setId("t" + id++);
					segment.setFrom("none");
					segment.setTo("none");

					segmentation.addSegment(segment);

					Classification classification = new Classification();
					classification.addSegment(segment.getId());

//					WordFormAlternatives wfAlt = new WordFormAlternatives();

					for (Class c : word.getClasses()) {
						FeatureStructure fs = new FeatureStructure();

						for (Id i : c.getIds()) {
							if (i.getAtrib().equals("LOW")) {
								segment.setFrom(i.getValue());
							} else if (i.getAtrib().equals("HIG")) {
								segment.setTo(i.getValue());
							} else {
								Feature f = new Feature();

								f.setName(i.getAtrib());
								f.setValue(i.getValue());

								fs.addFeature(i.getAtrib(), f);
							}
						}

						classification.addFeatureStructure(fs);

//						wfAlt.addWordForm(wf);
					}

//					wordFormAlternatives.add(wfAlt);
					morphoSyntacticAnnotation.addClassification(classification);
				}
			}
		}

		this.addSegmentation(segmentation);
		this.addMorphoSyntacticAnnotation(morphoSyntacticAnnotation);
	}
}
