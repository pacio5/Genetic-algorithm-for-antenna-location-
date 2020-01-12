package com.es.unex.practica_sistemas_inteligentes;
/**
 * Class defining the structure of the area to be covered and how to extract this matrix from the total area.
 * 
 * @author eliapacioni
 * @version 0.1
 *
 */
public class CoverageArea {

	/**
	 * Area coordinates
	 */
	private int x1, y1, x2, y2;
	/**
	 * Minimum signal coverage (power) within the area
	 */
	private int minimumPower;

	/**
	 * Manufacturer without parameters, zero area
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
	 * @param x1 coordinate X point 1
	 * @param y1 coordinate Y point 1
	 * @param x2 coordinate X point 2
	 * @param y2 coordinate Y point 2
	 * @param minimumPower Minimum signal coverage (power) within the area
	 * 
	 * Parametric builder, defines an area to cover
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
	 * @param areaTotal total area
	 * @return matrix of the area to be covered
	 * 
	 * Extracts the matrix of the area to be covered from the total area
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
	 * @return Minimum signal coverage (power) within the area
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
