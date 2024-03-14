package data;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.Iterator;
import database.*;
public class Data {

	private List<Example> data = new ArrayList<Example>(); // ex Object[][] data;
	private List<Attribute> attributeSet = new LinkedList<Attribute>(); // ex Attribute[] attributeSet;
	private int numberOfExamples;
	//aggiunto per un errore ma non specificato dalla prof
	private TableSchema ts;

	public Data(String tableName, String DBname) throws SQLException,DatabaseConnectionException,EmptySetException
	{
		DbAccess db= new DbAccess(DBname);
		db.initConnection();
		TableData table = new TableData(db);
		TableSchema dbTable = new TableSchema(db,tableName);
		data=table.getDistinctTransazioni(tableName);

		numberOfExamples=data.size();

		attributeSet = new LinkedList<Attribute>();

		for (int i=0; i<dbTable.getNumberOfAttributes(); i++)
		{
			TableSchema.Column col = dbTable.getColumn(i);
			TreeSet attribute =new TreeSet<>();
			attribute = (TreeSet) table.getDistinctColumnValues(tableName,col);
			if (!col.isNumber())
				attributeSet.add(new DiscreteAttribute(col.getColumnName(),i, attribute));
			else
			{
				try
				{
					Object min = table.getAggregateColumnValue(tableName,col,QUERY_TYPE.MIN);
					Object max = table.getAggregateColumnValue(tableName,col,QUERY_TYPE.MAX);
					attributeSet.add(new ContinuousAttribute(col.getColumnName(),i,(Double)min,(Double)max));
				}
				catch (NoValueException noValue)
				{
					System.out.println(noValue.getMessage());
				}
			}



		}
		db.closeConnection();
	}

	// restituisce il numero di tuple del dataset
	public int getNumberOfExamples() {
		return numberOfExamples;
	}

	// restituisce il numero di attributi del dataset
	public int getNumberOfExplanatoryAttributes() {
		return attributeSet.size();
	}
	// restituisce l'attributeSet di Data
	public List<Attribute> getAttributeSchema() {
		return attributeSet;
	}

	// restituisce un valore del dataset in posizione [exampleIndex, attributeIndex]
	public Object getAttributeValue(int exampleIndex, int attributeIndex) {
		Example example = data.get(exampleIndex);
		return example.get(attributeIndex);
	}


	/**
	 * NOME: getItemSet
	 * COMPORTAMENTO: crea un'istanza di Tuple che modelli la transazione
	 * con indice di riga index in data. Restituisce il riferimento a tale istanza.
	 * Utilizza l'RTTI per distinguere tra ContinuousAttribute e DiscreteAttribute
	 * (e quindi creare nella tupla un ContinuousItem o un DiscreteItem)
	 * 
	 * @param index
	 * @return tupla che rappresenta la riga i-esima del dataset.
	 */
	public Tuple getItemSet(int index) {
		Tuple tuple = new Tuple(attributeSet.size());
		for (int i = 0; i < attributeSet.size(); i++) {
			Attribute attribute = attributeSet.get(i);
			if (attribute instanceof ContinuousAttribute) {
				tuple.add(new ContinuousItem(attribute, (Double) getAttributeValue(index, i)), i);
			} else {
				tuple.add(new DiscreteItem((DiscreteAttribute) attribute, (String) getAttributeValue(index, i)), i);
			}
		}
		return tuple;
	}

