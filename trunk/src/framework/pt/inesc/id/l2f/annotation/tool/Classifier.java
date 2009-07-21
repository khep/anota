package pt.inesc.id.l2f.annotation.tool;

import java.util.ArrayList;
import java.util.List;

import pt.inesc.id.l2f.annotation.document.laf.MorphoSyntacticAnnotation;
import pt.inesc.id.l2f.annotation.document.laf.Segment;
import pt.inesc.id.l2f.annotation.document.laf.Segmentation;
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
public abstract class Classifier extends Tool {
	// current unit
	private LinguisticAnnotationProcessUnit _unit;

	public Classifier(ToolExecutionMode mode) {
		super(mode);
	}

	public void visit(InputDocumentProcessUnit unit) {
		_mode.input(this, unit);
	}

	public void visit(LinguisticAnnotationProcessUnit unit) {
		_mode.input(this, unit);
	}
	
	public void process(InputDocumentProcessUnit unit, ToolExecutionModeUnit result) {
		
		if (result != null) {
			// set current unit
			_unit = new LinguisticAnnotationProcessUnit();
			
			this.tagg(null, result.getInput(), result.getOutput());
			
			// put unit into next stage
			_stage.collect(_unit);
		} else {
			TextElement node = null;
			
			while ((node = unit.getInputDocument().next()) != null) {
				// set current unit
				_unit = new LinguisticAnnotationProcessUnit();
				
				List<String> input = new ArrayList<String>();
				
				String s = node.getText();

				if (s.matches("\\s+")) {
					continue;
				}

				input.add(s);
				
				this.tagg(null, input, null);
				
				// put unit into next stage
				_stage.collect(_unit);
			}
		}
	}

	@Override
	public void process(LinguisticAnnotationProcessUnit unit, ToolExecutionModeUnit result) {

//		if (result != null) {
			List<String> input = null;
			List<String> output = null;

			if (result != null) {
				input = result.getInput();
				output = result.getOutput();
			}
			
			// set current unit
			_unit = unit;

			MorphoSyntacticAnnotation morphoSyntacticAnnotation = unit.getDocument().getLastMorphoSyntacticAnnotation();
			
			this.tagg(morphoSyntacticAnnotation, input, output);

//			unit.merge(_unit.getDocument());

			_stage.collect(unit);
//		} else {
//			// TODO: lançar excepção própria
//			throw new RuntimeException("Operation not supported");
//		}
	}
	
	/**
	 * 
	 * 
	 * @param segmentation
	 */
	protected void addSegmentation(Segmentation segmentation) {
		_unit.addSegmentation(segmentation);
	}
	
	/**
	 * 
	 * 
	 * @param id
	 * @return
	 */
	protected Segment getSegment(String id) {
		return _unit.getDocument().getSegment(id);
	}

	/**
	 * 
	 * 
	 * @param morphoSyntacticAnnotation
	 */
	protected void addMorphoSyntacticAnnotation(MorphoSyntacticAnnotation morphoSyntacticAnnotation) {
		_unit.addMorphoSyntacticAnnotation(morphoSyntacticAnnotation);
	}

	/**
	 * 
	 * 
	 * @param input
	 * @param output
	 */
	protected abstract void tagg(MorphoSyntacticAnnotation morphoSyntacticAnnotation, List<String> input, List<String> output);
}
