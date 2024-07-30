package kMeansServer.database;

/**
 * Eccezione lanciata quando si tenta di accedere ad un insieme vuoto.
 */
public class EmptySetException extends Exception {
    /**
     * Costruttore di default.
     */
    public EmptySetException() {
        super();
    }
}
