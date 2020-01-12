package com.es.unex.practica_sistemas_inteligentes;

/**
 * Class defining the structure of an antenna
 * Implements the Cloneable interface to clone an antenna and not condition subsequent changes
 * 
 * @author eliapacioni
 * @version 0.1
 * 
 * 
 */
public class Antenna implements Cloneable {
	
	/**
	 * Antenna power
	 */
	private int coverageWidth;
	/**
	 * Antenna cost
	 */
	private int cost;
	/**
	 * Position
	 */
	private int x, y;
	
	/**
	 * Manufacturer without parameters, no antenna
	 */
	public Antenna() {
		coverageWidth = -1;
		cost = -1;
		x = -1;
		y = -1;
	}
	
	/**
	 * 
	 * @param coverageWidth power ofantenna
	 * @param cost antenna cost
	 * 
	 * Creates an antenna by defining its power and cost, 
	 * the position will be defined only when it will be used by an individual
	 */
	public Antenna(int coverageWidth, int cost) {
		this.coverageWidth = coverageWidth;
		this.cost = cost;
		x = -1;
		y = -1;
	}

	/**
	 * 
	 * @return recovers the power of the antenna
	 */
	public int getCoverageWidth() {
		return coverageWidth;
	}

	public void setCoverageWidth(int coverageWidth) {
		this.coverageWidth = coverageWidth;
	}

	/**
	 * 
	 * @return recovers the cost of the antenna
	 */
	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}
	
	/**
	 * 
	 * @param x coordinate x
	 * @param y coordinate y
	 * 
	 * Set position of antenna
	 */
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * 
	 * @return retrieve the X coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * 
	 * @return retrieve the Y coordinate
	 */
	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return "Antenna: \n coverageWidth=" + coverageWidth + "\n cost=" + cost + "\n x=" + x + ", y=" + y + "\n";
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
