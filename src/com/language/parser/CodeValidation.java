package com.language.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Codigo encargado del manero de variables y errores referentes al llamado de
 * las mismas
 * 
 * @author deiber
 *
 */
public class CodeValidation {

	public static List<String> listVariables = new ArrayList<String>();
	public static List<Integer> listValores = new ArrayList<Integer>();
	public static List<String> listExpresiones = new ArrayList<String>();
	public static List<String> listProcesos = new ArrayList<String>();
	public static List<Integer> listPosicion = Arrays.asList(0, 0, 0); // (Posicion X, Posicion Y, Orientacion)
	public static List<Integer> PosINICIAL = Arrays.asList(0, 0);

	public static boolean Corrida1 = true;

	static Integer arriba = 0;
	static Integer izq = 1;
	static Integer der = 2;
	static Integer abajo = 3;
	static Integer posicionX = 0;
	static Integer posicionY = 1;
	static Integer orientacion = 2;
	static Integer altura = 0;
	static Integer Id = -1;
	static Integer NoEncontrado = -1;
	public static Integer cantLuces = 0;

	public static Integer search(String buscado, List<String> listVariables) {

		Integer result = -1;
		for (int indice = 0; indice < listVariables.size(); indice++) {

			if (listVariables.get(indice).equals(buscado)) {
				result = indice;
			}
		}
		return result;
	}

	public static void var(String i) {

		var(i, 0);
	}

	public static void var(String i, Integer d) {

		if (listVariables.size() == 0) {
			listVariables.add(i);
			listValores.add(d);
		} else {
			Integer indice = search(i, listVariables);
			if (indice == NoEncontrado) {
				listVariables.add(i);
				listValores.add(d);
			} else {
				System.err.println("Error : Variable <" + i + "> is already exist");
				System.exit(1);
			}
		}
	}

	public static void set(String i, Integer d) {

		Integer indice = search(i, listVariables);
		if (indice != NoEncontrado) {
			listValores.set(indice, d);
		} else {
			System.err.println("Error : Variable <" + i + "> not exist");
			System.exit(1);
		}
	}

	public static void add(String i) {

		Integer indice = search(i, listVariables);
		if (indice != NoEncontrado) {
			listValores.set(indice, listValores.get(indice) + 1);
		} else {
			System.err.println("Error : Variable <" + i + "> not exist");
			System.exit(1);
		}
	}

	public static void less(String i) {

		Integer indice = search(i, listVariables);
		if (indice != NoEncontrado) {
			listValores.set(indice, listValores.get(indice) - 1);
		} else {
			System.err.println("Error : Variable <" + i + "> not exist");
			System.exit(1);
		}
	}

	public static void changeDir(String Orientation) {

		if (Orientation.equals("same")) {
		} else if (Orientation.equals("left") & listPosicion.get(orientacion) == arriba) {
			listPosicion.set(orientacion, izq);
			// CAMBIAR DIRECCION EN LEDS DE
			// ORIENTACION''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
			// EJEM: EN CADA UNO
			// static Integer arriba = 0;
			// static Integer izq = 1;
			// static Integer der = 2;
			// static Integer abajo = 3;
		} else if (Orientation.equals("left") & listPosicion.get(orientacion) == izq) {
			listPosicion.set(orientacion, abajo);
		} else if (Orientation.equals("left") & listPosicion.get(orientacion) == der) {
			listPosicion.set(orientacion, arriba);
		} else if (Orientation.equals("left") & listPosicion.get(orientacion) == abajo) {
			listPosicion.set(orientacion, der);
		} else if (Orientation.equals("right") & listPosicion.get(orientacion) == arriba) {
			listPosicion.set(orientacion, der);
		} else if (Orientation.equals("right") & listPosicion.get(orientacion) == izq) {
			listPosicion.set(orientacion, arriba);
		} else if (Orientation.equals("right") & listPosicion.get(orientacion) == der) {
			listPosicion.set(orientacion, abajo);
		} else if (Orientation.equals("right") & listPosicion.get(orientacion) == abajo) {
			listPosicion.set(orientacion, izq);
		} else if (Orientation.equals("back") & listPosicion.get(orientacion) == arriba) {
			listPosicion.set(orientacion, abajo);
		} else if (Orientation.equals("back") & listPosicion.get(orientacion) == izq) {
			listPosicion.set(orientacion, der);
		} else if (Orientation.equals("back") & listPosicion.get(orientacion) == der) {
			listPosicion.set(orientacion, izq);
		} else if (Orientation.equals("back") & listPosicion.get(orientacion) == abajo) {
			listPosicion.set(orientacion, arriba);
		}
	}

	public static void placeBlock() {

		if (altura != 0) {

			/// Integer posX =
			/// listPosicion.get(0);''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
			// Integer posY = listPosicion.get(1);
			// SIGUE EL LLAMADO A ENCENDER LED EN LA POSICION
			// (posX, posY) y tiene que meter "altura" tambien

			altura = 0;
		} else if (altura == 0) {
			// HACER LO MISMO
			/// Integer posX = listPosicion.get(0);
			// Integer posY = listPosicion.get(1);
			// SIGUE EL LLAMADO A ENCENDER LED EN LA POSICION
			// (posX, posY) "altura" es nivel 1

		}
	}

