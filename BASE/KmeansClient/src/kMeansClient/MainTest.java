package kMeansClient;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import kMeansClient.exception.ServerException;
import kMeansClient.keyboardinput.Keyboard;

/**
 * Classe che implementa il client per il clustering su dati in memoria
 * (lettura da file) e su dati in un database (lettura da tabella).
 * Il client si connette al server e invia richieste di clustering.
 */
public class MainTest {

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
        InetAddress addr = InetAddress.getByName(ip); // ip
        System.out.println("addr = " + addr);
        Socket socket = new Socket(addr, port); // Port
        System.out.println(socket);

        // stream con richieste del client
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        out.reset();
        try {
            InputStream is = socket.getInputStream();
            in = new ObjectInputStream(is);
        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    /**
     * Metodo che stampa il menu e legge la risposta dell'utente.
     * @return risposta dell'utente
     */
    private int menu() {
        int answer;
        System.out.println("\nScegli una opzione");
        do {
            System.out.println("(1) Carica Cluster da File");
            System.out.println("(2) Carica Dati");
            System.out.print("Risposta: ");
            answer = Keyboard.readInt();
            if (answer <= 0 || answer > 2)
                System.out.println("Opzione non valida!");
        } while (answer <= 0 || answer > 2);
        return answer;
    }

    /**
     * Metodo che invia al server nome della tabella e numero di cluster
     * che formano il nome del file su cui è salvato il clustering.
     * @return stirnga contenente i centroidi del clustering
     * @throws ServerException se si verifica un errore di connessione al server
     * @throws IOException se si verifica un errore di I/O
     * @throws ClassNotFoundException se si verifica un errore di classe non trovata nella lettura da stream
     */
    private String learningFromFile() throws ServerException, IOException, ClassNotFoundException {
        out.writeObject(3);
        System.out.print("Nome tabella: ");
        String tableName = Keyboard.readString();
        out.writeObject(tableName);
        System.out.print("Numero cluster: ");
        int numCluster = Keyboard.readInt();
        out.writeObject(numCluster);
        String result = (String) in.readObject();
        if (result.equals("OK"))
            return (String) in.readObject();
        else
            throw new ServerException(result);
    }

    /**
     * Metodo che invia al server il nome della tabella del database
     * da cui leggere i dati.
     * @throws ServerException se si verifica un errore di connessione al server
     * @throws IOException se si verifica un errore di I/O
     * @throws ClassNotFoundException se si verifica un errore di classe non trovata nella lettura da stream
     */
    private void storeTableFromDb() throws ServerException, IOException, ClassNotFoundException {
        out.writeObject(0);
        System.out.print("Nome tabella: ");
        String tabName = Keyboard.readString();
        out.writeObject(tabName);
        String result = (String) in.readObject();
        if (!result.equals("OK")) {
            throw new ServerException(result);
        }
    }


    /**
     * Metodo che invia al server il numero di cluster da generare e
     * riceve dal server il risultato del clustering
     * @return stringa che contiene il risultato del clustering
     * @throws ServerException se si verifica un errore di connessione al server
     * @throws IOException se si verifica un errore di I/O
     * @throws ClassNotFoundException se si verifica un errore di classe non trovata nella lettura da stream
     */
    private String learningFromDbTable() throws ServerException, IOException, ClassNotFoundException {
        out.writeObject(1);
        System.out.print("Numero di cluster: ");
        int k = Keyboard.readInt();
        out.writeObject(k);
        String result = (String) in.readObject();
        if (result.equals("OK")) {
            System.out.println("\nClustering output:" + in.readObject());
            return (String) in.readObject();
        } else
            throw new ServerException(result);

    }

    /**
     * Metodo che riceve la conferma dal server di aver salvato il clustering su file.
     * @throws ServerException se si verifica un errore di connessione al server
     * @throws IOException se si verifica un errore di I/O
     * @throws ClassNotFoundException se si verifica un errore di classe non trovata nella lettura da stream
     */
    private void storeClusterInFile() throws ServerException, IOException, ClassNotFoundException {
        out.writeObject(2);
        String result = (String) in.readObject();
        if (!result.equals("OK"))
            throw new ServerException(result);
    }

    /**
     * Metodo di init del client.
     * Crea un oggetto kMeansClient.kMeansClient.MainTest e invoca il metodo menu() e esegue l'operazione scelta.
     * L'esecuzione continua finchè non si desidera scegliere una voce del menu.
     * @param args argomenti da linea di comando
     */
    public static void main(String[] args) {
        String ip = null;
        int port = 0;
        if (args.length != 0) {
            ip = args[0];
            port = Integer.parseInt(args[1]);
        } else {
            System.out.print("Inserisci l'indirizzo ip del server: ");
            ip = Keyboard.readString();
            System.out.print("Inserisci la porta del server: ");
            port = Keyboard.readInt();
        }
        MainTest main = null;
        try {
            main = new MainTest(ip, port);
        } catch (IOException e) {
            System.out.println("Errore: " + e.getMessage());
            return;
        }
        System.out.println("Connessione effettuata con successo!");
        do {
            int menuAnswer = main.menu();
            switch (menuAnswer) {
                case 1:
                    try {
                        String kmeans = main.learningFromFile();
                        System.out.println("\n" + kmeans);
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println(e.getMessage());
                        return;
                    } catch (ServerException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2: // learning from db
                    while (true) {
                        try {
                            main.storeTableFromDb();
                            break; // esce fuori dal while
                        } catch (IOException | ClassNotFoundException e) {
                            System.out.println("Errore: " + e.getMessage());
                            return;
                        } catch (ServerException e) {
                            System.out.println("Errore: " + e.getMessage());
                        }
                    } // end while [viene fuori dal while con un db (in alternativa il programma
                      // termina)

                    char answer = 'y';// itera per learning al variare di k
                    do {
                        try {
                            String clusterSet = main.learningFromDbTable();
                            System.out.println(clusterSet);
                            main.storeClusterInFile();
                            System.out.println("Clustering salvato correttamente");
                        } catch (ClassNotFoundException | IOException e) {
                            System.out.println(e.getMessage());
                            return;
                        } catch (ServerException e) {
                            System.out.println(e.getMessage());
                        }
                        System.out.print("Vuoi ripetere l'esecuzione per la stessa tabella? (y/n): ");
                        answer = Keyboard.readChar();
                    } while (answer == 'y');
                    break; // fine case 2
            }

            System.out.print("Vuoi scegliere una nuova operazione da menu? (y/n): ");
            if (Keyboard.readChar() != 'y')
                break;
        } while (true);
    }
}