	public int[] sampling(int k) throws OutOfRangeSampleSize {
		if (k <= 0 || k > getNumberOfExamples()) {
			// Numero k fuori range, solleva l'eccezione
			throw new OutOfRangeSampleSize("Il numero di cluster è fuori range");
		}

		int[] centroidIndexes = new int[k];
		// sceglie k centroidi casuali in data
		Random rand = new Random();
		rand.setSeed(System.currentTimeMillis());
		for (int i = 0; i < k; i++) {
			boolean found = false;
			int c;
			do {
				found = false;
				c = rand.nextInt(getNumberOfExamples());
				// verifica che il centroide[c] non sia uguale al cenroide già immagazzinato in
				// CentroidIndexes
				for (int j = 0; j < i; j++) {
					if (data.get(centroidIndexes[j]).compareTo(data.get(c)) == 0) {
						found = true;
						break;
					}
				}

			} while (found);
			centroidIndexes[i] = c;
		}
		return centroidIndexes;
	}

	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("Outlook Temperature Humidity Wind PlayTennis\n");
		for (int i = 0; i < numberOfExamples; i++) {
			s.append((i + 1)).append(": ");
			Example example = data.get(i);
			for (int j=0; j< ts.getNumberOfAttributes(); j++) {
				Object o = example.get(j);
				s.append(o).append(" ");
			}
			s.append("\n");
		}
		return s.toString();
	}

	/*
	 * private boolean compare(int i, int j) {
	 * for(int k=0; k<attributeSet.length; k++){
	 * if(!data[i][k].equals(data[j][k])){
	 * return false; //le due righe confrontate non sono uguali
	 * }
	 * }
	 * return true; //le due righe confrontate sono uguali
	 * }
	 */

	/*
	 * 
	 * PERCHE' ERA PRESENTE IN PASSATO QUESTO METODO?
	 * 
	 * Object computePrototype(Set<Integer> idList, Attribute attribute) {
	 * if (attribute instanceof ContinuousAttribute) {
	 * return computePrototype(idList, (ContinuousAttribute) attribute);
	 * } else {
	 * return computePrototype(idList, (DiscreteAttribute) attribute);
	 * }
	 * }
	 */

	/**
	 * NOME: computePrototype
	 * COMPORTAMENTO: usa l'RTTI per determinare se attribute riferisce un'istanza
	 * di ContinuousAttribute o di DiscreteAttribute.
	 * Nel primo caso invoca computePrototype(idList, (ContinuousAttribute)
	 * attribute),
	 * altrimenti computePrototype(idList, (DiscreteAttribute) attribute).
	 * 
	 * @param idList
	 * @param attribute
	 * @return chiamata a computePrototype(idList, (ContinuousAttribute) attribute)
	 *         se attribute è un'istanza di ContinuousAttribute, altrimenti chiamata
	 *         a computePrototype(idList, (DiscreteAttribute) attribute).
	 */
	Object computePrototype(Set<Integer> idList, Attribute attribute) {
		if (attribute instanceof ContinuousAttribute) {
			return computePrototype(idList, (ContinuousAttribute) attribute);
		} else {
			return computePrototype(idList, (DiscreteAttribute) attribute);
		}
	}

	/**
	 * NOME: computePrototype
	 * COMPORTAMENTO: Determina il valore prototipo come valore più frequente
	 * tra i valori osservati per attribute nelle transazioni di data aventi
	 * indice di riga in idList.
	 * 
	 * @param idList
	 * @param attribute
	 * @return valore più frequente tra i valori osservati per attribute nelle
	 *         transazioni di data aventi indice di riga in idList.
	 */
	String computePrototype(Set<Integer> idList, DiscreteAttribute attribute) {
		// conta il numero di occorrenze di ogni valore dell'attributo
		// occorrenze: variabile intera che memorizza il numero di occorrenza del valore
		// preso in esame dell'attributo
		int occorrenze = 0;
		// max: variabile intera che memorizza il numero di occorrenze massimo tra i
		// valori dell'attributo
		int max = 0;
		// IndiceCentroide: variabile intera che memorizza l'indice
		int indiceCentroide = 0;
		// for per i valori del dominio dell'attributo
		Iterator<String> iterator = attribute.iterator();
		for (int i = 0; i < attribute.getNumberOfDistinctValues(); i++) {
			String value = iterator.next();
			occorrenze = attribute.frequency(this, idList, value);
			// trova il valore con il maggior numero di occorrenze
			if (occorrenze > max) {
				max = occorrenze;
				indiceCentroide = i;
			}
		}
		iterator = attribute.iterator();
		for (int i = 0; i < indiceCentroide; i++) {
			iterator.next();
		}
		return iterator.next();
	}

	/**
	 * NOME: computePrototype
	 * COMPORTAMENTO: Determina il valore prototipo come media dei valori
	 * osservati per attribute nelle transazioni di data aventi indice di riga
	 * in idList.
	 * 
	 * @param idList
	 * @param attribute
	 * @return media dei valori osservati per attribute nelle transazioni di data
	 *         aventi indice di riga in idList.
	 */
	Double computePrototype(Set<Integer> idList, ContinuousAttribute attribute) {
		double somma = 0.0;
		double media = 0.0;
		for (Integer i : idList) {
			somma += (Double) getAttributeValue(i, attribute.getIndex());
		}
		media = somma / idList.size();
		return media;
	}

}
