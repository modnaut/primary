package com.modnaut.common.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.modnaut.common.database.JdbcConnection;
import com.modnaut.common.database.SqlQueries;
import com.modnaut.common.properties.sqlMetaData.Query;
import com.modnaut.common.properties.sqlMetaData.StatementType;

public class UtilityDatabaseMethods {
    
	private static final String SP = "SP";
	private static final String CALL = "CALL ";
	
	public static ArrayList<String[]> getJustData(String queryName, String queryFile) {

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		ArrayList<String[]> data = new ArrayList<String[]>();

		try {
			con = JdbcConnection.getConnection();
			st = con.createStatement();

			Query q = SqlQueries.getQuery(queryName, queryFile);

			StatementType statement = q.getStatement();

			if (q.getType().equals(SP))
				rs = st.executeQuery(CALL + statement.getValue());
			else
				rs = st.executeQuery(statement.getValue());

			while (rs.next()) {
				String[] t = {rs.getString(2), rs.getString(5)};
				data.add(t);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return data;
	}
	

	public static ArrayList<String[]> getJustData(String queryName, HashMap<String, String> parms, String queryFile) {

		Connection con = JdbcConnection.getConnection();
		
		PreparedStatement st = null;
		ResultSet rs = null;

		ArrayList<String[]> data = new ArrayList<String[]>();


		try {
			
		    	Query q = SqlQueries.getQuery(queryName, queryFile);
		    	StatementType statement = q.getStatement();

		    	if (q.getType().equals(SP)) 
		    	    st = con.prepareStatement(CALL + statement.getValue());
			else
			    st = con.prepareStatement(statement.getValue());
 
		    	
		    	Iterator it = parms.entrySet().iterator();
		    	while (it.hasNext()) {

		    	    Map.Entry entry = (Map.Entry) it.next();
		    	    String key =  (String)entry.getKey();
		    	    
		    	   		    	    
		    	    String value = (String)entry.getValue();
		    	    
		    	    st.setString(1, value);
		    	}

		    	rs = st.executeQuery();
		    	
		    	while (rs.next()) {
		    	    	
				String[] t = {rs.getString(1), rs.getString(2)};
				data.add(t);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		return data;
	}

}
