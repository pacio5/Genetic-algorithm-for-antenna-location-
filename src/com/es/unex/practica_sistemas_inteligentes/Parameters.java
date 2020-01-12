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
		gridWidth = 0;
		nMaxAntenna = 0;
		modAntennas = null;
		coverageAreas = null;
		accidents = null;
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
