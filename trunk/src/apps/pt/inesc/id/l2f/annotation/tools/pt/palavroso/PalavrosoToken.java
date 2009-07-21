package pt.inesc.id.l2f.annotation.tools.pt.palavroso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.inesc.id.l2f.annotation.tools.pt.Lemma;
import pt.inesc.id.l2f.annotation.tools.pt.MorphologicalUnit;

public class PalavrosoToken extends MorphologicalUnit {
	private static Pattern pattern = Pattern.compile("\\[([^\\s]+)\\]([^\\s]+)?");
	
	// MOOD translation hash table
	private static Map<String, String> _pos3 = new HashMap<String, String>();

	// TENSE translation hash table
	private static Map<String, String> _pos4 = new HashMap<String, String>();

	// PERSON translation hash table
	private static Map<String, String> _pos5 = new HashMap<String, String>();

	// NUMBER translation hash table
	private static Map<String, String> _pos6 = new HashMap<String, String>();

	// GENDER translation hash table
	private static Map<String, String> _pos7 = new HashMap<String, String>();

	// DEGREE translation hash table
	private static Map<String, String> _pos8 = new HashMap<String, String>();

	// CASE translation hash table
	private static Map<String, String> _pos9 = new HashMap<String, String>();

	// FORMATION translation hash table
	private static Map<String, String> _pos10 = new HashMap<String, String>();

	// NOUN subcategory values
	private static Map<String, String> _pos2nou = new HashMap<String, String>();

	// PRONOUN subcategory values
	private static Map<String, String> _pos2pro = new HashMap<String, String>();

	// ARTICLE subcategory values
	private static Map<String, String> _pos2art = new HashMap<String, String>();

	// CONJUNCTION subcategory values
	private static Map<String, String> _pos2con = new HashMap<String, String>();

	// NUMERAL subcategory values
	private static Map<String, String> _pos2num = new HashMap<String, String>();

	// RESIDUAL subcategory values
	private static Map<String, String> _pos2res = new HashMap<String, String>();

	static {
		// initialize MOOD hash table
		_pos3.put("i", "ind");
		_pos3.put("s", "sbj");
		_pos3.put("m", "imp");
		_pos3.put("c", "cnd");
		_pos3.put("n", "inf");
		_pos3.put("f", "iif");
		_pos3.put("p", "par");
		_pos3.put("g", "ger");
		_pos3.put("=", "=");

		// initialize TENSE hash table
		_pos4.put("p", "prs");
		_pos4.put("i", "pim");
		_pos4.put("f", "fut");
		_pos4.put("s", "ppe");
		_pos4.put("q", "pmp");
		_pos4.put("=", "=");

		// initialize PERSON hash table
		_pos5.put("1", "1");
		_pos5.put("2", "2");
		_pos5.put("3", "3"); 
		_pos5.put("=", "=");

		// initialize NUMBER hash table
		_pos6.put("s", "s");
		_pos6.put("p", "p");
		_pos6.put("=", "=");

		// initialize GENDER hash table
		_pos7.put("m", "m");
		_pos7.put("f", "f");
		_pos7.put("=", "=");

		// initialize DEGREE hash table
		_pos8.put("p", "pst");
		_pos8.put("c", "cmp");
		_pos8.put("s", "sup");
		_pos8.put("=", "=");

		// initialize CASE hash table
		_pos9.put("n", "nom");
		_pos9.put("a", "acc"); 
		_pos9.put("d", "dat");
		_pos9.put("=", "=");

		// initialize FORMATION hash table
		_pos10.put("s", "sim");
		_pos10.put("f", "fus");
		_pos10.put("=", "=");

		// initialize NOUN subcategory values
		_pos2nou.put("c", "com");
		_pos2nou.put("p", "prp");

		// initialize PRONOUN subcategory values
		_pos2pro.put("p", "per");
		_pos2pro.put("d", "dem");
		_pos2pro.put("i", "idf");
		_pos2pro.put("o", "pos");
		_pos2pro.put("t", "itr");
		_pos2pro.put("r", "rel");
		_pos2pro.put("e", "exc");
		_pos2pro.put("f", "ref");

		// initialize ARTICLE subcategory values
		_pos2art.put("d", "def");
		_pos2art.put("i", "idf");

		// initialize CONJUNCTION subcategory values
		_pos2con.put("c", "coo");
		_pos2con.put("s", "sub");

		// initialize NUMERAL subcategory values
		_pos2num.put("c", "car");
		_pos2num.put("o", "ord");
		_pos2num.put("r", "roc");
		_pos2num.put("z", "roo");
		_pos2num.put("=", "=");

		// initialize RESIDUAL subcategory values
		_pos2res.put("f", "lwr");
		_pos2res.put("a", "abb");
		_pos2res.put("y", "acr");
		_pos2res.put("s", "sym");
		_pos2res.put("e", "ema");
		_pos2res.put("h", "htt");
		_pos2res.put("i", "ipp");
	}

