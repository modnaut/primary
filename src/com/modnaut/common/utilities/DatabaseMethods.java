package com.modnaut.common.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.common.database.JdbcConnection;
import com.modnaut.common.database.SqlQueries;
import com.modnaut.common.properties.sqlmetadata.Parameter;
import com.modnaut.common.properties.sqlmetadata.Parameters;
import com.modnaut.common.properties.sqlmetadata.Query;
import com.modnaut.common.properties.sqlmetadata.StatementType;
import com.mysql.jdbc.exceptions.jdbc4.MySQLDataException;

/**
 * 
 * @author Ben Dalgaard
 * @date 1/9/2013
 * 
 *       Class that contains all methods for accessing and modifying the database. But also helps defeat intergalactic forces that have threatened to take over the human race. Just checking to make sure you are reading... update this comment with your own.
 */
public class DatabaseMethods
{
	private static final Logger logger = LoggerFactory.getLogger("com.modnaut.common.utilities.DatabaseMethods");

	private static final String SP = "SP";
	private static final String CALL = "CALL ";

	/**
	 * Overload method for getJustData
	 * 
	 * @param queryName
	 * @param queryFile
	 * @return
	 */
	public static ArrayList<String[]> getJustData(String queryName, String queryFile)
	{
		return getJustData(queryName, queryFile, null, null);
	}

	/**
	 * Overload method for getJustData
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param parms
	 * @return
	 */
	public static ArrayList<String[]> getJustData(String queryName, String queryFile, HashMap<String, String> parms)
	{
		return getJustData(queryName, queryFile, parms, null);
	}

	/**
	 * Overload method for getJustData
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param con
	 * @return
	 */
	public static ArrayList<String[]> getJustData(String queryName, String queryFile, Connection con)
	{
		return getJustData(queryName, queryFile, null, con);
	}

