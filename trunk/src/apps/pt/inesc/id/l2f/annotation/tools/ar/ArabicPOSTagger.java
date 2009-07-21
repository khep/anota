package pt.inesc.id.l2f.annotation.tools.ar;

import java.io.StringReader;

import java.util.HashSet;
import java.util.List;

import gpl.pierrick.brihaye.aramorph.AraMorph;
import gpl.pierrick.brihaye.aramorph.Solution;
import gpl.pierrick.brihaye.aramorph.lucene.ArabicTokenizer;

import org.apache.lucene.analysis.Token;

import pt.inesc.id.l2f.annotation.document.laf.Classification;
import pt.inesc.id.l2f.annotation.document.laf.Feature;
import pt.inesc.id.l2f.annotation.document.laf.FeatureStructure;
import pt.inesc.id.l2f.annotation.document.laf.MorphoSyntacticAnnotation;
import pt.inesc.id.l2f.annotation.document.laf.Segment;
import pt.inesc.id.l2f.annotation.document.laf.Segmentation;
import pt.inesc.id.l2f.annotation.tool.Classifier;
import pt.inesc.id.l2f.annotation.tool.execution.JavaExecutionMode;

/**
 * 
 * 
 * @author Tiago Luis
 *
 */
public class ArabicPOSTagger extends Classifier {

	private AraMorph araMorph;

	public ArabicPOSTagger() {
		super(new JavaExecutionMode());

		// TODO: colocar num método init
		araMorph = new AraMorph();
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public void tagg(MorphoSyntacticAnnotation annotation, List<String> input, List<String> output) {
		Segmentation segmentation = new Segmentation();
		MorphoSyntacticAnnotation morphoSyntacticAnnotation = new MorphoSyntacticAnnotation();

		long i = 0;

		try {
			for (String segment : input) {
				ArabicTokenizer tokenizer = new ArabicTokenizer(new StringReader(segment));

				Token word = null;

				while ((word = tokenizer.next()) != null) {
					String term = word.termText(); 

					String from = new String();
					String to = new String();

					from = Integer.toString(word.startOffset());
					to = Integer.toString(word.endOffset());

					Segment seg = new Segment("t" + i++, from, to, term);
					segmentation.addSegment(seg);

					if (araMorph.analyzeToken(word.termText())) {
						HashSet<Solution> solutions = araMorph.getWordSolutions(word.termText());

						for (Solution solution : solutions) {
							morphoSyntacticAnnotation.addClassification(this.createClassification(seg, solution));
						}
					}
				}
			}

			this.addSegmentation(segmentation);
			this.addMorphoSyntacticAnnotation(morphoSyntacticAnnotation);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Classification createClassification(Segment segment, Solution solution) {
		Classification classification = new Classification();

		classification.addSegment(segment.getId());

		FeatureStructure fs = new FeatureStructure();

		// lemma feature
		Feature lemma = new Feature();

		lemma.setName("lemma");
		lemma.setValue(solution.getLemma());

		// stem POS feature
		Feature stemPOS = new Feature();

		stemPOS.setName("stemPOS");
		stemPOS.setValue(solution.getStemPOS());

		fs.addFeature("lemma", lemma);
		fs.addFeature("stemPOS", stemPOS);

		classification.addFeatureStructure(fs);

		return classification;
	}

	@Override
	public void close() {}

	@Override
	public void start() {}
}
