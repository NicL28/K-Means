package kMeansServer.data;

import java.io.Serializable;

/**
 * Classe astratta Attribute che rappresenta un attributo.
 * Ogni attributo ha un nome e un indice.
 * Attribute implementa l'interfaccia Serializable.
 *
 * @see Serializable
 *
 */
public abstract class Attribute implements Serializable {
    /**
     * Stringa che indica il nome dell'attributo
     */
    private String name;
    /**
     * Intero che indica l'indice dell'attributo
     */
    private int index;

    /**
     * Costruttore della classe Attribute *
     *
     * @param name  nome dell'attributo
     * @param index indice dell'attributo
     */
    Attribute(String name, int index){
        this.name = name;
        this.index = index;
    }

    /**
     * Metodo per ottenere il nome dell'attributo *
     *
     * @return nome dell'attributo
     */
    String getName(){
        return name;
    }

    /**
     * Metodo per ottenere l'indice dell'attributo
     *
     * @return indice dell'attributo
     */
    int getIndex(){
        return index;
    }

    /**
     * Metodo per ottenere il nome dell'attributo in formato stringa
     *
     * @return stringa del nome dell'attributo
     */
    public String toString(){
        return name;
    }
}