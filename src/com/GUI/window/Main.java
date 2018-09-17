package com.GUI.window;
	
import com.language.parser.AnalizerCreator;
import com.language.parser.CodeValidation;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.getIcons().add(new Image("/images/icon.png"));
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
			Scene scene = new Scene(root,600,600);
			scene.getStylesheets().add(getClass().getResource("gui.css").toExternalForm());
			primaryStage.setTitle("LightBotCC");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		int opcion = 3;
		java.util.Scanner in = new java.util.Scanner(System.in);
		do {
	        System.out.println("Seleccione una opción:");
	        System.out.println("1) Generar analizadores");
	        System.out.println("2) IDE");
	        System.out.println("3) Salir");
	        System.out.print("Opcion: ");
	        opcion = in.nextInt();
	        switch (opcion) {
	            case 1: {
	            	AnalizerCreator.createAnalyzers();
	            	System.exit(0);
	            }
	            case 2: {
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
	        		break;
	            }
	            case 3: {
	                System.out.println("¡Programa finalizado!\nVuelva pronto.");
	                break;
	            }
	            default: {
	                System.out.println("Opcion no valida.");
	                break;
	            }
	        }
	    } while (opcion != 3);
		System.exit(0);
		
		
		
		
		
//		launch(args);
	}
}
