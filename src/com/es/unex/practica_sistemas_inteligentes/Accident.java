package com.es.unex.practica_sistemas_inteligentes;

/**
 * @author eliapacioni
 * 
 * Definisce un accidente geografico. 
 * Il livello di incidenza non puà essere negativo, al più 0.
 */
public class Accident {
	/**
	 * Coordinate dell'accidente, formano un quadrilatero
	 */
	private int x1, y1, x2, y2;
	/**
	 * Livello di incidenza nella trasmissione
	 */
	private int incidence;
	
	/**
	 * Costruttore senza parametri, accidente nullo
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
	 * @param x1 valore x del primo punto 
	 * @param y1 valore y del primo punto
	 * @param x2 valore x del secondo punto
	 * @param y2 valore y del secondo punto
	 * @param incidence livello di incidenza nella trasmissione
	 * 
	 * Costruttore parametrico per la creazione di un accidente geografico
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
	 * @return valore X del punto 1
	 */
	public int getX1() {
		return x1;
	}

	/**
	 * 
	 * @return valore Y del punto 1
	 */
	public int getY1() {
		return y1;
	}

	/**
	 * 
	 * @return valore X del punto 2
	 */
	public int getX2() {
		return x2;
	}

	/**
	 * 
	 * @return valore Y del punto 2
	 */
	public int getY2() {
		return y2;
	}

	/**
	 * 
	 * @return livello di incidenza nella trasmissione del segnale
	 */
	public int getIncidence() {
		return incidence;
	}

	@Override
	public String toString() {
		return "Accidente:\n "
				+ "P1 (x,y) = (" + x1 + "," + y1 + "); P2(x,y) = (" + x2 + "," + y2 + "); Incidencia = " + incidence;
	}
	
	
	
}
