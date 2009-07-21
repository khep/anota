package pt.inesc.id.l2f.annotation.execution;

import java.util.ArrayList;
import java.util.List;

import pt.inesc.id.l2f.annotation.document.laf.LinguisticAnnotationDocument;
import pt.inesc.id.l2f.annotation.input.InputDocument;
import pt.inesc.id.l2f.annotation.input.TextDocument;
import pt.inesc.id.l2f.annotation.stage.Stage;
import pt.inesc.id.l2f.annotation.tool.Tool;
import pt.inesc.id.l2f.annotation.unit.InputDocumentProcessUnit;
import pt.inesc.id.l2f.annotation.unit.LinguisticAnnotationProcessUnit;

public class LocalExecutionMode extends ExecutionMode {
	
	public LocalExecutionMode(Stage ... stages) {
		super(stages);
	}
	
	public LocalExecutionMode(List<Stage> stages) {
		super(stages);
	}

	public LocalExecutionMode(Tool ... tools) {
		super(tools);
	}

	@Override
	public void start() {
		super.start();
	}

	@Override
	public void close() {
		super.close();
	}

	@Override
	public void annotateText(Text input, Path output) {
		Stage first = _stages.getFirst();

		// add ....
		first.process(new InputDocumentProcessUnit(new TextDocument(input.getText())));
	}
	
	@Override
	public void annotateText(List<Text> input, Path output) {
		Stage first = _stages.getFirst();

		// add ....
		List<String> list = new ArrayList<String>();
		
		for (Text text : input) {
			list.add(text.getText());
		}
		
		first.process(new InputDocumentProcessUnit(new TextDocument(list)));
	}

	@Override
	public void annotateFile(File input, Path output) {

	}

	@Override
	public void annotateText(Directory input, Path output) {

	}

	@Override
	public void annotate(List<LinguisticAnnotationDocument> annotations, Path output) {
//		Stage first = _stages.getFirst();
//
//		// create document that will contain all....
//		LinguisticAnnotationDocument document = new LinguisticAnnotationDocument();
//		
//		for (LinguisticAnnotationDocument doc : annotations) {
//			document.merge(doc);
//		}
//		
//		first.process(new LinguisticAnnotationProcessUnit(document, new LinguisticAnnotationDocument()));
	}

	@Override
	public void annotateInputDocuments(List<InputDocument> documents, Path output) {
		Stage first = _stages.getFirst();
		
		for (InputDocument doc : documents) {
			first.process(new InputDocumentProcessUnit(doc));
		}
	}

	@Override
	public void annotate(LinguisticAnnotationProcessUnit annotation, Path output) {
		Stage first = _stages.getFirst();
		
		first.process(annotation);
	}

	@Override
	public void annotateFile(List<File> input, Path output) {
		
	}
}
