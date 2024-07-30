package kMeansServer.data;

/**
 * La classe DiscreteItem rappresenta un item di tipo discreto.
 * Estende la classe astratta Item.
 *
 * @see Item
 */
public class DiscreteItem extends Item {

    /**
     * Costruttore della classe DiscreteItem.
     *
     * @param attribute attributo dell'item
     * @param value valore dell'item
     */
    DiscreteItem(DiscreteAttribute attribute, String value) {
        super(attribute, value);
    }

    /**
     * Calcola la distanza tra il valore dell'item corrente e il valore dell'item
     * passato come parametro.
     * Restituisce 0 se i valori sono uguali, 1 altrimenti.
     *
     * @param a
     * @return distanza tra i valori
     */
    double distance(Object a) {
        return getValue().equals(a) ? 0 : 1;
    }
}
