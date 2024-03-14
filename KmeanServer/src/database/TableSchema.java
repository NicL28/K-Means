package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



/**Modella lo schema di una tabella nel database relazionale.*/
public class TableSchema
{
    DbAccess db;
    /**Rappresenta una colonna di una tabella estratta dal database.*/
    public class Column
    {
        private String name;
        private String type;
        Column(String name,String type){
            this.name=name;
            this.type=type;
        }
        public String getColumnName(){
            return name;
        }
        public boolean isNumber(){
            return type.equals("number");
        }
        public String toString(){
            return name+":"+type;
        }
    }
    List<Column> tableSchema=new ArrayList<Column>();
    /**
     * Acquisisce informazioni sugli attributi ed i loro tipi nella tabella
     * del database desiderata.
     * @param db Permette di accedere al database ed estrarre informazioni sulla tabella.
     * @param tableName Nome della tabella del database.
     * @throws SQLException Viene sollevato qualora accada un errore di interazione col database.
     */
    public TableSchema(DbAccess db, String tableName) throws SQLException
    {
        this.db=db;
        HashMap<String,String> mapSQL_JAVATypes=new HashMap<String, String>();
        //http://java.sun.com/j2se/1.3/docs/guide/jdbc/getstart/mapping.html
        mapSQL_JAVATypes.put("CHAR","string");
        mapSQL_JAVATypes.put("VARCHAR","string");
        mapSQL_JAVATypes.put("LONGVARCHAR","string");
        mapSQL_JAVATypes.put("BIT","string");
        mapSQL_JAVATypes.put("SHORT","number");
        mapSQL_JAVATypes.put("INT","number");
        mapSQL_JAVATypes.put("LONG","number");
        mapSQL_JAVATypes.put("FLOAT","number");
        mapSQL_JAVATypes.put("DOUBLE","number");



        Connection con=db.getConnection();
        DatabaseMetaData meta = con.getMetaData();
        ResultSet res = meta.getColumns(null, null, tableName, null);

        while (res.next())
        {

            if(mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME")))
                tableSchema.add(new Column(
                        res.getString("COLUMN_NAME"),
                        mapSQL_JAVATypes.get(res.getString("TYPE_NAME")))
                );



        }
        res.close();

    }

    /**
     * @return Numero totale di attributi che la tabella acquisita dal database possiede.
     */
    public int getNumberOfAttributes(){
        return tableSchema.size();
    }
    /**
     *
     * @return Colonna della tabella estratta dal database nella posizione
     * dell'intero passato come parametro. */
    public Column getColumn(int index){
        return tableSchema.get(index);
    }


}




