package pt.inesc.id.l2f.annotation.tool;

import pt.inesc.id.l2f.annotation.document.laf.LinguisticAnnotationDocument;
import pt.inesc.id.l2f.annotation.document.laf.Segmentation;
import pt.inesc.id.l2f.annotation.input.InputDocument;
import pt.inesc.id.l2f.annotation.input.TextElement;
import pt.inesc.id.l2f.annotation.tool.execution.ToolExecutionMode;
import pt.inesc.id.l2f.annotation.tool.execution.ToolExecutionModeUnit;
import pt.inesc.id.l2f.annotation.unit.InputDocumentProcessUnit;
import pt.inesc.id.l2f.annotation.unit.LinguisticAnnotationProcessUnit;

/**
 * 
 * 
 * @author Tiago Luis
 *
 */
public abstract class Tokenizer extends Tool {

	public Tokenizer(ToolExecutionMode mode) {
		super(mode);
	}

	public void visit(InputDocumentProcessUnit unit) {
		_mode.input(this, unit);
	}

	public void visit(LinguisticAnnotationProcessUnit unit) {
		// TODO: criar excepção
		throw new RuntimeException("...");
	}

	//	public void visit(SyntacticProcessUnit unit) {
	//		// TODO: criar excepção
	//		throw new RuntimeException("...");
	//	}

	public void process(InputDocumentProcessUnit unit, ToolExecutionModeUnit result) {
		InputDocument idoc = unit.getInputDocument();

		LinguisticAnnotationDocument odoc = new LinguisticAnnotationDocument();

		// TODO: retirar esta dependência do DOM (Node)
		TextElement node = null;

		while ((node = idoc.next()) != null) {
			@SuppressWarnings("unused")
			String id = "none";
			String text = node.getText();

			if (text.matches("\\s+")) {
				continue;
			}

			// TODO: colocar id como atributo no text node
			//				if ((node.getAttributes() != null) && (node.getAttributes().getNamedItem("id") != null)) {
			//				id = node.getAttributes().getNamedItem("id").getNodeValue();
			//				}

			this.tokenize(text, odoc);
		}
		
		this.getStage().collect(new LinguisticAnnotationProcessUnit());
	}

	@Override
	public void process(LinguisticAnnotationProcessUnit unit, ToolExecutionModeUnit result) {
		// TODO: criar excepção
		throw new RuntimeException("...");
	}

	private void tokenize(String input, LinguisticAnnotationDocument odoc) {
		String id = "none";

		//		if (inode.getAttributes().getNamedItem("id") != null) {
		//		id = inode.getAttributes().getNamedItem("id").getNodeValue(); 
		//		}

		// get tokens from input string
		Segmentation segmentation = this.tokenize(input, id);

		// add tokens to output document
		odoc.addSegmentation(segmentation);
	}

	/**
	 * ...
	 * 
	 * @param input
	 * @param id
	 * 
	 * @return
	 */
	protected abstract Segmentation tokenize(String input, String id);
}
