package com.language.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author deiber
 */
public class Main {

    /**
     * Muestra un menu para elegir la opcion
     * mas conveniente
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int opcion = 3;
        java.util.Scanner in = new java.util.Scanner(System.in);
        do {
            System.out.println("Seleccione una opción:");
            System.out.println("1) Generar analizador lexico");
            System.out.println("2) Ejecutar");
            System.out.println("3) Salir");
            System.out.print("Opcion: ");
            opcion = in.nextInt();
            switch (opcion) {
                case 1: {
                    System.out.println("\n*** Generando ***\n");
                    String archLex = "";
                    if(args.length > 0){
                        System.out.println("\n*** Procesando archivo custom ***\n");
                        archLex = args[0];
                    }else{
                        System.out.println("\n*** Procesando archivo default ***\n");
                        archLex = "language/jflex/Scanner.flex";
                    }
//                    String[] entrada = {archLex};
                    String[] entrada = {"language/jflex/Scanner.flex"};
                    jflex.Main.main(entrada);
                    //Movemos el archivo generado al directorio src
//                    File arch = new File("src/com/language/parser/Scanner.java");
                    File arch = new File("language/jflex/Scanner.java");
                    if(arch.exists()){
                        System.out.println("" + arch);
                        Path currentRelativePath = Paths.get("");
                        String nuevoDir = currentRelativePath.toAbsolutePath().toString()
                                + File.separator + "src" + File.separator 
                                + "com" + File.separator + "language" + File.separator 
                                + "parser"+ File.separator+arch.getName();
                        File archViejo = new File(nuevoDir);
                        archViejo.delete();
                        if(arch.renameTo(new File(nuevoDir))){
                            System.out.println("\n*** Generado ***\n");
                            System.out.println("\n*** Saliendo automaticamente ***\n");
                            System.exit(0);
                        }else{
                            System.out.println("\n*** No generado ***\n");
                        }
                    }else{
                        System.out.println("\n*** Codigo no existente ***\n");
                    }
                    break;
                }
                case 2: {
                    String entrada = "";
                    System.out.println("\n*** Ejecutando ***\n");
                    if(args.length > 0){
                        System.out.println("\n*** Procesando archivo custom ***\n");
                        entrada = args[0];
                    }else{
                        System.out.println("\n*** Procesando archivo default ***\n");
                        entrada = "entrada.txt";
                    }
                    BufferedReader bf = null;
                    try {
                        bf = new BufferedReader(new FileReader(entrada));
                        Scanner a = new Scanner(bf);
                        Yytoken token = null;
                        do {
                            token = a.nextToken();
                            System.out.println(token);
                        } while (token != null);
                    } catch (Exception ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        try {
                            bf.close();
                        } catch (IOException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    System.out.println("\n*** Ejecucion finalizada ***\n");
                    break;
                }
                case 3: {
                    System.out.println("Programa finalizado!\nVuelva pronto.");
                    break;
                }
                default: {
                    System.out.println("Opcion no valida.");
                    break;
                }
            }
        } while (opcion != 3);
    }

}
