package com.es.unex.practica_sistemas_inteligentes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * 
 * This class contains the initial parameters read from the configuration file,
 * the parameters will be used throughout the program and are enclosed here for easy reading.
 * Since they must not change during the execution of the program, access to the properties is allowed in read-only mode.
 * 
 * @author eliapacioni
 * @version 0.1
 * 
 */
public class Parameters {

	/**
	 * Population size
	 */
	private int popSize; 
	/**
	 * Maximum number of iterations 
	 */
	private int iter; 
	/**
	 * Crossover Probility
	 */
	private float pCrossover;
	/**
	 * Probability of mutation
	 */
	private float pMutation; 
	/**
	 * Number of elitist (can be between 0 and population size)
	 */
	private int elitism;
	/**
	 * Type of replacement chosen, stationary or generational
	 * false: Stationary
	 * true: Generational 
	 */
	private boolean replacement;
	/**
	 * Crossover operator 
	 * false: crossoverMulti (2 points) 
	 * true: crossover (1 point)
	 */
	private boolean opCrossover; 
	/**
	 * Mutation operator
	 * false: mutate(Antennas[]), mutate an antenna
	 * true: mutate() , changes the position
	 */
	private boolean opMutation; 
	/**
	 * Selection algorithm
	 * false: RouletteWheel
	 * true: Tournament
	 */
	private boolean alSel; 
	/**
	 * Maximum number of antennas for an individual
	 */
	private int nMaxAntenna; 
	/**
	 * Average cost of antennas * nMaxAntenna
	 * It is used to calculate the goodness of the price in fitness
	 */
	private int avgCostAntennas; 
	/**
	 * Grid size (the grid is always square)
	 */
	private int gridWidth;
	/**
	 * Areas to be covered
	 */
	private CoverageArea[] coverageAreas; 
	/**
	 * Geographical incidents
	 */
	private Accident[] accidents; 
	/**
	 * Available antenna models
	 */
	private Antenna[] modAntennas; 
	/**
	 * Color management in the terminal: true: activate colors, false: deactivate
	 * For the final printing of the individual is always active
	 */
	private boolean colors;
	/**
	 * Importance of area coverage in the algorithm
	 */
	private int weightingAreas;
	/**
	 * Importance of keeping the cost of antennas low in the algorithm
	 */
	private int weightingCost;
	/**
	 * Importance of connection between areas
	 */
	private int weightingConnection;
	
	

	/**
	 * Manufacturer without parameters, instantiates the class with default values
	 */
	public Parameters() {
		popSize = 0;
		iter = 0;
		pCrossover = 100;
		pMutation = 100;
		elitism = 0;
		replacement = false;
		opCrossover = false;
		opMutation = false;
		alSel = false;
		gridWidth = 0;
		nMaxAntenna = 0;
		modAntennas = null;
		coverageAreas = null;
		accidents = null;
		colors = false;
		weightingAreas = 60;
		weightingCost = 30;
		weightingConnection = 10;
	}

