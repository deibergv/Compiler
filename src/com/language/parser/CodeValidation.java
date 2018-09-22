package com.language.parser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.language.parser.Scan;
import com.language.parser.Yytoken;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.language.parser.Scan;
import com.language.parser.Yytoken;

/**
 * Codigo encargado del manero de variables y errores referentes al llamado de
 * las mismas
 * 
 * @author deiber
 *
 */
public class CodeValidation {

	public static List<String> listVariables = new ArrayList<String>(); // Lista de Variables existentes
	public static List<Integer> listValores = new ArrayList<Integer>(); // Lista de Valores de las Variables
	public static List<Integer> listPosicion = Arrays.asList(0, 0, 0, 0); // (Posicion X, Posicion Y, Orientacion, Luz)
	public static List<Integer> PosINICIAL = Arrays.asList(0, 0, 0, 0); // Posiciones
	// X,Y iniciales del robot
	public static List<String> listExpresiones = new ArrayList<String>(); // Lista AUXILIAR para llenado de lista que
																			// parsea procesos y bucles
	public static List<String> listProcesos = new ArrayList<String>(); // Lista de Procesos existentes
	public static List<List<String>> listExprProc = new ArrayList<List<String>>(); // Lista de Listas que contiene Sub
																					// lista de expresiones de los
																					// procesos
	static List<String> listMovimiento = new ArrayList<String>();
	public static List<Integer> listAEnviar = new ArrayList<Integer>();

	List<String> listaTraducida = new ArrayList<String>(); // Traduccion
	public static ArrayList<Command> listaComandos = new ArrayList();

	public static ArrayList<Integer> LucesX = new ArrayList();
	public static ArrayList<Integer> LucesY = new ArrayList();

	public static boolean Corrida1 = true;
	static boolean ObteniendoExpr;
	static boolean NoEntrar;
	public static boolean ModoJuego;

	static Integer arriba = 1;
	static Integer izq = 3;
	static Integer der = 0;
	static Integer abajo = 2;
	static Integer posicionX = 0;
	static Integer posicionY = 1;
	static Integer orientacion = 2;
	static Integer altura = 1;
	// static Integer Id = -1;
	static Integer NoEncontrado = -1;
	public static Integer cantLuces = 0;
	public static Matrix matrix;
	static Integer luz = 0; // Si es 0 la luz objetivo esta apagada en ese bloque, si es 1 esta encendida en
							// ese bloque
	public static List<Integer> listPosicionCopia = Arrays.asList(posicionX, posicionY, orientacion, altura, luz);

	ServerSocket server;
	Socket socket;
	int puerto = 8080;
	DataOutputStream salida;
	BufferedReader entrada;

	public CodeValidation() throws InterruptedException {
		matrix = new Matrix(0, 0);
		listaComandos.add(new Command(150, "der", "ON"));
	}

