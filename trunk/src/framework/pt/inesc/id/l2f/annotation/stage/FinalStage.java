package pt.inesc.id.l2f.annotation.stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.zip.GZIPOutputStream;

import pt.inesc.id.l2f.annotation.document.xml.XMLWriter;
import pt.inesc.id.l2f.annotation.tool.IdentityTool;
import pt.inesc.id.l2f.annotation.unit.FinalProcessUnit;
import pt.inesc.id.l2f.annotation.unit.ProcessUnit;

public class FinalStage extends Stage {
	private boolean _compressed;
	
	private String _filename;
	
	public FinalStage(String filename) {
		super(new IdentityTool());
		
		_filename = filename;
		_compressed = false;
	}
	
	public FinalStage(String filename, boolean compressed) {
		super(new IdentityTool());

		_filename = filename;
		_compressed = compressed;
	}
	
	public void run() {
		OutputStream fos = null;
		XMLWriter xmlw = null;
		
		try {
			if (_compressed) {
				fos = new GZIPOutputStream(new FileOutputStream(_filename + ".gz"));
			} else {
				fos = new FileOutputStream(_filename);
			}
			
			xmlw = new XMLWriter(fos); 
			
			// write document start
			xmlw.writeStartDocument("UTF-8", "1.0");

			// write root element start
			xmlw.writeStartElement("units");
			
			while (true) {
				ProcessUnit unit = _input.take();
				
				if (unit instanceof FinalProcessUnit) {
					break;
				}
				
				unit.writeTo(xmlw);
			}
			
			// write root element end
			xmlw.writeEndElement();
			
			// write document end
			xmlw.writeEndDocument();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return _filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		_filename = filename;
	}
}
