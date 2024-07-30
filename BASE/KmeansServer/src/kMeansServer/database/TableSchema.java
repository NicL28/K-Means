package kMeansServer.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Classe TableSchema che rappresenta lo schema di una tabella.
 * Lo schema è costituito da una lista di colonne, ognuna delle quali è
 * rappresentata da un nome e da un tipo.
 */
public class TableSchema {
	/**
	 * Connessione al database.
	 */
	DbAccess db;

	/**
	 * Classe interna Column che rappresenta una colonna di una tabella.
	 */
	public static class Column {
		/**
         * Nome della colonna.
         */
		private String name;
		/**
         * Tipo della colonna.
         */
		private String type;

		/**
		 * Costruttore della classe Column.
		 *
		 * @param name nome della colonna.
		 * @param type tipo della colonna.
		 */
		Column(String name, String type) {
			this.name = name;
			this.type = type;
		}

		/**
		 * Restituisce il nome della colonna.
		 *
		 * @return nome della colonna.
		 */
		public String getColumnName() {
			return name;
		}

		/**
		 * Controlla se la colonna è di tipo numerico.
		 *
		 * @return true se la colonna è di tipo numerico, false altrimenti.
		 */
		public boolean isNumber() {
			return type.equals("number");
		}

		/**
		 * Restituisce una stringa che rappresenta la colonna.
		 *
		 * @return stringa che rappresenta la colonna.
		 */
		public String toString() {
			return name + ":" + type;
		}

	}

	/**
	 * Lista delle colonne della tabella.
	 */
	List<Column> tableSchema = new ArrayList<>();

	/**
	 * Costruttore della classe TableSchema.
	 *
	 * @param db        connessione al database.
	 * @param tableName nome della tabella.
	 * @throws SQLException eccezione lanciata in presenza di errori di accesso al
	 *                      database.
	 */
	public TableSchema(DbAccess db, String tableName) throws SQLException {
		this.db = db;
		HashMap<String, String> mapSQL_JAVATypes = new HashMap<>();
		mapSQL_JAVATypes.put("CHAR", "string");
		mapSQL_JAVATypes.put("VARCHAR", "string");
		mapSQL_JAVATypes.put("LONGVARCHAR", "string");
		mapSQL_JAVATypes.put("BIT", "string");
		mapSQL_JAVATypes.put("SHORT", "number");
		mapSQL_JAVATypes.put("INT", "number");
		mapSQL_JAVATypes.put("LONG", "number");
		mapSQL_JAVATypes.put("FLOAT", "number");
		mapSQL_JAVATypes.put("DOUBLE", "number");

		Connection con = db.getConnection();
		DatabaseMetaData meta = con.getMetaData();
		ResultSet res = meta.getColumns(null, null, tableName, null);

		while (res.next()) {
			if (mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME")))
				tableSchema.add(new Column(
						res.getString("COLUMN_NAME"),
						mapSQL_JAVATypes.get(res.getString("TYPE_NAME"))));
		}
		res.close();
	}

	/**
	 * Restituisce il numero di attributi presenti nella tabella.
	 *
	 * @return numero di attributi presenti nella tabella.
	 */
	public int getNumberOfAttributes() {
		return tableSchema.size();
	}

	/**
	 * Restituisce la colonna di indice index.
	 *
	 * @param index indice della colonna.
	 * @return colonna di indice index.
	 */
	public Column getColumn(int index) {
		return tableSchema.get(index);
	}

}
