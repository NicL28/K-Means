package data;

import java.util.Set;

public class Tuple {
    private Item[] tuple;

    public Tuple(int size) {
        tuple = new Item[size];
    }

    public int getLength() {
        return tuple.length;
    }

    public Item get(int i) {
        return tuple[i];
    }

    void add(Item c, int i) {
        tuple[i] = c;
    }

    public double getDistance(Tuple obj) {
        double distance = 0;
        for (int i = 0; i < tuple.length; i++) {
            distance += tuple[i].distance(obj.get(i));
        }
        return distance;
    }

    /*
     * public double avgDistance(Data data, Set<Integer> clusteredData) {
     * double p = 0.0, sumD = 0.0;
     * for (int i : clusteredData) {
     * double d = getDistance(data.getItemSet(i));
     * sumD += d;
     * }
     * p = sumD / clusteredData.size();
     * return p;
     * }
     */

    public double avgDistance(Data data, Set<Integer> clusteredData) {
        double p = 0.0, sumD = 0.0;
        for (int i : clusteredData) {
            double d = getDistance(data.getItemSet(i));
            sumD += d;
        }
        p = sumD / clusteredData.size();
        return p;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < tuple.length; i++) {
            sb.append(tuple[i]);
            if (i < tuple.length - 1) {
                sb.append(" ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

}
