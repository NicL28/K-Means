package kMeansServer.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import kMeansServer.database.TableSchema.Column;

/**
 * Classe che rappresenta i dati di una tabella.
 */
public class TableData {
    /**
     * Connessione al database.
     */
    DbAccess db;
    /**
     * Costruttore della classe.
     *
     * @param db connessione al database.
     */
    public TableData(DbAccess db) {
        this.db = db;
    }

    /**
     * Restituisce la lista delle transazioni presenti nella tabella.
     *
     * @param table nome della tabella.
     * @return lista delle transazioni.
     * @throws SQLException      eccezione lanciata in presenza di errori di accesso
     *                           al database.
     * @throws EmptySetException eccezione lanciata in presenza di un insieme vuoto.
     */
    public List<Example> getDistinctTransazioni(String table) throws SQLException, EmptySetException {
        TableSchema ts = new TableSchema(db, table);
        String sql = "SELECT DISTINCT * FROM " + table;
        Statement stmt = db.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Example> list = new ArrayList<Example>();
        while (rs.next()) {
            Example ex = new Example();
            for (int i = 0; i < ts.getNumberOfAttributes(); i++) {
                if (ts.getColumn(i).isNumber())
                    ex.add(rs.getDouble(i + 1));
                else
                    ex.add(rs.getString(i + 1));
            }
            list.add(ex);
        }
        if (list.isEmpty())
            throw new EmptySetException();
        
        return list;
    }

    /**
     * Restituisce l'insieme dei valori distinti presenti in una colonna.
     *
     * @param table  nome della tabella.
     * @param column colonna.
     * @return insieme dei valori distinti.
     * @throws SQLException eccezione lanciata in presenza di errori di accesso al
     *                      database.
     */
    public Set<Object> getDistinctColumnValues(String table, Column column) throws SQLException {
        String sql = "SELECT DISTINCT " + column.getColumnName() + " FROM " + table + " ORDER BY "
                + column.getColumnName() + " ASC";
        Statement stmt = db.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        Set<Object> set = new TreeSet<Object>();
        while (rs.next()) {
            if (column.isNumber())
                set.add(rs.getDouble(1));
            else
                set.add(rs.getString(1));
        }
        return set;
    }

    /**
     * Restituisce il valore minimo o massimo di una colonna.
     *
     * @param table nome della tabella del kMeansServer.database.
     * @param column oggetto di classe Column che rappresenta la colonna.
     * @param aggregate MAX se si vuole il valore massimo, MIN se si vuole il valore minimo.
     * @return oggetto di classe Object che rappresenta il valore massimo o minimo della colonna.
     * @throws SQLException se si verifica un errore nell'esecuzione della query
     * @throws NoValueException se la colonna è vuota
     * @see QUERY_TYPE
     */
    public Object getAggregateColumnValue(String table, Column column, QUERY_TYPE aggregate)
            throws SQLException, NoValueException {
        String sql = "SELECT " + aggregate.toString() + "(" + column.getColumnName() + ") FROM " + table;
        Statement stmt = db.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            if (column.isNumber()) {
                Double res = rs.getDouble(1);
                if (rs.wasNull())
                    throw new NoValueException();
                return res;
            } else {
                String res = rs.getString(1);
                if (rs.wasNull())
                    throw new NoValueException();
                return res;
            }
        } else
            throw new NoValueException();
    }

}
