package kMeansServer.data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import kMeansServer.database.DatabaseConnectionException;
import kMeansServer.database.DbAccess;
import kMeansServer.database.EmptySetException;
import kMeansServer.database.Example;
import kMeansServer.database.NoValueException;
import kMeansServer.database.QUERY_TYPE;
import kMeansServer.database.TableData;
import kMeansServer.database.TableSchema;

//TODO: verificare import

/**
 * Classe che rappresenta un insieme di transazioni o tuple di classe Example.
 *
 * @see Example
 */
// TODO: verificare codice e visibilità
public class Data {
	/**
	 * Lista di tuple di classe Example che rappresentano le transazioni
	 */
	private List<Example> data;
	/**
	 * Numero di transazione di data
	 */
	private int numberOfExamples;
	/**
	 * Lista di attributi di classe Attribute che rappresentano lo schema della
	 * tabella
	 */
	private List<Attribute> attributeSet;

	/**
	 * Costruttore della classe Data che inizializza la lista degli attributi e la
	 * lista delle transazioni del dataset.
	 * Il costruttore si connette al database, recupera lo schema della tabella
	 * specificata e carica gli attributi e le transazioni.
	 * Gestisce anche le eccezioni che possono verificarsi durante le operazioni di
	 * connessione al database e di caricamento dei dati.
	 *
	 *
	 * @throws DatabaseConnectionException se si verifica un errore di connessione al kMeansServer.database
	 * @throws SQLException se si verifica un errore di accesso al kMeansServer.database
	 * @throws NoValueException se si prende il valore da un attributo vuoto
	 * @throws EmptySetException se la tabella del kMeansServer.database è vuota
	 * @param tableName nome della tabella
	 */
	public Data(String tableName)
			throws DatabaseConnectionException, SQLException, NoValueException, EmptySetException {

		DbAccess db = new DbAccess();
		db.initConnection();
		TableData tb = new TableData(db);
		TableSchema ts = new TableSchema(db, tableName);

		// caricamento attributeset
		attributeSet = new LinkedList<Attribute>();
		for (int i = 0; i < ts.getNumberOfAttributes(); i++) {
			TableSchema.Column col = ts.getColumn(i);
			TreeSet<String> attribute = new TreeSet<String>();
			attribute = tb.getDistinctColumnValues(tableName, col).stream()
					.map(String::valueOf).collect(Collectors.toCollection(TreeSet::new));;
			if (!col.isNumber())
				attributeSet.add(new DiscreteAttribute(col.getColumnName(), i, attribute));
			else {

				Object min = tb.getAggregateColumnValue(tableName, col, QUERY_TYPE.MIN);
				Object max = tb.getAggregateColumnValue(tableName, col, QUERY_TYPE.MAX);
				attributeSet.add(new ContinuousAttribute(col.getColumnName(), i, (Double) min, (Double) max));
			}
		}

		// Caricamento data con le tuple della tabella
		List<Example> tempData = tb.getDistinctTransazioni(tableName);
		data = new ArrayList<>(tempData);
		numberOfExamples = data.size();

	}

	/**
	 * Restituisce il numero di transazioni del dataset
	 *
	 * @return numero di esempi del dataset
	 */
	public int getNumberOfExamples() {
		return numberOfExamples;
	}

	/**
	 * Restituisce il numero di attributi del dataset
	 * @return numero di attributi del dataset
	 */
	public int getNumberOfAttributes() {
		return attributeSet.size();
	}

	/**
	 * Restituisce il valore di un attributo di una tupla.
	 *
	 * @param exampleIndex   indice della tupla
	 * @param attributeIndex indice dell'attributo
	 * @return valore dell'attributo di una tupla
	 */
	public Object getAttributeValue(int exampleIndex, int attributeIndex) {
		return data.get(exampleIndex).get(attributeIndex);
	}

	/**
	 * Restituisce l'attributo di indice index (passato come parametro) dell'insieme di dati.
	 *
	 * @param index indice dell'attributo
	 * @return attributo di indice index
	 */
	private Attribute getAttribute(int index) {
		return attributeSet.get(index);
	}

	/**
	 * Restituisce una stringa che rappresenta il dataset
	 *
	 * @return stringa che rappresenta il dataset
	 */
	public String toString() {
		StringBuilder result = new StringBuilder();
		int i, j;
		for (i = 0; i < getNumberOfAttributes(); i++) {
			result.append(getAttribute(i));
			if (i < getNumberOfAttributes() - 1)
				result.append(",");
			else
				result.append("\n");
		}
		for (i = 0; i < getNumberOfExamples(); i++) {
			result.append((i + 1)).append(":");
			for (j = 0; j < getNumberOfAttributes(); j++) {
				result.append(getAttributeValue(i, j));
				if (j < getNumberOfAttributes() - 1)
					result.append(",");
			}
			if (i < getNumberOfExamples() - 1)
				result.append("\n");
		}
		return result.toString();
	}

