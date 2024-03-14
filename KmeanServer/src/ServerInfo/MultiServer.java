package ServerInfo;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Classe che gestisce un Server multithreading, permettendo di gestire
 * più Client contemporaneamente.
 */
public class MultiServer
{
    private final int PORT;

    public MultiServer(int port) throws IOException
    {
        this.PORT = port;
        run();
    }
    /**
     * Istanzia un oggetto istanza della classe ServerSocket che pone in attesa
     * di richiesta di connessioni da parte del client.<br> Ad ogni nuova richiesta
     * di connessione si istanzia un oggetto di tipo ServerOneClient.
     * @throws IOException
     */
    private void run() throws IOException
    {
        ServerSocket s = new ServerSocket(PORT);
        System.out.println("Server Started");
        try
        {
            while(true)
            {
                Socket socket = s.accept();
                System.out.println("Connessione effettuata!");
                try
                {
                    new ServeOneClient(socket);
                } catch (IOException e) {System.out.println(e.getMessage());}
            }
        }
        finally {s.close();}

    }

    public static void main(String[] args)
    {
        try
        {
            new MultiServer(8080);
        } catch (IOException e) {System.out.println(e.getMessage());}

    }
}