	public PalavrosoToken(String form, List<String> lemmas) {
		super();

		super.setForm(form);
		
		for (String lstring : lemmas) {
			Matcher matcher = pattern.matcher(lstring);
		    boolean matchFound = matcher.find();
		    
		    if (matchFound) {
		    	Lemma lemma = new Lemma(matcher.group(1));
		    	
		    	char[] classification = matcher.group(2).toCharArray();
		    	
		    	lemma.setFeature("POS", String.valueOf(classification[0]) + String.valueOf(classification[1]));
		    	
		    	// process classification
				this.processFeatures(lemma, classification);
				
				// add lemma
				super.getLemmas().add(lemma);
		    }
		}
		
		
//		// input: 	a maria comeu
//		// output: 	a ([a]Pd..=sf.== [a]S....==..s [a]Td...sf... [a]Pp..3sf.as [a]Nc...sm...)
//		// 			maria ([maria]Nc...sf... [maria]A....sfp..) comeu ([comer]V.is3s=...)
//
//		// input:	a maria far-se-lhos-ia todos
//		// output:	a ([a]Pd..=sf.== [a]S....==..s [a]Td...sf... [a]Pp..3sf.as [a]Nc...sm...)
//		//			maria ([maria]Nc...sf... [maria]A....sfp..)
//		//			far-se-lhos-ia ([fazer]V.c=3s=... -[se]Pf..3==.a=+[se]U.........+[se]Pi..===.== -[lhe]Pp..3==.d= -[o]Pp..3pm.a=)
//		//			todos ([todos]Pi..=pm.== [todo]Nc...pm... [todo]A....pmp..)
//
////		System.out.println("analysis: " + analysis);
//
//		if (analysis.startsWith("[ ([[]O.........)")) {
//			super.setForm("[");
//
//			Lemma lemma = new Lemma("[");
//			this.processFeatures(lemma, "O.........".toCharArray());
//			super.getLemmas().add(lemma);
//		} else if (analysis.startsWith("] ([]]O.........)")) {
//			super.setForm("]");
//
//			Lemma lemma = new Lemma("]");
//			this.processFeatures(lemma, "O.........".toCharArray());
//			super.getLemmas().add(lemma);
//		} else if (analysis.startsWith("( ([(]O.........)")) {
//			super.setForm("(");
//
//			Lemma lemma = new Lemma("(");
//			this.processFeatures(lemma, "O.........".toCharArray());
//			super.getLemmas().add(lemma);
//		} else if (analysis.startsWith(") ([)]O.........)")) {
//			super.setForm(")");
//
//			Lemma lemma = new Lemma(")");
//			this.processFeatures(lemma, "O.........".toCharArray());
//			super.getLemmas().add(lemma);
//		} else {
//			analysis = analysis.replaceAll("\\s", "");
//			
//			String toks1[] = analysis.split("\\(");
//
//			String toks2[] = null;
//
//			super.setForm(toks1[0]);
//
//			toks2 = toks1[1].split("\\[");
//
//			// toks2[0] is empty (always)			
//
//			for(int i = 1; i < toks2.length; i++) {
//				String toks3[] = toks2[i].split("\\]");
//
////				System.out.println("Stem: " + toks3[0]);
//				Lemma lemma = new Lemma(toks3[0]);
//				
////				System.out.println("DEBUG: " + toks3[0]);
//				
////				System.out.println("DEBUG (lemma): " + toks3[0]);
//				
////				System.out.println("Cat: " + toks3[1].toCharArray()[0]);
////				lemma.set_cat(new String(toks3[1].toCharArray(), 0, 1));
////				System.out.println("SubCat: " + toks3[1].toCharArray()[1]);
////				lemma.set_subcat(new String(toks3[1].toCharArray(), 1, 1));
////				System.out.println("Feats:" + toks3[1]);
////				lemma.set_feature("feats", toks3[1]);
//
//				this.processFeatures(lemma, toks3[1].toCharArray());
//
//				super.getLemmas().add(lemma);
//			}
//		}
	}

