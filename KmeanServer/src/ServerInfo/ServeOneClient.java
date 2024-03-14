package ServerInfo;

import java.io.*;
import java.net.Socket;
import data.*;
import mining.*;
import java.io.IOException;
import java.sql.SQLException;
import database.*;
import mining.*;
/**
 * Si occupa di gestire le richieste di un singolo client.
 */
public class ServeOneClient extends Thread
{
    Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;
    KmeansMiner kmeans;
    /**
     * Inizializza gli stream per comunicare col Client ed avvia
     * il thread.
     * @param s Socket per la comunicazione col client
     */
    public ServeOneClient(Socket s) throws IOException
    {
        socket = s;
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());

        start(); // Chiama run()
    }
    /**
     * avvia un nuovo thread che gestirà tutte le richieste del client connesso.
     */
    public void run()
    {
        Data data = null;
        String tableName = "";
        String DBname = "";
        try {
            while (true) {
                int step=(int)in.readObject();
                if(step==0)
                {
                    tableName = (String) in.readObject();
                    DBname = (String) in.readObject();
                    try {
                        data = new Data(tableName,DBname);
                        out.writeObject("OK");
                    } catch (DatabaseConnectionException e4) {
                        out.writeObject("Connessione fallita: controllare" +
                                " se il nome del database o della tabella sono corretti");
                    } catch (EmptySetException emptySet) {
                        System.out.println(emptySet.getMessage());
                    } catch (SQLException e) {
                        out.writeObject(e.getMessage());
                    }
                }
                if(step==1)
                {
                    int numClusters = (int)in.readObject();
                    try {
                        kmeans = new KmeansMiner(numClusters);
                        int numIter = kmeans.kmeans(data);
                        out.writeObject("OK");
                        out.writeObject("Numero di Iterazione:" + numIter + "\n" + kmeans.getC().toString(data));
                        //out.writeObject(kmeans.getC().toString(data));
                    } catch (OutOfRangeSampleSize e) {
                        out.writeObject(e.getMessage());
                    }
                }
                if(step==2)
                {
                    String fileName = (String)in.readObject();
                        try {
                            FileOutputStream filein = new FileOutputStream(fileName + "_tableName");
                            ObjectOutputStream saveTableName = new ObjectOutputStream(filein);
                            saveTableName.writeObject(tableName);
                            saveTableName.writeObject(DBname);
                            filein.close();
                            kmeans.salva(fileName);
                            out.writeObject("OK");
                        } catch (IOException e)
                        {
                            out.writeObject(e.getMessage());
                        }
                }
                if(step==3)
                {
                    String fileName = (String)in.readObject();
                    try
                    {
                        String result = loadClustersFromFile(fileName);
                        out.writeObject("OK");
                        out.writeObject(result);
                    } catch (DatabaseConnectionException | SQLException | EmptySetException e)
                    {
                        out.writeObject(e.getMessage());
                    }
                    catch (FileNotFoundException e)
                    {
                        out.writeObject("File specificato inesistente, impossibile caricare un clustering");
                    }

                }
            }
        } catch(IOException e) {
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                in.close();
                out.close();
                socket.close();System.err.println("Socket closed");
            } catch(IOException e) {
                System.err.println("Socket not closed");
            }

        }
    }

    /**
     * Carica un clusterSet salvato in precedenza su file.
     * @param fileName nome del file da cui caricare un clusterSet salvato.
     * @return stringa che visualizza tutte le informazioni sui cluster estratti.
     */
    private String loadClustersFromFile(String fileName) throws IOException, ClassNotFoundException,
            DatabaseConnectionException, SQLException, EmptySetException,FileNotFoundException
    {
        FileInputStream filein = new FileInputStream(fileName+"_tableName");
        ObjectInputStream loadTable = new ObjectInputStream(filein);
        String tableName = (String)loadTable.readObject();
        String DBname = (String)loadTable.readObject();
        filein.close();
        Data data = new Data(tableName,DBname);
        KmeansMiner clusterSet = new KmeansMiner(fileName);
        return  clusterSet.getC().toString(data);
    }
}


