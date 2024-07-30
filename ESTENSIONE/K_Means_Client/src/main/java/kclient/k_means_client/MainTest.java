package kclient.k_means_client;


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Classe che implementa il client per il clustering su dati in memoria
 * (lettura da file) e su dati in un database (lettura da tabella).
 * Il client si connette al server e invia richieste di clustering.
 */
public class MainTest {

    private static MainTest instance;
    /**
     * Stream di output verso il server.
     */
    private ObjectOutputStream out;
    /**
     * Stream di input dal server.
     */
    private ObjectInputStream in;
    /**
     * Costruttore del client.
     * Crea un socket e inizializza gli stream di input e output.
     * @param ip   indirizzo ip del server
     * @param port porta del server
     * @throws IOException se si verifica un errore di I/O
     */
    private MainTest(String ip, int port) throws IOException {
        try{
        InetAddress addr = InetAddress.getByName(ip); // ip
        System.out.println("addr = " + addr);
        Socket socket = new Socket(addr, port); // Port
        System.out.println(socket);

        // stream con richieste del client
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        out.reset();

            InputStream is = socket.getInputStream();
            in = new ObjectInputStream(is);
        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    public static MainTest createInstance(String ip, int port) throws IOException {
        if (instance == null) {
            instance = new MainTest(ip, port);
        }
        return instance;
    }

    public static MainTest getInstance() throws IOException {
        return instance;
    }

    public ObjectInputStream getIn(){
        return in;
    }

    public ObjectOutputStream getOut(){
        return out;
    }



}


