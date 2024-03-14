package data;

import java.util.Set;

public abstract class Item {
    private Attribute attribute;
    private Object value;

    public Item(Attribute attribute, Object value) {
        this.attribute = attribute;
        this.value = value;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public Object getValue() {
        return value;
    }

    // COMPORTAMENTO:Sovrascrive metodo ereditato dalla superclasse e restuisce la
    // stringa rappresentante lo stato dell'oggetto
    public String toString() {
        return String.valueOf(value);
    }

    abstract double distance(Object a);

    /*
     * //modifica il membro value dell'oggetto su cui viene invocato il metodo
     * public void update(Data data, ArraySet clusteredData) {
     * this.value = data.computePrototype(clusteredData, attribute);
     * }
     */
    public void update(Data data, Set<Integer> clusteredData) {
        this.value = data.computePrototype(clusteredData, attribute);
    }

}