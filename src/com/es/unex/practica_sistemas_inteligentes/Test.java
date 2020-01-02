/**
 * 
 */
package com.es.unex.practica_sistemas_inteligentes;

/**
 * @author eliapacioni
 *
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		Parameters p = new Parameters(true);
		
		Population pop = new Population(p);
		pop.init();
		
		pop.evalua();
		
		System.out.println(pop.toString());
		
		Individual ind = pop.rouletteWheel();
		System.out.println(ind.toString());
		
	}

}
