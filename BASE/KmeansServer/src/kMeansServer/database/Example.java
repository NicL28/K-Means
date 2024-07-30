package kMeansServer.database;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che rappresenta una tupla di esempio.
 * La classe implementa l'interfaccia Comparable in modo da poter confrontare
 * due tuple.
 *
 * @see Comparable
 */
public class Example implements Comparable<Example> {
    /**
     * Lista di oggetti che rappresentano i valori della tupla.
     */
    private List<Object> example = new ArrayList<>();

    /**
     * Aggiunge un oggetto alla tupla.
     *
     * @param o Oggetto da aggiungere alla tupla.
     */
    void add(Object o) {
        example.add(o);
    }

    /**
     * Restituisce l'oggetto in posizione i.
     *
     * @param i Posizione dell'oggetto da restituire.
     * @return Oggetto in posizione i.
     */
    public Object get(int i) {
        return example.get(i);
    }

    /**
     * Confronta la tupla corrente con un'altra tupla passata come parametro.
     *
     * @param ex Tupla con cui confrontare la tupla corrente.
     * @return 0 se le tuple sono uguali, un valore negativo se la tupla corrente è
     *         minore della tupla passata come parametro, un valore positivo
     *         altrimenti.
     */
    public int compareTo(Example ex) {
        int i = 0;
        for (Object o : ex.example) {
            if (!o.equals(this.example.get(i)))
                return ((Comparable) o).compareTo(example.get(i));
            i++;
        }
        return 0;
    }

    /**
     * Restituisce una rappresentazione testuale della tupla.
     *
     * @return Rappresentazione testuale della tupla.
     */
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Object o : example)
            str.append(o.toString()).append(" ");
        return str.toString();
    }

}