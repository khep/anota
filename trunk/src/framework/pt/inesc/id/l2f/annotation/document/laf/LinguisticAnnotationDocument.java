package pt.inesc.id.l2f.annotation.document.laf;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.hadoop.io.Writable;

import pt.inesc.id.l2f.annotation.document.Document;
import pt.inesc.id.l2f.annotation.document.util.ListWritable;
import pt.inesc.id.l2f.annotation.document.xml.XMLReader;
import pt.inesc.id.l2f.annotation.document.xml.XMLWriter;

public class LinguisticAnnotationDocument implements Document, Writable {
	// ...
	private ListWritable<Segmentation> _segmentations;
	// ...
	private ListWritable<MorphoSyntacticAnnotation> _annotations;
	
	public LinguisticAnnotationDocument() {
		_segmentations = new ListWritable<Segmentation>();
		_annotations = new ListWritable<MorphoSyntacticAnnotation>();
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public List<Segmentation> getSegmentations() {
		return Collections.unmodifiableList(_segmentations);
	}
	
	public Segment getSegment(String id) {
		
		for (Segmentation segmentation : _segmentations) {
			if (segmentation.getSegment(id) != null) {
				return segmentation.getSegment(id);
			}
		}
		
		return null;
	}
	
	/**
	 * 
	 * 
	 * @return
	 * @throws NoSuchElementException
	 */
	public Segmentation getSegmentation() throws NoSuchElementException {
		return _segmentations.get(_segmentations.size() - 1);
	}

	/**
	 * 
	 * 
	 * @param segmentations
	 */
	public void setSegmentations(List<Segmentation> segmentations) {
		_segmentations = new ListWritable<Segmentation>();
		
		for (Segmentation segmentation : segmentations) {
			_segmentations.add(segmentation);
		}
	}
	
	/**
	 * 
	 * 
	 * @param segmentation
	 */
	public void addSegmentation(Segmentation segmentation) {
		_segmentations.add(segmentation);
	}
	
	/**
	 * 
	 * 
	 * @param annotation
	 */
	public void addMorphoSyntacticAnnotation(MorphoSyntacticAnnotation annotation) {
		_annotations.add(annotation);
	}

	/**
	 * 
	 * 
	 * @return
	 * @throws NoSuchElementException
	 */
	public MorphoSyntacticAnnotation getLastMorphoSyntacticAnnotation() throws NoSuchElementException {
		return _annotations.get(_annotations.size() - 1);
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public List<MorphoSyntacticAnnotation> getMorphoSyntacticAnnotations() {
		return Collections.unmodifiableList(_annotations);
	}
	
	public void merge(LinguisticAnnotationDocument doc) {
		
		for (Segmentation segmentation : doc.getSegmentations()) {
			_segmentations.add(segmentation);
		}
		
		for (MorphoSyntacticAnnotation annotation : doc.getMorphoSyntacticAnnotations()) {
			_annotations.add(annotation);
		}
	}

//	public void writeTo(Writer writer) {
//		XMLWriter xmlw = new XMLWriter(writer);
//		this.writeTo(xmlw);
//	}

	public void writeTo(XMLWriter xmlw) {
		// write root element start
		xmlw.writeStartElement("laf");
		
		// write segmentations
		if (_segmentations.size() > 0) {
			for (Segmentation segmentation : _segmentations) {
				segmentation.writeTo(xmlw);
			}
		}

		// write classifications of segments
		if (_annotations.size() > 0) {
			for (MorphoSyntacticAnnotation annotation : _annotations) {
				annotation.writeTo(xmlw);
			}
		}
		
		// write root element end
		xmlw.writeEndElement();
	}

//	public void readFrom(Reader reader) {
//		XMLReader xmlr = new XMLReader(reader);
//		this.readFrom(xmlr);
//	}

	public void readFrom(XMLReader xmlr) {
		int event = -1;
		
		while (true) {
			event = xmlr.next();
			
			if (xmlr.isElementEnd(event, "laf")) {
				break;
			}
			
			if (xmlr.isElementStart(event)) {
				String name = xmlr.getElementName();
				
				if (name.equals("edgeSet")) {
					Segmentation segmentation = new Segmentation();
					segmentation.readFrom(xmlr);
					_segmentations.add(segmentation);
				}
				
				if (name.equals("nodeSet")) {
					MorphoSyntacticAnnotation annotation = new MorphoSyntacticAnnotation();
					annotation.readFrom(xmlr);
					_annotations.add(annotation);
				}
			}
		}
	}

	public void readFields(DataInput in) throws IOException {
		_segmentations = new ListWritable<Segmentation>();
		_segmentations.readFields(in);
		
		_annotations = new ListWritable<MorphoSyntacticAnnotation>();
		_annotations.readFields(in);
	}

	public void write(DataOutput out) throws IOException {
		_segmentations.write(out);
		_annotations.write(out);
	}
}
