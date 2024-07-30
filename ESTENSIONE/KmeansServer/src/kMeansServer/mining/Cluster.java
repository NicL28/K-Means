package kMeansServer.mining;

import java.util.HashSet;
import java.util.Set;
import kMeansServer.data.Data;
import kMeansServer.data.Tuple;
import java.io.Serializable;

/**
 * Classe che rappresenta un cluster.
 * Un cluster è costituito da un centroide e da un insieme di transazioni.
 * Il centroide è rappresentato da una tupla, le righe del dataset sono
 * rappresentate da un insieme di indici.
 */
public class Cluster implements Serializable {
	/**
	 * Variabile che rappresenta il centroide del cluster.
	 */
	private Tuple centroid;
	/**
	 * Variabile che rappresenta l'insieme di transazioni appartenenti al cluster.
	 */
	private Set<Integer> clusteredData;

	/**
	 * Costruttore della classe Cluster.
	 *
	 * @param centroid Tupla che rappresenta il centroide del cluster.
	 */
	Cluster(Tuple centroid) {
		this.centroid = centroid;
		clusteredData = new HashSet<>();
	}
	/**
	 * Restituisce il centroide del cluster.
	 *
	 * @return centroide del cluster.
	 */
	Tuple getCentroid() {
		return centroid;
	}
	/**
	 * Aggiorna il centroide del cluster che appare più frequentemente.
	 *
	 * @param data tabella contenente i dati.
	 */
	void computeCentroid(Data data) {
		for (int i = 0; i < centroid.getLength(); i++) {
			centroid.get(i).update(data, clusteredData);
		}
	}

	/**
	 * Aggiunge una riga al cluster
	 *
	 * @param id indice della riga da aggiungere
	 * @return true se l'aggiunta è andata a buon fine, false altrimenti.	 */
	boolean addData(int id) {
		return clusteredData.add(id);
	}

	/**
	 * Verifica se il cluster contiene una riga.
	 *
	 * @param id indice della riga da verificare
	 * @return true se la riga appartiene al cluster, false altrimenti
	 */
	boolean contain(int id) {
		return clusteredData.contains(id);
	}

	/**
	 * Rimuove una riga dal cluster.
	 *
	 * @param id indice della riga da rimuovere
	 */
	void removeTuple(int id) {
		clusteredData.remove(id);
	}

	/**
	 * Rappresentazione testuale del cluster.
	 *
	 * @return stringa che rappresenta il cluster
	 */
	public String toString() {
		StringBuilder str = new StringBuilder("Centroid=(");
		for (int i = 0; i < centroid.getLength(); i++) {
			str.append(centroid.get(i));
			if (i != centroid.getLength() - 1)
				str.append(", ");
		}
		str.append(")");
		return str.toString();
	}

	/**
	 * Metodo che restituisce una stringa che rappresenta il cluster.
	 * La stringa è formata dall'elenco delle righe contenute nel cluster.
	 *
	 * @param data tabella contenente i dati
	 * @return stringa che rappresenta il cluster
	 */
	public String toString(Data data) {
		StringBuilder str = new StringBuilder("Centroid=(");
		for (int i = 0; i < centroid.getLength(); i++) {
			str.append(centroid.get(i));
			if(i != centroid.getLength() - 1)
				str.append(", ");
		}
		str.append(")\nExamples:\n");

		for (Integer i : clusteredData) {
			str.append("[");
			for (int j = 0; j < data.getNumberOfAttributes(); j++)
				str.append(data.getAttributeValue(i, j)).append(" ");
			str.append("] dist=").append(getCentroid().getDistance(data.getItemSet(i))).append("\n");
		}
		str.append("\nAvgDistance=").append(getCentroid().avgDistance(data, clusteredData)).append("\n");
		return str.toString();
	}
}