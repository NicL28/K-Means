package kMeansServer.data;
/**
 * Classe che rappresenta un attributo continuo.
 * Estende la classe Attribute.
 * Ogni attributo continuo ha un valore minimo e un valore massimo.
 *
 * @see Attribute
 *
 */
public class ContinuousAttribute extends Attribute{
    /**
     * Valore minimo dell'attributo numerico.
     */
    private double min;
    /**
     * Valore massimo dell'attributo numerico.
     */
    private double max;

    /**
     * Costruttore della classe ContinuousAttribute.
     *
     * @param name nome dell'attributo
     * @param index indice dell'attributo
     * @param min valore minimo dell'attributo
     * @param max valore massimo dell'attributo
     */
    ContinuousAttribute(String name, int index, double min, double max) {
        super(name, index);
        this.min = min;
        this.max = max;
    }

    /**
     * Metodo per ottenere il valore scalato
     *
     * @param v valore dell'attributo da normalizzare
     * @return valore scalato, ottenuto come rapporto tra la differenza tra v e min
     *         e la differenza tra max e min
     */
    double getScaledValue(double v){
        return (v - min) / (max - min);
    }
}