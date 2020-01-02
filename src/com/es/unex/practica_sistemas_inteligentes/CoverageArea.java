package com.es.unex.practica_sistemas_inteligentes;
/**
 * 
 * @author eliapacioni
 * @version 0.1
 * 
 * Classe che definisce la struttura dell'area che deve essere coperta e il modo di estrarre tale matrice dalla zona totale.
 *
 */
public class CoverageArea {

	/**
	 * Coordinate dell'area
	 */
	private int x1, y1, x2, y2;
	/**
	 * Copertura(potenza) minima del segnale all'interno dell'area
	 */
	private int minimumPower;

	/**
	 * Costruttore senza parametri, area nulla
	 */
	public CoverageArea() {
		x1 = -1;
		y1 = -1;
		x2 = -1;
		y2 = -1;
		minimumPower = -1;
	}

	/**
	 * 
	 * @param x1 coordinata X punto 1
	 * @param y1 coordinata Y punto 1
	 * @param x2 coordinata X punto 2
	 * @param y2 coordinata Y punto 2
	 * @param minimumPower Copertura(potenza) minima del segnale all'interno dell'area
	 * 
	 * Costruttore parametrico, definisce un'area da coprire
	 */
	public CoverageArea(int x1, int y1, int x2, int y2, int minimumPower) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.minimumPower = minimumPower;
	}

	/**
	 * 
	 * @param areaTotal zona totale
	 * @return matrice dell'area da coprire
	 * 
	 * Estrae la matrice dell'area da coprire dalla zona totale
	 */
	public int[][] getMatrixArea(int[][] areaTotal) {
		int startX, endX, startY, endY;

		if (x1 < x2) {
			startX = x1;
			endX = x2;
		} else {
			startX = x2;
			endX = x1;
		}

		if (y1 < y2) {
			startY = y1;
			endY = y2;
		} else {
			startY = y2;
			endY = y1;
		}
		
		int[][] matrixArea = new int[Math.abs(endY - startY) + 1][Math.abs(endX - startX) + 1];
		int inx, iny = startY;
		for (int i = 0; i <= Math.abs(startY - endY); i++) {
			inx = startX;
			for (int j = 0; j <= Math.abs(startX - endX); j++) {
				matrixArea[i][j] = areaTotal[iny][inx];
				inx++;
			}
			iny++;
		}
		
		return matrixArea;		
	}
	
	
	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}

	/**
	 * 
	 * @return Copertura(potenza) minima del segnale all'interno dell'area
	 */
	public int getMinimumPower() {
		return minimumPower;
	}

	@Override
	public String toString() {
		return "Area Coverage \n"
				+ "P1(x,y) = (" + x1 + "," + y1 + "), P2(x,y) = (" + x2 + "," + y2 + "), minimumPower = "
				+ minimumPower;
	}
	
}
