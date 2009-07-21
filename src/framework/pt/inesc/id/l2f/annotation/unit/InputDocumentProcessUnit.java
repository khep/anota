package pt.inesc.id.l2f.annotation.unit;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import pt.inesc.id.l2f.annotation.document.xml.XMLReader;
import pt.inesc.id.l2f.annotation.document.xml.XMLWriter;
import pt.inesc.id.l2f.annotation.input.InputDocument;
import pt.inesc.id.l2f.annotation.tool.Tool;
import pt.inesc.id.l2f.annotation.tool.ToolVisitor;
import pt.inesc.id.l2f.annotation.tool.execution.ToolExecutionModeUnit;

public class InputDocumentProcessUnit extends ProcessUnit {
	
	private InputDocument _idoc;
	
	public InputDocumentProcessUnit(InputDocument idoc) {
		_idoc = idoc;
	}
	
	@Override
	public void accept(ToolVisitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public void execute(Tool tool, ToolExecutionModeUnit unit) {
		tool.process(this, unit);
	}

	/**
	 * 
	 * 
	 * @return the input document
	 */
	public InputDocument getInputDocument() {
		return _idoc;
	}

//	@Override
//	public void writeTo(Writer writer) {}

	@Override
	public void writeTo(XMLWriter xmlw) {}

	@Override
	public void readFrom(XMLReader xmlr) {
		
	}

	public void readFields(DataInput in) throws IOException {}

	public void write(DataOutput out) throws IOException {}
	
//	@Override
//	public void writeTo(String filename) {}
}
