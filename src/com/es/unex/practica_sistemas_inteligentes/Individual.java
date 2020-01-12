package com.es.unex.practica_sistemas_inteligentes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;

/**
 * Class that defines the structure and actions of an individual.
 * 			Essential within the program because all the execution is
 * 			based on the use of individuals. Implements the interface
 * 			Cloneable to carry out the cloning of the individual, so as not to
 * 			having problems during subsequent changes Implement
 * 			the Comparable interface to make the comparison between individuals and
 * 			in order to order them by fitness value
 * 
 * @author eliapacioni
 * @version 0.1
 * 
 */
public class Individual implements Comparable<Individual>, Cloneable {
	private Antenna[] genes;
	private float fitnessValue;

	/**
	 * Builder with no parameters, no individual.
	 */
	public Individual() {
		genes = null;
		fitnessValue = -1;
	}

	/**
	 * 
	 * @param nMaxAntennas maximum number of antennas that can compose the individual
	 * 
	 * It instantiates the individual by defining only the maximum number of
	 * antennas that can compose it, without defining some
	 * value.
	 */
	public Individual(int nMaxAntennas) {
		genes = new Antenna[nMaxAntennas];
	}

	/**
	 * 
	 * @param nMaxAntena  maximum number of antennas that can compose the individual
	 * @param modAntennas available antenna models
	 * @param gridWidth   grid width
	 * @param accidents	  Geographic accidents
	 * 
	 *                  He instantiates the individual by setting all his
	 * 					characteristics, each time an antenna is created it evaluates whether
	 * 					is valid and in case it solves the problem.
	 */
	public Individual(int nMaxAntena, Antenna[] modAntennas, int gridWidth, Accident[] accidents) {
		genes = new Antenna[nMaxAntena];
		for (int i = 0; i < nMaxAntena; i++) {
			Random rand = new Random();
			try {
				genes[i] = (Antenna) modAntennas[rand.nextInt(modAntennas.length)].clone();
				genes[i].setPosition(rand.nextInt(gridWidth), rand.nextInt(gridWidth));

				boolean isValid = true;
				do {
					if (antennaInvalid(i, accidents)) {
						genes[i].setPosition(rand.nextInt(gridWidth), rand.nextInt(gridWidth));
						isValid = false;
					} else {
						isValid = true;
					}
				} while (isValid == false);
			} catch (CloneNotSupportedException e) {
				System.out.println("Problems with antenna cloning");
				i--;
			}
		}

	}

	/**
	 * 
	 * @param gridWidth       gird witdh
	 * @param accidents       geographical incidents
	 * @param aCoverage       areas to cover
	 * @param avgCostAntennas average cost of antennas
	 * @param weightingAreas  importance of area coverage in the algorithm
	 * @param weightingCost	  importance of keeping the cost of antennas low in the algorithm
	 * @param weightingConnection importance of connection between areas
	 * 
	 *                        Evaluates the individual and saves his fitness value
	 */
	public void evaluate(int gridWidth, Accident[] accidents, CoverageArea[] aCoverage, int avgCostAntennas, int weightingAreas, int weightingCost, int weightingConnection) {
		int[][] areaTotal = insertAntennasArea(gridWidth);
		areaTotal = insertAccidents(areaTotal, accidents);
		ArrayList<int[][]> aEvaluate = areaToEvaluate(areaTotal, aCoverage);

		float fitnessArea = 0;
		for (int i = 0; i < aEvaluate.size(); i++) {
			int minimumCoverage = aCoverage[i].getMinimumPower();
			float total = 0;
			int[][] area = aEvaluate.get(i);
			for (int j = 0; j < area.length; j++)
				for (int k = 0; k < area[0].length; k++) {
					if (minimumCoverage > area[j][k])
						total += minimumCoverage - area[j][k];
				}
			minimumCoverage *= area.length * area[0].length;
			total = (minimumCoverage - total) / minimumCoverage;
			fitnessArea += total;
		}

		fitnessArea /= aEvaluate.size();

		float costAntennas = 0;

		for (Antenna a : genes)
			costAntennas += a.getCost();

		float fitnessAntennas = avgCostAntennas / costAntennas;
		if (fitnessAntennas > 1)
			fitnessAntennas = 1;

		float fitnessConnections = evaluaAreasConnections(aCoverage);

		fitnessValue = (fitnessArea * weightingAreas + fitnessAntennas * weightingCost + fitnessConnections * weightingConnection) / 100;
		fitnessValue = (float) (Math.ceil(fitnessValue * Math.pow(10, 2)) / Math.pow(10, 2));
	}

