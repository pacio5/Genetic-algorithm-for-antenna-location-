package com.es.unex.practica_sistemas_inteligentes;

import java.util.Arrays;
import java.util.Random;

/**
 * @author eliapacioni
 * @version 0.1
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Utility.infoDeveloper();

		Parameters p = new Parameters(true);

		// Creo la popolazione
		Population population = new Population(p);
		population.init();

		System.out.println("Population initial");
		System.out.println(population.toString());

		Individual best = population.getBestIndividual();
		if (best.getFitnessValue() == 1) {
			System.out.println("Best solution met: " + best.toString());
		}

		// Ciclo principale, continua fin quando ho iterazioni disponibili o fin quando
		// non incontro il massimo
		int iter = 0;
		boolean top = false;

		while (iter < p.getIter() && top == false) {
			Population newPop = new Population(p);
			Random rand = new Random();
			for (int j = 0; j < p.getElitism(); j++) {
				// Elitismo, essendo la popolazione ordinata in modo decrescente, prendo i primi
				// elementi
				newPop.setIndividual(population.getIndividual(j), j);
			}

			// questo ciclo viene eseguito il numero di volte necessarie per creare
			// la nuova popolazione delle stesse dimensioni della precedente
			for (int i = p.getElitism(); i < population.getIndividuals().length; i += 2) {

				Individual[] ind = new Individual[2];
				// seletion (torneo - ruletta)

				if (p.isAlSel()) {
					ind[0] = (Individual) population.torneo().clone();
					ind[1] = (Individual) population.torneo().clone();
				} else {
					ind[0] = (Individual) population.rouletteWheel().clone();
					ind[1] = (Individual) population.rouletteWheel().clone();
				}

				// crossover (singolo punto - multi punto) - valuta la probabilità
				Individual[] childs = new Individual[2];

				if (rand.nextDouble() < p.getpCrossover()) {
					if(p.isOpCrossover())
						childs = Population.crossover(ind[0], ind[1]);
					else childs = Population.crossoverMulti(ind[0], ind[1]);
				} else {
					childs[0] = (Individual) ind[0].clone();
					childs[1] = (Individual) ind[1].clone();
				}

				// mutation - valuta la probabilità e l'operatore scelto
				if (rand.nextDouble() < p.getpMutation()) {
					if (p.isOpMutation())
						childs[0].mutate(p.gridWidth(), p.getAccidents());
					else
						childs[0].mutate(p.getModAntennas());
				}

				if (rand.nextDouble() < p.getpMutation()) {
					if (p.isOpMutation())
						childs[1].mutate(p.gridWidth(), p.getAccidents());
					else
						childs[1].mutate(p.getModAntennas());
				}

				// algoritmo di rimpiazzamento
				if (p.isReplacement()) {
					// Generacional

					// Controlla se i figli sono validi, se lo sono li inserisce
					if (childs[0].isInvalid(p.getAccidents()))
						newPop.setIndividual(childs[0], i);
					else
						i--;

					try {
						if (childs[0].isInvalid(p.getAccidents())) {
							newPop.setIndividual(childs[1], i + 1);
						} else {
							i--;
						}
					} catch (Exception exc) {
						// System.out.println(exc.toString());
					}
				} else {
					// Estacionario

					Individual[] test = new Individual[4];
					test[0] = ind[0];
					test[1] = ind[1];

					if (!childs[0].isInvalid(p.getAccidents()))
						childs[0].setFitnessValue(-1);
					else
						childs[0].evaluate(p.gridWidth(), p.getAccidents(), p.getCoverageArea(),
								p.getAvgCostAntennas());

					test[2] = childs[0];

					if (!childs[1].isInvalid(p.getAccidents()))
						childs[1].setFitnessValue(-1);
					else
						childs[1].evaluate(p.gridWidth(), p.getAccidents(), p.getCoverageArea(),
								p.getAvgCostAntennas());

					test[3] = childs[1];

					Arrays.sort(test);

					newPop.setIndividual(test[0], i);

					try {
						newPop.setIndividual(test[1], i + 1);
					} catch (Exception exc) {
						// System.out.println(exc.toString());
					}
				}

			}

			// rimpiazza la popolazione con la nuova
			population.setPopulation(newPop);
			// evaluation
			population.evalua();
			//System.out.println("Iterazione: " + iter + "\nMigliore individuo: " + population.getBestIndividual());
			Utility.bestIndividual(iter+1, p.isColors());
			System.out.println(population.getBestIndividual().toString());
			if (population.getBestIndividual().getFitnessValue() == 1)
				top = true;

			iter++;
		}

		if (top == false)
			System.out.println("Hai terminato le iterazioni a disposizione, il miglior individuo è: \n"
					+ population.getBestIndividual().toString());
		else
			System.out.println("Hai incontrato il massimo dopo: " + iter + " iterazioni; \n "
					+ population.getBestIndividual().toString());

		
		for (int i = 0; i < p.getCoverageArea().length; i++) 
			System.out.println(p.getCoverageArea()[i].toString());
		System.out.println("\n");
		
		for (int i = 0; i < p.getAccidents().length; i++)
			System.out.println(p.getAccidents()[i].toString());
		System.out.println("\n");
		
		population.getBestIndividual().print(p.gridWidth(), p.getAccidents(), p.getCoverageArea());

		Utility.end();
	}

}
