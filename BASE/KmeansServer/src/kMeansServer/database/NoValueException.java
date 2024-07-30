package kMeansServer.database;

/**
 * Eccezione lanciata quando si tenta di accedere ad un valore non presente.
 */
public class NoValueException extends Exception {
    /**
     * Costruttore di default.
     */
    public NoValueException() {
        super();
    }
}
