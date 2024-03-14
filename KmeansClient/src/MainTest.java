import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import keyboardinput.*;


/**
 * Classe main che avvia il client e che contiene le varie richieste
 * da inviare al Server.
 */
public class MainTest {
    /**Stream che contiene le richieste del client inviate al Server.*/
    private ObjectOutputStream out;
    /**Stream che riceve le informazioni inviate dal Server.*/
    private ObjectInputStream in ;

    /**
     * Costruttore che inizializza la connessione col Server e gli Stream per
     * la comunicazione con esso.
     * @param ip IP del server
     * @param port porta logica del server
     * @throws IOException viene sollevato al fallimento di connessione col server
     * o ad un altro tipo di errore I/O.
     */
    public MainTest(String ip, int port) throws IOException{
        InetAddress addr = InetAddress.getByName(ip); //ip
        System.out.println("addr = " + addr);
        Socket socket = new Socket(addr, port); //Port
        System.out.println(socket);

        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Stampa a video un menù che descrive le azioni eseguibili dall'utente.
     * @return Intero che corrisponde all'indice, scelto dall'utente, delle
     * azioni possibili descritte nel menù. */
    private int menu(){
        int answer;
        System.out.println("Scegli una opzione");
        do{
            System.out.println("(1) Carica Cluster da File");
            System.out.println("(2) Carica Dati");
            System.out.print("Risposta:");
            answer=Keyboard.readInt();
        }
        while(answer<=0 || answer>2);
        return answer;

    }

    /**
     * Passa in outstream il nome del file da cui voler caricare un clusterSet salvato in precedenza
     * e restituisce il risultato dell'operazione.
     * @return restituisce,in caso di successo dell'operazione da parte del Server,
     * il clusterSet salvato su file in forma di Stringa; In caso di fallimento, verrà sollevata
     * un eccezione.
     * @throws  ServerException sollevata in caso di fallimento nell'operazione di caricamento:
     * contiene un messaggio d'errore, passato nello stream dal Server, che specifica in
     * dettaglio il problema sorto.
     */
    private String learningFromFile() throws SocketException, ServerException,IOException,ClassNotFoundException{
        out.writeObject(3);
        System.out.print("Nome file da cui caricare un clusterSet:");
        String tabName=Keyboard.readString();
        out.writeObject(tabName);
        String result = (String)in.readObject();
        if(result.equals("OK"))
            return (String)in.readObject();
        else throw new ServerException(result);

    }

    /**
     * Passa in outstream il nome di una tabella del database per poter permettere al Server
     * l'estrazione dei dati contenuti in essa, e riceve l'esito dell'operazione effettuata.
     * <br>Se l'esito è negativo, verrà sollevata un'eccezione.
     * @throws SocketException sollevata in caso di problemi di connessione col socket
     * @throws ServerException sollevata in caso di fallimento nell'operazione di estrazione
     * dati dalla tabella da parte del Server: contiene il messaggio di errore passato nello stream dal Server,
     * che specifica in dettaglio il problema sorto.
     * @throws ClassNotFoundException sollevata da readObject()
     */
    private void storeTableFromDb() throws SocketException,ServerException,IOException,ClassNotFoundException{
        out.writeObject(0);
        System.out.print("Nome dataBase:");
        String DBname=Keyboard.readString();
        System.out.print("Nome tabella:");
        String tabName=Keyboard.readString();
        out.writeObject(tabName);
        out.writeObject(DBname);
        String result = (String)in.readObject();
        if(!result.equals("OK"))
            throw new ServerException(result);

    }

    /**
     * Passa in outstream il numero di cluster che si vogliono utilizzare e riceve
     * il risultato dell'operazione dal Server.
     * @return risultato dell'operazione: se è positivo, verrà passata una stringa
     * contenente la clusterizzazione effettuata; Se negativo, verrà lanciata un'eccezione.
     * @throws SocketException eccezione sollevata in caso di risultato negativo ricevuto dal Server:
     * contiene il messaggio di errore passato nello stream dal Server.
     */
    private String learningFromDbTable() throws SocketException,ServerException,IOException,ClassNotFoundException{
        out.writeObject(1);
        System.out.print("Numero di cluster:");
        int k=Keyboard.readInt();
        out.writeObject(k);
        String result = (String)in.readObject();
        if(result.equals("OK")){
            System.out.println("Clustering output:\n");
            return (String)in.readObject();
        }
        else throw new ServerException(result);
    }

    /**
     * Passa in outstream il nome del file su cui si vuole salvare un ClusterSet; Il server
     * elaborerà la risposta: se positiva, il metodo notificherà all'utente il successo
     * dell'operazione, se è negativa, verrà sollevata una eccezione.
     * @throws ServerException sollevata in caso di fallimento dell'operazione di salvataggio
     * su file: contiene il messaggio di errore passato nello stream dal Server.
     */
    private void storeClusterInFile() throws SocketException,ServerException,IOException,ClassNotFoundException {
        out.writeObject(2);
        System.out.println("Nome del file in cui salvare il ClusterSet: ");
        String fileName=Keyboard.readString();
        out.writeObject(fileName);
        String result = (String)in.readObject();
        if(result.equals("OK"))
            System.out.println("ClusterSet correttamente salvato!");
        else throw new ServerException(result);

    }

    /**
     * Main del client che gestisce le interazioni con il server e le richieste ad esso.
     * @param args si passano come arguments IP e port del Server a cui si vuole collegare.
     */
    public static void main(String[] args)
    {
        String ip=args[0];
        int port= Integer.parseInt(args[1]);
        MainTest main=null;
        try{
            main=new MainTest(ip,port);
        }
        catch (IOException e){
            System.out.println("Connessione al server fallita, ricontrollare le credenziali" +
                    "d'accesso,\nDescrizione del problema: " + e);
            return;
        }


        do{
            int menuAnswer=main.menu();
            switch(menuAnswer)
            {
                case 1:
                    try {
                        String kmeans=main.learningFromFile();
                        System.out.println(kmeans);
                    }
                    catch (SocketException e) {
                        System.out.println(e);
                        return;
                    }
                    catch (FileNotFoundException e) {
                        System.out.println(e);
                        return ;
                    } catch (IOException e) {
                        System.out.println(e);
                        return;
                    } catch (ClassNotFoundException e) {
                        System.out.println(e);
                        return;
                    }
                    catch (ServerException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2: // learning from db

                    while(true){
                        try{
                            main.storeTableFromDb();
                            break; //esce fuori dal while
                        }

                        catch (SocketException e) {
                            System.out.println(e);
                            return;
                        }
                        catch (FileNotFoundException e) {
                            System.out.println(e);
                            return;

                        } catch (IOException e) {
                            System.out.println(e);
                            return;
                        } catch (ClassNotFoundException e) {
                            System.out.println(e);
                            return;
                        }

                        catch (ServerException e) {
                            System.out.println(e.getMessage());
                        }

                    } //end while [viene fuori dal while con un db (in alternativa il programma termina)

                    char answer='y';//itera per learning al variare di k
                    do{
                        try
                        {
                            String clusterSet=main.learningFromDbTable();
                            System.out.println(clusterSet);

                            main.storeClusterInFile();

                        }
                        catch (SocketException e) {
                            System.out.println(e);
                            return;
                        }
                        catch (FileNotFoundException e) {
                            System.out.println(e);
                            return;
                        }
                        catch (ClassNotFoundException e) {
                            System.out.println(e);
                            return;
                        }catch (IOException e) {
                            System.out.println(e);
                            return;
                        }
                        catch (ServerException e) {
                            System.out.println(e.getMessage());
                        }
                        System.out.print("Vuoi ripetere l'esecuzione?(y/n)");
                        answer=Keyboard.readChar();
                    }
                    while(answer=='y');
                    break; //fine case 2
                default:
                    System.out.println("Opzione non valida!");
            }

            System.out.print("Vuoi scegliere una nuova operazione da menu?(y/n)");
            if(Keyboard.readChar()!='y')
                break;
        }
        while(true);
    }
}



