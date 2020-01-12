package com.es.unex.practica_sistemas_inteligentes;

import java.io.File;
import java.util.Arrays;
import java.util.Random;

/**
 * Main class of the program 
 * 
 * @author eliapacioni
 * @version 0.1
 *
 */
public class Main {

	/**
	 * @param args unused
	 */
	public static void main(String[] args) {

		Utility.infoDeveloper();

		Parameters p = new Parameters();
		p.implementParameters(new File("config.txt"));

		Population population = new Population(p);
		population.init();

		System.out.println("Population initial");
		System.out.println(population.toString());

		Individual best = population.getBestIndividual();
		if (best.getFitnessValue() == 1) {
			System.out.println("Best solution met: " + best.toString());
		}

		// Main cycle, continue as long as I have iterations available or until
		// I'm not meeting the maximum
		int iter = 0;
		boolean top = false;

		while (iter < p.getIter() && top == false) {
			Population newPop = new Population(p);
			Random rand = new Random();
			for (int j = 0; j < p.getElitism(); j++) {
				// Elitism, being the population in descending order, I take the first elements
				newPop.setIndividual(population.getIndividual(j), j);
			}

			// this cycle is performed as many times as necessary to create
			// the new population of the same size as the previous one
			for (int i = p.getElitism(); i < population.getIndividuals().length; i += 2) {

				Individual[] ind = new Individual[2];
				// seletion (torneo - roulette)

				if (p.isAlSel()) {
					ind[0] = (Individual) population.torneo().clone();
					ind[1] = (Individual) population.torneo().clone();
				} else {
					ind[0] = (Individual) population.rouletteWheel().clone();
					ind[1] = (Individual) population.rouletteWheel().clone();
				}

				// crossover (single point - multi point) - assesses the probability
				Individual[] childs = new Individual[2];

				if (rand.nextDouble() < p.getpCrossover()) {
					if(p.isOpCrossover())
						childs = Population.crossover(ind[0], ind[1]);
					else childs = Population.crossoverMulti(ind[0], ind[1]);
				} else {
					childs[0] = (Individual) ind[0].clone();
					childs[1] = (Individual) ind[1].clone();
				}

				// mutation - assess the probability and the chosen operator
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

				// replacement algorithm
				if (p.isReplacement()) {
					// Generational

					// Check if the children are valid, if they are, insert them
					if (!childs[0].isInvalid(p.getAccidents()))
						newPop.setIndividual(childs[0], i);
					else
						i--;

					try {
						if (!childs[0].isInvalid(p.getAccidents())) {
							newPop.setIndividual(childs[1], i + 1);
						} else {
							i--;
						}
					} catch (Exception exc) {

					}
				} else {
					// Stationary

					Individual[] test = new Individual[4];
					test[0] = ind[0];
					test[1] = ind[1];

					if (!childs[0].isInvalid(p.getAccidents()))
						childs[0].setFitnessValue(-1);
					else
						childs[0].evaluate(p.gridWidth(), p.getAccidents(), p.getCoverageArea(),
								p.getAvgCostAntennas(), p.getWeightingAreas(), p.getWeightingCost(), p.getWeightingConnection());

					test[2] = childs[0];

					if (!childs[1].isInvalid(p.getAccidents()))
						childs[1].setFitnessValue(-1);
					else
						childs[1].evaluate(p.gridWidth(), p.getAccidents(), p.getCoverageArea(),
								p.getAvgCostAntennas(), p.getWeightingAreas(), p.getWeightingCost(), p.getWeightingConnection());

					test[3] = childs[1];

					Arrays.sort(test);

					newPop.setIndividual(test[0], i);

					try {
						newPop.setIndividual(test[1], i + 1);
					} catch (Exception exc) {

					}
				}

			}

			// replaces the population with the new
			population.setPopulation(newPop);
			// evaluation
			population.evalua();
			Utility.bestIndividual(iter+1, p.isColors());
			System.out.println(population.getBestIndividual().toString());
			if (population.getBestIndividual().getFitnessValue() == 1)
				top = true;

			iter++;
		}

		if (top == false)
			System.out.println("You've finished the iterations available, the best individual is: \n"
					+ population.getBestIndividual().toString());
		else
			System.out.println("You've met the best after: " + iter + " iterations; \n "
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
