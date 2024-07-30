package kMeansServer.data;
/**
 * Classe che rappresenta un Item continuo.
 * Estende la classe Item.
 *
 * @see Item
 *
 */
public class ContinuousItem extends Item {

    /**
     * Costruttore della classe ContinuousItem
     *
     * @param attribute attributo dell'item
     * @param value     valore dell'item
     */
    // TODO: verificare se è necessario il public
    ContinuousItem(Attribute attribute, Double value) {
        super(attribute, value);
    }

    /**
     * Determina la distanza (in valore assoluto)
     * tra il valore scalato memorizzato nell'item corrente (this.getValue())
     * e quello scalato associato al parametro a.
     * Per ottenere valori scalati fare uso di getScaleValue().
     *
     * @param a
     * @return valore assoluto della distanza tra this e a
     */

    double distance(Object a) {
        double thisvalue = ((ContinuousAttribute) this.getAttribute()).getScaledValue((Double) this.getValue());
        double othervalue = ((ContinuousAttribute) this.getAttribute()).getScaledValue((Double) a);
        return Math.abs(thisvalue - othervalue);
    }

}
