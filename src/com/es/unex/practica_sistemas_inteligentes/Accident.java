package com.es.unex.practica_sistemas_inteligentes;

/**
 * Defines a geographical incident. 
 * The level of incidence cannot be negative, at most 0.
 * 
 * @author eliapacioni
 * @version 0.1
 * 
 * 
 */
public class Accident {
	/**
	 * Coordinates of the accident, forming a quadrilateral
	 */
	private int x1, y1, x2, y2;
	/**
	 * Incidence level in transmission
	 */
	private int incidence;
	
	/**
	 * Manufacturer without parameters, damn zero
	 */
	public Accident() {
		x1 = -1;
		y1 = -1;
		x2 = -1;
		y2 = -1;
		incidence = 0;
	}
	
	/**
	 * 
	 * @param x1 x value of the first point 
	 * @param y1 y value of the first point 
	 * @param x2 x value of the second point 
	 * @param y2 y value of the second point 
	 * @param incidence level of incidence in transmission
	 * 
	 * Parametric builder for the creation of a geographical accident
	 */
	public Accident(int x1, int y1, int x2, int y2, int incidence) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		if(incidence < 0)
			incidence = 0;
		this.incidence = incidence;
	}

	/**
	 * 
	 * @return X value of point 1
	 */
	public int getX1() {
		return x1;
	}

	/**
	 * 
	 * @return Y value of point 1
	 */
	public int getY1() {
		return y1;
	}

	/**
	 * 
	 * @return X value of point 2
	 */
	public int getX2() {
		return x2;
	}

	/**
	 * 
	 * @return Y value of point 2
	 */
	public int getY2() {
		return y2;
	}

	/**
	 * 
	 * @return level of incidence in signal transmission
	 */
	public int getIncidence() {
		return incidence;
	}

	@Override
	public String toString() {
		return "Accident:\n "
				+ "P1 (x,y) = (" + x1 + "," + y1 + "); P2(x,y) = (" + x2 + "," + y2 + "); Incidence = " + incidence;
	}
	
	
	
}
