package data;

import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Set;

public class DiscreteAttribute extends Attribute implements Iterable<String> {
    private TreeSet<String> values;

    DiscreteAttribute(String name, int index, TreeSet<String> values){
        super(name, index);
        this.values = values;
    }

    public int getNumberOfDistinctValues() {
        return values.size();
    }

   //La prof ha scritto nel pdf Iterator<T>
   public  Iterator<String> iterator() {
        return values.iterator();
    }
     
    /* 
    public String getValue(int i){
        return values[i];
    }
    */    

    /*
     * INPUT: data, idList, v
     * OUTPUT: count
     * COMPORTAMENTO: restituisce il numero di volte che l'attributo discreto assume
     * il valore v
     */
    public int frequency(Data data, Set<Integer> idList, String v) {
        int count = 0;
        for (Integer i : idList) {
            Object value = data.getAttributeValue(i, getIndex());
            if (value instanceof String && ((String) value).equals(v)) {
                count++;
            }
        }
        return count;
    }

    /*
     * public int frequency(Data data, ArraySet idList, String v){
     * int count = 0;
     * for(int i = 0; i < idList.size(); i++){
     * Object value = data.getAttributeValue(idList.get(i), getIndex());
     * if(value instanceof String && ((String) value).equals(v)){
     * count++;
     * }
     * }
     * return count;
     * }
     */
}
