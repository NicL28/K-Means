package kMeansClient.exception;

/**
 * Classe che rappresenta un'eccezione generata dal server.
 */
public class ServerException extends Exception {
    /**
     * Costruttore con messaggio di errore.
     *
     * @param message messaggio di errore
     */
    public ServerException(String message) {
        super(message);
    }
}