	/**
	 * Crea un'istanza di Tuple che modelli la transazione
	 * con indice di riga index in data. Restituisce il riferimento a tale istanza.
	 * Utilizza l'RTTI per distinguere tra ContinuousAttribute e DiscreteAttribute
	 * (e quindi creare nella tupla un ContinuousItem o un DiscreteItem)
	 *
	 * @param index
	 * @return tupla che rappresenta la riga i-esima del dataset.
	 */
	public Tuple getItemSet(int index) {
		Tuple tuple = new Tuple(attributeSet.size());
		for (int i = 0; i < attributeSet.size(); i++)
			if (attributeSet.get(i).getClass().equals(DiscreteAttribute.class))
				tuple.add(new DiscreteItem((DiscreteAttribute) attributeSet.get(i), (String) data.get(index).get(i)),
						i);
			else
				tuple.add(
						new ContinuousItem(attributeSet.get(i), (Double) data.get(index).get(i)),
						i);
		return tuple;
	}

	/**
	 * Restituisce un array di indici di esempi campionati casualmente da data.
	 *
	 * @param k numero di esempi da campionare
	 * @return array di indici di esempi campionati casualmente da data
	 * @throws OutOfRangeSampleSize
	 */
	public int[] sampling(int k) throws OutOfRangeSampleSize {
		if (k > numberOfExamples || k <= 0)
			throw new OutOfRangeSampleSize("Il numero di centroidi da generare deve essere compreso tra 1 e "
					+ numberOfExamples + " (numero di tuple distinte)");

		int[] centroidIndexes = new int[k];
		Random rand = new Random();
		rand.setSeed(System.currentTimeMillis());
		for (int i = 0; i < k; i++) {
			boolean found;
			int c;
			do {
				found = false;
				c = rand.nextInt(getNumberOfExamples());
				for (int j = 0; j < i; j++)
					if (compare(centroidIndexes[j], c)) {
						found = true;
						break;
					}
			} while (found);
			centroidIndexes[i] = c;
		}
		return centroidIndexes;
	}

	/**
	 * Metodo che confronta due righe della tabella. Restituisce true se le due righe
	 * sono uguali, false altrimenti.
	 *
	 * @param i indice della prima riga
	 * @param j indice della seconda riga
	 * @return true se le due righe sono uguali, false altrimenti
	 */
	private boolean compare(int i, int j) {
		for (int y = 0; y < getNumberOfAttributes(); y++) {
			if (!(getAttributeValue(i, y).equals(getAttributeValue(j, y))))
				return false;
		}
		return true;
	}

	/**
	 * Usa l'RTTI per determinare se attribute riferisce un'istanza di
	 * ContinuousAttribute o di DiscreteAttribute.
	 *
	 * @param idList
	 * @param attribute
	 * @return chiamata a computePrototype(idList, (ContinuousAttribute) attribute)
	 *         se attribute è un'istanza di ContinuousAttribute, altrimenti chiamata
	 *         a computePrototype(idList, (DiscreteAttribute) attribute).
	 */
	Object computePrototype(Set<Integer> idList, Attribute attribute) {
		if (attribute.getClass().equals(ContinuousAttribute.class))
			return computePrototype(idList, (ContinuousAttribute) attribute);
		else
			return computePrototype(idList, (DiscreteAttribute) attribute);
	}

	/**
	 * Determina il valore prototipo come valore più frequentetra i valori osservati
	 * per attribute nelle transazioni di data aventi indice di riga in idList.
	 *
	 * @param idList
	 * @param attribute
	 * @return valore più frequente tra i valori osservati per attribute nelle
	 *         transazioni di data aventi indice di riga in idList.
	 */
	private String computePrototype(Set<Integer> idList, DiscreteAttribute attribute) {
		int maxFrequency = 0;
		String maxFrequencyValue = attribute.iterator().next();

		for (String a : attribute) {
			int frequency = attribute.frequency(this, idList, a);
			if (frequency > maxFrequency) {
				maxFrequency = frequency;
				maxFrequencyValue = a;
			}
		}

		return maxFrequencyValue;
	}

	/**
	 * Determina il valore prototipo come media dei valori
	 * osservati per attribute nelle transazioni di data aventi indice di riga
	 * in idList.
	 *
	 * @param idList
	 * @param attribute
	 * @return media dei valori osservati per attribute nelle transazioni di data
	 *         aventi indice di riga in idList.
	 */
	private Double computePrototype(Set<Integer> idList, ContinuousAttribute attribute) {
		double sum = 0;
		for (Integer i : idList)
			sum += (Double) getAttributeValue(i, attribute.getIndex());
		return sum / idList.size();
	}

}
