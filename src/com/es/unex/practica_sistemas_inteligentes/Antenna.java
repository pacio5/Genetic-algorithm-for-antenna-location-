package com.es.unex.practica_sistemas_inteligentes;

/**
 * @author eliapacioni
 * @version 0.1
 * 
 * Classe che definisce la struttura di un antenna
 * Implementa l'interfaccia Cloneable per eseguire la clonazione di un antenna e non condizionare le successive modifiche
 */
public class Antenna implements Cloneable {
	
	/**
	 * Potenza dell'antenna
	 */
	private int coverageWidth;
	/**
	 * Costo dell'antenna
	 */
	private int cost;
	/**
	 * Posizione
	 */
	private int x, y;
	
	/**
	 * Costruttore senza parametri, antenna nulla
	 */
	public Antenna() {
		coverageWidth = -1;
		cost = -1;
		x = -1;
		y = -1;
	}
	
	/**
	 * 
	 * @param coverageWidth potenza antenna
	 * @param cost costo dell'antenna
	 * 
	 * Crea un'antenna definendone potenza e costo, 
	 * la posizione verrà definita solo nel momento in cui verrà usata da un individuo
	 */
	public Antenna(int coverageWidth, int cost) {
		this.coverageWidth = coverageWidth;
		this.cost = cost;
		x = -1;
		y = -1;
	}

	/**
	 * 
	 * @return recupera la potenza dell'antenna
	 */
	public int getCoverageWidth() {
		return coverageWidth;
	}

	public void setCoverageWidth(int coverageWidth) {
		this.coverageWidth = coverageWidth;
	}

	/**
	 * 
	 * @return recupera il costo dell'antenna
	 */
	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}
	
	/**
	 * 
	 * @param x coordinata x
	 * @param y coordinata y
	 * 
	 * Imposta la posizione dell'antenna
	 */
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * 
	 * @return recupera la coordinata X
	 */
	public int getX() {
		return x;
	}

	/**
	 * 
	 * @return recupera la coordinata Y
	 */
	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return "Antena: \n coverageWidth=" + coverageWidth + "\n cost=" + cost + "\n x=" + x + ", y=" + y + "\n";
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	
}