	/**
	 * 
	 * @param gridWidth grid width
	 * @return grid containing the antennas
	 * 
	 *         Place the antennas of an individual within the grid
	 */
	private int[][] insertAntennasArea(int gridWidth) {
		int[][] areaTotal = new int[gridWidth][gridWidth];
		for (int i = 0; i < gridWidth; i++)
			for (int j = 0; j < gridWidth; j++)
				areaTotal[i][j] = 0;

		// Fills the matrix with the values of the antennas used
		for (int i = 0; i < genes.length; i++) {
			int x = genes[i].getX();
			int y = genes[i].getY();
			int width = genes[i].getCoverageWidth();
			areaTotal[y][x] += width;

			int tam_fila = width * 2 - 1;
			for (int j = 1; j < width; j++) {
				int y_temp = y - width + j;
				int x_temp = x - width + j;

				for (int k = 0; k < tam_fila; k++) {
					if (x_temp + k >= 0 && x_temp + k < gridWidth && y_temp >= 0 && y_temp < gridWidth)
						areaTotal[y_temp][x_temp + k] += j;
				}

				x_temp += tam_fila - 1;
				y_temp++;

				for (int k = 0; k < tam_fila - 1; k++) {
					if (y_temp + k >= 0 && y_temp + k < gridWidth && x_temp >= 0 && x_temp < gridWidth)
						areaTotal[y_temp + k][x_temp] += j;
				}

				y_temp += (tam_fila - 2);
				x_temp--;

				for (int k = 0; k < tam_fila - 1; k++) {
					if (x_temp - k >= 0 && x_temp - k < gridWidth && y_temp >= 0 && y_temp < gridWidth)
						areaTotal[y_temp][x_temp - k] += j;
				}

				x_temp -= (tam_fila - 2);
				y_temp--;

				for (int k = 0; k < tam_fila - 2; k++) {
					if (x_temp >= 0 && x_temp < gridWidth && y_temp - k >= 0 && y_temp - k < gridWidth)
						areaTotal[y_temp - k][x_temp] += j;
				}

				tam_fila -= 2;
			}
		}

		return areaTotal;
	}

	/**
	 * 
	 * @param zonaTotal total grid containing the antennas
	 * @param accidents geographical incidents
	 * @return total grid containing antennas and incidents
	 * 
	 *         Always call after you place the antennas.
	 */
	private int[][] insertAccidents(int[][] zonaTotal, Accident[] accidents) {
		for (int i = 0; i < accidents.length; i++) {
			int startX, endX;
			if (accidents[i].getX2() > accidents[i].getX1()) {
				startX = accidents[i].getX1();
				endX = accidents[i].getX2();
			} else {
				startX = accidents[i].getX2();
				endX = accidents[i].getX1();
			}

			int startY, endY;

			if (accidents[i].getY2() > accidents[i].getY1()) {
				startY = accidents[i].getY1();
				endY = accidents[i].getY2();
			} else {
				startY = accidents[i].getY2();
				endY = accidents[i].getY1();
			}

			for (int j = startX; j <= endX; j++) {
				for (int k = startY; k <= endY; k++) {
					zonaTotal[k][j] -= accidents[i].getIncidence();
					if (zonaTotal[k][j] < 0)
						zonaTotal[k][j] = 0;
				}
			}

		}

		return zonaTotal;
	}

