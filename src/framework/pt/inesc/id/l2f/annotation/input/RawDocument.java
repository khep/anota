package pt.inesc.id.l2f.annotation.input;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class RawDocument extends InputDocument {
	// ...
	private BufferedReader _br;
	
	public RawDocument(String filename) throws FileNotFoundException {
		_br = new BufferedReader(new FileReader(filename));
	}
	
	@Override
	public TextElement next() {
		
		try {
			String line = _br.readLine();
			
			if (line != null) {
				return new TextElement(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
