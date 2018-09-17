/**
 *
 * @author deiber
 */

package com.language.parser;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author deiber
 */
public class AnalizerCreator {
	
	public final static int GENERAR = 1;
	public final static int EJECUTAR = 2;
	public final static int SALIR = 3;

	public static void createAnalyzers() {

		String archLexico = "language/jflex/Scanner.flex";
		String archSintactico = "language/Cup/Parser.cup";
		String[] ALexico = { archLexico };
		String[] ASintactico = { "-parser", "Parser", archSintactico };
		jflex.Main.main(ALexico);
		try {
			java_cup.Main.main(ASintactico);
		} catch (Exception ex) {
			Logger.getLogger(AnalizerCreator.class.getName()).log(Level.SEVERE, null, ex);
		}
		// movemos los archivos generados
		boolean mvAL = moverArch("language/jflex/Scanner.java");
		boolean mvAS = moverArch("Parser.java");
		boolean mvSym = moverArch("sym.java");
		if (mvAL && mvAS && mvSym) {
			System.out.println("\n*** Analizadores creados sin problemas :) ***\n");
		}
	}
	
	public static void execute() {
		String[] Codigo = { "code.txt" };
		Parser.main(Codigo);
	}

	public static boolean moverArch(String archNombre) {
		boolean efectuado = false;
		File arch = new File(archNombre);
		if (arch.exists()) {
			//System.out.println("\n*** Moviendo " + arch + " ***\n");
			Path currentRelativePath = Paths.get("");
			String nuevoDir = currentRelativePath.toAbsolutePath().toString() + File.separator + "src" + File.separator
					+ "com" + File.separator + "language" + File.separator + "parser" + File.separator + arch.getName();
			File archViejo = new File(nuevoDir);
			archViejo.delete();
			if (arch.renameTo(new File(nuevoDir))) {
				System.out.println("*** Generado " + archNombre + "***\n");
				efectuado = true;
			} else {
				System.out.println("*** No movido " + archNombre + " ***\n");
			}

		} else {
			System.out.println("*** Codigo no existente ***\n");
		}
		return efectuado;
	}
}