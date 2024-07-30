package kMeansServer.database;

/**
 * Eccezione lanciata quando la connessione al database non è possibile.

 */
public class DatabaseConnectionException extends Exception {
    /**
     * Costruttore di default
     */
    public DatabaseConnectionException() {
        super();
    }
}
