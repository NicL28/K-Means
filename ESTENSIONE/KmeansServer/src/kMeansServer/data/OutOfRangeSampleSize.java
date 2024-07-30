package kMeansServer.data;


/**
 * Eccezione lanciata quando la dimensione del campione è fuori dal range
 * consentito.
 */
public class OutOfRangeSampleSize extends Exception{

    /**
     * Costruttore dell'eccezione OutOfRangeSampleSize
     *
     * @param message
     */
    public OutOfRangeSampleSize(String message){
        super(message);
    }
}