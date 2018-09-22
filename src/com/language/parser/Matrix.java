package com.language.parser;

import java.util.ArrayList;

public class Matrix {

	public int[][] positionMatrix = { { 0, 1, 2, 3, 4, 5, 6 }, { 13, 12, 11, 10, 9, 8, 7 },
			{ 14, 15, 16, 17, 18, 19, 20 }, { 27, 26, 25, 24, 23, 22, 21 }, { 28, 29, 30, 31, 32, 33, 34 },
			{ 41, 40, 39, 38, 37, 36, 35 }, { 42, 43, 44, 45, 46, 47, 48 } };

	public ArrayList<LedNode> listaLeds = new ArrayList<LedNode>();

	public int currentPosX;
	public int currentPosY;
	public int currentHeight;
	public int direction;

	ArduinoLink arduino;

	Matrix(int x, int y) throws InterruptedException {

		currentPosX = x;
		currentPosY = y;
		currentHeight = 1;
		direction = 1;

		arduino = new ArduinoLink();

		// Fila 1
		listaLeds.add(new LedNode(0, 0, 0, 13, 14));
		listaLeds.add(new LedNode(1, 0, 1, 12, 15));
		listaLeds.add(new LedNode(2, 0, 2, 11, 16));
		listaLeds.add(new LedNode(3, 0, 3, 10, 17));
		listaLeds.add(new LedNode(4, 0, 4, 9, 18));
		listaLeds.add(new LedNode(5, 0, 5, 8, 19));
		listaLeds.add(new LedNode(6, 0, 6, 7, 20));

		// Fila 2
		listaLeds.add(new LedNode(7, 0, 21, 34, 35));
		listaLeds.add(new LedNode(8, 0, 22, 33, 36));
		listaLeds.add(new LedNode(9, 0, 23, 32, 37));
		listaLeds.add(new LedNode(10, 0, 24, 31, 38));
		listaLeds.add(new LedNode(11, 0, 25, 30, 39));
		listaLeds.add(new LedNode(12, 0, 26, 29, 40));
		listaLeds.add(new LedNode(13, 0, 27, 28, 41));

		// Fila 3
		listaLeds.add(new LedNode(14, 0, 42, 55, 56));
		listaLeds.add(new LedNode(15, 0, 43, 54, 57));
		listaLeds.add(new LedNode(16, 0, 44, 53, 58));
		listaLeds.add(new LedNode(17, 0, 45, 52, 59));
		listaLeds.add(new LedNode(18, 0, 46, 51, 60));
		listaLeds.add(new LedNode(19, 0, 47, 50, 61));
		listaLeds.add(new LedNode(20, 0, 48, 49, 62));

		// Fila 4
		listaLeds.add(new LedNode(21, 0, 63, 76, 77));
		listaLeds.add(new LedNode(22, 0, 64, 75, 78));
		listaLeds.add(new LedNode(23, 0, 65, 74, 79));
		listaLeds.add(new LedNode(24, 0, 66, 73, 80));
		listaLeds.add(new LedNode(25, 0, 67, 72, 81));
		listaLeds.add(new LedNode(26, 0, 68, 71, 82));
		listaLeds.add(new LedNode(27, 0, 69, 70, 83));

		// Fila 5
		listaLeds.add(new LedNode(28, 0, 84, 97, 98));
		listaLeds.add(new LedNode(29, 0, 85, 96, 99));
		listaLeds.add(new LedNode(30, 0, 86, 95, 100));
		listaLeds.add(new LedNode(31, 0, 87, 94, 101));
		listaLeds.add(new LedNode(32, 0, 88, 93, 102));
		listaLeds.add(new LedNode(33, 0, 89, 92, 103));
		listaLeds.add(new LedNode(34, 0, 90, 91, 104));

		// Fila 6
		listaLeds.add(new LedNode(35, 0, 105, 118, 119));
		listaLeds.add(new LedNode(36, 0, 106, 117, 120));
		listaLeds.add(new LedNode(37, 0, 107, 116, 121));
		listaLeds.add(new LedNode(38, 0, 108, 115, 122));
		listaLeds.add(new LedNode(39, 0, 109, 114, 123));
		listaLeds.add(new LedNode(40, 0, 110, 113, 124));
		listaLeds.add(new LedNode(41, 0, 111, 112, 125));

		// Fila 7
		listaLeds.add(new LedNode(42, 0, 126, 139, 140));
		listaLeds.add(new LedNode(43, 0, 127, 138, 141));
		listaLeds.add(new LedNode(44, 0, 128, 137, 142));
		listaLeds.add(new LedNode(45, 0, 129, 136, 143));
		listaLeds.add(new LedNode(46, 0, 130, 135, 144));
		listaLeds.add(new LedNode(47, 0, 131, 134, 145));
		listaLeds.add(new LedNode(48, 0, 132, 133, 146));

		// Leds direccion
		listaLeds.add(new LedNode(147, 0, 147, 147, 147));
		listaLeds.add(new LedNode(148, 0, 148, 148, 148));
		listaLeds.add(new LedNode(149, 0, 149, 149, 149));
		listaLeds.add(new LedNode(151, 0, 151, 151, 151));

	}

