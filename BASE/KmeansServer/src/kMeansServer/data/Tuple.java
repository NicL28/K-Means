package kMeansServer.data;

import java.io.Serializable;
import java.util.Set;


 /**
 * La classe Tuple rappresenta una tupla della tabella, ovvero un insieme di
 * valori.
 *
 * @see Item
 *      La classe Tuple implementa l'interfaccia Serializable.
 * @see Serializable
 */

public class Tuple implements Serializable {
     /**
      * Array di Item che rappresenta la tupla.
      *
      * @see Item
      */
    private Item[] tuple;

    /**
     * Costruttore della classe Tuple
     *
     * @param size dimensione della tupla
     */
    Tuple(int size){
        this.tuple = new Item[size];
    }

     /**
      * Restituisce la dimensione della tupla
      *
      * @return dimensione della tupla
      */
    public int getLength(){
        return this.tuple.length;
    }

    /**
     * Restituisce l'item in posizione i.
     *
     * @param i indice dell'item da restituire
     * @return item in posizione i della tupla
     */
    public Item get(int i){
        return tuple[i];
    }

    /**
     * Aggiunge l'item c in posizione i
     *
     * @param c item da aggiungere
     * @param i indice in cui aggiungere l'item
     */
    void add(Item c, int i){
        tuple[i] = c;
    }
    
    /**
     * Calcola la distanza tra la tupla corrente e la tupla passata come parametro.
     *
     * @param obj tupla con cui calcolare la distanza
     * @return distanza tra le due tuple
     */
    public double getDistance(Tuple obj){
        double dist = 0;
        for(int i = 0; i < this.getLength(); i++){
            dist += this.get(i).distance(obj.get(i).getValue());
        }
        return dist;
    }

    /**
     * Calcola la distanza media tra la tupla corrente e l'insieme di tuple passato
     * come parametro.
     *
     * @param data tabella di dati
     * @param clusteredData indici delle tuple da considerare
     * @return distanza media
     */
    public double avgDistance(Data data, Set<Integer> clusteredData){
        double p, sumD = 0.0;
        for (Integer i : clusteredData) {
            double d = getDistance(data.getItemSet(i));
            sumD += d;   
        }
        p = sumD / clusteredData.size();
        return p;
    }
}
