/************************************************************************
 * 
 * Copyright (c) 2008 L2F (INESC-ID LISBOA)
 * All Rights Reserved.
 *
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF L2F (INESC-ID LISBOA)
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 * 
 ************************************************************************/
package pt.inesc.id.l2f.annotation.main;

import java.io.FileNotFoundException;
import java.io.FileReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;

import pt.inesc.id.l2f.annotation.document.laf.LinguisticAnnotationDocument;
import pt.inesc.id.l2f.annotation.document.xml.XMLReader;
import pt.inesc.id.l2f.annotation.execution.Directory;
import pt.inesc.id.l2f.annotation.execution.ExecutionMode;
import pt.inesc.id.l2f.annotation.execution.File;
import pt.inesc.id.l2f.annotation.execution.LafFile;
import pt.inesc.id.l2f.annotation.execution.LocalExecutionMode;
import pt.inesc.id.l2f.annotation.execution.RawFile;
import pt.inesc.id.l2f.annotation.execution.TeiFile;
import pt.inesc.id.l2f.annotation.execution.hadoop.HadoopExecutionMode;
import pt.inesc.id.l2f.annotation.input.InputDocument;
import pt.inesc.id.l2f.annotation.input.RawDocument;
import pt.inesc.id.l2f.annotation.input.TEIDocument;
import pt.inesc.id.l2f.annotation.stage.FinalStage;
import pt.inesc.id.l2f.annotation.tool.Tool;
import pt.inesc.id.l2f.annotation.tools.ar.ArabicPOSTagger;
import pt.inesc.id.l2f.annotation.tools.cn.ChinesePOSTagger;
import pt.inesc.id.l2f.annotation.tools.en.StanfordEnglishPOSTagger;
import pt.inesc.id.l2f.annotation.tools.jp.JapanesePOSTagger;
import pt.inesc.id.l2f.annotation.tools.pt.jmarv.JMARv;
import pt.inesc.id.l2f.annotation.tools.pt.palavroso.Palavroso;
import pt.inesc.id.l2f.annotation.tools.pt.rudrico.Rudrico;
import pt.inesc.id.l2f.annotation.unit.Dependencies;
import pt.inesc.id.l2f.annotation.unit.LinguisticAnnotationProcessUnit;

/**
 * 
 * 
 * 
 * @author Tiago Luis
 *
 */
public class Main {

