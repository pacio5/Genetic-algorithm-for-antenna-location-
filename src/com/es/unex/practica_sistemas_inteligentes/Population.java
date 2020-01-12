package com.es.unex.practica_sistemas_inteligentes;

import java.util.Arrays;
import java.util.Random;

/**
 * This class defines the structure, properties and methods of the population.
 * 
 * @author eliapacioni
 * @version 0.1
 *
 */
public class Population {

	/**
	 * General parameters
	 */
	private Parameters p;

	/**
	 * Vector of the individuals that make up the population
	 */
	private Individual[] individuals;

	/**
	 * Default constructor
	 */
	public Population() {
		individuals = null;
		p = null;
	}

	/**
	 * 
	 * @param p general parameters of the program, read from the configuration file
	 * 
	 * Parametric constructor, sets parameters and number of individuals required
	 */
	public Population(Parameters p) {
		this.p = p;
		individuals = new Individual[this.p.getPopSize()];

	}

	/**
	 * Create the population from the parameters indicated
	 */
	public void init() {
		for (int i = 0; i < individuals.length; i++)
			individuals[i] = new Individual(p.getnMaxAntennas(), p.getModAntennas(), p.gridWidth(), p.getAccidents());
		evalua();

	}

	/**
	 * Evaluates the entire population and orders it for the fitness value of each individual
	 */
	public void evalua() {
		for (int i = 0; i < individuals.length; i++) {
			individuals[i].evaluate(p.gridWidth(), p.getAccidents(), p.getCoverageArea(),
					p.getAvgCostAntennas(), p.getWeightingAreas(), p.getWeightingCost(), p.getWeightingConnection());
		}

		Arrays.parallelSort(individuals);
	}

	/**
	 * 
	 * @param ind1 individual 1
	 * @param ind2 individual 2
	 * @return Individual[] vector of individuals after the crossover
	 * 
	 * Make the crossover at one point on the two individuals
	 */
	public static Individual[] crossover(Individual ind1, Individual ind2) {
		Random rand = new Random();
		int crossoverPoint = rand.nextInt(ind1.getAntennas().length);
		Individual[] newInd = new Individual[2];
		newInd[0] = new Individual(ind1.getAntennas().length);
		newInd[1] = new Individual(ind1.getAntennas().length);

		int i;

		for (i = 0; i < crossoverPoint; i++) {
			newInd[0].setGene(i, ind1.getGene(i));
			newInd[1].setGene(i, ind2.getGene(i));
		}

		for (; i < newInd[0].getAntennas().length; i++) {
			newInd[0].setGene(i, ind2.getGene(i));
			newInd[1].setGene(i, ind1.getGene(i));
		}

		return newInd;
	}

	/**
	 * 
	 * @param ind1 individual 1
	 * @param ind2 individual 2
	 * @return Individual[] vector of individuals after the crossover
	 * 
	 * Effettua il crossover in due punti sui due individui
	 */
	public static Individual[] crossoverMulti(Individual ind1, Individual ind2) {
		Individual[] newInd = new Individual[2];
		if (ind1.getAntennas().length == 1) {
			newInd[0] = ind1;
			newInd[1] = ind2;
		} else {

			Random rand = new Random();
			int[] crossoverPoint = new int[2];
			crossoverPoint[0] = rand.nextInt(ind1.getAntennas().length);
			do {
				crossoverPoint[1] = rand.nextInt(ind1.getAntennas().length);
			} while (crossoverPoint[1] == crossoverPoint[0]);

			if (crossoverPoint[0] > crossoverPoint[1]) {
				int temp = crossoverPoint[0];
				crossoverPoint[0] = crossoverPoint[1];
				crossoverPoint[1] = temp;
			}
			newInd[0] = new Individual(ind1.getAntennas().length);
			newInd[1] = new Individual(ind1.getAntennas().length);

			int i = 0;
			
			for (; i < crossoverPoint[0]; i++) {
				newInd[0].setGene(i, ind1.getGene(i));
				newInd[1].setGene(i, ind2.getGene(i));
			}
			
			for (; i < crossoverPoint[1]; i++) {
				newInd[0].setGene(i, ind2.getGene(i));
				newInd[1].setGene(i, ind1.getGene(i));
			}
			
			for (; i < newInd[0].getAntennas().length; i++) {
				newInd[0].setGene(i, ind1.getGene(i));
				newInd[1].setGene(i, ind2.getGene(i));
			}
		
		}
		
		return newInd;
	}

	/**
	 * 
	 * @return Individual, individual selected
	 * 
	 * Selection by tournament
	 */
	public Individual torneo() {
		Random rand = new Random();
		int numero_individuo = rand.nextInt((int) Math.round(Math.sqrt(individuals.length))) + 2;
		Individual[] ind = new Individual[numero_individuo];

		for (int i = 0; i < numero_individuo; i++) {
			ind[i] = individuals[rand.nextInt(individuals.length)];
		}

		Individual best = ind[0];

		for (int i = 1; i < numero_individuo; i++) {
			if (best.getFitnessValue() < ind[i].getFitnessValue())
				best = ind[i];
		}

		return best;

	}
	
	/**
	 * 
	 * @return Individual, individual selected
	 * 
	 * Selection by roulette
	 */
	public Individual rouletteWheel() {
		float[] fitnessNormalized = new float[individuals.length];
		float total = 0;
		for(int i = 0; i < individuals.length; i++) {
			total += individuals[i].getFitnessValue();
		}
		
		for(int i = 0; i < individuals.length; i++) {
			fitnessNormalized[i] = individuals[i].getFitnessValue() / total;
		}
				
		Random rand = new Random();
		float value = rand.nextFloat();
		int i = 0;
		float xn = 0;
		while(xn <= value) {
			xn += (float) fitnessNormalized[i];
			i++;
		}
		
		if(i > 0) --i;
		return individuals[i];
	}

	
	/**
	 * 
	 * @param ind New invidivual
	 * @param index index of individual
	 * 
	 * Replace the old individual with the new
	 */
	public void setIndividual(Individual ind, int index) {
		individuals[index] = (Individual) ind.clone();
	}

	/**
	 * 
	 * @return Individual[] all individuals in the population
	 */
	public Individual[] getIndividuals() {
		return individuals;
	}

	/**
	 * 
	 * @param index index of the required individual
	 * @return Individual required individual
	 */
	public Individual getIndividual(int index) {
		return individuals[index];
	}

	/**
	 * 
	 * @return Individual individual with the best fitness
	 */
	public Individual getBestIndividual() {
		return individuals[0];
	}

	/**
	 * 
	 * @param pop replaces the current population with the new popopulation
	 */
	public void setPopulation(Population pop) {
		this.individuals = pop.getIndividuals().clone();
	}

	@Override
	public String toString() {
		String p = "";
		for (int i = 0; i < individuals.length; i++)
			p += "Individual " + i + ": \n" + individuals[i].toString();

		return p;
	}

}