	public static void placeBlock(Integer n) {// CAMBIA DE POSICION CON CADA N

		if (listPosicion.get(orientacion) == arriba & listPosicion.get(posicionY) + n > 6) {
			System.err.println("Error : Place block <" + n + "> out of range");
			System.exit(1);
		} else if (listPosicion.get(orientacion) == der & listPosicion.get(posicionX) + n > 6) {
			System.err.println("Error : Place block <" + n + "> out of range");
			System.exit(1);
		} else if (listPosicion.get(orientacion) == izq & listPosicion.get(posicionX) - n < 0) {
			System.err.println("Error : Place block <" + n + "> out of range");
			System.exit(1);
		} else if (listPosicion.get(orientacion) == abajo & listPosicion.get(posicionY) - n < 0) {
			System.err.println("Error : Place block <" + n + "> out of range");
			System.exit(1);
		}

		if (listPosicion.get(orientacion) == arriba) {
			for (int i = 0; i < n; i++) {
				placeBlock();
				listPosicion.set(1, listPosicion.get(posicionY) + 1); // se mueve al bloque siguiente
			}
		} else if (listPosicion.get(orientacion) == abajo) {
			for (int i = 0; i < n; i++) {
				placeBlock();
				listPosicion.set(1, listPosicion.get(posicionY) - 1); // se mueve al bloque siguiente
			}
		} else if (listPosicion.get(orientacion) == izq) {
			for (int i = 0; i < n; i++) {
				placeBlock();
				listPosicion.set(0, listPosicion.get(posicionX) - 1); // se mueve al bloque siguiente
			}
		} else if (listPosicion.get(orientacion) == der) {
			for (int i = 0; i < n; i++) {
				placeBlock();
				listPosicion.set(0, listPosicion.get(posicionX) + 1); // se mueve al bloque siguiente
			}
		}
	}

	public static void highBlock() {

		altura = 2;
	}

	public static void highBlock(Integer n) {

		if (n == 1 | n == 2 | n == 3) {
			altura = n;
		} else {
			System.err.println("Error : Block height <" + n + "> not allowed");
			System.exit(1);
		}
	}

	public static void putLight() {

		Integer X = listPosicion.get(posicionX);
		Integer Y = listPosicion.get(posicionY);

		// LLAMA A LA CREACION DE LUZ EN (X,
		// Y)'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
		cantLuces += 1;
	}

	public static void pos(String x, String y) {

		Integer indiceX = search(x, listVariables);
		if (indiceX == NoEncontrado) {
			System.err.println("Error : Variable <" + x + "> not exist");
			System.exit(1);
		}
		Integer indiceY = search(y, listVariables);
		if (indiceY == NoEncontrado) {
			System.err.println("Error : Variable <" + y + "> not exist");
			System.exit(1);
		}

		Integer posX = listValores.get(search(x, listVariables));
		Integer posY = listValores.get(search(y, listVariables));

		if (posX < 0 | posX > 6) {
			System.err.println("Error : Variable <" + x + "> out of range of possible positions");
			System.exit(1);
		} else if (posY < 0 | posY > 6) {
			System.err.println("Error : Variable <" + y + "> out of range of possible positions");
			System.exit(1);
		} else {
			listPosicion.set(posicionX, posX);
			listPosicion.set(posicionY, posY);
		}
	}

	public static void posStart(String x, String y) {

		Integer indiceX = search(x, listVariables);
		if (indiceX == NoEncontrado) {
			System.err.println("Error : Variable <" + x + "> not exist");
			System.exit(1);
		}
		Integer indiceY = search(y, listVariables);
		if (indiceY == NoEncontrado) {
			System.err.println("Error : Variable <" + y + "> not exist");
			System.exit(1);
		}

		Integer posX = listValores.get(search(x, listVariables));
		Integer posY = listValores.get(search(y, listVariables));

		if (posX < 0 | posX > 6) {
			System.err.println("Error : Variable <" + x + "> out of range of possible positions");
			System.exit(1);
		} else if (posY < 0 | posY > 6) {
			System.err.println("Error : Variable <" + y + "> out of range of possible positions");
			System.exit(1);
		} else {
			PosINICIAL.set(0, posX);
			PosINICIAL.set(1, posY);

			// LLAMADO A EL POSTSTART DE SU MATRIZ DE
			// LEDS'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''

		}
	}

	public static void call(String proc) {

		Integer indice = search(proc, listProcesos);
		if (indice != NoEncontrado) {
			// llamar al
			// proc////////////////////////////////////////////////////////////////////////////////////////////////////////
		} else {
			System.err.println("Error : Process <" + proc + "> not exist");
			System.exit(1);
		}
	}

	// keep////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void forTimes(Integer nTimes) {

		Id = nTimes;

		for (Integer i = 0; i < nTimes; i++) {
			System.err.println("llamado a cada metodo de lista de expressions");
		}

	}

	public static void whenThen(Integer comparison) {

		if (Id == comparison) {

		}
	}
}