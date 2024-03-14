/**
 * Eccezione che notifica dei problemi riscontrati durante le operazioni
 * effettuate dal Server.
 */
public class ServerException extends Exception
{
    public ServerException(String msg) {
        super(msg);
    }
}
