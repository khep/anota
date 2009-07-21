package pt.inesc.id.l2f.annotation.unit;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import pt.inesc.id.l2f.annotation.document.xml.XMLReader;
import pt.inesc.id.l2f.annotation.document.xml.XMLWriter;
import pt.inesc.id.l2f.annotation.tool.Tool;
import pt.inesc.id.l2f.annotation.tool.ToolVisitor;
import pt.inesc.id.l2f.annotation.tool.execution.ToolExecutionModeUnit;

public class FinalProcessUnit extends ProcessUnit {
	
	public FinalProcessUnit() {
		super(true);
	}

	@Override
	public void accept(ToolVisitor visitor) {
		// TODO: ver isto.....................
	}
	
	@Override
	public void execute(Tool tool, ToolExecutionModeUnit unit) {
		// TODO: ver isto.....................
	}

	@Override
	public void writeTo(XMLWriter xmlw) {}
	
	@Override
	public void readFrom(XMLReader xmlr) {}

	public void readFields(DataInput in) throws IOException {}

	public void write(DataOutput out) throws IOException {}
}
