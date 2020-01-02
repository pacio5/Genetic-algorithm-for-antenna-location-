package com.es.unex.practica_sistemas_inteligentes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * @author eliapacioni
 * @version 0.1
 * 
 * Questa classe contiene i parametri iniziali letti dal file di configurazione,
 * i parametri verranno usati all'interno di tutto il programma e sono racchiusi qui per facilitarne la lettura.
 * Poiché non devono mutare nel corso dell'esecuzione del programma, l'accesso alle proprietà è consentito in sola lettura.
 * 
 */
public class Parameters {

	/**
	 * Dimensione della popolazione
	 */
	private int popSize; 
	/**
	 * Numero di iterazioni massime 
	 */
	private int iter; 
	/**
	 * Proabilità di effettuare il crossover
	 */
	private float pCrossover;
	/**
	 * Probabilità di effettuare la mutazione
	 */
	private float pMutation; 
	/**
	 * Numero di elitisti (può essere compreso tra 0 e la dimensione della popolazione)
	 */
	private int elitism;
	/**
	 * Tipo di rimpiazzamento scelto, stazionario o generazionale
	 * false -> Stazionario
	 * true -> Generazionale 
	 */
	private boolean replacement;
	/**
	 * Operatore di crossover 
	 * false -> crossoverMulti (2 punti) 
	 * true -> crossover (1 punto)
	 */
	private boolean opCrossover; 
	/**
	 * Operatore di mutazione
	 * false -> mutate(Antennas[]), muta un'antenna
	 * true -> mutate() , muta la posizione
	 */
	private boolean opMutation; 
	/**
	 * Algoritmo di selezione
	 * false -> RouletteWheel
	 * true -> Torneo
	 */
	private boolean alSel; 
	/**
	 * Numero massimo di antenne per un individuo
	 */
	private int nMaxAntenna; 
	/**
	 * Costo Medio delle antenne * nMaxAntenna
	 * Viene usato per calcolare la bontà del prezzo nella fitness
	 */
	private int avgCostAntennas; 
	/**
	 * Dimensione della griglia (la griglia è sempre quadrata)
	 */
	private int gridWidth;
	/**
	 * Zone che devono essere coperte
	 */
	private CoverageArea[] coverageAreas; 
	/**
	 * Accidenti geografici
	 */
	private Accident[] accidents; 
	/**
	 * Modelli di antenne disponibili
	 */
	private Antenna[] modAntennas; 
	/**
	 * Gestione di colori nel terminale: true -> attiva i colori, false -> disattiva
	 */
	
	private boolean colors;
	

	/**
	 * Costruttore senza parametri, istanzia la classe con valori di default
	 */
	public Parameters() {
		popSize = 0;
		iter = 0;
		pCrossover = 0;
		pMutation = 0;
		elitism = 0;
		replacement = false;
		opCrossover = false;
		opMutation = false;
		alSel = false;
		nMaxAntenna = 0;
		modAntennas = null;
		gridWidth = 0;
		colors = false;
	}

	public Parameters(boolean test) {
		popSize = 1000;
		iter = 1000;
		pCrossover = 100;
		pMutation = 100;
		elitism = 1;
		replacement = true;
		opCrossover = false;
		opMutation = true;
		alSel = true;
		nMaxAntenna = 5;
		gridWidth = 100;

		modAntennas = new Antenna[6];
		modAntennas[0] = new Antenna(4, 200);
		modAntennas[1] = new Antenna(7, 1000);
		modAntennas[2] = new Antenna(8, 425);
		modAntennas[3] = new Antenna(12, 1300);
		modAntennas[4] = new Antenna(5, 350);
		modAntennas[5] = new Antenna(10, 1050);

		coverageAreas = new CoverageArea[4];

		coverageAreas[0] = new CoverageArea(8, 7, 10, 24, 5);
		coverageAreas[1] = new CoverageArea(32, 37, 23, 47, 7);
		coverageAreas[2] = new CoverageArea(82, 87, 73, 67, 4);
		coverageAreas[3] = new CoverageArea(2, 87, 6, 77, 4);

		int nAccidents = 2;
		accidents = new Accident[nAccidents];

		Random rand = new Random();

		for (int i = 0; i < nAccidents; i++) {
			accidents[i] = new Accident(rand.nextInt(gridWidth), rand.nextInt(gridWidth),
					rand.nextInt(gridWidth), rand.nextInt(gridWidth), rand.nextInt(5));
		}

		avgCostAntennas = 0;

		for (int i = 0; i < modAntennas.length; i++)
			avgCostAntennas += modAntennas[i].getCost();
		
		avgCostAntennas/= modAntennas.length;
			
		
		avgCostAntennas*= nMaxAntenna;
		
		colors = false;
		
	}

	/**
	 * 
	 * @param config file da cui leggere i parametri di configurazione
	 * 
	 * Legge i parametri di configurazione e li memorizza nelle apposite proprietà.
	 */
	public void implementParameters(File config) {
		File f = config;
		// Leggo il file di configurazione
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "utf-8"));
			while (br.ready())
				System.out.println(br.readLine());

			br.close();
		} catch (FileNotFoundException e) {
			Utility.error(colors);
			System.out.println("Problema al leer el fichero, fichero no encontrado");
			return;
		} catch (IOException e) {
			System.out.println("Problema al leer el fichero, IO exception");
			return;
		}
	}

	/**
	 * 
	 * @return int dimensione della popolazione
	 */
	public int getPopSize() {
		return popSize;
	}

	/**
	 * 
	 * @return int numero di iterazioni massime
	 */
	public int getIter() {
		return iter;
	}
	
	

	/**
	 * 
	 * @return float probabilità di effettuare il crossover
	 */
	public float getpCrossover() {
		return pCrossover;
	}

	/**
	 * 
	 * @return float probabilità di effettuare la mutazione
	 */
	public float getpMutation() {
		return pMutation;
	}

	/**
	 * 
	 * @return int numero di elitisti
	 */
	public int getElitism() {
		return elitism;
	}

	/**
	 * 
	 * @return boolean strategia di rimpiazzamento scelta
	 */
	public boolean isReplacement() {
		return replacement;
	}

	/**
	 * 
	 * @return boolean operatore di crossover scelto
	 */
	public boolean isOpCrossover() {
		return opCrossover;
	}

	/**
	 * 
	 * @return boolean operatore di mutazione scelto
	 */
	public boolean isOpMutation() {
		return opMutation;
	}

	/**
	 * 
	 * @return boolean operatore di selezione scelto
	 */
	public boolean isAlSel() {
		return alSel;
	}

	/**
	 * 
	 * @return int numero massimo di antenne posizionabili per un individuo
	 */
	public int getnMaxAntennas() {
		return nMaxAntenna;
	}

	/**
	 * 
	 * @return Antenna[] modelli di antenne disponibili
	 */
	public Antenna[] getModAntennas() {
		return modAntennas;
	}

	/**
	 * 
	 * @return int dimensione lato del quadrato
	 */
	public int gridWidth() {
		return gridWidth;
	}

	/**
	 * 
	 * @return CoverageAreas[] zone di copertura 
	 */
	public CoverageArea[] getCoverageArea() {
		return coverageAreas;
	}

	/**
	 * 
	 * @return Accident[] accidentes
	 */
	public Accident[] getAccidents() {
		return accidents;
	}

	/**
	 * @return the costoAntennasMedio
	 */
	public int getAvgCostAntennas() {
		return avgCostAntennas;
	}
	
	/**
	 * 
	 * @return serve per gestire i colori nel terminale
	 */
	public boolean isColors() {
		return colors;
	}
	

}