	/**
	 * 
	 * @param config file from which to read the configuration parameters
	 * 
	 * Reads the configuration parameters and stores them in the appropriate properties.
	 */
	public void implementParameters(File config) {
		File f = config;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "utf-8"));
			ArrayList<Antenna> antennasTemp = new ArrayList<Antenna>();
			ArrayList<CoverageArea> coverageAreaTemp = new ArrayList<CoverageArea>();
			ArrayList<Accident> accidentsTemp = new ArrayList<Accident>();
			while (br.ready()) {
				//System.out.println(br.readLine());
				String line = br.readLine();
				if(!line.isEmpty()) {
					StringTokenizer tok = new StringTokenizer(line);
					String name = tok.nextToken().toLowerCase();
					switch(name) {
						
					case "pobsize":
						popSize = Integer.parseInt(tok.nextToken());
						break;
						
					case "iter":
						iter = Integer.parseInt(tok.nextToken());
						break;
						
					case "pcrossover":
						pCrossover = Integer.parseInt(tok.nextToken());
						break;
						
					case "pmutation":
						pMutation = Integer.parseInt(tok.nextToken());
						break;
					
					case "elitism":
						elitism = Integer.parseInt(tok.nextToken());
						break;
						
					case "replacement":
						replacement = Boolean.parseBoolean(tok.nextToken());
						break;
						
					case "opcrossover":
						opCrossover = Boolean.parseBoolean(tok.nextToken());
						break;
						
					case "opmutation":
						opMutation = Boolean.parseBoolean(tok.nextToken());
						break;
						
					case "alsel":
						alSel = Boolean.parseBoolean(tok.nextToken());
						break;
					
					case "nmaxantenna":
						nMaxAntenna = Integer.parseInt(tok.nextToken());
						break;
						
					case "anchocuadricula":
						gridWidth = Integer.parseInt(tok.nextToken());
						break;
						
					case "colors":
						colors = Boolean.parseBoolean(tok.nextToken());
						break;
						
					case "zonacobertura":
						coverageAreaTemp.add(new CoverageArea(Integer.parseInt(tok.nextToken()), Integer.parseInt(tok.nextToken()), Integer.parseInt(tok.nextToken()), Integer.parseInt(tok.nextToken()), Integer.parseInt(tok.nextToken())));
						break;
						
					case "accidente":
						accidentsTemp.add(new Accident(Integer.parseInt(tok.nextToken()), Integer.parseInt(tok.nextToken()), Integer.parseInt(tok.nextToken()), Integer.parseInt(tok.nextToken()), Integer.parseInt(tok.nextToken())));
						break;
						
					case "antena":
						antennasTemp.add(new Antenna(Integer.parseInt(tok.nextToken()), Integer.parseInt(tok.nextToken())));
						break;
					
					case "weightingareas":
						weightingAreas = Integer.parseInt(tok.nextToken());
						break;
						
					case "weightingcost":
						weightingCost = Integer.parseInt(tok.nextToken());
						break;
						
					case "weightingconnection":
						weightingConnection = Integer.parseInt(tok.nextToken());
						break;
					
					}
					
				}
			}
			
			br.close();
			
			coverageAreas = new CoverageArea[coverageAreaTemp.size()];
			coverageAreaTemp.toArray(coverageAreas);
			
			accidents = new Accident[accidentsTemp.size()];
			accidentsTemp.toArray(accidents);
			
			modAntennas = new Antenna[antennasTemp.size()];
			antennasTemp.toArray(modAntennas);
			
			avgCostAntennas = 0;

			for (int i = 0; i < modAntennas.length; i++)
				avgCostAntennas += modAntennas[i].getCost();
			
			avgCostAntennas/= modAntennas.length;
				
			
			avgCostAntennas*= nMaxAntenna;
			
		} catch (FileNotFoundException e) {
			Utility.error(colors);
			System.out.println("File not found");
			return;
		} catch (IOException e) {
			Utility.error(colors);
			System.out.println("IO Exception, syntax error in the file");
			return;
		}
	}

	/**
	 * 
	 * @return int population size
	 */
	public int getPopSize() {
		return popSize;
	}

	/**
	 * 
	 * @return int maximum number of iterations
	 */
	public int getIter() {
		return iter;
	}
	
	

	/**
	 * 
	 * @return float probability of crossover
	 */
	public float getpCrossover() {
		return pCrossover;
	}

	/**
	 * 
	 * @return float probability of mutation
	 */
	public float getpMutation() {
		return pMutation;
	}

	/**
	 * 
	 * @return int number of elitist
	 */
	public int getElitism() {
		return elitism;
	}

	/**
	 * 
	 * @return boolean chosen replacement strategy
	 */
	public boolean isReplacement() {
		return replacement;
	}

	/**
	 * 
	 * @return boolean selected crossover operator
	 */
	public boolean isOpCrossover() {
		return opCrossover;
	}

	/**
	 * 
	 * @return boolean selected mutation operator
	 */
	public boolean isOpMutation() {
		return opMutation;
	}

	/**
	 * 
	 * @return boolean selected operator
	 */
	public boolean isAlSel() {
		return alSel;
	}

	/**
	 * 
	 * @return int maximum number of antennas that can be placed for an individual
	 */
	public int getnMaxAntennas() {
		return nMaxAntenna;
	}

	/**
	 * 
	 * @return Antenna[] models of available antennas
	 */
	public Antenna[] getModAntennas() {
		return modAntennas;
	}

	/**
	 * 
	 * @return int side size of the square
	 */
	public int gridWidth() {
		return gridWidth;
	}

	/**
	 * 
	 * @return CoverageAreas[] coverage areas 
	 */
	public CoverageArea[] getCoverageArea() {
		return coverageAreas;
	}

	/**
	 * 
	 * @return Accident[] accidents
	 */
	public Accident[] getAccidents() {
		return accidents;
	}

	/**
	 * @return the average cost of antennas
	 */
	public int getAvgCostAntennas() {
		return avgCostAntennas;
	}
	
	/**
	 * 
	 * @return is used to manage the colours in the terminal
	 */
	public boolean isColors() {
		return colors;
	}

	/**
	 * 
	 * @return weigthing of areas coverage
	 */
	public int getWeightingAreas() {
		return weightingAreas;
	}

	/**
	 * 
	 * @return weigthing of antennas cost
	 */
	public int getWeightingCost() {
		return weightingCost;
	}

	/**
	 * 
	 * @return weigthing of areas connection
	 */
	public int getWeightingConnection() {
		return weightingConnection;
	}
	
	
	

}