	/**
	 * 
	 * @param zonaTotal area containing the antennas and the geographical incidents
	 * @param aCoverage areas to cover
	 * @return extracts the zones to be covered and returns an ArrayList containing the zones
	 * interested.
	 */
	private ArrayList<int[][]> areaToEvaluate(int[][] zonaTotal, CoverageArea[] aCoverage) {
		ArrayList<int[][]> areas = new ArrayList<int[][]>();
		for (int i = 0; i < aCoverage.length; i++)
			areas.add(aCoverage[i].getMatrixArea(zonaTotal));

		return areas;
	}

	/**
	 * 
	 * @param antennas models of available antennas
	 * 
	 *          Apply the mutation to the individual, changing a pattern of
	 * 			antenna, chosen randomly, but keeping the position
	 * 			of the previous antenna
	 */
	public void mutate(Antenna[] antennas) {
		Random rand = new Random();
		int index = rand.nextInt(genes.length);
		int x = genes[rand.nextInt(genes.length)].getX();
		int y = genes[rand.nextInt(genes.length)].getY();

		genes[index] = antennas[rand.nextInt(antennas.length)];
		genes[index].setPosition(x, y);
	}

	/**
	 * 
	 * @param gridWidth grid width
	 * @param accidents geographical incidents
	 * 
	 *                  Apply the mutation to the individual, changing position
	 * 					of a randomly chosen antenna.
	 */
	public void mutate(int gridWidth, Accident[] accidents) {
		Random rand = new Random();
		int index = rand.nextInt(genes.length);
		genes[index].setPosition(rand.nextInt(gridWidth), rand.nextInt(gridWidth));
		if (antennaInvalid(index, accidents))
			genes[index].setPosition(rand.nextInt(gridWidth), rand.nextInt(gridWidth));
	}

	/**
	 * 
	 * @param index     index of the antenna to be verified
	 * @param accidents geographical incidents
	 * @return false: antenna valid, true: antenna invalid
	 * 
	 *         Check if an antenna is invalid. An antenna is invalid if it is
	 * 			placed on top of another antenna or on top of a geographical incident.
	 */
	private boolean antennaInvalid(int index, Accident[] accidents) {
		boolean invalid = false;
		int lmax = 0;
		for (int i = 0; i < genes.length; i++) {
			if (genes[i] != null)
				lmax++;
		}

		for (int i = 0; i < lmax; i++) {
			if (i != index) {
				if (genes[i].getX() == genes[index].getX() && genes[i].getY() == genes[index].getY()) {
					invalid = true;
				}
			}
		}

		if (!invalid)
			for (int i = 0; i < accidents.length; i++) {
				int startX, endX, startY, endY;
				if (accidents[i].getX1() > accidents[i].getX2()) {
					startX = accidents[i].getX2();
					endX = accidents[i].getX1();
				} else {
					startX = accidents[i].getX1();
					endX = accidents[i].getX2();
				}

				if (accidents[i].getY1() > accidents[i].getY2()) {
					startY = accidents[i].getY2();
					endY = accidents[i].getY1();
				} else {
					startY = accidents[i].getY1();
					endY = accidents[i].getY2();
				}

				if (genes[index].getX() > startX && genes[index].getX() < endX && genes[index].getY() > startY
						&& genes[index].getY() < endY)
					invalid = true;
			}

		return invalid;
	}

	/**
	 * 
	 * @param accidents Geographic accidents
	 * @return false: valid, true: invalid
	 * 
	 *         Check if an individual is disabled. An individual is disabled if he has
	 * 			at least one invalid antenna.
	 */
	public boolean isInvalid(Accident[] accidents) {
		boolean invalid = false;

		for (int i = 0; i < genes.length; i++) {
			if (antennaInvalid(i, accidents))
				invalid = true;
		}

		return invalid;
	}

