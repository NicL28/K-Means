package kMeansServer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

import kMeansServer.data.Data;
import kMeansServer.data.OutOfRangeSampleSize;
import kMeansServer.database.DatabaseConnectionException;
import kMeansServer.database.EmptySetException;
import kMeansServer.database.NoValueException;
import kMeansServer.mining.KmeansMiner;

/**
 * Classe che implementa un server multithread.
 * Il server si mette in ascolto su una porta.
 */
public class MultiServer {
    /**
     * Porta da cui viene avviarto il server.
     */
    private static int PORT = 8080;
    /**
     * Main del programma. Crea un oggetto kMeansServer.MultiServer che avvia il server sulla porta specificata.
     *
     * @param args argomenti da linea di comando
     */
    public static void main(String[] args) {
        new MultiServer(PORT);
    }

    /**
     * Costruttore di kMeansServer.MultiServer che avvia il server sulla porta specificata.
     *
     * @param Port porta su cui avviare il server
     */
    private MultiServer(int Port) {
        PORT = Port;
        run();
    }

    /**
     * Avvia il server sulla porta specificata.
     * Crea un socket e si mette in attesa di connessioni.
     * Quando un client si connette, crea un thread kMeansServer.ServerOneClient che gestisce la connessione con il client.
     */
    private void run() {
        ServerSocket s;
        try {
            s = new ServerSocket(PORT);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Server inizializzato nella porta:" + PORT + "...");

        try {
            while (true) {
                Socket socket = s.accept();
                System.out.println("Socket accettata: " + socket);
                try {
                    new ServerOneClient(socket);
                } catch (IOException e) {
                    socket.close();
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                s.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

/**
 * Inner class che gestisce la connessione con un client.
 * Viene avviato ogni volta che un client si connette al server.
 * Viene richiamato dal metodo run() della classe kMeansServer.MultiServer.
 * Riceve i dati dal client e li elabora.
 * Invia i risultati al client.
 * @see MultiServer
 */
class ServerOneClient extends Thread {
    /**
     * Socket su cui avviene la connessione con il client.
     */
    private Socket socket;
    /**
     * Stream di output verso il client.
     */
    private ObjectOutputStream out;
    /**
     * Stream di input dal client.
     */
    private ObjectInputStream in;

    /**
     * Costruttore della classe kMeansServer.ServerOneClient.
     *
     * @param s socket su cui avviare la connessione
     * @throws IOException se si verifica un errore di I/O
     */
    ServerOneClient(Socket s) throws IOException {
        socket = s;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        start();
    }

    /**
     * Thread che gestisce la connessione con il client.
     * Riceve i dati dal client e li elabora.
     * Invia i risultati al client.
     */
    public void run() {
        try {
            String tableName = "";
            Integer numCluster = 0;
            KmeansMiner kmeans = null;
            Data data = null;
            while (true) {
                int scelta = (Integer) in.readObject();
                switch (scelta) {
                    case 0:
                        tableName = (String) in.readObject();
                        try {
                            data = new Data(tableName);
                            out.writeObject("OK");
                        } catch (DatabaseConnectionException | SQLException | NoValueException | EmptySetException e) {
                            System.out.println("La tabella " + tableName + " non esiste");
                            System.out.println(e.getMessage());
                            out.writeObject("La tabella " + tableName + " non esiste");
                        }
                        break;
                    case 1:
                        try {
                            numCluster = (Integer) in.readObject();
                            kmeans = new KmeansMiner(numCluster);
                            int numIter = kmeans.kmeans(data);
                            out.writeObject("OK");
                            out.writeObject("\nNumero di iterazioni: " + numIter + "\n");
                            out.writeObject(kmeans.getC().toString(data));
                        } catch (ClassNotFoundException | OutOfRangeSampleSize e) {
                            System.out.println("Errore nella generazione dei cluster ");
                            System.out.println(e.getMessage());
                            out.writeObject(e.getMessage());
                        }
                        break;
                    case 2:
                        try {
                            kmeans.salva(tableName + "_" + numCluster + ".dat");
                            out.writeObject("OK");
                        } catch (FileNotFoundException e) {
                            System.out.println("Salvataggio File fallito");
                            System.out.println(e.getMessage());
                            out.writeObject(e.getMessage());
                        }
                        break;
                    case 3:
                        try {
                            tableName = (String) in.readObject();
                            numCluster = (Integer) in.readObject();
                            kmeans = new KmeansMiner(".\\salvataggi\\" + tableName + "_" + numCluster + ".dat");
                            out.writeObject("OK");
                            out.writeObject(kmeans.getC().toString());
                        } catch (FileNotFoundException e) {
                            System.out.println(e.getMessage());
                            out.writeObject("Il file non esiste!");
                        } catch (ClassNotFoundException | IOException e) {
                            System.out.println(e.getMessage());
                            out.writeObject("Caricamento file fallito");
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("chiusura...");
            System.err.println("IO Exception: connessione chiusa");
        } catch (ClassNotFoundException e) {
            System.err.println("Classe non trovata");
            System.err.println(e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Socket non chiusa");
                System.out.println(e.getMessage());
            }
        }
    }
}
