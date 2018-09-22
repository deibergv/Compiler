package com.GUI.window;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

import com.language.parser.AnalizerCreator;
import com.language.parser.CodeValidation;
import com.language.parser.Command;

public class MainController {

//---Donde se definen los tokens y palabras reservadas. 
	private static final String[] KEYWORDS = new String[] { "Var", "Set", "Add", "Less", "ChangeDir", "Place", "High",
			"Block", "Put Light", "Pos", "Keep", "Skip", "Kend", "For", "Times", "Fend", "When", "Then", "Whend",
			"PosStart", "Call" };

	private static final String[] MAINWORDS = new String[] { "Proc", "End-Proc", "Begin", "End" };

	private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
	private static final String PAREN_PATTERN = "\\(|\\)";
	private static final String BRACKET_PATTERN = "\\[|\\]";
	private static final String SEMICOLON_PATTERN = "\\;";
	private static final String COMMENT_PATTERN = "//[^\n]*";
	private static final String MAINWORD_PATTERN = "\\b(" + String.join("|", MAINWORDS) + ")\\b";
	private static final Pattern PATTERN = Pattern.compile("(?<KEYWORD>" + KEYWORD_PATTERN + ")" + "|(?<MAINWORD>"
			+ MAINWORD_PATTERN + ")" + "|(?<PAREN>" + PAREN_PATTERN + ")" + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
			+ "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")" + "|(?<COMMENT>" + COMMENT_PATTERN + ")");

	private final String fileTemplate = "\n//Incluir aqui nombre y funcionalidad del codigo.\n\nBegin\n\nEnd\n\nProc name\n\n//Expressions\n\nEnd-Proc;";
// ----------------------------------------------------

//  Carga algunos elementos graficos fxml de fxml. 
	private String localFile = "";

	@FXML
	private TextArea console;

	@FXML
	private CodeArea codeArea;

//	Los controllers para el menu bar de arriba.
	@FXML
	private void SearchDirectory(ActionEvent event) {
		showSingleFileChooser();
	}

	@FXML
	private void SaveFile(ActionEvent event) {
		saveSingleFile();
	}

	@FXML
	private void CloseFile(ActionEvent event) {
		closeFiles();
	}

//************Atencion***************
//	Aqui es la opcion del menu bar donde se hace el run para el codigo, donde se tiene que cambiar para eso. 
	@FXML
	private void RunCode(ActionEvent event) throws InterruptedException {

//		sendMessage("Iniciando :)"); 
//		AnalizerCreator.createAnalyzers(); //creacion de analizadores

		File tempFile = new File("code.txt");
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile), "utf-8"))) {
			writer.write(codeArea.getText());
		} catch (IOException e) {
			e.printStackTrace();
		}

		CodeValidation.listVariables.add("Id");
		CodeValidation.listValores.add(-1);
		AnalizerCreator.execute(); // Primera ejecucion del compilador, para inicializacion de Variables y Procesos
		CodeValidation.Corrida1 = false;
		AnalizerCreator.execute(); // Segunda corrida comprobacion de expresiones
		if (CodeValidation.cantLuces == 0) {
			System.err.println("Error : There are no existing lights"); // Error en caso de no existir luces
			System.exit(1);
		}

		// LLAMADO A ENVIAR LISTA CON leds A ENCENDER //
		for (Command cmd : CodeValidation.listaComandos) {
			if (cmd.action.equals("clear")) {
				if (cmd.id.equals("der")) {
					CodeValidation.matrix.clearDirectionLed(2);
				} else if (cmd.id.equals("izq")) {
					CodeValidation.matrix.clearDirectionLed(1);
				} else if (cmd.id.equals("arriba")) {
					CodeValidation.matrix.clearDirectionLed(0);
				} else if (cmd.id.equals("abajo")) {
					CodeValidation.matrix.clearDirectionLed(3);
				} else {
					CodeValidation.matrix.clearLed(cmd.x, cmd.y, cmd.id);
				}
			} else if (cmd.action.equals("ON")) {
				if (cmd.id.equals("der")) {
					CodeValidation.matrix.setDirectionLed(2);
				} else if (cmd.id.equals("izq")) {
					CodeValidation.matrix.setDirectionLed(1);
				} else if (cmd.id.equals("arriba")) {
					CodeValidation.matrix.setDirectionLed(0);
				} else if (cmd.id.equals("abajo")) {
					CodeValidation.matrix.setDirectionLed(3);
				} else {
					CodeValidation.matrix.turnOnLed(cmd.x, cmd.y, cmd.id);
				}
			}
		}
		
		if (CodeValidation.listPosicion.get(2) == 2); {
			CodeValidation.matrix.clearDirectionLed(2);
		}
		if (CodeValidation.listPosicion.get(2) == 3); {
			CodeValidation.matrix.clearDirectionLed(3);
		}
		if (CodeValidation.listPosicion.get(2) == 0); {
			CodeValidation.matrix.clearDirectionLed(0);
		}
		if (CodeValidation.listPosicion.get(2) == 1); {
			CodeValidation.matrix.clearDirectionLed(1);
		}
		CodeValidation.ModoJuego = true;
	}

