package pt.inesc.id.l2f.annotation.tools.pt.jmarv;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.inesc.id.l2f.annotation.document.laf.Classification;
import pt.inesc.id.l2f.annotation.document.laf.FeatureStructure;
import pt.inesc.id.l2f.annotation.document.laf.MorphoSyntacticAnnotation;
import pt.inesc.id.l2f.annotation.document.util.Pair;
import pt.inesc.id.l2f.annotation.tool.Classifier;
import pt.inesc.id.l2f.annotation.tool.execution.JavaExecutionMode;

import pt.inesc.id.l2f.jmarv.Disambiguator;
import pt.inesc.id.l2f.jmarv.Element;
import pt.inesc.id.l2f.jmarv.MarvToken;
import pt.inesc.id.l2f.jmarv.Segment;

/**
 * 
 * 
 * 
 * @author Tiago Luis
 *
 */
public class JMARv extends Classifier {

	private Disambiguator _disambiguator;

	private static Map<String, String> _cat = new HashMap<String, String>();

	private static Map<String, String> _subcat = new HashMap<String, String>();

	static {
		// initialize CAT hash table
		_cat.put("nou", "N");
		_cat.put("ver", "V");
		_cat.put("adj", "A");
		_cat.put("pro", "P");
		_cat.put("pre", "S");
		_cat.put("art", "T");
		_cat.put("adv", "R");
		_cat.put("con", "C");
		_cat.put("num", "M");
		_cat.put("int", "I");
		_cat.put("pmk", "U");
		_cat.put("res", "X");
		_cat.put("pun", "O");

		// initialize SCT hash table
		_subcat.put("dem", "d");
	}

	public JMARv() {
		super(new JavaExecutionMode());

		try {
			_disambiguator = new Disambiguator("jmarv/dict/");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {}

	@Override
	public void start() {}

	@Override
	public void tagg(MorphoSyntacticAnnotation annotation, List<String> input, List<String> output) {
		MorphoSyntacticAnnotation morphoSyntacticAnnotation = new MorphoSyntacticAnnotation();

		List<Pair<String, Classification>> classifications = new ArrayList<Pair<String, Classification>>();

		// JMARv segment
		Segment segment = new Segment();

		for (Classification classification : annotation.getClassifications()) {
			pt.inesc.id.l2f.annotation.document.laf.Segment s = this.getSegment(classification.getSegments().get(0));
			
			MarvToken marvToken = new MarvToken(s.getWord());

			// FIXME: pode ser mais de um token
			if (classification.getSegments() != null) {
				

				for (FeatureStructure fs : classification.getFeatureStructures()) {
//					FeatureStructure fs = wordForm.getFeatureStructure();

					String cat = _cat.get(fs.getFeature("CAT").getValue());
					String subcat = ".";

					if (fs.getFeature("SCT") != null) {
						subcat = _subcat.get(fs.getFeature("SCT").getValue());
					}

//					marvToken = new MarvToken(s.getWord());
					marvToken.addClassification(new pt.inesc.id.l2f.jmarv.Classification(cat + subcat));

//					System.out.println("word: " + s.getWord() + "\t" + "classification: " + cat + " " + subcat);
					
//					if (mapfs.get(s.getWord()) == null) {
//						mapfs.put(s.getWord(), new ArrayList<FeatureStructure>());
//					}
//
//					mapfs.get(s.getWord()).add(fs);
				}
				
				classifications.add(new Pair<String, Classification>(s.getWord(), classification));
				
				if (marvToken.getClassifications().size() > 0) {
					segment.add(marvToken);
				} else {
					marvToken.addClassification(new pt.inesc.id.l2f.jmarv.Classification("U."));
					
					segment.add(marvToken);
				}
			}
		}

		int i = 0;
		
		if (segment.size() > 1) {
			List<Element<String, String>> result = _disambiguator.disambiguate(segment);

			for (Element<String, String> value : result) {
				String token = value.getFirst();
				String classification = value.getSecond();
				
				Classification c = this.getClassification(classifications, token);

				if (c != null) {
					for (FeatureStructure fs : c.getFeatureStructures()) {
//						FeatureStructure fs = wordForm.getFeatureStructure();

						String cat = _cat.get(fs.getFeature("CAT").getValue());
						String subcat = ".";

						if (fs.getFeature("SCT") != null) {
							subcat = _subcat.get(fs.getFeature("SCT").getValue());
						}

						if (classification.equals(cat + subcat)) {
							// add wordform to document
//							wordForms.add(wordForm);

							Classification c2 = new Classification();

							c2.setSegments(c.getSegments());
							c2.addFeatureStructure(fs);
							
							morphoSyntacticAnnotation.addClassification(c2);
						}
					}
				}
				
				i++;
			}
		}
		
		this.addMorphoSyntacticAnnotation(morphoSyntacticAnnotation);
	}
	
	private Classification getClassification(List<Pair<String, Classification>> classifications, String word) {
		Pair<String, Classification> classification = null;
		
		while (classifications.size() > 0) {
			classification = classifications.get(0);
			
			// remove element found
			classifications.remove(classification);
			
			if (classification.getFirst().equals(word)) {
				break;
			}
		}
		
		return classification.getSecond();
	}
}
