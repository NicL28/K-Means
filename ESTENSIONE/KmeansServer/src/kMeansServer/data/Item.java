package kMeansServer.data;

import java.io.Serializable;
import java.util.Set;

/**
 * La classe astratta Item rappresenta una coppia attributo-valore.
 */
public abstract class Item implements Serializable {
    /**
     * Attributo dell'item.
     */
    private Attribute attribute;
    /**
     * Valore dell'item.
     */
    private Object value;

    /**
     * Costruttore della classe Item.
     *
     * @param attribute attributo dell'item
     * @param value valore dell'item
     */
    Item(Attribute attribute, Object value){
        this.attribute = attribute;
        this.value = value;
    }

    /**
     * Restituisce il valore dell'item
     *
     * @return valore
     */
    Attribute getAttribute(){
        return attribute;
    }

    /**
     * Restituisce il valore dell'item
     *
     * @return valore
     */
    Object getValue(){
        return value;
    }

    /**
     * Restituisce una rappresentazione testuale dell'item
     *
     * @return rappresentazione testuale
     */
    public String toString(){
        return attribute.getName() + ": " + value;
    }

    /**
     * Calcola la distanza tra il valore dell'item corrente e il valore dell'item
     * passato come parametro.
     *
     * @param a oggetto con cui calcolare la distanza
     * @return distanza tra i valori
     */
    abstract double distance(Object a);

    /**
     * Aggiorna il valore dell'item in base ai dati e all'insieme di dati
     * clusterizzati.
     *
     * @param data tabella dei dati
     * @param clusteredData insieme di righe di un cluster
     */
    public void update(Data data, Set<Integer> clusteredData){
        value = data.computePrototype(clusteredData, attribute);
    }
}
