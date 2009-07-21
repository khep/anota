package pt.inesc.id.l2f.annotation.tools.cn;

import java.io.StringReader;

import java.util.List;

import pt.inesc.id.l2f.annotation.document.laf.Classification;
import pt.inesc.id.l2f.annotation.document.laf.Feature;
import pt.inesc.id.l2f.annotation.document.laf.FeatureStructure;
import pt.inesc.id.l2f.annotation.document.laf.MorphoSyntacticAnnotation;
import pt.inesc.id.l2f.annotation.document.laf.Segment;
import pt.inesc.id.l2f.annotation.document.laf.Segmentation;
import pt.inesc.id.l2f.annotation.tool.Classifier;
import pt.inesc.id.l2f.annotation.tool.execution.JavaExecutionMode;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * 
 * 
 * 
 * @author Tiago Luis
 *
 */
public class ChinesePOSTagger extends Classifier {
	// chinese model
	private static final String DICTIONARY = "/afs/l2f/home/tmcl/work/tools/en/stanford-tagger/stanford-chinese-tagger-2008-07-07/chinese.tagger";
	
	static {
		try {
			MaxentTagger.init(DICTIONARY);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ChinesePOSTagger() {
		super(new JavaExecutionMode());
	}
	
	@SuppressWarnings("unchecked")
	public void tagg(MorphoSyntacticAnnotation a, List<String> input, List<String> output) {
		Segmentation segmentation = new Segmentation();
		MorphoSyntacticAnnotation annotation = new MorphoSyntacticAnnotation();

		int offset = 0;
		long i = 0;

		try {
			String process = "";
			
			for (String segment : input) {
				process += segment;
			}

			List<Sentence> sentences = MaxentTagger.tokenizeText(new StringReader(process));

			for (Sentence sentence : sentences) {
				Sentence<TaggedWord> taggedSentence = MaxentTagger.tagSentence(sentence);
				
				for (int j = 0; j < sentence.length(); j++) {
					TaggedWord word = taggedSentence.getHasWord(j);
					
					String from = new String();
					String to = new String();

					from = Integer.toString(((Word) sentence.getHasWord(j)).beginPosition());
					to = Integer.toString(((Word) sentence.getHasWord(j)).endPosition());
					
					Segment s = new Segment("t" + i++, from, to, ((HasWord) word).word());
					segmentation.addSegment(s);

					annotation.addClassification(this.createClassification(s, word.tag()));

					offset += ((HasWord) word).word().length();
				}
			}

			this.addSegmentation(segmentation);
			this.addMorphoSyntacticAnnotation(annotation);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Classification createClassification(Segment segment, String c) {
		Classification classification = new Classification();
		classification.addSegment(segment.getId());

		FeatureStructure fs = new FeatureStructure();

		Feature pos = new Feature();
		pos.setName("pos");
		pos.setValue(c);

		fs.addFeature("pos", pos);

		classification.addFeatureStructure(fs);

		return classification;
	}

	@Override
	public void close() {}
	
	@Override
	public void start() {}
}