	public void setHeight(int x, int y, int h) {

		int i = positionMatrix[x][y];

		listaLeds.get(i).setHeight(h);

	}

	public void placeBlock() {

		int i = positionMatrix[currentPosX][currentPosY];

		listaLeds.get(i).setHeight(currentHeight);

		currentHeight = 0;
	}

	public void PlaceBlock(int n) {

		int tempX = currentPosX;
		int tempY = currentPosY;

	}

	public void setPosition(int x, int y) {

		currentPosX = x;
		currentPosY = y;
	}

	public void Move() {

		///// movimiento hacia arriba
		if (direction == 0) {

			currentPosY--;
			if (currentPosY < 0) {
				currentPosY = 0;
			}
		}

		///// movimiento hacia la izquierda
		if (direction == 1) {

			currentPosX--;
			if (currentPosX < 0) {
				currentPosX = 0;
			}
		}

		///// movimiento hacia la derecha
		if (direction == 2) {

			currentPosX++;
			if (currentPosX > 6) {
				currentPosX = 6;
			}
		}

		///// movimiento hacia abajo
		if (direction == 3) {

			currentPosY++;
			if (currentPosY > 6) {
				currentPosY = 6;
			}
		}
	}

	public void setDirection(int dir) {
		direction = dir;
	}

	///////// Apagar un led
	public void clearLed(int x, int y, String id) throws InterruptedException {

		if (id.equals("player")) {
			String index = Integer.toString(listaLeds.get(positionMatrix[x][y]).getPlayerLed());

			arduino.sendData("<clear," + index + ",0>");
		}

		if (id.equals("block")) {
			String index = Integer.toString(listaLeds.get(positionMatrix[x][y]).getHeightLed());

			arduino.sendData("<clear," + index + ",0>");
		}

		if (id.equals("light")) {
			String index = Integer.toString(listaLeds.get(positionMatrix[x][y]).getObjLed());
			listaLeds.get(positionMatrix[x][y]).clearObjective();
			arduino.sendData("<clear," + index + ",0>");
		}

	}

	////////////// Encender un led
	public void turnOnLed(int x, int y, String id) throws InterruptedException {

		if (id.equals("player")) {
			String index = Integer.toString(listaLeds.get(positionMatrix[x][y]).getPlayerLed());
//			System.out.println(
//					"accediendo a posicion de matriz : " + positionMatrix[x][y] + "con coord: " + x + ", " + y);

			arduino.sendData("<player," + index + ",0>");
		}

		if (id.equals("block")) {
			String index = Integer.toString(listaLeds.get(positionMatrix[x][y]).getHeightLed());
//			System.out.println(
//					"accediendo a posicion de matriz : " + positionMatrix[x][y] + "con coord: " + x + ", " + y);
			String h = Integer.toString(listaLeds.get(positionMatrix[x][y]).getHeight());

			arduino.sendData("<block," + index + "," + h + ">");
		}

		if (id.equals("light")) {
			String index = Integer.toString(listaLeds.get(positionMatrix[x][y]).getObjLed());
			listaLeds.get(positionMatrix[x][y]).setObjective();
			//			System.out.println(
//					"accediendo a posicion de matriz : " + positionMatrix[x][y] + " con coord: " + x + ", " + y);

			arduino.sendData("<light," + index + ",0>");
		}

	}

	public void setDirectionLed(int dir) throws InterruptedException {

		if (dir == 2) {
			arduino.sendData("<abajo,150,0>");
			setDirection(dir);
		}

		if (dir == 3) {
			arduino.sendData("<izq,148,0>");
			setDirection(dir);
		}

		if (dir == 0) {
			arduino.sendData("<der,149,0>");
			setDirection(dir);
		}

		if (dir == 1) {
			arduino.sendData("<arriba,147,0>");
			setDirection(dir);
		}

	}

	public void clearDirectionLed(int dir) throws InterruptedException {

		if (dir == 2) {
			arduino.sendData("<clear,150,0>");
			setDirection(dir);
		}

		if (dir == 3) {
			arduino.sendData("<clear,148,0>");
			setDirection(dir);
		}

		if (dir == 0) {
			arduino.sendData("<clear,149,0>");
			setDirection(dir);
		}

		if (dir == 1) {
			arduino.sendData("<clear,147,0>");
			setDirection(dir);
		}
	}
}