	private void processFeatures(Lemma lemma, char[] tagg) {

		// transforms a tag in a multiple PROP/value elements
		if (tagg[0] == 'N') {
			lemma.setFeature("CAT", "nou");

			if (tagg[1] != '=') { lemma.setFeature("SCT", _pos2nou.get(String.valueOf(tagg[1]))); }
			if (tagg[5] != '=') { lemma.setFeature("NUM", _pos6.get(String.valueOf(tagg[5]))); }
			if (tagg[6] != '=') { lemma.setFeature("GEN", _pos7.get(String.valueOf(tagg[6]))); }
		} else if (tagg[0] == 'V') {
			lemma.setFeature("CAT", "ver");

			if (tagg[2] != '=') { lemma.setFeature("MOD", _pos3.get(String.valueOf(tagg[2]))); } 
			if (tagg[3] != '=') { lemma.setFeature("TEN", _pos4.get(String.valueOf(tagg[3]))); }
			if (tagg[4] != '=') { lemma.setFeature("PER", _pos5.get(String.valueOf(tagg[4]))); }
			if (tagg[5] != '=') { lemma.setFeature("NUM", _pos6.get(String.valueOf(tagg[5]))); }
			if (tagg[6] != '=') { lemma.setFeature("GEN", _pos7.get(String.valueOf(tagg[6]))); }
		} else if (tagg[0] == 'A') {
			lemma.setFeature("CAT", "adj");

			if (tagg[5] != '=') { lemma.setFeature("NUM", _pos6.get(String.valueOf(tagg[5]))); }
			if (tagg[6] != '=') { lemma.setFeature("GEN", _pos7.get(String.valueOf(tagg[6]))); }
			if (tagg[7] != '=') { lemma.setFeature("DEG", _pos8.get(String.valueOf(tagg[7]))); }
		} else if (tagg[0] == 'P') {
			lemma.setFeature("CAT", "pro");

			if (tagg[1] != '=') { lemma.setFeature("SCT", _pos2pro.get(String.valueOf(tagg[1]))); }
			if (tagg[4] != '=') { lemma.setFeature("PER", _pos5.get(String.valueOf(tagg[4]))); }
			if (tagg[5] != '=') { lemma.setFeature("NUM", _pos6.get(String.valueOf(tagg[5]))); }
			if (tagg[6] != '=') { lemma.setFeature("GEN", _pos7.get(String.valueOf(tagg[6]))); }
			if (tagg[8] != '=') { lemma.setFeature("CAS", _pos9.get(String.valueOf(tagg[8]))); }
			if (tagg[9] != '=') { lemma.setFeature("FOR", _pos10.get(String.valueOf(tagg[9]))); }
		} else if (tagg[0] == 'T') {
			lemma.setFeature("CAT", "art");

			if (tagg[1] != '=') { lemma.setFeature("SCT", _pos2art.get(String.valueOf(tagg[1]))); }
			if (tagg[5] != '=') { lemma.setFeature("NUM", _pos6.get(String.valueOf(tagg[5]))); }
			if (tagg[6] != '=') { lemma.setFeature("GEN", _pos7.get(String.valueOf(tagg[6]))); }
		} else if (tagg[0] == 'R') {
			lemma.setFeature("CAT", "adv");

			if (tagg[7] != '=') { lemma.setFeature("DEG", _pos8.get(String.valueOf(tagg[7]))); }
		} else if (tagg[0] == 'S') {
			lemma.setFeature("CAT", "pre");

			if (tagg[5] != '=') { lemma.setFeature("NUM", _pos6.get(String.valueOf(tagg[5]))); }
			if (tagg[6] != '=') { lemma.setFeature("GEN", _pos7.get(String.valueOf(tagg[6]))); }
			if (tagg[9] != '=') { lemma.setFeature("FOR", _pos10.get(String.valueOf(tagg[9]))); }
		} else if (tagg[0] == 'C') {
			lemma.setFeature("CAT", "con");

			if (tagg[6] != '=') { lemma.setFeature("SCT", _pos2con.get(String.valueOf(tagg[1]))); } // result += "        <id atrib="SCT" value="". $POS2CON{tagg[1]} . ""/>" . "\n";};
		} else if (tagg[0] == 'M') {
			lemma.setFeature("CAT", "num");

			if (tagg[1] != '=') { lemma.setFeature("SCT", _pos2num.get(String.valueOf(tagg[1]))); }
			if (tagg[5] != '=') { lemma.setFeature("NUM", _pos6.get(String.valueOf(tagg[5]))); }
			if (tagg[6] != '=') { lemma.setFeature("GEN", _pos7.get(String.valueOf(tagg[6]))); }
		} else if (tagg[0] == 'I') {
			lemma.setFeature("CAT", "int");
		} else if (tagg[0] == 'U') {
			lemma.setFeature("CAT", "pmk");
		} else if (tagg[0] == 'X') {
			lemma.setFeature("CAT", "res");

			if (tagg[6] != '=') {
				lemma.setFeature("SCT", _pos2res.get(String.valueOf(tagg[1])));
			}
		} else if (tagg[0] == 'O') {
			lemma.setFeature("CAT", "pun");
		} else {
			// TODO: definir uma excepção
			throw new RuntimeException("Error in CAT");
		}
	}
}
