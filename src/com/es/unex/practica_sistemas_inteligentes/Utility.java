package com.es.unex.practica_sistemas_inteligentes;

/**
 * @author eliapacioni
 *
 *         Incapsula i metodi di stampa delle informazioni a video per snellire
 *         la programmazione e aumentare la leggibilità nelle altre classi
 */
public class Utility {

	/**
	 * Stampa a video le informazioni del programmatore
	 */
	public static void infoDeveloper() {
		System.out.println("----------------------------------------------------- \n"
				+ "-- Sistemas Inteligentes                           -- \n"
				+ "-- Curso 2019/20                                   -- \n"
				+ "-- Professor: Francisco Chávez de la O             -- \n"
				+ "-- Student: Elia Pacioni                           -- \n"
				+ "-----------------------------------------------------");
	}

	/**
	 * Indica che c'è stato un errore
	 */
	public static void error(boolean colors) {
		if (colors)
			System.out.println(ConsoleColors.RED + "-----------------------------------------------------\n"
					+ "--                      Error                      --\n"
					+ "-----------------------------------------------------" + ConsoleColors.RESET);
		else
			System.out.println("-----------------------------------------------------\n"
					+ "--                      Error                      --\n"
					+ "-----------------------------------------------------");
	}

	/**
	 * Segnala la stampa dell'individuo migliore
	 */
	public static void bestIndividual(int i, boolean colors) {
		if (colors)
			System.out.println(ConsoleColors.GREEN + "-----------------------------------------------------\n"
					+ "--          Best Individual, Iteration:   " + i + "       --\n"
					+ "-----------------------------------------------------" + ConsoleColors.RESET);
		else
			System.out.println("-----------------------------------------------------\n"
					+ "--          Best Individual, Iteration:   " + i + "       --\n"
					+ "-----------------------------------------------------");
	}

	/**
	 * Informa che un valore non è corretto
	 * 
	 * @param value - valore da segnalare come incorretto
	 */
	public static void incorrectValue(String value) {
		System.out.println("Valor incorrecto para: " + value);
	}

	/**
	 * Segnala che l'esecuzione è terminata
	 */
	public static void end() {
		System.out.println("----------------------------------------------------- \n"
				+ "--              Execution completed                -- \n"
				+ "-----------------------------------------------------");
	}

	/**
	 * 
	 * @param areaTotal matrice della zona da stampare
	 * 
	 *                  Stampa a video la matrice che definisce una zona
	 */
	public static void printArea(int[][] areaTotal) {
		System.out.println("\n");
		System.out.printf("%3s", "");

		int lx = areaTotal[0].length;
		int ly = areaTotal.length;
		for (int i = 0; i < lx; i++)
			System.out.printf("%3d", i);

		System.out.println("\n");

		for (int i = 0; i < ly; i++) {
			System.out.printf("%3d", i);
			for (int j = 0; j < lx; j++)
				if (areaTotal[i][j] != 0)
					System.out.printf(ConsoleColors.GREEN_BOLD + "%3d" + ConsoleColors.RESET, areaTotal[i][j]);
				else
					System.out.printf("%3d", areaTotal[i][j]);
			System.out.println("");
		}
	}

}
