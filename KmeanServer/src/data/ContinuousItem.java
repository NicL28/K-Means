package data;

public class ContinuousItem extends Item {
    public ContinuousItem(Attribute attribute, Double value) {
        super(attribute, value);
    }

    /**
     * NOME: distance
     * COMPORTAMENTO: determina la distanza (in valore assoluto)
     * tra il valore scalato memorizzato nell'item corrente (this.getValue())
     * e quello scalato associato al parametro a.
     * Per ottenere valori scalati fare uso di getScaleValue().
     * @param a
     * @return valore assoluto della distanza tra this e a
     */
    double distance(Object a) {
        return Math.abs((Double)(this.getValue())-((Double)((Item)a).getValue()));
    }
}
