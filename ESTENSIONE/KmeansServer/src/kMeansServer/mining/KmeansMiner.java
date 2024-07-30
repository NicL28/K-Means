package kMeansServer.mining;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import kMeansServer.data.Data;
import kMeansServer.data.OutOfRangeSampleSize;


/**
 * Classe che rappresenta un algoritmo di clustering.
 *
 * @see ClusterSet
 */
public class KmeansMiner {
    /**
     * Cluster set su cui eseguire l'algoritmo
     */
    private ClusterSet C;

    /**
     * Costruttore di KmeansMiner che inizializza un oggetto ClusterSet con k cluster.
     *
     * @param k numero di cluster
     * @throws OutOfRangeSampleSize se il numero di cluster è minore di 1
     */
    public KmeansMiner(int k) throws OutOfRangeSampleSize {
        if (k < 1)
            throw new OutOfRangeSampleSize("Il numero di cluster deve essere maggiore di 0");
        this.C = new ClusterSet(k);
    }

    /**
     * Costruttore di KmeansMiner che carica un oggetto ClusterSet da file.
     *
     * @param fileName nome del file da cui caricare l'oggetto ClusterSet
     * @throws FileNotFoundException se il file non esiste
     * @throws IOException se si verifica un errore di I/O
     * @throws ClassNotFoundException se la classe non è stata trovata nella lettura da stream
     */
    public KmeansMiner(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream inFile = new FileInputStream(fileName);
        ObjectInputStream inStream = new ObjectInputStream(inFile);
        C = (ClusterSet) inStream.readObject();
        inStream.close();
    }

    /**
     * Salva l'oggetto ClusterSet su file.
     *
     * @param fileName nome del file su cui salvare l'oggetto ClusterSet
     * @throws IOException se si verifica un errore di I/O
     */
    public void salva(String fileName) throws IOException {
        String path = ".\\salvataggi\\";
        if (!new File(path).exists())
            new File(path).mkdirs();
        String filepath = path + fileName;
        File myfile = new File(filepath);
        if (!myfile.exists())
            myfile.createNewFile();
        FileOutputStream outFile = new FileOutputStream(filepath, false);
        ObjectOutputStream outStream = new ObjectOutputStream(outFile);
        outStream.writeObject(C);
        outStream.close();
    }

    /**
     * Restituisce il cluster set.
     *
     * @return cluster set
     */
    public ClusterSet getC() {
        return C;
    }

    /**
     * Esegue l'algoritmo di k-means.
     *
     * @param data dataset su cui eseguire l'algoritmo
     * @return numero di iterazioni eseguite
     * @throws OutOfRangeSampleSize se il numero di cluster è maggiore del numero di tuple
     */
    public int kmeans(Data data) throws OutOfRangeSampleSize {
        int numberOfIterations = 0;
        // STEP 1
        C.initializeCentroids(data);
        boolean changedCluster;
        do {
            numberOfIterations++;
            // STEP 2
            changedCluster = false;
            for (int i = 0; i < data.getNumberOfExamples(); i++) {
                Cluster nearestCluster = C.nearestCluster(data.getItemSet(i));
                Cluster oldCluster = C.currentCluster(i);
                boolean currentChange = nearestCluster.addData(i);
                if (currentChange)
                    changedCluster = true;
                // rimuovo la tupla dal vecchio cluster
                if (currentChange && oldCluster != null)
                    // il nodo va rimosso dal suo vecchio cluster
                    oldCluster.removeTuple(i);
            }
            // STEP 3
            C.updateCentroids(data);
        } while (changedCluster);
        return numberOfIterations;
    }
}
