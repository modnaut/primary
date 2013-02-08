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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.common.database.JdbcConnection;
import com.modnaut.common.database.SqlQueries;
import com.modnaut.common.properties.sqlmetadata.Parameter;
import com.modnaut.common.properties.sqlmetadata.ParameterType;
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
		return getData(queryName, queryFile, null, null, false);
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
		return getData(queryName, queryFile, parms, null, false);
	}

	/**
	 * Overload method for getJustData
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param con
	 * @return
	 */
	public static ArrayList<String[]> getJustData(String queryName, String queryFile, Connection connection)
	{
		return getData(queryName, queryFile, null, connection, false);
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
	public static String[] getJustDataFirstRow(String queryName, String queryFile, Connection connection)
	{
		return getJustDataFirstRow(queryName, queryFile, null, connection);
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
	public static String[] getJustDataFirstRow(String queryName, String queryFile, HashMap<String, String> parms, Connection connection)
	{
		// use a prepared statement for sql queries and sps that have input and output parameters.
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		String[] data = null;
		boolean was_connection_passed = true;

		try
		{
			if (connection == null)
			{
				connection = JdbcConnection.getConnection();
				was_connection_passed = false;
			}

			preparedStatement = initializeQuery(queryName, queryFile, preparedStatement, parms, connection, false);

			logger.debug(preparedStatement.toString());
			resultSet = preparedStatement.executeQuery();

			// We will only return the first row
			if (resultSet.next())
			{
				// metadata output of ResultSetObject, allows for retrieving information about the statement dynamically
				// without having to know exactly what the statement is. Puts results into a string array and then a final array to be returned.
				ResultSetMetaData rsmd = resultSet.getMetaData();
				data = new String[rsmd.getColumnCount()];
				for (int i = 0; data.length > i; i++)
				{
					data[i] = resultSet.getString(i + 1);
				}
			}
		}
		catch (MySQLDataException ex)
		{
			logger.error(ex.getMessage());
			logger.error(preparedStatement.toString());
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
				if (preparedStatement != null)
					preparedStatement.close();

				if (connection != null && !was_connection_passed)
					connection.close();
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
	public static int updateData(String queryName, String queryFile, Connection connection)
	{
		return updateData(queryName, queryFile, null, connection);
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
	public static int updateData(String queryName, String queryFile, HashMap<String, String> parms, Connection connection)
	{
		// use a prepared statement for sql queries and sps that have input and output parameters.
		PreparedStatement preparedStatement = null;
		int row_count = 0;
		boolean was_connection_passed = true;

		try
		{
			if (connection == null)
			{
				connection = JdbcConnection.getConnection();
				was_connection_passed = false;
			}

			preparedStatement = initializeQuery(queryName, queryFile, preparedStatement, parms, connection, false);

			// executeUpdate() automatically returns row count affected. If none were affected, zero is returned.
			row_count = preparedStatement.executeUpdate();
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
				if (preparedStatement != null)
					preparedStatement.close();

				if (connection != null && !was_connection_passed)
					connection.close();
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
	public static int insertDataReturnId(String queryName, String queryFile, Connection connection)
	{
		return insertDataReturnId(queryName, queryFile, connection);
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
	public static int insertDataReturnId(String queryName, String queryFile, HashMap<String, String> parms, Connection connection)
	{
		// use a prepared statement for sql queries and sps that have input and output parameters.
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int row_id = 0;
		boolean was_connection_passed = true;

		try
		{
			if (connection == null)
			{
				connection = JdbcConnection.getConnection();
				was_connection_passed = false;
			}

			preparedStatement = initializeQuery(queryName, queryFile, preparedStatement, parms, connection, true);

			// first execute the statement
			int affectedRows = preparedStatement.executeUpdate();
			if (affectedRows == 0)
			{
				throw new SQLException("Creating user failed, no rows affected.");
			}

			// second grab the generatedkeys (ie: row id )
			resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet.next())
			{
				row_id = resultSet.getInt(1);
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
				if (preparedStatement != null)
					preparedStatement.close();

				if (connection != null && !was_connection_passed)
					connection.close();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		return row_id;
	}

	/**
	 * Overload method for getData
	 * 
	 * @param queryName
	 * @param queryFile
	 * @return
	 */
	public static ArrayList<String[]> getData(String queryName, String queryFile)
	{
		return getData(queryName, queryFile, null, null, true);
	}

	/**
	 * Overload method for getData
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param parms
	 * @return
	 */
	public static ArrayList<String[]> getData(String queryName, String queryFile, HashMap<String, String> parms)
	{
		return getData(queryName, queryFile, parms, null, true);
	}

	/**
	 * Overload method for getData
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param con
	 * @return
	 */
	public static ArrayList<String[]> getData(String queryName, String queryFile, Connection connection)
	{
		return getData(queryName, queryFile, null, connection, true);
	}

	/**
	 * Method for retrieving data (w/column names) from database.
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param parms
	 * @param connection
	 * @param include_column_names
	 * @return
	 */
	public static ArrayList<String[]> getData(String queryName, String queryFile, HashMap<String, String> parms, Connection connection, boolean include_column_names)
	{
		// use a prepared statement for sql queries and sps that have input and output parameters.
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		ArrayList<String[]> data = new ArrayList<String[]>();
		boolean was_connection_passed = true;

		try
		{
			if (connection == null)
			{
				connection = JdbcConnection.getConnection();
				was_connection_passed = false;
			}

			preparedStatement = initializeQuery(queryName, queryFile, preparedStatement, parms, connection, false);

			logger.debug(preparedStatement.toString());
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
			{
				// metadata output of ResultSetObject, allows for retrieving information about the statement dynamically
				// without having to know exactly what the statement is. Puts results into a string array and then a final array to be returned.
				ResultSetMetaData rsmd = resultSet.getMetaData();
				String[] dataRow = new String[rsmd.getColumnCount()];

				if (include_column_names == true)
				{
					String[] columnRow = new String[rsmd.getColumnCount()];
					for (int i = 0; columnRow.length > i; i++)
					{
						columnRow[i] = rsmd.getColumnLabel(i + 1);
					}
					data.add(columnRow);

					include_column_names = false;
				}

				for (int i = 0; dataRow.length > i; i++)
				{
					dataRow[i] = resultSet.getString(i + 1);
				}
				data.add(dataRow);
			}
		}
		catch (MySQLDataException ex)
		{
			logger.error(ex.getMessage());
			logger.error(preparedStatement.toString());
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
				if (preparedStatement != null)
					preparedStatement.close();

				if (connection != null && !was_connection_passed)
					connection.close();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		return data;
	}

	/**
	 * Overload method for getColumnNames
	 * 
	 * @param queryName
	 * @param queryFile
	 * @return
	 */
	public static ArrayList<String[]> getColumnNames(String queryName, String queryFile)
	{
		return getColumnNames(queryName, queryFile, null, null);
	}

	/**
	 * Overload method for getColumnNames
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param parms
	 * @return
	 */
	public static ArrayList<String[]> getColumnNames(String queryName, String queryFile, HashMap<String, String> parms)
	{
		return getColumnNames(queryName, queryFile, parms, null);
	}

	/**
	 * Overload method for getColumnNames
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param con
	 * @return
	 */
	public static ArrayList<String[]> getColumnNames(String queryName, String queryFile, Connection connection)
	{
		return getColumnNames(queryName, queryFile, null, connection);
	}

	/**
	 * Method for retrieving column names of table from database.
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param parms
	 * @param con
	 * @return
	 */
	public static ArrayList<String[]> getColumnNames(String queryName, String queryFile, HashMap<String, String> parms, Connection connection)
	{
		// use a prepared statement for sql queries and sps that have input and output parameters.
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		ArrayList<String[]> data = new ArrayList<String[]>();
		boolean was_connection_passed = true;

		try
		{
			if (connection == null)
			{
				connection = JdbcConnection.getConnection();
				was_connection_passed = false;
			}

			preparedStatement = initializeQuery(queryName, queryFile, preparedStatement, parms, connection, false);

			logger.debug(preparedStatement.toString());
			resultSet = preparedStatement.executeQuery();

			// We will only return the first row
			if (resultSet.next())
			{
				// metadata output of ResultSetObject, allows for retrieving information about the statement dynamically
				// without having to know exactly what the statement is. Puts results into a string array and then a final array to be returned.
				ResultSetMetaData rsmd = resultSet.getMetaData();
				String[] columnRow = new String[rsmd.getColumnCount()];

				for (int i = 0; columnRow.length > i; i++)
				{
					columnRow[i] = rsmd.getColumnName(i + 1);
				}
				data.add(columnRow);
			}
		}
		catch (MySQLDataException ex)
		{
			logger.error(ex.getMessage());
			logger.error(preparedStatement.toString());
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
				if (preparedStatement != null)
					preparedStatement.close();

				if (connection != null && !was_connection_passed)
					connection.close();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		return data;
	}

	/**
	 * Overload method for getDataObjects
	 * 
	 * @param queryName
	 * @param queryFile
	 * @return
	 */
	public static ArrayList<Object[]> getDataObjects(String queryName, String queryFile)
	{
		return getDataObjects(queryName, queryFile, null, null, true);
	}

	/**
	 * Overload method for getDataObjects
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param parms
	 * @return
	 */
	public static ArrayList<Object[]> getDataObjects(String queryName, String queryFile, HashMap<String, String> parms)
	{
		return getDataObjects(queryName, queryFile, parms, null, true);
	}

	/**
	 * Overload method for getDataObjects
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param con
	 * @return
	 */
	public static ArrayList<Object[]> getDataObjects(String queryName, String queryFile, Connection connection)
	{
		return getDataObjects(queryName, queryFile, null, connection, true);
	}

	/**
	 * Overload method for getJustDataObjects
	 * 
	 * @param queryName
	 * @param queryFile
	 * @return
	 */
	public static ArrayList<Object[]> getJustDataObjects(String queryName, String queryFile)
	{
		return getDataObjects(queryName, queryFile, null, null, false);
	}

	/**
	 * Overload method for getJustDataObjects
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param parms
	 * @return
	 */
	public static ArrayList<Object[]> getJustDataObjects(String queryName, String queryFile, HashMap<String, String> parms)
	{
		return getDataObjects(queryName, queryFile, parms, null, false);
	}

	/**
	 * Overload method for getJustDataObjects
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param con
	 * @return
	 */
	public static ArrayList<Object[]> getJustDataObjects(String queryName, String queryFile, Connection connection)
	{
		return getDataObjects(queryName, queryFile, null, connection, false);
	}

	/**
	 * Method for retrieving data as objects from database.
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param parms
	 * @param connection
	 * @param include_column_names
	 * @return
	 */
	public static ArrayList<Object[]> getDataObjects(String queryName, String queryFile, HashMap<String, String> parms, Connection connection, boolean include_column_names)
	{
		// use a prepared statement for sql queries and sps that have input and output parameters.
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		ArrayList<Object[]> data = new ArrayList<Object[]>();
		boolean was_connection_passed = true;

		try
		{
			if (connection == null)
			{
				connection = JdbcConnection.getConnection();
				was_connection_passed = false;
			}

			preparedStatement = initializeQuery(queryName, queryFile, preparedStatement, parms, connection, false);

			logger.debug(preparedStatement.toString());
			resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
			{
				// metadata output of ResultSetObject, allows for retrieving information about the statement dynamically
				// without having to know exactly what the statement is. Puts results into a string array and then a final array to be returned.
				ResultSetMetaData rsmd = resultSet.getMetaData();
				Object[] dataRow = new Object[rsmd.getColumnCount()];

				if (include_column_names == true)
				{
					String[] columnRow = new String[rsmd.getColumnCount()];
					for (int i = 0; columnRow.length > i; i++)
					{
						columnRow[i] = rsmd.getColumnLabel(i + 1);
					}
					data.add(columnRow);

					include_column_names = false;
				}

				for (int i = 0; dataRow.length > i; i++)
				{
					dataRow[i] = resultSet.getObject(i + 1);
				}

				data.add(dataRow);
			}
		}
		catch (MySQLDataException ex)
		{
			logger.error(ex.getMessage());
			logger.error(preparedStatement.toString());
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
				if (preparedStatement != null)
					preparedStatement.close();

				if (connection != null && !was_connection_passed)
					connection.close();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		return data;
	}

	private static PreparedStatement initializeQuery(String queryName, String queryFile, PreparedStatement preparedStatement, HashMap<String, String> parms, Connection connection, boolean auto_generate_keys)
	{
		try
		{
			Query query = SqlQueries.getQuery(queryName, queryFile);
			StatementType statement = query.getStatement();
			String statementString = statement.getValue();

			if (query.getType().value().equals(SP))
				statementString = CALL + statementString;

			// tells the database to give access to any rows affected for retrieval after statment has been executed.
			if (auto_generate_keys == true)
				preparedStatement = connection.prepareStatement(statementString, Statement.RETURN_GENERATED_KEYS);
			else
				preparedStatement = connection.prepareStatement(statementString);

			Parameters parameters = query.getParameters();

			if (parms != null)
			{
				// grab parameters from sqlmetadata file. If parameters exist in passed in hashmap, values of the hashmap are used.
				// If they do not exist, the value set the sqlmetadata file will be used.

				List<Parameter> parameterList = parameters.getParameter();
				for (int i = 0; parameterList.size() > i; i++)
				{
					Parameter parameter = parameterList.get(i);
					ParameterType parameterType = parameter.getType();
					int parameter_type_value = getParameterTypeValue(parameterType.value());

					if (parms.containsKey(parameter.getName()))
					{
						preparedStatement.setObject(parameter.getId().intValue(), parms.get(parameter.getName()), parameter_type_value);
					}
					else
					{
						preparedStatement.setObject(parameter.getId().intValue(), parameter.getValue(), parameter_type_value);
					}
				}
			}
			else
			{
				// Need to replace parameters for Stored Procedures, even if developer did not pass them in...
				if (parameters != null)
				{
					List<Parameter> parameterList = parameters.getParameter();

					if (parameterList != null)
					{
						for (int i = 0; parameterList.size() > i; i++)
						{
							Parameter parameter = parameterList.get(i);
							ParameterType parameterType = parameter.getType();
							int parameter_type_value = getParameterTypeValue(parameterType.value());

							preparedStatement.setObject(parameter.getId().intValue(), parameter.getValue(), parameter_type_value);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return preparedStatement;
	}

	private static int getParameterTypeValue(String parameterTypeString)
	{
		if (parameterTypeString.equals("CHAR"))
			return java.sql.Types.CHAR;

		if (parameterTypeString.equals("DATE"))
			return java.sql.Types.DATE;

		if (parameterTypeString.equals("DECIMAL"))
			return java.sql.Types.DECIMAL;

		if (parameterTypeString.equals("DOUBLE"))
			return java.sql.Types.DOUBLE;

		if (parameterTypeString.equals("FLOAT"))
			return java.sql.Types.FLOAT;

		if (parameterTypeString.equals("INT"))
			return java.sql.Types.INTEGER;

		if (parameterTypeString.equals("VARCHAR"))
			return java.sql.Types.VARCHAR;

		if (parameterTypeString.equals("TIMESTAMP"))
			return java.sql.Types.TIMESTAMP;

		if (parameterTypeString.equals("DATE"))
			return java.sql.Types.DATE;

		if (parameterTypeString.equals("TIME"))
			return java.sql.Types.TIME;

		return 0;
	}
}
