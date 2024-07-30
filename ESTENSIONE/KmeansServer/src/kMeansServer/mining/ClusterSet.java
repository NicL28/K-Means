package kMeansServer.mining;
import java.io.Serializable;
import kMeansServer.data.Data;
import kMeansServer.data.OutOfRangeSampleSize;
import kMeansServer.data.Tuple;

/**
 * Classe che rappresenta un insieme di cluster.
 *
 * @see Cluster
 */
public class ClusterSet implements Serializable{
    /**
     * Array di cluster
     */
    private Cluster[] C;
    /**
     * Indice del cluster
     */
    private int i = 0;

    /**
     * Costruttore di ClusterSet
     *
     * @param k numero di cluster
     */
    ClusterSet(int k){
        this.C = new Cluster[k];
    }

    /**
     * Aggiunge un cluster al ClusterSet
     *
     * @param c cluster da aggiungere
     */
    void add(Cluster c){
        this.C[i] = c;
        i++;
    }

    /**
     * Inizializza i centroidi.
     * Aggiunge al ClusterSet un cluster per ogni centroide individuato.
     *
     * @param data tabella contenente i dati
     * @throws OutOfRangeSampleSize se il numero di centroidi è maggiore del numero di tuple
     */
    void initializeCentroids(Data data) throws OutOfRangeSampleSize{
        int[] centroidIndexes = data.sampling(C.length);
        for (int centroidIndex : centroidIndexes) {
            Tuple centroidI = data.getItemSet(centroidIndex);
            add(new Cluster(centroidI));
        }
    }

    /**
     * Trova il cluster più vicino a una tupla kMeansServer.data confrontando le distanze tra la tupla e tutti i cluster
     *
     * @param tuple tupla di cui si vuole trovare il cluster più vicino
     * @return cluster più vicino alla tupla
     */
    Cluster nearestCluster(Tuple tuple){
        double minDistance = tuple.getDistance(C[0].getCentroid());
        Cluster nearestCluster = C[0];
        for(int i = 1; i < C.length; i++){
            double distance = tuple.getDistance(C[i].getCentroid());
            if(distance < minDistance){
                minDistance = distance;
                nearestCluster = C[i];
            }
        }
        return nearestCluster;
    }

    /**
     * Trova il cluster a cui appartiene una tupla.
     *
     * @param id indice della tupla
     * @return cluster a cui appartiene la tupla
     */
    Cluster currentCluster(int id){
        for (Cluster cluster : C) {
            if (cluster.contain(id)) {
                return cluster;
            }
        }
        return null;
    }

    /**
     * Aggiorna il valore delle tuple di tutti i centroidi con quello più frequente
     *
     * @param data tabella contenente i dati
     */
    void updateCentroids(Data data){
        for (Cluster cluster : C) {
            cluster.computeCentroid(data);
        }
    }

    /**
     * Restituisce una stringa rappresentante il cluserSet.
     *
     * La stringacontiene, per ogni cluster, solo il centroide.
     * @return stringa rappresentante il clusterSet
     */
    public String toString(){
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < C.length; i++){
            str.append("Cluster ").append(i).append(": ").append(C[i].toString()).append("\n");
        }
        return str.toString();
    }

    /**
     * Restituisce una stringa rappresentante il cluserSet.
     * La stringa contiene, per ogni cluster, anche le tuple che lo compongono.
     *
     * @param data tabella contenente i dati
     * @return stringa rappresentante il clusterSet
     */
    public String toString(Data data){
        StringBuilder str= new StringBuilder();
        for(int i=0;i<C.length;i++){
            if (C[i]!=null){
                str.append(i).append(":").append(C[i].toString(data)).append("\n");
            }
        }
        return str.toString();
    }
}