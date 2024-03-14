package mining;

import java.io.Serializable;

import data.*;

public class ClusterSet  implements Serializable{
    private Cluster C[];
    private int i = 0;

    public ClusterSet(int k) {
        C = new Cluster[k];
    }

    void add(Cluster c) {
        C[i] = c;
        i++;
    }

    Cluster get(int i) {
        return C[i];
    }

    void initializeCentroids(Data data) throws OutOfRangeSampleSize {
        int centroidIndexes[] = data.sampling(C.length);
        for (int i = 0; i < centroidIndexes.length; i++) {
            Tuple centroidI = data.getItemSet(centroidIndexes[i]);
            add(new Cluster(centroidI));
        }
    }

    Cluster nearestCluster(Tuple tuple) {
        Cluster nearestCluster = C[0];
        double minDistance = tuple.getDistance(nearestCluster.getCentroid());
        for (int i = 1; i < C.length; i++) {
            double distance = tuple.getDistance(C[i].getCentroid());
            if (distance < minDistance) {
                minDistance = distance;
                nearestCluster = C[i];
            }
        }
        return nearestCluster;
    }

    Cluster currentCluster(int id) {
        for (int i = 0; i < C.length; i++) {
            if (C[i].contain(id))
                return C[i];
        }
        return null;
    }

    void updateCentroids(Data data) {
        for (int i = 0; i < C.length; i++) {
            C[i].computeCentroid(data);
        }
    }

    // DA TESTARE
    public String toString() {
        String str = "";
        for (int i = 0; i < C.length; i++) {
            str += i + ": " + C[i].getCentroid() + "\n";
        }
        return str;
    }

    public String toString(Data data) {
        String str = "";
        for (int i = 0; i < C.length; i++) {
            if (C[i] != null) {
                str += i + ": " + C[i].toString(data) + "\n";
            }
        }
        return str;
    }

}
