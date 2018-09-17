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
//----------------------------------------------------

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
	private void RunCode(ActionEvent event) {
		sendMessage("Iniciando :)"); // este es un metodo publico de este controller que puede imprimir en consola
										// del IDE.
		// Lo puede usar para errores y eso. O ver si funciona el parser y verificar que
		// este haciendo algo.

//		saveSingleFile();		Metodo que guarde el codigo de la ventana e invoque a mi analizador 
		//falta que agarre codigo del ide y lo ponga en el txt llamado code
		
		
//		AnalizerCreator.createAnalyzers(); //creacion de analizadores
		
		System.out.println("no he entrado, la lista de variables es: " + CodeValidation.listVariables + " y la de valores es: " + CodeValidation.listValores);
		AnalizerCreator.execute(); // ejecucion del compilador
		System.out.println("luego de primera corrida, la lista de variables es: " + CodeValidation.listVariables + " y la de valores es: " + CodeValidation.listValores);
		CodeValidation.Corrida1 = false;
		AnalizerCreator.execute();
		System.out.println("luego de segunda corrida, la lista de variables es: " + CodeValidation.listVariables + " y la de valores es: " + CodeValidation.listValores);
		if (CodeValidation.cantLuces == 0) {
			System.err.println("Error : Don't exist lights");
			System.exit(1);
		}
		
		//LLAMADO A ENVIAR LISTA CON leds A ENCENDER // 
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
		// run: `cleanupWhenNoLongerNeedIt.unsubscribe();`
	}

	private static StyleSpans<Collection<String>> computeHighlighting(String text) {
		Matcher matcher = PATTERN.matcher(text);
		int lastKwEnd = 0;
		StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
		while (matcher.find()) {
			String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                    matcher.group("MAINWORD") != null ? "mainword" :
                    matcher.group("PAREN") != null ? "paren" :
                    matcher.group("BRACKET") != null ? "bracket" :
                    matcher.group("SEMICOLON") != null ? "semicolon" :
                    matcher.group("COMMENT") != null ? "comment" :
                    null; /* never happens */ assert styleClass != null;
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
		    		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				    		new FileOutputStream(selectedFile), "utf-8"))) {
				    	writer.write(codeArea.getText());
				    } catch (IOException e) {
				    	e.printStackTrace();
				    }
				    localFile = selectedFile.getAbsolutePath();
		    	} else {
			    	File tempFile = new File(selectedFile.getAbsolutePath() + ".txt");
				    try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				    		new FileOutputStream(tempFile), "utf-8"))) {
				    	writer.write(codeArea.getText());
				    } catch (IOException e) {
				    	e.printStackTrace();
				    }
				    localFile = tempFile.getAbsolutePath();
		    	}
		    }
		} else {
			File temp = new File(localFile);
			 try (Writer writer = new BufferedWriter(new OutputStreamWriter(
			    		new FileOutputStream(temp), "utf-8"))) {
			    	writer.write(codeArea.getText());
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
		}
	}
	
	private void closeFiles() {
		localFile = "";
		codeArea.clear();
		console.clear();
	}
//??????????????????????????????????????????????????????????	

//	Este es el metodo print en consola.
	public void sendMessage(String mesg) {
		String temp = console.getText() + ">>" + mesg + '\n';
		console.setText(temp);
	}
}
