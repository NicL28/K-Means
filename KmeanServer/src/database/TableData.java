package database;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import java.util.ArrayList;
import java.util.HashSet;


import java.util.List;
import java.util.Set;



import database.TableSchema.Column;;


public class TableData {

	DbAccess db;

	
	
	public TableData(DbAccess db) {
		this.db=db;
	}

	public List<Example> getDistinctTransazioni(String table) throws SQLException, EmptySetException{
		TableSchema t= new TableSchema(db, table);
		List<Example> DistinctTransazioni= new ArrayList<>();
		Connection con=db.getConnection();
		Statement st=con.createStatement();
	    ResultSet res = st.executeQuery("SELECT DISTINCT * FROM " + table);
		System.out.println(res.getFetchSize());
		if(res.next()){
			throw new EmptySetException();
		}
		
		do{
			Example e = new Example();
			for (int i = 0; i < t.getNumberOfAttributes(); i++) {
				if (t.getColumn(i).isNumber()){
					e.add(res.getFloat(t.getColumn(i).getColumnName()));
				}else{
					e.add(res.getString(t.getColumn(i).getColumnName()));
				}
			}
			DistinctTransazioni.add(e);
		}while(res.next());

	return DistinctTransazioni;
	}

	
	public  Set<Object>getDistinctColumnValues(String table,Column column) throws SQLException{
		Connection con = db.getConnection();
		Statement st = con.createStatement();
		ResultSet res = st.executeQuery(new String("SELECT " + column + " FROM " + table + " ORDER BY " + column + " ASC;"));
		Set<Object> DistinctColumnValues = new HashSet<>();
		if (column.isNumber()){
			while(res.next()){
				DistinctColumnValues.add(res.getFloat(column.getColumnName()));
			}
		}else{
			while(res.next()){
				DistinctColumnValues.add(res.getString(column.getColumnName()));
			}
		}
		return DistinctColumnValues;
	}
	// controllare se AggregatoCercato è accettato da getFloat/String
	public  Object getAggregateColumnValue(String table,Column column,QUERY_TYPE aggregate) throws SQLException,NoValueException{
		Connection con = db.getConnection();
		Statement st = con.createStatement();
		ResultSet res = st.executeQuery(new String("SELECT " + aggregate +"("+ column.getColumnName()+ ") AS AggregatoCercato " + "FROM " +  table));
		if (res.next()){

			if(res.equals(null)){
				throw new NoValueException();
			
			}else{
				if (column.isNumber()){
				return res.getFloat("AggregatoCercato");
				} else {
				return res.getString("AggregatoCercato");
				}
			}
		}else{
			throw new NoValueException();
		}
	}
}