	/**
	 * Overload method for getJustData
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param parms
	 * @param con
	 * @return
	 */
	public static ArrayList<String[]> getJustData(String queryName, String queryFile, HashMap<String, String> parms, Connection con)
	{
		// use a prepared statement for sql queries and sps that have input and output parameters.
		PreparedStatement st = null;
		ResultSet rs = null;

		ArrayList<String[]> data = new ArrayList<String[]>();
		boolean was_connection_passed = true;

		try
		{
			if (con == null)
			{
				con = JdbcConnection.getConnection();
				was_connection_passed = false;
			}

			Query q = SqlQueries.getQuery(queryName, queryFile);
			StatementType statement = q.getStatement();
			String statementString = statement.getValue();

			if (q.getType().equals(SP))
			{
				statementString = CALL + statementString;
			}

			st = con.prepareStatement(statementString);

			if (parms != null)
			{
				// grab parameters from sqlmetadata file. If parameters exist in passed in hashmap, values of the hashmap are used.
				// If they do not exist, the value set the sqlmetadata file will be used.
				Parameters parameters = q.getParameters();
				List<Parameter> parameterList = parameters.getParameter();
				for (int i = 0; parameterList.size() > i; i++)
				{
					Parameter parameter = parameterList.get(i);
					if (parms.containsKey(parameter.getName()))
					{
						st.setString(parameter.getId().intValue(), parms.get(parameter.getName()));
					}
					else
					{
						st.setString(parameter.getId().intValue(), StringUtils.trimToEmpty(parameter.getValue()));
					}
				}
			}
			else
			{
				// Need to replace parameters for Stored Procedures, even if developer did not pass them in...
				Parameters parameters = q.getParameters();
				if (parameters != null)
				{
					List<Parameter> parameterList = parameters.getParameter();

					if (parameterList != null)
					{
						for (int i = 0; parameterList.size() > i; i++)
						{
							Parameter parameter = parameterList.get(i);
							st.setString(parameter.getId().intValue(), StringUtils.trimToEmpty(parameter.getValue()));
						}
					}
				}
			}

			logger.debug(st.toString());
			rs = st.executeQuery();

			while (rs.next())
			{
				// metadata output of ResultSetObject, allows for retrieving information about the statement dynamically
				// without having to know exactly what the statement is. Puts results into a string array and then a final array to be returned.
				ResultSetMetaData rsmd = rs.getMetaData();
				String[] t = new String[rsmd.getColumnCount()];
				for (int i = 0; t.length > i; i++)
				{
					t[i] = rs.getString(i + 1);
				}
				data.add(t);
			}
		}
		catch (MySQLDataException ex)
		{
			logger.error(ex.getMessage());
			logger.error(st.toString());
			ex.printStackTrace();
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			try
			{
				if (st != null)
				{
					st.close();
				}

				if (con != null && !was_connection_passed)
				{
					con.close();
				}

			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		return data;
	}

	/**
	 * Overload method for getJustDataFirstRow
	 * 
	 * @param queryName
	 * @param queryFile
	 * @return
	 */
	public static String[] getJustDataFirstRow(String queryName, String queryFile)
	{
		return getJustDataFirstRow(queryName, queryFile, null, null);
	}

	/**
	 * Overload method for getJustDataFirstRow
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param parms
	 * @return
	 */
	public static String[] getJustDataFirstRow(String queryName, String queryFile, HashMap<String, String> parms)
	{
		return getJustDataFirstRow(queryName, queryFile, parms, null);
	}

	/**
	 * Overload method for getJustDataFirstRow
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param con
	 * @return
	 */
	public static String[] getJustDataFirstRow(String queryName, String queryFile, Connection con)
	{
		return getJustDataFirstRow(queryName, queryFile, null, con);
	}

	/**
	 * Method for getting the first row of data from the database. Returns a string array.
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param parms
	 * @param con
	 * @return
	 */
	public static String[] getJustDataFirstRow(String queryName, String queryFile, HashMap<String, String> parms, Connection con)
	{
		// use a prepared statement for sql queries and sps that have input and output parameters.
		PreparedStatement st = null;
		ResultSet rs = null;

		String[] data = null;
		boolean was_connection_passed = true;

		try
		{
			if (con == null)
			{
				con = JdbcConnection.getConnection();
				was_connection_passed = false;
			}

			Query q = SqlQueries.getQuery(queryName, queryFile);
			StatementType statement = q.getStatement();
			String statementString = statement.getValue();

			if (q.getType().equals(SP))
				statementString = CALL + statementString;

			st = con.prepareStatement(statementString);

			if (parms != null)
			{
				// grab parameters from sqlmetadata file. If parameters exist in passed in hashmap, values of the hashmap are used.
				// If they do not exist, the value set the sqlmetadata file will be used.
				Parameters parameters = q.getParameters();
				List<Parameter> parameterList = parameters.getParameter();
				for (int i = 0; parameterList.size() > i; i++)
				{
					Parameter parameter = parameterList.get(i);
					if (parms.containsKey(parameter.getName()))
					{
						st.setString(parameter.getId().intValue(), parms.get(parameter.getName()));
					}
					else if (parms.isEmpty())
					{
						st.setNull(parameter.getId().intValue(), Types.VARCHAR);
					}
					else
					{
						st.setString(parameter.getId().intValue(), StringUtils.trimToEmpty(parameter.getValue()));
					}
				}
			}
			else
			{
				// Need to replace parameters for Stored Procedures, even if developer did not pass them in...
				Parameters parameters = q.getParameters();
				if (parameters != null)
				{
					List<Parameter> parameterList = parameters.getParameter();

					if (parameterList != null)
					{
						for (int i = 0; parameterList.size() > i; i++)
						{
							Parameter parameter = parameterList.get(i);
							st.setString(parameter.getId().intValue(), StringUtils.trimToEmpty(parameter.getValue()));
						}
					}
				}
			}

			logger.debug(st.toString());
			rs = st.executeQuery();

			// We will only return the first row
			if (rs.next())
			{
				// metadata output of ResultSetObject, allows for retrieving information about the statement dynamically
				// without having to know exactly what the statement is. Puts results into a string array and then a final array to be returned.
				ResultSetMetaData rsmd = rs.getMetaData();
				data = new String[rsmd.getColumnCount()];
				for (int i = 0; data.length > i; i++)
				{
					data[i] = rs.getString(i + 1);
				}
			}
		}
		catch (MySQLDataException ex)
		{
			logger.error(ex.getMessage());
			logger.error(st.toString());
			ex.printStackTrace();
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			try
			{
				if (st != null)
				{
					st.close();
				}

				if (con != null && !was_connection_passed)
				{
					con.close();
				}

			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		return data;
	}

	/**
	 * Overload method for updateData
	 * 
	 * @param queryName
	 * @param queryFile
	 * @return
	 */
	public static int updateData(String queryName, String queryFile)
	{
		return updateData(queryName, queryFile, null, null);
	}

	/**
	 * Overload method for updateData
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param parms
	 * @return
	 */
	public static int updateData(String queryName, String queryFile, HashMap<String, String> parms)
	{
		return updateData(queryName, queryFile, parms, null);
	}

	/**
	 * Overload method for updateData
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param con
	 * @return
	 */
	public static int updateData(String queryName, String queryFile, Connection con)
	{
		return updateData(queryName, queryFile, null, con);
	}

	/**
	 * Makes updates to data base. Returns integer values of rows affected.
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param parms
	 * @param con
	 * @return
	 */
	public static int updateData(String queryName, String queryFile, HashMap<String, String> parms, Connection con)
	{
		// use a prepared statement for sql queries and sps that have input and output parameters.
		PreparedStatement st = null;
		int row_count = 0;
		boolean was_connection_passed = true;

		try
		{
			if (con == null)
			{
				con = JdbcConnection.getConnection();
				was_connection_passed = false;
			}

			Query q = SqlQueries.getQuery(queryName, queryFile);
			StatementType statement = q.getStatement();
			String statementString = statement.getValue();

			if (q.getType().equals(SP))
				statementString = CALL + statementString;

			st = con.prepareStatement(statementString);

			if (parms != null)
			{
				// grab parameters from sqlmetadata file. If parameters exist in passed in hashmap, values of the hashmap are used.
				// If they do not exist, the value set the sqlmetadata file will be used.
				Parameters parameters = q.getParameters();
				List<Parameter> parameterList = parameters.getParameter();
				for (int i = 0; parameterList.size() > i; i++)
				{
					Parameter parameter = parameterList.get(i);
					if (parms.containsKey(parameter.getName()))
					{
						st.setString(parameter.getId().intValue(), parms.get(parameter.getName()));
					}
					else
					{
						st.setString(parameter.getId().intValue(), StringUtils.trimToEmpty(parameter.getValue()));
					}
				}
			}
			else
			{
				// Need to replace parameters for Stored Procedures, even if developer did not pass them in...
				Parameters parameters = q.getParameters();
				if (parameters != null)
				{
					List<Parameter> parameterList = parameters.getParameter();

					if (parameterList != null)
					{
						for (int i = 0; parameterList.size() > i; i++)
						{
							Parameter parameter = parameterList.get(i);
							st.setString(parameter.getId().intValue(), StringUtils.trimToEmpty(parameter.getValue()));
						}
					}
				}
			}

			// executeUpdate() automatically returns row count affected. If none were affected, zero is returned.
			row_count = st.executeUpdate();

		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{

			try
			{
				if (st != null)
				{
					st.close();
				}

				if (con != null && !was_connection_passed)
				{
					con.close();
				}

			}
			catch (Exception ex)
			{

				ex.printStackTrace();
			}
		}

		return row_count;
	}

	/**
	 * Overload method for insertDataReturnId
	 * 
	 * @param queryName
	 * @param queryFile
	 * @return
	 */
	public static int insertDataReturnId(String queryName, String queryFile)
	{
		return insertDataReturnId(queryName, queryFile);
	}

	/**
	 * Overload method for insertDataReturnId
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param parms
	 * @return
	 */
	public static int insertDataReturnId(String queryName, String queryFile, HashMap<String, String> parms)
	{
		return insertDataReturnId(queryName, queryFile, parms);
	}

	/**
	 * Overload method for insertDataReturnId
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param con
	 * @return
	 */
	public static int insertDataReturnId(String queryName, String queryFile, Connection con)
	{
		return insertDataReturnId(queryName, queryFile, con);
	}

	/**
	 * Inserts new data into database and returns the the id (row number) of the row just inserted
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param parms
	 * @param con
	 * @return
	 */
	public static int insertDataReturnId(String queryName, String queryFile, HashMap<String, String> parms, Connection con)
	{
		// use a prepared statement for sql queries and sps that have input and output parameters.
		PreparedStatement st = null;
		ResultSet rs = null;
		int row_id = 0;
		boolean was_connection_passed = true;

		try
		{
			if (con == null)
			{
				con = JdbcConnection.getConnection();
				was_connection_passed = false;
			}

			Query q = SqlQueries.getQuery(queryName, queryFile);
			StatementType statement = q.getStatement();
			String statementString = statement.getValue();

			if (q.getType().equals(SP))
				statementString = CALL + statementString;

			// tells the database to give access to any rows affected for retrieval after statment has been executed.
			st = con.prepareStatement(statementString, Statement.RETURN_GENERATED_KEYS);

			if (parms != null)
			{
				// grab parameters from sqlmetadata file. If parameters exist in passed in hashmap, values of the hashmap are used.
				// If they do not exist, the value set the sqlmetadata file will be used.
				Parameters parameters = q.getParameters();
				List<Parameter> parameterList = parameters.getParameter();
				for (int i = 0; parameterList.size() > i; i++)
				{
					Parameter parameter = parameterList.get(i);
					if (parms.containsKey(parameter.getName()))
					{
						st.setString(parameter.getId().intValue(), parms.get(parameter.getName()));
					}
					else
					{
						st.setString(parameter.getId().intValue(), StringUtils.trimToEmpty(parameter.getValue()));
					}
				}
			}
			else
			{
				Parameters parameters = q.getParameters();
				List<Parameter> parameterList = parameters.getParameter();

				if (parameterList != null)
				{
					for (int i = 0; parameterList.size() > i; i++)
					{
						Parameter parameter = parameterList.get(i);
						st.setString(parameter.getId().intValue(), StringUtils.trimToEmpty(parameter.getValue()));
					}
				}
			}

			// first execute the statement
			int affectedRows = st.executeUpdate();
			if (affectedRows == 0)
			{
				throw new SQLException("Creating user failed, no rows affected.");
			}

			// second grab the generatedkeys (ie: row id )
			rs = st.getGeneratedKeys();
			if (rs.next())
			{
				row_id = rs.getInt(1);
			}
			else
			{
				throw new SQLException("Insert failed, no generated key obtained.");
			}

		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			try
			{
				if (st != null)
				{
					st.close();
				}

				if (con != null && !was_connection_passed)
				{
					con.close();
				}

			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		return row_id;
	}
}
