package kMeansServer.database;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe che si occupa di gestire la connessione al database.
 */
public class DbAccess {
    /**
     * Nome del driver da utilizzare per la connessione al database.
     */
    private String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    /**
     * Stringa che rappresenta il DBMS.
     */
    private final String DBMS = "jdbc:mysql";
    /**
     * Nome del server a cui connettersi.
     */
    private final String SERVER = "localhost";
    /**
     * Nome del database a cui connettersi.
     */
    private final String DATABASE = "MapDB";
    /**
     * Porta a cui connettersi.
     */
    private final String PORT = "3306";
    /**
     * Nome utente per la connessione al database.
     */
    private final String USER_ID = "MapUser";
    /**
     * Password per la connessione al database.
     */
    private final String PASSWORD = "map";
    /**
     * Oggetto di tipo Connection utilizzato per la connessione al kMeansServer.database.
     */
    private Connection conn;

    /**
     * Inizializza la connessione al database.
     * @throws DatabaseConnectionException se si verifica un errore nella connessione al kMeansServer.database
     */
    public void initConnection() throws DatabaseConnectionException {
        try {
            Class.forName(DRIVER_CLASS_NAME).getDeclaredConstructors()[0].newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IllegalArgumentException
                | InvocationTargetException | SecurityException e) {
            System.out.println("[!] ClassNotFoundException: " + e.getMessage());
            throw new DatabaseConnectionException();
        }
        String url = DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE + "?user=" + USER_ID + "&password=" + PASSWORD
                + "&serverTimezone=UTC";
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println("[!] SQLException: " + e.getMessage());
            System.out.println("[!] SQLState: " + e.getSQLState());
            System.out.println("[!] VendorError: " + e.getErrorCode());
            throw new DatabaseConnectionException();
        }
    }

    /**
     * Restituisce la connessione al database.
     * @return connessione al database.
     */
    Connection getConnection() {
        return conn;
    }

    /**
     * Chiude la connessione al database.
     * @throws SQLException se si verifica un errore nella chiusura della connessione al kMeansServer.database
     */
    void closeConnection() throws SQLException {
        conn.close();
    }
}
