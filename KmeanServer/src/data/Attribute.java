package data;/*La classe astratta Attribute definisce i membri e i metodi che sono comuni a tutti gli attributi, sia continui che discreti.
             Inoltre, il costruttore viene definito in modo che sia richiesto il nome e l'identificativo numerico dell'attributo,
             e il metodo toString() viene sovrascritto per restituire il nome dell'attributo come rappresentazione dell'oggetto. */

public abstract class Attribute {
    // Membri attributi
    protected String name; // nome simbolico dell'attributo
    protected int index; // identificativo numerico dell'attributo

    // Costruttore
    Attribute(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // Metodo per ottenere il nome dell'attributo
    String getName() {
        return name;
    }

    // Metodo per ottenere l'identificativo numerico dell'attributo
    int getIndex() {
        return index;
    }

    // sovrascrive metodo ereditato dalla superclasse e restuisce la stringa
    // rappresentante lo stato dell'oggetto
    public String toString() {
        return name;
    }
}
