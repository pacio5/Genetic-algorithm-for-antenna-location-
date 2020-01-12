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
 * @author eliapacioni
 * @version 0.1
 * 
 *          Classe che definisce la struttura e le azioni di un individuo.
 *          Fondamentale all'interno del programma perché tutta l'esecuzione si
 *          basa sull'utilizzo degli individui. Implementa l'interfaccia
 *          Cloneable per effettuare la clonazione dell'individuo, così da non
 *          avere problemi durante le modifiche successive Implemente
 *          l'interfaccia Comparable per effettuare il confronto tra individui e
 *          per poterli ordinare in base al valore di fitness
 * 
 */
public class Individual implements Comparable<Individual>, Cloneable {
	private Antenna[] genes;
	private float fitnessValue;

	/**
	 * Costruttore senza parametri, individuo nullo.
	 */
	public Individual() {
		genes = null;
		fitnessValue = -1;
	}

	/**
	 * 
	 * @param nMaxAntennas numero massimo di antenne che possono comporre
	 *                     l'individuo
	 * 
	 *                     Istanzia l'individuo definendo solo il numero massimo di
	 *                     antenne che possono comporlo, senza definire alcune
	 *                     valore.
	 */
	public Individual(int nMaxAntennas) {
		genes = new Antenna[nMaxAntennas];
	}

	/**
	 * 
	 * @param nMaxAntena  numero massimo di antenne che possono comporre l'individuo
	 * @param modAntennas modelli di antenna disponibili
	 * @param gridWidth   dimensione griglia
	 * 
	 *                    Istanzia l'individuo impostando tutte le sue
	 *                    caratteristiche, ad ogni creazione di un'antenna valuta se
	 *                    è valida ed in caso risolve il problema.
	 */
	public Individual(int nMaxAntena, Antenna[] modAntennas, int gridWidth, Accident[] accidents) {
		genes = new Antenna[nMaxAntena];
		for (int i = 0; i < nMaxAntena; i++) {
			Random rand = new Random();
			// cambia seme random
			try {
				genes[i] = (Antenna) modAntennas[rand.nextInt(modAntennas.length)].clone();
				genes[i].setPosition(rand.nextInt(gridWidth), rand.nextInt(gridWidth));
				// Un'antenna è invalida se sta nella stessa posizione di un'altra o sopra un
				// accidente
				
				// controlla con un ciclo while se l'antenna è valida o no
				
				boolean isValid = true;
				do {
					if (antennaInvalid(i, accidents)) {
						genes[i].setPosition(rand.nextInt(gridWidth), rand.nextInt(gridWidth));
						isValid = false;
					} else {
						isValid = true;
					}
				}
				while(isValid == false);
			} catch (CloneNotSupportedException e) {
				System.out.println("Problemi nella clonazione dell'antenna");
				i--;
			}
		}

	}

	/**
	 * 
	 * @param gridWidth       dimensione griglia
	 * @param accidents       accidenti geografici
	 * @param aCoverage       aree da coprire
	 * @param avgCostAntennas costo medio antenne
	 * 
	 *                        Valuta l'individuo e salva il suo valore di fitness
	 */
	public void evaluate(int gridWidth, Accident[] accidents, CoverageArea[] aCoverage, int avgCostAntennas) {
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

		//System.out.println("FitnessConnections: " + fitnessConnections);
		/*
		System.out.println("FitnessArea: " + fitnessArea);
		System.out.println("FitnessAntennas: " + fitnessAntennas);*/
		// Ponderazione della fitness di connessione posta a 0 perché al momento ha problemi
		fitnessValue = (fitnessArea * 70 + fitnessAntennas * 30 + fitnessConnections * 0) / 100;
		fitnessValue = (float) (Math.ceil(fitnessValue * Math.pow(10, 2)) / Math.pow(10, 2));
	}

