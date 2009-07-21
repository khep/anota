package pt.inesc.id.l2f.annotation.tools.pt.palavroso;

import java.util.List;

import pt.inesc.id.l2f.annotation.document.laf.Classification;
import pt.inesc.id.l2f.annotation.document.laf.Feature;
import pt.inesc.id.l2f.annotation.document.laf.FeatureStructure;
import pt.inesc.id.l2f.annotation.document.laf.MorphoSyntacticAnnotation;
import pt.inesc.id.l2f.annotation.document.laf.Segment;
import pt.inesc.id.l2f.annotation.document.laf.Segmentation;
import pt.inesc.id.l2f.annotation.tools.pt.Lemma;
import pt.inesc.id.l2f.annotation.tool.Classifier;

/**
 * 
 * 
 * @author Tiago Luis
 *
 */
public class Palavroso extends Classifier {
	// palavroso command
	private static final String[] COMMAND = {"/usr/bin/morfolog", "-spu", "-D", "/usr/share/split/palavroso/dict/palav.lex"};
	// palavroso environment variables
	private static final String[][] ENVIRONMENT = {{"PALAVROSO", "palavroso/services/lemmatization/"}};
	// palavroso charset
	private static final String CHARSET = "ISO-8859-1";

	public Palavroso() {
		super(new PalavrosoExecutionMode(COMMAND, ENVIRONMENT, CHARSET));
//		super(new PalavrosoThriftClientExecutionMode(ThriftExecutionMode.findFreePort()));
//		super(new PalavrosoThriftClientExecutionMode("wc09", 9090));
		
		// HACK: cannot refer to 'this' while explicitly invoking a constructor
		_mode.setTool(this);
	}

	public void start() {
		_mode.init();

		_mode.start();
	}

	@Override
	public void close() {
		_mode.close();
	}

	@Override
	public void tagg(MorphoSyntacticAnnotation a, List<String> input, List<String> output) {
		Segmentation segmentation = new Segmentation();
		MorphoSyntacticAnnotation annotation = new MorphoSyntacticAnnotation();
		
		int i = 0;
		int id = 0;
		
		for (String line : output) {
//			String id = token.getId();
//			String line = output.get(i++);
			
			String current = input.get(i);
			int offset = 0;
			
			// TODO: isto pode acontecer?
//			if (line.matches("\\s*")) {
//				continue;
//			}

//			String[] t1= line.replaceAll("\\s", "").split("\\)");

			PalavrosoSegment pseg = new PalavrosoSegment();
			List<PalavrosoToken> ptokens = pseg.process(line);
			
			for (PalavrosoToken ptoken : ptokens) {
//			for (String t2 : t1) {
//				if (t2.equals("")) {
//					continue;
//				}

//				MorphologicalUnit palavrosoToken = new PalavrosoToken(t2);
//				// TODO: ver se passou-se algo aqui
//				pseg.add(palavrosoToken);

//				WordFormAlternatives wfAlt = new WordFormAlternatives();

				int from = current.indexOf(ptoken.getForm(), offset);
				
				offset += from + ptoken.getForm().length();
				
				Segment segment = new Segment("t" + id++, String.valueOf(from), String.valueOf(from + ptoken.getForm().length()), ptoken.getForm());
				segmentation.addSegment(segment);
				
				if (ptoken.getLemmas() == null)
					continue;
				
				Classification classification = new Classification();
				classification.addSegment(segment.getId());
				
				for (Lemma lemma : ptoken.getLemmas()) {
//					List<Segment> t = new ArrayList<Segment>();
//					t.add(this.getToken(id));
//					t.add(segment);
//					wordForm.setAttribute("ref", segment.getAttributes().getNamedItem("id").getNodeValue());

					FeatureStructure fs = new FeatureStructure(); 

					// add lemma feature
					fs.addFeature("LEMMA", new Feature("LEMMA", lemma.getGStem()));
					
					// add features
					for (String name : lemma.getFeatures().keySet()) {
						String value = lemma.getFeatures().get(name);

						if (value != null) {
							Feature feature = new Feature();

							feature.setName(name);
							feature.setValue(value);

							fs.addFeature(name, feature);
						}
					}

					classification.addFeatureStructure(fs);

//					wfAlt.addWordForm(wordForm);
				}

//				wordFormAlternatives.add(wfAlt);
				annotation.addClassification(classification);
			}
		}
		
		this.addSegmentation(segmentation);
		this.addMorphoSyntacticAnnotation(annotation);
	}
}
