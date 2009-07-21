package pt.inesc.id.l2f.annotation.tools.pt.palavroso;

import java.util.ArrayList;
import java.util.List;

public class PalavrosoSegment {
	List<PalavrosoToken> _tokens;
	
	public PalavrosoSegment() {
		_tokens = new ArrayList<PalavrosoToken>();
	}
	
//	List<MorphologicalUnit> _tokens = new ArrayList<MorphologicalUnit>();
//	
//	public MorphologicalUnit token(int i) {
//		return _tokens.get(i);
//	}
//
//	public void add(MorphologicalUnit t) {
//		_tokens.add(t);
//	}
//
//	public int size() {
//		return _tokens.size();
//	}
//
//	public MorphologicalUnit get(int i) {
//		return _tokens.get(i);
//	}
//
//	public void accept(SegmentVisitor visitor) {
//		visitor.visitPalavrosoSegment(this);
//	}
//
//	public void reset() {
//		_tokens = new ArrayList<MorphologicalUnit>();
//	}

	public List<PalavrosoToken> process(String analysis) {
		analysis = analysis.replaceAll("\n", "");
		
		String[] elements = analysis.split("\\s");
		
		for (int i = 0; i < elements.length; i++) {
			String word = elements[i++];
			
//			System.out.println("word: " + word);
			
			List<String> list = new ArrayList<String>();
			
			while (i < elements.length) {
				String element = elements[i];
				
//				System.out.println("classification: " + element);
				
				if (element.startsWith("(")) {
					element = element.substring("(".length());
				}
				
				list.add(element);
				
				if (element.endsWith(")")) {
					_tokens.add(new PalavrosoToken(word, list));
					
					break;
				}

				i++;
			}
		}
		
//		String pstring = "";
//		String[] elements = analysis.split(" ");
//		
//		int i;
//		
//		for (i = 0; i < elements.length; i++) {
//			pstring += elements[i] + " ";
//			
//			if (elements[i].startsWith("(")) {
//				
//				if (elements[i].length() == 1) {
//					break;
//				}
//				
//				for (i = i + 1; i < elements.length; i++) {
//					pstring += elements[i] + " ";
//					
//					if (elements[i].endsWith(")")) {
////						System.out.println("DEBUG: " + pstring);
//						
//						PalavrosoToken ptoken = new PalavrosoToken(pstring);
//						_tokens.add(ptoken);
//						
//						pstring = "";
//						
//						break;
//					}
//				}
//			}
//		}
		
		return _tokens;
	}
}
