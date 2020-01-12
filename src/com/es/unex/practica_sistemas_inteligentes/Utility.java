package com.es.unex.practica_sistemas_inteligentes;

/**
 * Encapsulates on-screen information printing methods for streamlining programming and increase readability in other classes
 * 
 * @author eliapacioni
 *
 */
public class Utility {

	/**
	 * Print programmer information on screen
	 */
	public static void infoDeveloper() {
		System.out.println("----------------------------------------------------- \n"
				+ "-- Sistemas Inteligentes                           -- \n"
				+ "-- Course 2019/20                                   -- \n"
				+ "-- Professor: Francisco Chávez de la O             -- \n"
				+ "-- Student: Elia Pacioni                           -- \n"
				+ "-----------------------------------------------------");
	}

	/**
	 * @param colors Color management in the terminal: true if print colors
	 * Indicates that there has been an error
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
	 * @param i index of iteration
	 * @param colors Color management in the terminal: true if print colors
	 * Report the press of the best individual
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
	 * Report that the execution has ended
	 */
	public static void end() {
		System.out.println("----------------------------------------------------- \n"
				+ "--              Execution completed                -- \n"
				+ "-----------------------------------------------------");
	}

	/**
	 * 
	 * @param areaTotal matrix of the zone to be printed
	 * 
	 *                  Print on screen the matrix that defines a zone
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
