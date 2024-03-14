package database;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbAccess {
    private String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    private final String DBMS="jdbc:mysql";
    private final String SERVER="localhost";
    private String DATABASE="MapDB";
    private final String PORT="3306";
    private final String USER_ID="MapUser";
    private final String PASSWORD="map";
    Connection conn;

    public DbAccess(String DBname)
    {
        this.DATABASE = DBname;
    }

    @SuppressWarnings("deprecation")
    public void initConnection() throws DatabaseConnectionException{
        
        try {
			Class.forName(DRIVER_CLASS_NAME).newInstance();
            String connectionString = DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE
            + "?user=" + USER_ID + "&password=" + PASSWORD + "&serverTimezone=UTC";
            conn = DriverManager.getConnection(connectionString);
		} catch(ClassNotFoundException e) {
			System.out.println("[!] Driver not found: " + e.getMessage());
			throw new DatabaseConnectionException();
		} catch(InstantiationException e){
			System.out.println("[!] Error during the instantiation : " + e.getMessage());
			throw new DatabaseConnectionException();
		} catch(IllegalAccessException e){
			System.out.println("[!] Cannot access the driver : " + e.getMessage());
			throw new DatabaseConnectionException();
		}catch(SQLException e){
            System.out.println("SQL exception:"+e.getMessage());
        }

    }

    Connection getConnection(){
        return conn;
    }
    
    public void closeConnection(){
        try{
        conn.close();
        } catch(SQLException e){
            System.err.println("Fallimento chiusura connessione");
        }
    } 
}