	/**
	 * 
	 * @param aCoverage coverage areas
	 * @return fitness [0,1]
	 * 
	 * Assesses whether the areas are connected to each other and attributes a fitness
	 */
	private float evaluaAreasConnections(CoverageArea[] aCoverage) {
		
		Graph<Antenna, DefaultEdge> g = new DefaultUndirectedGraph<Antenna, DefaultEdge>(DefaultEdge.class);
		// the vertexes of the graph are all the antennae of the individual
		for (int i = 0; i < genes.length; i++)
			g.addVertex(genes[i]);

		// arches are composed of connected vertexes
		for (int i = 0; i < genes.length; i++)
			for (int j = i; j < genes.length; j++)
				if (i != j && isConnected(genes[i], genes[j]))
					g.addEdge(genes[i], genes[j]);
		
		

		// I check the antennas covering the areas
		ArrayList<Antenna> a = new ArrayList<Antenna>();
		ArrayList<ArrayList<Integer>> ar = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < genes.length; i++) {
			ArrayList<Integer> temp = new ArrayList<Integer>();
			
			temp = coversTheArea(i, aCoverage);
			
			if(temp.size() > 0) {
				// At this point I have two ArrayLists, one with the antennas covering the areas and one with the areas covered by each antenna
				a.add(genes[i]);
				ar.add(temp);
			}
		}

		// Graph paths
		ConnectivityInspector<Antenna, DefaultEdge> isp = new ConnectivityInspector<Antenna, DefaultEdge>(g);

		List<Set<Antenna>> l = isp.connectedSets();
		
		int count = 0;
		// For each antenna present I add the list of zones to an ArrayList, then delete the same zone numbers and get the number of connected zones
		for(int i = 0; i < l.size(); i++) {
			ArrayList<Integer> zonaTemp = new ArrayList<Integer>();
			for(int j = 0; j < a.size(); j++) {
				if(l.get(i).contains(a.get(j))) {
					for(int k = 0; k < ar.get(j).size(); k++)
						zonaTemp.add(ar.get(j).get(k));
				}
			}
			
			for(int j = 0; j < zonaTemp.size(); j++) {
				for(int k = 0; k < zonaTemp.size(); k++) {
					if(j != k && zonaTemp.get(j).equals(zonaTemp.get(k))) {
						zonaTemp.remove(k);
						k = 0;
					}
				}
			}
			
			if(zonaTemp.size() > count)
				count = zonaTemp.size();
			
		}
		
		float fit = 0;
		if(count > 1)
			fit = count / aCoverage.length;
		