	public void crearTxt(String lista) throws IOException, InterruptedException {
		File archivo = null;
		try {
			listMovimiento.clear();
			archivo = new File("/home/deiber/GitKraken-Projects/LightBotIDE/testo.txt");
			FileWriter escribir = new FileWriter(archivo, true);
			escribir.write(lista);
			escribir.close();
		} catch (Exception e) {
			System.out.println("Error al escribir");
		}
		String entrada = "";
		entrada = "/home/deiber/GitKraken-Projects/LightBotIDE/testo.txt";
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new FileReader(entrada));
			Scan a = new Scan(bf);
			Yytoken token = null;
			do {
				token = a.nextToken();
				System.out.println(token);
			} while (token != null);
		} catch (Exception ex) {
			Logger.getLogger(CodeValidation.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				bf.close();
			} catch (IOException ex) {
				Logger.getLogger(CodeValidation.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		// System.out.println(CodeValidation.listMovimiento);
		if (archivo.exists()) {
			archivo.delete();
		}
		juegoApp(listMovimiento);

	}

	public void init() throws InterruptedException {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
//					System.out.println("SERVER");
					server = new ServerSocket(puerto);
					while (true) {
						socket = new Socket();
						socket = server.accept();
						entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						String mensaje = entrada.readLine();
						crearTxt(mensaje);
						salida = new DataOutputStream(socket.getOutputStream());
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	public void juegoApp(List<String> lista) throws InterruptedException {

		Integer luces = cantLuces;
		List<Integer> listaAUX = new ArrayList<Integer>(PosINICIAL);

		if (ModoJuego == false) {
			System.err.println("Error : The game is not started");
			System.exit(1);
		}
		matrix.clearDirectionLed(matrix.direction);

		for (int i = 0; i < LucesX.size() - 1; i++) {
			matrix.turnOnLed(LucesX.get(i), LucesY.get(i), "light");
		}
		matrix.clearLed(matrix.currentPosX, matrix.currentPosY, "player");
		matrix.currentPosX = 0;
		matrix.currentPosY = 0;
		matrix.turnOnLed(matrix.currentPosX, matrix.currentPosY, "player");
		matrix.setDirectionLed(2);
		matrix.direction = 2;

		for (int i = 0; i <= lista.size() - 1; i++) {
			String dato;
			dato = lista.get(i);
			if (dato.equals("Forward")) {
				if (listaAUX.get(orientacion) == arriba & listaAUX.get(posicionY) > 6)
					System.err.println("Error : out of range");
				else if (listaAUX.get(orientacion) == der & listaAUX.get(posicionX) > 6)
					System.err.println("Error : out of range");
				else if (listaAUX.get(orientacion) == izq & listaAUX.get(posicionX) < 0)
					System.err.println("Error : out of range");
				else if (listaAUX.get(orientacion) == abajo & listaAUX.get(posicionY) < 0)
					System.err.println("Error : out of range");

				if (listaAUX.get(orientacion) == arriba) {
					Integer X = listaAUX.get(posicionX);
					Integer Y = listaAUX.get(posicionY);
					if (Y + 1 <= 6) {
						Integer alturaActual = matrix.listaLeds.get(matrix.positionMatrix[X][Y]).getHeight();
						Integer alturaSiguiente = matrix.listaLeds.get(matrix.positionMatrix[X][Y + 1]).getHeight();
						if (alturaActual == alturaSiguiente) {
							matrix.clearLed(listaAUX.get(posicionX), listaAUX.get(posicionY), "player");
							listaAUX.set(posicionY, listaAUX.get(posicionY) + 1);
							matrix.turnOnLed(listaAUX.get(posicionX), listaAUX.get(posicionY), "player");
						} else {
							System.err.println("Error : unable to continue to right");
//							System.exit(1);
						}
					}

				} else if (listaAUX.get(orientacion) == abajo) {
					Integer X = listaAUX.get(posicionX);
					Integer Y = listaAUX.get(posicionY);
					if (Y - 1 >= 0) {
						Integer alturaActual = matrix.listaLeds.get(matrix.positionMatrix[X][Y]).getHeight();
						Integer alturaSiguiente = matrix.listaLeds.get(matrix.positionMatrix[X][Y - 1]).getHeight();
						if (alturaActual == alturaSiguiente) {
							matrix.clearLed(listaAUX.get(posicionX), listaAUX.get(posicionY), "player");
							listaAUX.set(posicionY, listaAUX.get(posicionY) - 1);
							matrix.turnOnLed(listaAUX.get(posicionX), listaAUX.get(posicionY), "player");
						} else {
							System.err.println("Error : unable to continue to down");
//							System.exit(1);
						}

					}

				} else if (listaAUX.get(orientacion) == der) {
					Integer X = listaAUX.get(posicionX);
					Integer Y = listaAUX.get(posicionY);
					if (X + 1 <= 6) {
						Integer alturaActual = matrix.listaLeds.get(matrix.positionMatrix[X][Y]).getHeight();
						Integer alturaSiguiente = matrix.listaLeds.get(matrix.positionMatrix[X + 1][Y]).getHeight();
						if (alturaActual == alturaSiguiente) {
							matrix.clearLed(listaAUX.get(posicionX), listaAUX.get(posicionY), "player");
							listaAUX.set(posicionX, listaAUX.get(posicionX) + 1);
							matrix.turnOnLed(listaAUX.get(posicionX), listaAUX.get(posicionY), "player");
						} else {
							System.err.println("Error : unable to continue to right");
//							System.exit(1);
						}

					}

				} else if (listaAUX.get(orientacion) == izq) {
					Integer X = listaAUX.get(posicionX);
					Integer Y = listaAUX.get(posicionY);
					if (X - 1 >= 0) {
						Integer alturaActual = matrix.listaLeds.get(matrix.positionMatrix[X][Y]).getHeight();
						Integer alturaSiguiente = matrix.listaLeds.get(matrix.positionMatrix[X - 1][Y]).getHeight();
						if (alturaActual == alturaSiguiente) {
							matrix.clearLed(listaAUX.get(posicionX), listaAUX.get(posicionY), "player");
							listaAUX.set(posicionX, listaAUX.get(posicionX) - 1);
							matrix.turnOnLed(listaAUX.get(posicionX), listaAUX.get(posicionY), "player");
						} else {
							System.err.println("Error : unable to continue to left");
//							System.exit(1);
						}

					}

				}
				// CHANGE DIR //
			} else if (dato.equals("Left")) {
				if (listaAUX.get(orientacion) == arriba) {
					listaAUX.set(orientacion, izq);
					matrix.direction = 1;
					matrix.clearDirectionLed(0);
					matrix.setDirectionLed(1);
				} else if (listaAUX.get(orientacion) == izq) {
					listaAUX.set(orientacion, abajo);
					matrix.direction = 1;
					matrix.clearDirectionLed(1);
					matrix.setDirectionLed(3);
				} else if (listaAUX.get(orientacion) == abajo) {
					listaAUX.set(orientacion, der);
					matrix.direction = 1;
					matrix.clearDirectionLed(3);
					matrix.setDirectionLed(2);
				} else if (listaAUX.get(orientacion) == der) {
					listaAUX.set(orientacion, arriba);
					matrix.direction = 1;
					matrix.clearDirectionLed(2);
					matrix.setDirectionLed(0);
				}
			} else if (dato.equals("Right")) {
				if (listaAUX.get(orientacion) == arriba) {
					listaAUX.set(orientacion, der);
					matrix.direction = 1;
					matrix.clearDirectionLed(0);
					matrix.setDirectionLed(2);
				} else if (listaAUX.get(orientacion) == der) {
					listaAUX.set(orientacion, abajo);
					matrix.direction = 1;
					matrix.clearDirectionLed(2);
					matrix.setDirectionLed(3);
				} else if (listaAUX.get(orientacion) == abajo) {
					listaAUX.set(orientacion, izq);
					matrix.direction = 1;
					matrix.clearDirectionLed(3);
					matrix.setDirectionLed(1);
				} else if (listaAUX.get(orientacion) == izq) {
					listaAUX.set(orientacion, arriba);
					matrix.direction = 1;
					matrix.clearDirectionLed(1);
					matrix.setDirectionLed(0);
				}
			} else if (dato.equals("Jump")) {
				if (listaAUX.get(orientacion) == arriba & listaAUX.get(posicionY) > 6) {
					System.err.println("Error : out of range");
				} else if (listaAUX.get(orientacion) == der & listaAUX.get(posicionX) > 6) {
					System.err.println("Error : out of range");
				} else if (listaAUX.get(orientacion) == izq & listaAUX.get(posicionX) < 0) {
					System.err.println("Error : out of range");
				} else if (listaAUX.get(orientacion) == abajo & listaAUX.get(posicionY) < 0) {
					System.err.println("Error : out of range");
				}
				if (listaAUX.get(orientacion) == arriba) {
					Integer X = listaAUX.get(posicionX);
					Integer Y = listaAUX.get(posicionY);
					if (Y + 1 <= 6) {
						Integer alturaActual = matrix.listaLeds.get(matrix.positionMatrix[X][Y]).getHeight();
						Integer alturaSiguiente = matrix.listaLeds.get(matrix.positionMatrix[X][Y + 1]).getHeight();
						if (alturaActual == alturaSiguiente + 2 || alturaActual == alturaSiguiente + 1
								|| alturaActual == alturaSiguiente - 1) {
							if (alturaSiguiente != 0) {
								matrix.clearLed(listaAUX.get(posicionX), listaAUX.get(posicionY), "player");
								listaAUX.set(posicionY, listaAUX.get(posicionY) + 1);
								matrix.turnOnLed(listaAUX.get(posicionX), listaAUX.get(posicionY), "player");
							}
							matrix.clearLed(listaAUX.get(posicionX), listaAUX.get(posicionY), "player");
							listaAUX.set(posicionY, listaAUX.get(posicionY) + 1);
							matrix.turnOnLed(listaAUX.get(posicionX), listaAUX.get(posicionY), "player");
						} else {
							System.err.println("Error : unable to continue to up in jump");
//							System.exit(1);
						}
					}

				} else if (listaAUX.get(orientacion) == abajo) {

					Integer X = listaAUX.get(posicionX);
					Integer Y = listaAUX.get(posicionY);
					if (Y - 1 >= 0) {
						Integer alturaActual = matrix.listaLeds.get(matrix.positionMatrix[X][Y]).getHeight();
						Integer alturaSiguiente = matrix.listaLeds.get(matrix.positionMatrix[X][Y - 1]).getHeight();
						if (alturaActual == alturaSiguiente + 2 || alturaActual == alturaSiguiente + 1
								|| alturaActual == alturaSiguiente - 1) {
							if (alturaSiguiente != 0) {
								matrix.clearLed(listaAUX.get(posicionX), listaAUX.get(posicionY), "player");
								listaAUX.set(posicionY, listaAUX.get(posicionY) - 1);
								matrix.turnOnLed(listaAUX.get(posicionX), listaAUX.get(posicionY), "player");
							}
						} else {
							System.err.println("Error : unable to continue to down in jump");
//							System.exit(1);
						}

					}

				} else if (listaAUX.get(orientacion) == izq) {
					Integer X = listaAUX.get(posicionX);
					Integer Y = listaAUX.get(posicionY);
					if (X - 1 >= 0) {
						Integer alturaActual = matrix.listaLeds.get(matrix.positionMatrix[X][Y]).getHeight();
						Integer alturaSiguiente = matrix.listaLeds.get(matrix.positionMatrix[X - 1][Y]).getHeight();
						if (alturaActual == alturaSiguiente + 2 || alturaActual == alturaSiguiente + 1
								|| alturaActual == alturaSiguiente - 1) {
							if (alturaSiguiente != 0) {
								matrix.clearLed(listaAUX.get(posicionX), listaAUX.get(posicionY), "player");
								listaAUX.set(posicionX, listaAUX.get(posicionX) - 1);
								matrix.turnOnLed(listaAUX.get(posicionX), listaAUX.get(posicionY), "player");
							}
						} else {
							System.err.println("Error : unable to continue to left in jump");
//							System.exit(1);
						}

					}

				} else if (listaAUX.get(orientacion) == der) {

					Integer X = listaAUX.get(posicionX);
					Integer Y = listaAUX.get(posicionY);
					if (X + 1 <= 6) {
						Integer alturaActual = matrix.listaLeds.get(matrix.positionMatrix[X][Y]).getHeight();
						Integer alturaSiguiente = matrix.listaLeds.get(matrix.positionMatrix[X + 1][Y]).getHeight();
						if (alturaActual == alturaSiguiente + 2 || alturaActual == alturaSiguiente + 1
								|| alturaActual == alturaSiguiente - 1) {
							if (alturaSiguiente != 0) {
								matrix.clearLed(listaAUX.get(posicionX), listaAUX.get(posicionY), "player");
								listaAUX.set(posicionX, listaAUX.get(posicionX) + 1);
								matrix.turnOnLed(listaAUX.get(posicionX), listaAUX.get(posicionY), "player");
							}
						} else {
							System.err.println("Error : Unable to continue to right in jump");
//							System.exit(1);
						}

					}

				}
			} else if (dato.equals("Light")) {

				if (matrix.listaLeds
						.get(matrix.positionMatrix[listaAUX.get(posicionX)][listaAUX.get(posicionY)]).objective) {
					matrix.listaLeds.get(matrix.positionMatrix[listaAUX.get(posicionX)][listaAUX.get(posicionY)])
							.clearObjective();
					matrix.clearLed(listaAUX.get(posicionX), listaAUX.get(posicionY), "light");
					luces -= 1;
				} else {
					System.err.println("Error : Light not exist");
//					System.exit(1);
				}

			}
		}
		matrix.currentPosX = listaAUX.get(posicionX);
		matrix.currentPosY = listaAUX.get(posicionY);

		if (luces == 0) {
			System.out.println("★░░░░░░░░░░░████░░░░░░░░░░░░░░░░░░░░★\n" + "★░░░░░░░░░███░██░░░░░░░░░░░░░░░░░░░░★\n"
					+ "★░░░░░░░░░██░░░█░░░░░░░░░░░░░░░░░░░░★\n" + "★░░░░░░░░░██░░░██░░░░░░░░░░░░░░░░░░░★\n"
					+ "★░░░░░░░░░░██░░░███░░░░░░░░░░░░░░░░░★\n" + "★░░░░░░░░░░░██░░░░██░░░░░░░░░░░░░░░░★\n"
					+ "★░░░░░░░░░░░██░░░░░███░░░░░░░░░░░░░░★\n" + "★░░░░░░░░░░░░██░░░░░░██░░░░░░░░░░░░░★\n"
					+ "★░░░░░░░███████░░░░░░░██░░░░░░░░░░░░★\n" + "★░░░░█████░░░░░░░░░░░░░░███░██░░░░░░★\n"
					+ "★░░░██░░░░░████░░░░░░░░░░██████░░░░░★\n" + "★░░░██░░████░░███░░░░░░░░░░░░░██░░░░★\n"
					+ "★░░░██░░░░░░░░███░░░░░░░░░░░░░██░░░░★\n" + "★░░░░██████████░███░░░░░░░░░░░██░░░░★\n"
					+ "★░░░░██░░░░░░░░████░░░░░░░░░░░██░░░░★\n" + "★░░░░███████████░░██░░░░░░░░░░██░░░░★\n"
					+ "★░░░░░░██░░░░░░░████░░░░░██████░░░░░★\n" + "★░░░░░░██████████░██░░░░███░██░░░░░░★\n"
					+ "★░░░░░░░░░██░░░░░████░███░░░░░░░░░░░★\n" + "★░░░░░░░░░█████████████░░░░░░░░░░░░░★\n"
					+ "★░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░★\n");
			System.exit(1);
		} else {
			System.err.println("Try again :c");
		}

	}

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

	public static void changeDir(String Orientation) throws InterruptedException {

		if (Orientation.equals("same")) {
//			changeDirLed(149, "der");
		} else if (Orientation.equals("left") & listPosicion.get(orientacion) == arriba) {
			listPosicion.set(orientacion, izq);
//			changeDirLed(149, "izq");
			listaComandos.add(new Command(149, "arriba", "clear"));
			listaComandos.add(new Command(147, "izq", "ON"));
			matrix.setDirection(izq);
		} else if (Orientation.equals("left") & listPosicion.get(orientacion) == izq) {
			listPosicion.set(orientacion, abajo);
//			changeDirLed(147, "abajo");
			listaComandos.add(new Command(147, "izq", "clear"));
			listaComandos.add(new Command(148, "abajo", "ON"));
			matrix.setDirection(abajo);
		} else if (Orientation.equals("left") & listPosicion.get(orientacion) == der) {
			listPosicion.set(orientacion, arriba);
//			changeDirLed(150, "arriba");
			listaComandos.add(new Command(150, "der", "clear"));
			listaComandos.add(new Command(149, "arriba", "ON"));
			matrix.setDirection(arriba);
		} else if (Orientation.equals("left") & listPosicion.get(orientacion) == abajo) {
			listPosicion.set(orientacion, der);
//			changeDirLed(148, "der");
			listaComandos.add(new Command(148, "abajo", "clear"));
			listaComandos.add(new Command(150, "der", "ON"));
			matrix.setDirection(der);
		} else if (Orientation.equals("right") & listPosicion.get(orientacion) == arriba) {
			listPosicion.set(orientacion, der);
//			changeDirLed(149, "der");
			listaComandos.add(new Command(149, "arriba", "clear"));
			listaComandos.add(new Command(150, "der", "ON"));
			matrix.setDirection(der);
		} else if (Orientation.equals("right") & listPosicion.get(orientacion) == izq) {
			listPosicion.set(orientacion, arriba);
//			changeDirLed(147, "arriba");
			listaComandos.add(new Command(147, "izq", "clear"));
			listaComandos.add(new Command(149, "arriba", "ON"));
			matrix.setDirection(arriba);
		} else if (Orientation.equals("right") & listPosicion.get(orientacion) == der) {
			listPosicion.set(orientacion, abajo);
//			changeDirLed(150, "abajo");
			listaComandos.add(new Command(150, "der", "clear"));
			listaComandos.add(new Command(148, "abajo", "ON"));
			matrix.setDirection(abajo);
		} else if (Orientation.equals("right") & listPosicion.get(orientacion) == abajo) {
			listPosicion.set(orientacion, izq);
//			changeDirLed(148, "izq");
			listaComandos.add(new Command(148, "abajo", "clear"));
			listaComandos.add(new Command(147, "izq", "ON"));
			matrix.setDirection(izq);
		} else if (Orientation.equals("back") & listPosicion.get(orientacion) == arriba) {
			listPosicion.set(orientacion, abajo);
//			changeDirLed(149, "abajo");
			listaComandos.add(new Command(149, "arriba", "clear"));
			listaComandos.add(new Command(148, "abajo", "ON"));
			matrix.setDirection(abajo);
		} else if (Orientation.equals("back") & listPosicion.get(orientacion) == izq) {
			listPosicion.set(orientacion, der);
//			changeDirLed(147, "der");
			listaComandos.add(new Command(147, "izq", "clear"));
			listaComandos.add(new Command(150, "der", "ON"));
			matrix.setDirection(der);
		} else if (Orientation.equals("back") & listPosicion.get(orientacion) == der) {
			listPosicion.set(orientacion, izq);
//			changeDirLed(150, "izq");
			listaComandos.add(new Command(150, "der", "clear"));
			listaComandos.add(new Command(147, "izq", "ON"));
			matrix.setDirection(izq);
		} else if (Orientation.equals("back") & listPosicion.get(orientacion) == abajo) {
			listPosicion.set(orientacion, arriba);
//			changeDirLed(148, "arriba");
			listaComandos.add(new Command(148, "abajo", "clear"));
			listaComandos.add(new Command(149, "arriba", "ON"));
			matrix.setDirection(arriba);
		}
	}

	public static void placeBlock() throws InterruptedException {

		if (altura == 2 || altura == 3) {
			Integer X = listPosicion.get(posicionX);
			Integer Y = listPosicion.get(posicionY);
			matrix.listaLeds.get(matrix.positionMatrix[X][Y]).setHeight(altura);
			// matrix.turnOnLed(X, Y, "block");
			listaComandos.add(new Command(X, Y, "block", "ON"));

			altura = 1;
		} else if (altura == 1) {
			Integer X = listPosicion.get(posicionX);
			Integer Y = listPosicion.get(posicionY);

			matrix.listaLeds.get(matrix.positionMatrix[X][Y]).setHeight(altura);
			// matrix.turnOnLed(X, Y, "block");
			listaComandos.add(new Command(X, Y, "block", "ON"));

		}
	}

	public static void placeBlock(Integer n) throws InterruptedException {// CAMBIA DE POSICION CON CADA N

		if (listPosicion.get(orientacion) == arriba & listPosicion.get(posicionY) + n > 7) {
			System.err.println("Error : Place block <" + n + "> out of range");
			System.exit(1);
		} else if (listPosicion.get(orientacion) == der & listPosicion.get(posicionX) + n > 7) {
			System.err.println("Error : Place block <" + n + "> out of range");
			System.exit(1);
		} else if (listPosicion.get(orientacion) == izq & listPosicion.get(posicionX) - n < -1) {
			System.err.println("Error : Place block <" + n + "> out of range");
			System.exit(1);
		} else if (listPosicion.get(orientacion) == abajo & listPosicion.get(posicionY) - n < -1) {
			System.err.println("Error : Place block <" + n + "> out of range");
			System.exit(1);
		}

		if (listPosicion.get(orientacion) == arriba) {
			for (int i = 1; i < n; i++) {
				placeBlock();
				listPosicion.set(1, listPosicion.get(posicionY) + 1); // se mueve al bloque siguiente
			}
		} else if (listPosicion.get(orientacion) == abajo) {
			for (int i = 1; i < n; i++) {
				placeBlock();
				listPosicion.set(1, listPosicion.get(posicionY) - 1); // se mueve al bloque siguiente
			}
		} else if (listPosicion.get(orientacion) == izq) {
			for (int i = 1; i < n; i++) {
				placeBlock();
				listPosicion.set(0, listPosicion.get(posicionX) - 1); // se mueve al bloque siguiente
			}
		} else if (listPosicion.get(orientacion) == der) {
			for (int i = 1; i < n; i++) {
				placeBlock();
				listPosicion.set(0, listPosicion.get(posicionX) + 1); // se mueve al bloque siguiente
			}
		}
		placeBlock();
	}

	public static void highBlock() {

		altura = 2;
	}

	public static void highBlock(Integer n) {

		if (n == 2 | n == 3) {
			altura = n;
		} else {
			System.err.println("Error : Block height <" + n + "> not allowed");
			System.exit(1);
		}
	}

	public static void putLight() throws InterruptedException {

		Integer X = listPosicion.get(posicionX);
		Integer Y = listPosicion.get(posicionY);

		if (matrix.listaLeds.get(matrix.positionMatrix[X][Y]).getHeight() != 0) {
			matrix.listaLeds.get(matrix.positionMatrix[X][Y]).setObjective();
			// matrix.turnOnLed(X, Y, "light");
			listaComandos.add(new Command(X, Y, "light", "ON"));

			LucesX.add(X);
			LucesY.add(Y);
			cantLuces += 1;
		} else {
			System.err.println("Error : Block not exist");
			System.exit(1);
		}
	}

	public static void pos(String y, String x) {

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
			matrix.setPosition(posX, posY);
		}
	}

	public static void posStart(String y, String x) {

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
			listaComandos.add(new Command(0, 0, "player", "clear"));

			listPosicion.set(posicionX, PosINICIAL.get(posicionX));
			listPosicion.set(posicionY, PosINICIAL.get(posicionY));
//			matrix.setPosition(posX, posY);
			listaComandos.add(new Command(posX, posY, "player", "ON"));
		}
	}

	public static void call(String proc) throws InterruptedException {

		Integer indice = search(proc, listProcesos);
		if (indice == NoEncontrado) {
			System.err.println("Error : Process <" + proc + "> not exist");
			System.exit(1);
		} else if (indice != NoEncontrado) {
			ParserDeExpresiones(listExprProc.get(indice));
		}
	}

	public static void ParserDeExpresiones(List<String> lista) throws InterruptedException {
		for (int i = 0; i < lista.size(); i++) {
			if (lista.get(i).equals("set")) {
				String identificador = lista.get(i + 1);
				Integer valor = Integer.parseInt(lista.get(i + 2));
				set(identificador, valor);
			} else if (lista.get(i).equals("add")) {
				add(lista.get(i + 1));
			} else if (lista.get(i).equals("less")) {
				less(lista.get(i + 1));
			} else if (lista.get(i).equals("changeDir")) {
				changeDir(lista.get(i + 1));
			} else if (lista.get(i).equals("placeBlock")) {
				placeBlock();
			} else if (lista.get(i).equals("placeBlockN")) {
				Integer n = Integer.parseInt(lista.get(i + 1));
				placeBlock(n);
			} else if (lista.get(i).equals("highBlock")) {
				highBlock();
			} else if (lista.get(i).equals("highBlockN")) {
				Integer n = Integer.parseInt(lista.get(i + 1));
				highBlock(n);
			} else if (lista.get(i).equals("putLight")) {
				putLight();
			} else if (lista.get(i).equals("pos")) {
				pos(lista.get(i + 1), lista.get(i + 2));
			} else if (lista.get(i).equals("posStart")) {
				posStart(lista.get(i + 1), lista.get(i + 2));
			} else if (lista.get(i).equals("call")) {
				call(lista.get(i + 1));

//			} else if (lista.get(i).equals("keep")) {
//				keep(lista.get(i+1));
//			} else if (lista.get(i).equals("for")) {
//				for(lista.get(i+1));
//			} else if (lista.get(i).equals("when")) {
//				when(lista.get(i+1));
			}
		}
	}

}