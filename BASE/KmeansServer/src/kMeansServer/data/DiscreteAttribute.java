package kMeansServer.data;

import java.util.TreeSet;
import java.util.Iterator;
import java.util.Set;

/**
 * La classe DiscreteAttribute rappresenta un attributo di tipo discreto.
 * Estende la classe astratta Attribute e implementa l'interfaccia Iterable.
 *
 * @see Attribute
 */
public class DiscreteAttribute extends Attribute implements Iterable<String>{
    /**
     * Insieme di valori distinti che l'attributo discreto puo' assumere
     */
    private TreeSet<String> values;

    /**
     * Costruttore della classe DiscreteAttribute che inizializza il nome e l'indice
     * dell'attributo discreto.
     *
     * @param name   nome dell'attributo
     * @param index  indice dell'attributo
     * @param values insieme di valori distinti che l'attributo discreto puo'
     *               assumere
     */
    DiscreteAttribute(String name, int index, TreeSet<String> values) {
        super(name, index);
        this.values = values;
    }

    /**
     *  Restituisce il numero di volte che il valore v e' presente nell'attributo discreto.
     *
     *  @param data tabella di dati
     * @param idList insieme di righe da considerare per il conteggio delle occorrenze del valore v
     * @param v valore di cui contare le occorrenze
     * @return numero di occorrenze di v
     */
    int frequency(Data data, Set<Integer> idList, String v){
        int count = 0;
        for(int i: idList){
            if(data.getAttributeValue(i, getIndex()).equals(v))
                count++;
        }
        return count;
    }

    /**
     * Restituisce l'iteratore sull'insieme di valori distinti che l'attributo
     * discreto puo' assumere.
     *
     * @return iteratore sull'insieme di valori distinti
     */
    public Iterator<String> iterator() {
        return values.iterator();
    }
}