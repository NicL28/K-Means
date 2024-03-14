package mining;

import java.util.Set;

import data.*;

import java.io.Serializable;
import java.util.HashSet;

public class Cluster implements Serializable{

	private Tuple centroid;
	private Set<Integer> clusteredData;

	public Cluster(Tuple centroid) {
		this.centroid = centroid;
		clusteredData = new HashSet<Integer>();

	}

	Tuple getCentroid() {
		return centroid;
	}

	void computeCentroid(Data data) {
		for (int i = 0; i < centroid.getLength(); i++) {
			centroid.get(i).update(data, clusteredData);

		}

	}

	// restituisce "vero" se la tupla sta cambiando cluster
	boolean addData(int id) {
		return clusteredData.add(id);

	}

	// verifica se una transazione è clusterizzata nell'array corrente
	boolean contain(int id) {
		return clusteredData.contains(id);
	}

	// rimuove la tupla che ha cambiato cluster
	void removeTuple(int id) {
		clusteredData.remove(id);

	}

	public String toString() {
		String str = "Centroid=(";
		for (int i = 0; i < centroid.getLength(); i++)
			str += centroid.get(i);
		str += ")";
		return str;

	}

	// VERIFCARE SE utilizzare "int array[]=new int[clusteredData.size()];" è
	// coerente con l'implementazione di clusteredData
	public String toString(Data data) {
		String str = "Centroid=(";
		for (int i = 0; i < centroid.getLength(); i++)
			str += centroid.get(i) + " ";
		str += ")\nExamples:\n";
		int array[] = new int[clusteredData.size()];
		for (int i = 0; i < array.length; i++) {
			str += "[";
			for (int j = 0; j < data.getNumberOfExplanatoryAttributes(); j++)
				str += data.getAttributeValue(array[i], j) + " ";
			str += "] dist=" + getCentroid().getDistance(data.getItemSet(array[i])) + "\n";

		}
		str += "AvgDistance=" + getCentroid().avgDistance(data, clusteredData);
		str += "\n";
		return str;

	}

}