	/**
	 * 
	 * @param gridWidth dimensione della griglia
	 * @return griglia contenente le antenne
	 * 
	 *         Posiziona le antenne di un individuo all'interno della griglia
	 */
	private int[][] insertAntennasArea(int gridWidth) {
		int[][] areaTotal = new int[gridWidth][gridWidth];
		for (int i = 0; i < gridWidth; i++)
			for (int j = 0; j < gridWidth; j++)
				areaTotal[i][j] = 0;

		// Riempie la matrice con i valori delle antenne utilizzate
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
	 * @param zonaTotal griglia totale contente le antenne
	 * @param accidents accidenti geografici
	 * @return griglia totale contenente antenne e accidenti
	 * 
	 *         Va chiamato sempre dopo aver posizionato le antenne.
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
	 * @param zonaTotal area contenente le antenne e gli accidenti geografici
	 * @param aCoverage zone da coprire
	 * @return estre le zone da coprire e restituisce un ArrayList contente le zone
	 *         interessate.
	 */
	private ArrayList<int[][]> areaToEvaluate(int[][] zonaTotal, CoverageArea[] aCoverage) {
		ArrayList<int[][]> areas = new ArrayList<int[][]>();
		for (int i = 0; i < aCoverage.length; i++)
			areas.add(aCoverage[i].getMatrixArea(zonaTotal));

		return areas;
	}

	/**
	 * 
	 * @param antennas modelli di antenne disponibili
	 * 
	 *                 Applica la mutazione all'individuo, cambiando un modello di
	 *                 antenna, scelto aleatoriamente, ma mantenendo la posizione
	 *                 dell'antenna precedente
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
	 * @param gridWidth dimensione della griglia
	 * 
	 *                  Applica la mutazione all'individuo, cambiando la posizione
	 *                  di un'antenna scelta aleatoriamente.
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
	 * @param index     indice dell'antenna da verificare
	 * @param accidents accidenti geografici
	 * @return false -> antenna valida, true -> antenna invalida
	 * 
	 *         Verifica se un'antenna è invalida. Un'antenna è invalida se è
	 *         posizionata sopra un'altra antenna o sopra un'accidente geografico.
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
				// Verificare se è compreso nell'accidente
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
	 * @return false -> valido, true -> invalido
	 * 
	 *         Verifica se un individuo è invalido. Un'individuo è invalido se ha
	 *         almeno un'antenna invalida.
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
	 * @param aCoverage
	 * @return
	 */
	private float evaluaAreasConnections(CoverageArea[] aCoverage) {
		float fit = 0;
		Graph<Antenna, DefaultEdge> g = new DefaultUndirectedGraph<Antenna, DefaultEdge>(DefaultEdge.class);
		// i vertici del grafo sono tutte le antenne dell'individuo
		for (int i = 0; i < genes.length; i++)
			g.addVertex(genes[i]);

		// gli archi sono composti da vertici connessi
		for (int i = 0; i < genes.length; i++)
			for (int j = i; j < genes.length; j++)
				if (i != j && isConnected(genes[i], genes[j]))
					g.addEdge(genes[i], genes[j]);
		
		// recupero le antenne che sono nelle zone e vedo se fanno parte di un percorso
		// del grafo
		ArrayList<Antenna> a = new ArrayList<Antenna>();
		for (int i = 0; i < genes.length; i++)
			if (coversTheArea(i, aCoverage).size() == 0)
				a.add(genes[i]);

		// Percorsi del grafo
		ConnectivityInspector<Antenna, DefaultEdge> isp = new ConnectivityInspector<Antenna, DefaultEdge>(g);

		List<Set<Antenna>> l = isp.connectedSets();

		float bestRoute = 0;
		for (int i = 0; i < l.size(); i++) {
			float count = 0;
			for (int j = 0; j < a.size(); j++) {
				if (l.get(i).contains(a.get(j)))
					count++;
			}

			if (bestRoute < count)
				bestRoute = count;
		}

		
		if (bestRoute >= aCoverage.length)
			fit = 1;
		else if(bestRoute == 1)
			fit = 0;
		else 
			fit = bestRoute / aCoverage.length;
		return fit;

	}

	/**
	 * 
	 * @param init
	 * @param end
	 * @return
	 */
	private boolean isConnected(Antenna init, Antenna end) {
		float distance = (float) Math
				.sqrt(Math.pow(init.getX() - end.getX(), 2) + Math.pow(init.getY() - end.getY(), 2));
		return distance < init.getCoverageWidth() + end.getCoverageWidth() ? true : false;

	}

	private ArrayList<Integer> coversTheArea(int i, CoverageArea[] aCoverage) {
		// verificare se l'antenna copre l'area, non importa l'intensità e la percentuale di area coperta.
		// Se la copre ritorna l'area che copre, potrebbe coprirne più di una, quindi il valore di ritorno è un array di aree
		// Se la lunghezza di "areas" è 0, l'antenna non copre nessuna area
		
		ArrayList<Integer> areas = new ArrayList<Integer>();
		
		int startX, endX, startY, endY;
		
		for (int j = 0; j < aCoverage.length; j++) {
			
			if (aCoverage[j].getX1() < aCoverage[j].getX2()) {
				startX = aCoverage[j].getX1();
				endX = aCoverage[j].getX2();
			} else {
				startX = aCoverage[j].getX2();
				endX = aCoverage[j].getX1();
			}
			
			if (aCoverage[j].getY1() < aCoverage[j].getY2()) {
				startY = aCoverage[j].getY1();
				endY = aCoverage[j].getY2();
			} else {
				startY = aCoverage[j].getY2();
				endY = aCoverage[j].getY1();
			}
			
		
			
			if (genes[i].getX() >= startX && genes[i].getX() <= endX && genes[i].getY() >= startY
					&& genes[i].getY() <= endY) {
				areas.add(j);
			} else {
				// Verificare se l'antenna si trova fuori ma copre la zona, in caso ritorna true
				// Calcolo la distanza tra i vertici della zona e il punto centrale dell'antenna
				// Se sono minori del raggio dell'antenna, l'antenna copre la zona
			}
			
		}
		
		/*
		boolean present = false;
		int startX, endX, startY, endY;
		for (int j = 0; j < aCoverage.length; j++) {
			if (aCoverage[j].getX1() < aCoverage[j].getX2()) {
				startX = aCoverage[j].getX1();
				endX = aCoverage[j].getX2();
			} else {
				startX = aCoverage[j].getX2();
				endX = aCoverage[j].getX1();
			}

			if (aCoverage[j].getY1() < aCoverage[j].getY2()) {
				startY = aCoverage[j].getY1();
				endY = aCoverage[j].getY2();
			} else {
				startY = aCoverage[j].getY2();
				endY = aCoverage[j].getY1();
			}

			if (genes[i].getX() >= startX && genes[i].getX() <= endX && genes[i].getY() >= startY
					&& genes[i].getY() <= endY) {
				present = true;
			} else {
				// Verificare se l'antenna si trova fuori ma copre la zona, in caso ritorna true
			}

		}
		*/

		return areas;
	}

	/**
	 * 
	 * @return valore di fitness dell'individuo
	 */
	public float getFitnessValue() {
		return fitnessValue;
	}

	/**
	 * 
	 * @param index indice dell'antenna
	 * @return antenna richiesta
	 */
	public Antenna getGene(int index) {
		return this.genes[index];
	}

	/**
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
	 * @param fitnessValue valore di fitness dell'individuo
	 */
	public void setFitnessValue(float fitnessValue) {
		this.fitnessValue = fitnessValue;
	}

	/**
	 * 
	 * @param gridWidth dimnensione della griglia
	 * @param accidents accidenti geografici
	 * 
	 *                  Stampa a schermo una rappresentazione parziale
	 *                  dell'individuo, senza considerare le zone da coprire
	 */
	public void print(int gridWidth, Accident[] accidents) {
		int[][] areaTotal = insertAntennasArea(gridWidth);
		areaTotal = insertAccidents(areaTotal, accidents);
		Utility.printArea(areaTotal);
	}

	/**
	 * 
	 * @param gridWidth dimensione della griglia
	 * @param accidents accidenti geografici
	 * @param aCoverage zone da coprire
	 * 
	 *                  Stampa a schermo una rappresentazione completa
	 *                  dell'individuo
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
	 * @return tutte le antenne che compongono l'individuo
	 */
	public Antenna[] getAntennas() {
		return this.genes;
	}

	@Override
	public String toString() {
		String ind = "Modelli antenne: \n";

		for (int i = 0; i < genes.length; i++)
			ind += "Antenna " + i + ", coverage: " + genes[i].getCoverageWidth() + ", cost: " + genes[i].getCost()
					+ "€, Position (x,y): " + genes[i].getX() + " , " + genes[i].getY() + " \n";

		ind += "Fitness: " + fitnessValue + "\n";

		return ind;

	}

	/**
	 * Compara due individui 0 -> se hanno la stessa fitness 1 -> se ind1.fitness <
	 * ind2.fitness -1 -> se ind1.fitness > ind2.fitness
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
	 * Clona un individuo, viene eseguita una clonazione profonda
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
