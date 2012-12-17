package com.modnaut.common.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.modnaut.common.database.JdbcConnection;
import com.modnaut.common.database.SqlQueries;
import com.modnaut.common.properties.sqlmetadata.Parameter;
import com.modnaut.common.properties.sqlmetadata.Parameters;
import com.modnaut.common.properties.sqlmetadata.Query;
import com.modnaut.common.properties.sqlmetadata.StatementType;

public class DatabaseMethods {
    
	private static final String SP = "SP";
	private static final String CALL = "CALL ";
	
	public static ArrayList<String[]> getJustData(String queryName, String queryFile) {

	    Connection con = null;
	    Statement st = null;
	    ResultSet rs = null;

	    ArrayList<String[]> data = new ArrayList<String[]>();
	    
	    try {
	    
		con = JdbcConnection.getConnection();
		
		//use statement type object when using static sql with no parameters
		st = con.createStatement();

		Query q = SqlQueries.getQuery(queryName, queryFile);

		StatementType statement = q.getStatement();
		String statementString = statement.getValue();
		
		//check to see if statement is a stored procedure.  This requires additional handling.
		if (q.getType().equals(SP))
		    statementString = CALL + statementString;

		rs = st.executeQuery(statementString);

		while (rs.next()) {
		    
		    //metadata output of ResultSetObject, allows for retrieving information about the statement dynamically 
		    //without have to know exactly what the statement is.  Puts results into a string array and then a final array to be returned.
		    ResultSetMetaData rsmd = rs.getMetaData();
		    String[] t = new String[rsmd.getColumnCount()];
		    for (int i = 0; t.length > i; i++)
			t[i] = rs.getString(i + 1);

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

	    Connection con = null;
	    
	    //use a prepared statement for sql queries and sps that have input and output parameters.
	    PreparedStatement st = null;
	    ResultSet rs = null;

	    ArrayList<String[]> data = new ArrayList<String[]>();
	    
	    try {
		
		con = JdbcConnection.getConnection();
		
		Query q = SqlQueries.getQuery(queryName, queryFile);
		StatementType statement = q.getStatement();
		String statementString = statement.getValue();
			
		if (q.getType().equals(SP))
			statementString = CALL + statementString;

		st = con.prepareStatement(statementString);
	
		//grab parameters from sqlmetadata file.  If parameters exist in passed in hashmap, values of the hashmap are used.  
		//If they do not exist, the value set the sqlmetadata file will be used.
		Parameters parameters = q.getParameters();
		List parameterList = parameters.getParameter();
		for (int i = 0; parameterList.size() > i; i++)
		{
		    Parameter parameter = (Parameter) parameterList.get(i);
		    if (parms.containsKey(parameter.getName())) {
			st.setString(i + 1, parms.get(parameter.getName()));
		    }
		    else {
			st.setString(parameter.getId().intValue(), StringUtils.trimToEmpty(parameter.getValue()));
		    }
		}
	
		rs = st.executeQuery();
		    	
		while (rs.next()) {
		    
		    //metadata output of ResultSetObject, allows for retrieving information about the statement dynamically 
		    //without have to know exactly what the statement is.  Puts results into a string array and then a final array to be returned.
		    ResultSetMetaData rsmd = rs.getMetaData();
		    String[] t = new String[rsmd.getColumnCount()];
		    for (int i = 0; t.length > i; i++)
			t[i] = rs.getString(i + 1);

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
	
	public static int insertData(String queryName, HashMap<String, String> parms, String queryFile) {
	    return 0;
	}
	
	public static int insertDataReturnId(String queryName, HashMap<String, String> parms, String queryFile) {
	    return 0;
	}
	
	public static int updateData(String queryName, HashMap<String, String> parms, String queryFile) {
	    return 0;
	}

}
