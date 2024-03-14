package mining;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import data.*;

public class KmeansMiner implements Serializable{
    private ClusterSet C;

    
    public KmeansMiner(int k) throws OutOfRangeSampleSize{
        if (k<1) 
        throw new OutOfRangeSampleSize("Il numero di cluster è fuori range");

        C = new ClusterSet(k);
    }

    public ClusterSet getC() {
        return C;
    }
 
    public KmeansMiner(String fileName) throws FileNotFoundException,IOException,ClassNotFoundException{
      //  InputStream filePath = getClass().getClassLoader().getResourceAsStream(fileName);
        ObjectInputStream in= new ObjectInputStream(new FileInputStream(fileName));
        KmeansMiner deserializedObject = (KmeansMiner) in.readObject();
        this.C = deserializedObject.C;
        in.close();
    }
 
    public int kmeans(Data data) throws OutOfRangeSampleSize {
        int numberOfIterations = 0;
        // STEP1
        C.initializeCentroids(data);
        boolean changedCluster = false;
        do {
            numberOfIterations++;
            // STEP2
            changedCluster = false;
            for (int i = 0; i < data.getNumberOfExamples(); i++) {
                Cluster nearestCluster = C.nearestCluster(
                        data.getItemSet(i));
                Cluster oldCluster = C.currentCluster(i);
                boolean currentChange = nearestCluster.addData(i);
                if (currentChange)
                    changedCluster = true;
                // rimuovono la tupla dal vecchio cluster
                if (currentChange && oldCluster != null)
                    // il nodo va rimosso dal suo vecchio cluster
                    oldCluster.removeTuple(i);

            }
            // STEP 3
            C.updateCentroids(data);
        } while (changedCluster);
        return numberOfIterations;
    }

    public void salva(String fileName) throws FileNotFoundException,IOException,ClassNotFoundException{
        ObjectOutputStream out= new ObjectOutputStream(new FileOutputStream(fileName) );
        out.writeObject(this);
        out.close();
    }
}