	public static void main(String[] args) {
		ExecutionMode mode = null;

		// input option
		Option tinput = new Option("tei", true, "TEI document filename");
		tinput.setArgs(Option.UNLIMITED_VALUES);

		Option rinput = new Option("raw", true, "RAW document filename (one sentence per line)");
		rinput.setArgs(Option.UNLIMITED_VALUES);

		Option linput = new Option("laf", true, "LAF annotation filename");
		linput.setArgs(Option.UNLIMITED_VALUES);

		Option dependencies = new Option("dependencies", false, "LAF annotation dependencies");
		dependencies.setArgs(Option.UNLIMITED_VALUES);

		OptionGroup inputGroup = new OptionGroup();
		inputGroup.addOption(tinput);
		inputGroup.addOption(rinput);
		inputGroup.addOption(linput);
		inputGroup.setRequired(true);

		// output option
		Option output = new Option("output", true, "LAF annotation filename");
		output.setArgs(Option.UNLIMITED_VALUES);

		OptionGroup outputGroup = new OptionGroup();
		outputGroup.addOption(output);
		outputGroup.setRequired(true);

		// execution mode
		Option local = new Option("local", false, "local execution mode");
		Option parallel = new Option("parallel", false, "parallel execution mode");

		OptionGroup executionGroup = new OptionGroup();
		executionGroup.addOption(local);
		executionGroup.addOption(parallel);
		executionGroup.setRequired(true);

		// parallel execution mode options
		Option mappers = new Option("mappers", true, "mappers number");
		Option reducers = new Option("reducers", true, "reducers number");
		
		Option compressall = new Option("compressall", false, "compress intermediate and output");
		Option compressoutput = new Option("compressoutput", false, "compress output");
		Option compressintermediate = new Option("compressintermediate", false, "compress intermediate");

		// tools options
		Option english = new Option("english", false, "Stanford English POS Tagger");
		Option arabic = new Option("arabic", false, "Arabic POS Tagger");
		Option japanese = new Option("japanese", false, "Japanese POS Tagger");
		Option chinese = new Option("chinese", false, "Chinese POS Tagger");
		Option palavroso = new Option("palavroso", false, "Portuguese POS Tagger");
		Option rudrico = new Option("rudrico", false, "Portuguese Post....");
		Option jmarv = new Option("jmarv", false, "Portuguese Disambiguator");
		Option xip = new Option("xip", false, "Portuguese XIP Chain");

		OptionGroup toolsGroup = new OptionGroup();
		toolsGroup.addOption(english);
		toolsGroup.addOption(arabic);
		toolsGroup.addOption(japanese);
		toolsGroup.addOption(chinese);
		toolsGroup.addOption(palavroso);
		toolsGroup.addOption(rudrico);
		toolsGroup.addOption(jmarv);
		toolsGroup.addOption(xip);
		toolsGroup.setRequired(true);

		Options options = new Options();
		options.addOptionGroup(inputGroup);
		options.addOption(dependencies);
		options.addOptionGroup(outputGroup);
		options.addOptionGroup(executionGroup);
		options.addOption(mappers);
		options.addOption(reducers);
		options.addOption(compressall);
		options.addOption(compressoutput);
		options.addOption(compressintermediate);
		options.addOptionGroup(toolsGroup);

		// help printer
		HelpFormatter formatter = new HelpFormatter();

		Parser parser = new GnuParser();

		List<Tool> tools = new ArrayList<Tool>();

		try {
			CommandLine cl = parser.parse(options, args);

			if (cl.hasOption("palavroso")) {
				tools.add(new Palavroso());
			} else if (cl.hasOption("rudrico")) {
				tools.add(new Rudrico());
			} else if (cl.hasOption("jmarv")) {
				tools.add(new JMARv());
			} else if (cl.hasOption("xip")) {
				tools.add(new Palavroso());
				tools.add(new JMARv());
			} else if (cl.hasOption("english")) {
				tools.add(new StanfordEnglishPOSTagger());
			} else if (cl.hasOption("arabic")) {
				tools.add(new ArabicPOSTagger());
			} else if (cl.hasOption("japanese")) {
				tools.add(new JapanesePOSTagger());
			} else if (cl.hasOption("chinese")) {
				tools.add(new ChinesePOSTagger());
			}

			if (cl.hasOption("local")) {
				mode = new LocalExecutionMode(tools.toArray(new Tool[tools.size()]));

				// add final stage
				mode.addFinalStage(new FinalStage(cl.getOptionValue("output"), cl.hasOption("compress")));

				// start execution mode
				mode.start();

				Map<String, LinguisticAnnotationProcessUnit> units = new HashMap<String, LinguisticAnnotationProcessUnit>();

				if (cl.hasOption("laf")) {

					if (cl.hasOption("dependencies")) {
						for (Object value : cl.getOptionValues("dependencies")) {
							XMLReader xmlr = new XMLReader(new FileReader(value.toString()));

							while (true) {
								LinguisticAnnotationProcessUnit unit = readLinguisticAnnotationUnit(xmlr);

								if (unit == null) {
									break;
								}

								units.put(unit.getId(), unit);
							}
						}
					}

					XMLReader xmlr = new XMLReader(new FileReader(cl.getOptionValue("laf")));

					while (true) {
						LinguisticAnnotationProcessUnit unit = readLinguisticAnnotationUnit(xmlr);

						if (unit == null) {
							break;
						}

						LinguisticAnnotationDocument annotation = new LinguisticAnnotationDocument();

						Dependencies dep = new Dependencies();

						for (String dependency : unit.getDependencies()) {

							if (units.get(dependency) != null) {
								LinguisticAnnotationDocument doc = units.get(dependency).getDocument();
								
								annotation.merge(doc);

								dep.addDependency(dependency);
							} else {
								System.err.println("Dependency with id " + dependency + " does not exist.");
								System.exit(1);
							}
						}

						dep.addDependency(unit.getId());

						annotation.merge(unit.getDocument());

						mode.annotate(new LinguisticAnnotationProcessUnit(annotation, new LinguisticAnnotationDocument(), dep, unit.getAnnotationId(), unit.getStageNumber() + 1), null);
					}
				} else if (cl.hasOption("raw")) {
					List<InputDocument> list = new ArrayList<InputDocument>();

					for (Object value : cl.getOptionValues("raw")) {
						RawDocument doc = new RawDocument(value.toString());

						// add annotation document (after processing)
						list.add(doc);
					}

					mode.annotateInputDocuments(list, null);
				} else if (cl.hasOption("tei")) {
					List<InputDocument> list = new ArrayList<InputDocument>();

					for (Object value : cl.getOptionValues("tei")) {
						TEIDocument doc = new TEIDocument(value.toString());

						// add annotation document (after processing)
						list.add(doc);
					}

					mode.annotateInputDocuments(list, null);
				}

				mode.close();
			} else if (cl.hasOption("parallel")) {
				mode = new HadoopExecutionMode(tools.toArray(new Tool[tools.size()]));

				mode.start();

				((HadoopExecutionMode) mode).setMappers(Integer.valueOf(cl.getOptionValue("mappers", "2")));
				((HadoopExecutionMode) mode).setReducers(Integer.valueOf(cl.getOptionValue("reducers", "1")));

				if (cl.hasOption("compressall")) {
					((HadoopExecutionMode) mode).compressMapOutput();
					((HadoopExecutionMode) mode).compressFinalOutput();
				}
				
				if (cl.hasOption("compressintermediate")) {
					((HadoopExecutionMode) mode).compressMapOutput();
				}
				
				if (cl.hasOption("compressoutput")) {
					((HadoopExecutionMode) mode).compressFinalOutput();
				}

				if (cl.hasOption("raw")) {
					mode.annotateFile(new RawFile(cl.getOptionValue("raw")), new Directory(cl.getOptionValue("output")));
				} else if (cl.hasOption("tei")) {
					mode.annotateFile(new TeiFile(cl.getOptionValue("tei")), new Directory(cl.getOptionValue("output")));
				} else if (cl.hasOption("laf")) {
					List<File> files = new ArrayList<File>();
					
					for (Object value : cl.getOptionValues("laf")) {
						files.add(new LafFile(value.toString()));
					}
					
					mode.annotateFile(files, new Directory(cl.getOptionValue("output")));
				}

				mode.close();
			}
		} catch (ParseException e) {
			System.err.println(e.getMessage());
			formatter.printHelp("split", options);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// TODO: remover
	private static LinguisticAnnotationProcessUnit readLinguisticAnnotationUnit(XMLReader xmlr) {
		int event = -1;

		while (true) {
			event = xmlr.next();

			if (xmlr.isDocumentEnd(event)) {
				break;
			}

			if (xmlr.isElementStart(event)) {
				String name = xmlr.getElementName();

				if (name.equals("unit")) {
					Map<String, String> feature = xmlr.getAttributes();
					
					LinguisticAnnotationProcessUnit unit = new LinguisticAnnotationProcessUnit(feature.get("id"), feature.get("annotation"), Integer.valueOf(feature.get("stage")));
					unit.readFrom(xmlr);

					return unit;
				}
			}
		}

		return null;
	}
}
