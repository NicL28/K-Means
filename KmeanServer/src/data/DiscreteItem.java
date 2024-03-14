package data;

public class DiscreteItem extends Item {
    DiscreteItem(DiscreteAttribute attribute, String value) {
        super(attribute, value);
    }

    // INPUT: a
    // OUTPUT: 0 se a e' uguale a this, 1 altrimenti
    // COMPORTAMENTO: restituisce la distanza tra this "b" e a
    public double distance(Object a) {
        if (a instanceof DiscreteItem) {
            DiscreteItem b = (DiscreteItem) a;
            if (getValue().equals(b.getValue())) {
                return 0; // se sono uguali
            } else {
                return 1; // se sono diversi
            }
        } else {
            return 1;
        }
    }
}