		return fit;

	}

	/**
	 * 
	 * @param init first antenna
	 * @param end second antenna
	 * @return true: connection, false: no connection
	 * 
	 * Check if there is a connection between two antennas
	 */
	private boolean isConnected(Antenna init, Antenna end) {
		float distance = (float) Math
				.sqrt(Math.pow(init.getX() - end.getX(), 2) + Math.pow(init.getY() - end.getY(), 2));
		return distance < init.getCoverageWidth() + end.getCoverageWidth() ? true : false;

	}

	/**
	 * 
	 * @param i index of antenna a evaluar
	 * @param aCoverage all areas to cover
	 * @return list of coverage area of selected antenna
	 */
	private ArrayList<Integer> coversTheArea(int i, CoverageArea[] aCoverage) {
		
		ArrayList<Integer> areas = new ArrayList<Integer>();

		for (int j = 0; j < aCoverage.length; j++) {

			boolean coverage = false;
			float distance = (float) Math.sqrt(Math.pow(aCoverage[j].getX1() - genes[i].getX(), 2)
					+ Math.pow(aCoverage[j].getY1() - genes[i].getY(), 2));

			if (distance < genes[i].getCoverageWidth()) {
				coverage = true;
			} else {
				distance = (float) Math.sqrt(Math.pow(aCoverage[j].getX1() - genes[i].getX(), 2)
						+ Math.pow(aCoverage[j].getY2() - genes[i].getY(), 2));

				if (distance < genes[i].getCoverageWidth()) {
					coverage = true;
				} else {
					distance = (float) Math.sqrt(Math.pow(aCoverage[j].getX2() - genes[i].getX(), 2)
							+ Math.pow(aCoverage[j].getY1() - genes[i].getY(), 2));

					if (distance < genes[i].getCoverageWidth()) {
						coverage = true;
					} else {
						distance = (float) Math.sqrt(Math.pow(aCoverage[j].getX2() - genes[i].getX(), 2)
								+ Math.pow(aCoverage[j].getY2() - genes[i].getY(), 2));

						if (distance < genes[i].getCoverageWidth()) {
							coverage = true;
						}
					}

				}
			}

			if (coverage)
				areas.add(j);

		}

		return areas;
	}

	/**
	 * 
	 * @return fitness value of the individual
	 */
	public float getFitnessValue() {
		return fitnessValue;
	}

	/**
	 * 
	 * @param index index of antenna
	 * @return antenna requested
	 */
	public Antenna getGene(int index) {
		return this.genes[index];
	}

	/**
	 * @param index index of antenna
	 * @param gene the genes to set
	 */
	public void setGene(int index, Antenna gene) {
		try {
			this.genes[index] = (Antenna) gene.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param fitnessValue fitness value of the individual
	 */
	public void setFitnessValue(float fitnessValue) {
		this.fitnessValue = fitnessValue;
	}

	/**
	 * 
	 * @param gridWidth grid width
	 * @param accidents geographical incidents
	 * 
	 *                  Print a partial representation on screen
	 * 					of the individual, without considering the areas to be covered
	 */
	public void print(int gridWidth, Accident[] accidents) {
		int[][] areaTotal = insertAntennasArea(gridWidth);
		areaTotal = insertAccidents(areaTotal, accidents);
		Utility.printArea(areaTotal);
	}

	/**
	 * 
	 * @param gridWidth grid width
	 * @param accidents geographical incidents
	 * @param aCoverage areas coverage
	 * 
	 *                  Print a complete representation on screen
	 * 					of the individual
	 */
	public void print(int gridWidth, Accident[] accidents, CoverageArea[] aCoverage) {
		int[][] areaTotal = insertAntennasArea(gridWidth);
		areaTotal = insertAccidents(areaTotal, accidents);
		Utility.printArea(areaTotal);
		ArrayList<int[][]> aEvaluate = areaToEvaluate(areaTotal, aCoverage);
		for (int[][] z : aEvaluate)
			Utility.printArea(z);
	}

	/**
	 * 
	 * @return all the antennas that make up the individual
	 */
	public Antenna[] getAntennas() {
		return this.genes;
	}

	@Override
	public String toString() {
		String ind = "Antennas models: \n";

		for (int i = 0; i < genes.length; i++)
			ind += "Antenna " + i + ", coverage: " + genes[i].getCoverageWidth() + ", cost: " + genes[i].getCost()
					+ "€, Position (x,y): " + genes[i].getX() + " , " + genes[i].getY() + " \n";

		ind += "Fitness: " + fitnessValue + "\n";

		return ind;

	}

	/**
	 * Compare two individuals 0: if they have the same fitness 1: if ind1.fitness  smaller than
	 * ind2.fitness else -1
	 */
	@Override
	public int compareTo(Individual ind) {
		int result = 0;
		if (fitnessValue > ind.getFitnessValue())
			result = -1;
		else if (fitnessValue < ind.getFitnessValue())
			result = 1;
		return result;
	}

	/**
	 * Clone an individual, a deep cloning is performed
	 */
	@Override
	public Object clone() {
		Individual ind = new Individual(this.getAntennas().length);
		ind.setFitnessValue(fitnessValue);
		for (int i = 0; i < ind.getAntennas().length; i++)
			ind.setGene(i, this.genes[i]);
		return ind;

	}

}
