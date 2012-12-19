package com.modnaut.common.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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
		//without having to know exactly what the statement is.  Puts results into a string array and then a final array to be returned.
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
	

    public static ArrayList<String[]> getJustData(String queryName, HashMap<String, String> parms, Connection con, String queryFile) {
	    
	//use a prepared statement for sql queries and sps that have input and output parameters.
	PreparedStatement st = null;
	ResultSet rs = null;

	ArrayList<String[]> data = new ArrayList<String[]>();
	    
	try {
	
	    if (con == null)
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
		    st.setString(parameter.getId().intValue(), parms.get(parameter.getName()));
		}
		else {
		    st.setString(parameter.getId().intValue(), StringUtils.trimToEmpty(parameter.getValue()));
		}
	    }
	
	    rs = st.executeQuery();
		    	
	    while (rs.next()) {
		    
		//metadata output of ResultSetObject, allows for retrieving information about the statement dynamically 
		//without having to know exactly what the statement is.  Puts results into a string array and then a final array to be returned.
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
	
	
    public static int updateData(String queryName, HashMap<String, String> parms, Connection con, String queryFile) {
	   
	//use a prepared statement for sql queries and sps that have input and output parameters.
	PreparedStatement st = null;
	int row_count = 0;
	    
	try {
		
	    if (con == null)
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
		    st.setString(parameter.getId().intValue(), parms.get(parameter.getName()));
		}
		else {
		    st.setString(parameter.getId().intValue(), StringUtils.trimToEmpty(parameter.getValue()));
		}
	    }

	    //executeUpdate() automatically returns row count affected.  If none were affected, zero is returned.
	    row_count = st.executeUpdate();

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
     
	return row_count;
    }
	
	
    public static int insertDataReturnId(String queryName, HashMap<String, String> parms, Connection con, String queryFile) {
	    
	//use a prepared statement for sql queries and sps that have input and output parameters.
	PreparedStatement st = null;
	ResultSet rs = null;
	int row_id = 0;
	    
	try {
		
	    if (con == null)
		con = JdbcConnection.getConnection();
		
	    Query q = SqlQueries.getQuery(queryName, queryFile);
	    StatementType statement = q.getStatement();
	    String statementString = statement.getValue();
			
	    if (q.getType().equals(SP))
		statementString = CALL + statementString;

	    //tells the database to give access to any rows affected for retrieval after statment has been executed.
	    st = con.prepareStatement(statementString, Statement.RETURN_GENERATED_KEYS);
	
	    //grab parameters from sqlmetadata file.  If parameters exist in passed in hashmap, values of the hashmap are used.  
	    //If they do not exist, the value set the sqlmetadata file will be used.
	    Parameters parameters = q.getParameters();
	    List parameterList = parameters.getParameter();
	    for (int i = 0; parameterList.size() > i; i++)
	    {
		Parameter parameter = (Parameter) parameterList.get(i);
		if (parms.containsKey(parameter.getName())) {
		    st.setString(parameter.getId().intValue(), parms.get(parameter.getName()));
		}
		else {
		    st.setString(parameter.getId().intValue(), StringUtils.trimToEmpty(parameter.getValue()));
		}
	    }

	    //first execute the statement
	    int affectedRows = st.executeUpdate();
	    if (affectedRows == 0) {
		throw new SQLException("Creating user failed, no rows affected.");
	    }

	    //second grab the generatedkeys (ie: row id )
	    rs = st.getGeneratedKeys();
	    if (rs.next()) {
		row_id = rs.getInt(1);
	    } else {
		throw new SQLException("Insert failed, no generated key obtained.");
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
 
	return row_id;	    
    }
}