//?????Importante pero no es necesario modificarlo o algo asi.
	@FXML
	public void initialize() {

		codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));

		// recompute the syntax highlighting 500 ms after user stops editing area
		Subscription cleanupWhenNoLongerNeedIt = codeArea
				// plain changes = ignore style changes that are emitted when syntax
				// highlighting is reapplied
				// multi plain changes = save computation by not rerunning the code multiple
				// times
				// when making multiple changes (e.g. renaming a method at multiple parts in
				// file)
				.multiPlainChanges()
				// do not emit an event until 500 ms have passed since the last emission of
				// previous stream
				.successionEnds(Duration.ofMillis(500))
				// run the following code block when previous stream emits an event
				.subscribe(ignore -> codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText())));
		// when no longer need syntax highlighting and wish to clean up memory leaks
		codeArea.replaceText(fileTemplate);
	}

	private static StyleSpans<Collection<String>> computeHighlighting(String text) {
		Matcher matcher = PATTERN.matcher(text);
		int lastKwEnd = 0;
		StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
		while (matcher.find()) {
			String styleClass = matcher.group("KEYWORD") != null ? "keyword"
					: matcher.group("MAINWORD") != null ? "mainword"
							: matcher.group("PAREN") != null ? "paren"
									: matcher.group("BRACKET") != null ? "bracket"
											: matcher.group("SEMICOLON") != null ? "semicolon"
													: matcher.group("COMMENT") != null ? "comment" : null;
			/* never happens */ assert styleClass != null;
			spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
			spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
			lastKwEnd = matcher.end();
		}
		spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
		return spansBuilder.create();
	}

	private void showSingleFileChooser() {

		FileChooser fileChooser = new FileChooser();
		ExtensionFilter filter = new ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.setTitle("Select FIle");
		fileChooser.getExtensionFilters().add(filter);
		File selectedFile = fileChooser.showOpenDialog(null);

		if (selectedFile != null) {
			try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {

				String total = "";
				String line;
				while ((line = reader.readLine()) != null) {
					total += (line + '\n');
				}
				codeArea.replaceText(total);
				localFile = selectedFile.getAbsolutePath();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void saveSingleFile() {
		if (localFile.isEmpty()) {
			FileChooser fileChooser = new FileChooser();
			ExtensionFilter filter = new ExtensionFilter("TXT files (*.txt)", "*.txt");
			fileChooser.setTitle("Save File");
			fileChooser.getExtensionFilters().add(filter);
			File selectedFile = fileChooser.showSaveDialog(null);
			if (selectedFile != null) {

				if (selectedFile.getAbsolutePath().endsWith(".txt")) {
					try (Writer writer = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(selectedFile), "utf-8"))) {
						writer.write(codeArea.getText());
					} catch (IOException e) {
						e.printStackTrace();
					}
					localFile = selectedFile.getAbsolutePath();
				} else {
					File tempFile = new File(selectedFile.getAbsolutePath() + ".txt");
					try (Writer writer = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(tempFile), "utf-8"))) {
						writer.write(codeArea.getText());
					} catch (IOException e) {
						e.printStackTrace();
					}
					localFile = tempFile.getAbsolutePath();
				}
			}
		} else {
			File temp = new File(localFile);
			try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(temp), "utf-8"))) {
				writer.write(codeArea.getText());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void closeFiles() {
		localFile = "";
		codeArea.replaceText(fileTemplate);
		;
		console.clear();
	}
//??????????????????????????????????????????????????????????	

//	Este es el metodo print en consola.
	public void sendMessage(String mesg) {
		String temp = console.getText() + ">>" + mesg + '\n';
		console.setText(temp);
	}
}
