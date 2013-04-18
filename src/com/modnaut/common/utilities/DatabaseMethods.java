package com.modnaut.common.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.framework.database.JdbcConnection;
import com.modnaut.framework.database.SqlQueries;
import com.modnaut.framework.properties.sqlmetadata.Parameter;
import com.modnaut.framework.properties.sqlmetadata.ParameterType;
import com.modnaut.framework.properties.sqlmetadata.Parameters;
import com.modnaut.framework.properties.sqlmetadata.Query;
import com.modnaut.framework.properties.sqlmetadata.StatementType;

/**
 * 
 * @author Ben Dalgaard
 * @date 1/9/2013
 * 
 *       Class that contains all methods for accessing and modifying the database. But also helps defeat intergalactic forces that have threatened to take over the human race. Just checking to make sure you are reading... update this comment with your own. This comment is accurate though.
 */
public class DatabaseMethods
{
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseMethods.class);
	private static final String CLASS_NAME_PATH = DatabaseMethods.class.getCanonicalName();
	private static final String EXECUTE_QUERY_METHOD = "executeQuery";

	private static final String SP = "SP";
	private static final String CALL = "CALL ";

	private static final String REGEX_QUESTION_MARK = "\\?";
	private static final String STRING_FORMAT_PLACEHOLDER = "%s";

	private static enum QUERY_METHOD
	{
		GET_DATA, GET_OBJECTS, GET_MULTIPLE, GET_FIRST_ROW, GET_FIRST_ROW_FIRST_COLUMN, UPDATE, INSERT
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
		return (ArrayList<String[]>) executeQuery(queryName, queryFile, null, null, QUERY_METHOD.GET_DATA, true);
	}

	/**
	 * Overload method for getData
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param parms
	 * @return
	 */
	public static ArrayList<String[]> getData(String queryName, String queryFile, HashMap<String, Object> parms)
	{
		return (ArrayList<String[]>) executeQuery(queryName, queryFile, parms, null, QUERY_METHOD.GET_DATA, true);
	}

	/**
	 * Overload method for getData
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param con
	 * @return
	 */
	public static ArrayList<String[]> getData(String queryName, String queryFile, HashMap<String, Object> parms, Connection connection)
	{
		return (ArrayList<String[]>) executeQuery(queryName, queryFile, parms, connection, QUERY_METHOD.GET_DATA, true);
	}

	/**
	 * Overload method for getJustData
	 * 
	 * @param queryName
	 * @param queryFile
	 * @return
	 */
	public static ArrayList<String[]> getJustData(String queryName, String queryFile)
	{
		return (ArrayList<String[]>) executeQuery(queryName, queryFile, null, null, QUERY_METHOD.GET_DATA, false);
	}

	/**
	 * Overload method for getJustData
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param parms
	 * @return
	 */
	public static ArrayList<String[]> getJustData(String queryName, String queryFile, HashMap<String, Object> parms)
	{
		return (ArrayList<String[]>) executeQuery(queryName, queryFile, parms, null, QUERY_METHOD.GET_DATA, false);
	}

	/**
	 * Overload method for getJustData
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param con
	 * @return
	 */
	public static ArrayList<String[]> getJustData(String queryName, String queryFile, HashMap<String, Object> parms, Connection connection)
	{
		return (ArrayList<String[]>) executeQuery(queryName, queryFile, parms, connection, QUERY_METHOD.GET_DATA, false);
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
		return (String[]) executeQuery(queryName, queryFile, null, null, QUERY_METHOD.GET_FIRST_ROW, false);
	}

	/**
	 * Overload method for getJustDataFirstRow
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param parms
	 * @return
	 */
	public static String[] getJustDataFirstRow(String queryName, String queryFile, HashMap<String, Object> parms)
	{
		return (String[]) executeQuery(queryName, queryFile, parms, null, QUERY_METHOD.GET_FIRST_ROW, false);
	}

	/**
	 * Overload method for getJustDataFirstRow
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param con
	 * @return
	 */
	public static String[] getJustDataFirstRow(String queryName, String queryFile, HashMap<String, Object> parms, Connection connection)
	{
		return (String[]) executeQuery(queryName, queryFile, parms, connection, QUERY_METHOD.GET_FIRST_ROW, false);
	}

	/**
	 * Overload method for getJustDataFirstRowFirstColumn
	 * 
	 * @param queryName
	 * @param queryFile
	 * @return
	 */
	public static String getJustDataFirstRowFirstColumn(String queryName, String queryFile, HashMap<String, Object> parms, Connection connection)
	{
		return (String) executeQuery(queryName, queryFile, parms, connection, QUERY_METHOD.GET_FIRST_ROW_FIRST_COLUMN, false);
	}

	/**
	 * Overload method for getJustDataFirstRowFirstColumn
	 * 
	 * @param queryName
	 * @param queryFile
	 * @return
	 */
	public static String getJustDataFirstRowFirstColumn(String queryName, String queryFile, HashMap<String, Object> parms)
	{
		return (String) executeQuery(queryName, queryFile, parms, null, QUERY_METHOD.GET_FIRST_ROW_FIRST_COLUMN, false);
	}

	/**
	 * Overload method for getJustDataFirstRowFirstColumn
	 * 
	 * @param queryName
	 * @param queryFile
	 * @return
	 */
	public static String getJustDataFirstRowFirstColumn(String queryName, String queryFile)
	{
		return (String) executeQuery(queryName, queryFile, null, null, QUERY_METHOD.GET_FIRST_ROW_FIRST_COLUMN, false);
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
		return (ArrayList<Object[]>) executeQuery(queryName, queryFile, null, null, QUERY_METHOD.GET_OBJECTS, true);
	}

	/**
	 * Overload method for getDataObjects
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param parms
	 * @return
	 */
	public static ArrayList<Object[]> getDataObjects(String queryName, String queryFile, HashMap<String, Object> parms)
	{
		return (ArrayList<Object[]>) executeQuery(queryName, queryFile, parms, null, QUERY_METHOD.GET_OBJECTS, true);
	}

	/**
	 * Overload method for getDataObjects
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param con
	 * @return
	 */
	public static ArrayList<Object[]> getDataObjects(String queryName, String queryFile, HashMap<String, Object> parms, Connection connection)
	{
		return (ArrayList<Object[]>) executeQuery(queryName, queryFile, parms, connection, QUERY_METHOD.GET_OBJECTS, true);
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
		return (ArrayList<Object[]>) executeQuery(queryName, queryFile, null, null, QUERY_METHOD.GET_OBJECTS, false);
	}

	/**
	 * Overload method for getJustDataObjects
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param parms
	 * @return
	 */
	public static ArrayList<Object[]> getJustDataObjects(String queryName, String queryFile, HashMap<String, Object> parms)
	{
		return (ArrayList<Object[]>) executeQuery(queryName, queryFile, parms, null, QUERY_METHOD.GET_OBJECTS, false);
	}

	/**
	 * Overload method for getJustDataObjects
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param con
	 * @return
	 */
	public static ArrayList<Object[]> getJustDataObjects(String queryName, String queryFile, HashMap<String, Object> parms, Connection connection)
	{
		return (ArrayList<Object[]>) executeQuery(queryName, queryFile, parms, connection, QUERY_METHOD.GET_OBJECTS, false);
	}

	/**
	 * Overload method for getMultipleResults
	 * 
	 * @param queryName
	 * @param queryFile
	 * @return
	 */
	public static ArrayList<ArrayList<String[]>> getMultipleResults(String queryName, String queryFile)
	{
		return (ArrayList<ArrayList<String[]>>) executeQuery(queryName, queryFile, null, null, QUERY_METHOD.GET_MULTIPLE, true);
	}

	/**
	 * Overload method for getMulitpleResults
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param parms
	 * @return
	 */
	public static ArrayList<ArrayList<String[]>> getMulitpleResults(String queryName, String queryFile, HashMap<String, Object> parms)
	{
		return (ArrayList<ArrayList<String[]>>) executeQuery(queryName, queryFile, parms, null, QUERY_METHOD.GET_MULTIPLE, true);
	}

	/**
	 * Overload method for getData
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param con
	 * @return
	 */
	public static ArrayList<ArrayList<String[]>> getMultipleResults(String queryName, String queryFile, HashMap<String, Object> parms, Connection connection)
	{
		return (ArrayList<ArrayList<String[]>>) executeQuery(queryName, queryFile, parms, connection, QUERY_METHOD.GET_MULTIPLE, true);
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
		return (int) executeQuery(queryName, queryFile, null, null, QUERY_METHOD.INSERT, false);
	}

	/**
	 * Overload method for insertDataReturnId
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param parms
	 * @return
	 */
	public static int insertDataReturnId(String queryName, String queryFile, HashMap<String, Object> parms)
	{
		return (int) executeQuery(queryName, queryFile, parms, null, QUERY_METHOD.INSERT, false);
	}

	/**
	 * Overload method for insertDataReturnId
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param con
	 * @return
	 */
	public static int insertDataReturnId(String queryName, String queryFile, HashMap<String, Object> parms, Connection connection)
	{
		return (int) executeQuery(queryName, queryFile, parms, connection, QUERY_METHOD.INSERT, false);
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
		return (int) executeQuery(queryName, queryFile, null, null, QUERY_METHOD.UPDATE, false);
	}

	/**
	 * Overload method for updateData
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param parms
	 * @return
	 */
	public static int updateData(String queryName, String queryFile, HashMap<String, Object> parms)
	{
		return (int) executeQuery(queryName, queryFile, parms, null, QUERY_METHOD.UPDATE, false);
	}

	/**
	 * Overload method for updateData
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param con
	 * @return
	 */
	public static int updateData(String queryName, String queryFile, HashMap<String, Object> parms, Connection connection)
	{
		return (int) executeQuery(queryName, queryFile, null, connection, QUERY_METHOD.UPDATE, false);
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
		return (ArrayList<String[]>) executeQuery(queryName, queryFile, null, null, QUERY_METHOD.GET_FIRST_ROW, true);
	}

	/**
	 * Overload method for getColumnNames
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param parms
	 * @return
	 */
	public static ArrayList<String[]> getColumnNames(String queryName, String queryFile, HashMap<String, Object> parms)
	{
		return (ArrayList<String[]>) executeQuery(queryName, queryFile, parms, null, QUERY_METHOD.GET_FIRST_ROW, true);
	}

	/**
	 * Overload method for getColumnNames
	 * 
	 * @param queryName
	 * @param queryFile
	 * @param con
	 * @return
	 */
	public static ArrayList<String[]> getColumnNames(String queryName, String queryFile, HashMap<String, Object> parms, Connection connection)
	{
		return (ArrayList<String[]>) executeQuery(queryName, queryFile, parms, connection, QUERY_METHOD.GET_FIRST_ROW, true);
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
	public static Object executeQuery(String queryName, String queryFile, HashMap<String, Object> parms, Connection connection, QUERY_METHOD queryMethod, boolean return_column_names)
	{
		// use a prepared statement for sql queries and sps that have input and output parameters.
		PreparedStatement preparedStatement = null;
		String statementString = ICommonConstants.NONE;

		Object data = new Object();
		boolean was_connection_passed = true;

		String fullStatement = null;

		try
		{
			if (connection == null)
			{
				// throws enriched exception
				connection = JdbcConnection.getConnection();
				was_connection_passed = false;
			}

			Query query = SqlQueries.getQuery(queryName, queryFile);
			StatementType statement = query.getStatement();
			statementString = statement.getValue();

			if (query.getType().value().equals(SP))
				statementString = CALL + statementString;

			preparedStatement = connection.prepareStatement(statementString);

			Parameters parameters = query.getParameters();
			Object[] parametersForStatementString = null;
			if (parameters != null)
			{
				List<Parameter> parameterList = parameters.getParameter();
				if (parameterList != null)
				{
					parametersForStatementString = new Object[parameters.getParameter().size()];
					for (int i = 0; parameterList.size() > i; i++)
					{
						Parameter parameter = parameterList.get(i);
						ParameterType parameterType = parameter.getType();
						int parameter_type_value = getParameterTypeValue(parameterType.value());

						if (parms != null)
						{
							if (parms.containsKey(parameter.getName()))
							{
								Object parameterValue = parms.get(parameter.getName());
								preparedStatement.setObject(i + 1, parameterValue, parameter_type_value);
								parametersForStatementString[i] = parameterValue;
							}
							else
							{
								preparedStatement.setObject(i + 1, parameter.getValue(), parameter_type_value);
								parametersForStatementString[i] = parameter.getValue();
							}
						}
						else
						{
							preparedStatement.setObject(i + 1, parameter.getValue(), parameter_type_value);
							parametersForStatementString[i] = parameter.getValue();
						}
					}

					for (int i = 0; i < parametersForStatementString.length; i++)
					{
						if (parametersForStatementString[i] instanceof String)
							parametersForStatementString[i] = ICommonConstants.SINGLE_QUOTE + parametersForStatementString[i] + ICommonConstants.SINGLE_QUOTE;
					}
				}
			}

			fullStatement = String.format(statementString.replaceAll(REGEX_QUESTION_MARK, STRING_FORMAT_PLACEHOLDER), parametersForStatementString);

			StopWatch clock = new StopWatch();
			clock.start();

			if (queryMethod == QUERY_METHOD.GET_DATA)
				data = executeGetData(preparedStatement, return_column_names);

			else if (queryMethod == QUERY_METHOD.GET_FIRST_ROW)
				data = executeGetDataFirstRow(preparedStatement, return_column_names);

			else if (queryMethod == QUERY_METHOD.GET_FIRST_ROW_FIRST_COLUMN)
				data = executeGetDataFirstRowFirstColumn(preparedStatement);

			else if (queryMethod == QUERY_METHOD.UPDATE)
				data = executeUpdateData(preparedStatement);

			else if (queryMethod == QUERY_METHOD.INSERT)
				data = executeInsertReturnId(preparedStatement);

			else if (queryMethod == QUERY_METHOD.GET_OBJECTS)
				data = executeGetDataObjects(preparedStatement, return_column_names);

			else if (queryMethod == QUERY_METHOD.GET_MULTIPLE)
				data = executeGetDataMultiple(preparedStatement, return_column_names);

			clock.stop();
			LOGGER.debug("{} ms: {}", clock.getTime(), fullStatement);
		}
		catch (SQLException e)
		{
			// e.printStackTrace();
			if (fullStatement == null)
				fullStatement = buildStatementString(statementString, parms);
			throw new EnrichableException(CLASS_NAME_PATH, EXECUTE_QUERY_METHOD + ICommonConstants.COLON + queryMethod, ICommonConstants.DB_LOG, ICommonConstants.ERROR, "Check sql statement and parameters. \n" + fullStatement, e);
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
			catch (SQLException e)
			{
				// e.printStackTrace();
				throw new EnrichableException(CLASS_NAME_PATH, EXECUTE_QUERY_METHOD, ICommonConstants.DB_LOG, ICommonConstants.WARNING, "Error closing database connection and or prepared statement", e);
			}
		}

		return data;
	}

	/**
	 * http://dev.mysql.com/doc/refman/5.0/en/connector-j-reference-type-conversions.html
	 * 
	 * @param parameterTypeString
	 * @return
	 */
	private static int getParameterTypeValue(String parameterTypeString)
	{
		switch (parameterTypeString)
		{
			case ICommonConstants.CHAR:
				return java.sql.Types.CHAR;

			case ICommonConstants.DATE:
				return java.sql.Types.DATE;

			case ICommonConstants.DECIMAL:
				return java.sql.Types.DECIMAL;

			case ICommonConstants.DOUBLE:
				return java.sql.Types.DOUBLE;

			case ICommonConstants.FLOAT:
				return java.sql.Types.FLOAT;

			case ICommonConstants.INT:
				return java.sql.Types.INTEGER;

			case ICommonConstants.VARCHAR:
				return java.sql.Types.VARCHAR;

			case ICommonConstants.TIMESTAMP:
				return java.sql.Types.TIMESTAMP;

			case ICommonConstants.TIME:
				return java.sql.Types.TIME;

			case ICommonConstants.BLOB:
				return java.sql.Types.BLOB;
			case ICommonConstants.TINYINT:
				return java.sql.Types.TINYINT;
			case ICommonConstants.SMALLINT:
				return java.sql.Types.SMALLINT;
			case ICommonConstants.MEDIUMINT:
				return java.sql.Types.INTEGER;
			case ICommonConstants.BIGINT:
				return java.sql.Types.BIGINT;
			case ICommonConstants.BIT:
				return java.sql.Types.BOOLEAN;
			case ICommonConstants.BINARY:
				return java.sql.Types.BINARY;
			case ICommonConstants.VARBINARY:
				return java.sql.Types.VARBINARY;
		}

		return 0;
	}

	public static ArrayList<String[]> executeGetData(PreparedStatement preparedStatement, boolean return_column_names) throws SQLException
	{
		ArrayList<String[]> data = new ArrayList<String[]>();

		ResultSet resultSet = null;
		resultSet = preparedStatement.executeQuery();
		while (resultSet.next())
		{
			// ResultSetMetaData, allows for retrieving information about the statement dynamically without having to know exactly what the statement is.
			// Puts results into a string array and then a final array to be returned.

			ResultSetMetaData rsmd = resultSet.getMetaData();
			String[] dataRow = new String[rsmd.getColumnCount()];

			if (return_column_names == true)
			{
				String[] columnRow = new String[rsmd.getColumnCount()];
				for (int i = 0; columnRow.length > i; i++)
				{
					columnRow[i] = rsmd.getColumnLabel(i + 1);
				}
				data.add(columnRow);

				return_column_names = false;
			}

			for (int i = 0; dataRow.length > i; i++)
			{
				dataRow[i] = resultSet.getString(i + 1);
			}
			data.add(dataRow);
		}

		return data;
	}

	public static ArrayList<Object[]> executeGetDataObjects(PreparedStatement preparedStatement, boolean return_column_names) throws SQLException
	{
		ArrayList<Object[]> data = new ArrayList<Object[]>();
		ResultSet resultSet = null;

		resultSet = preparedStatement.executeQuery();
		while (resultSet.next())
		{
			// metadata output of ResultSetObject, allows for retrieving information about the statement dynamically
			// without having to know exactly what the statement is. Puts results into a string array and then a final array to be returned.
			ResultSetMetaData rsmd = resultSet.getMetaData();
			Object[] dataRow = new Object[rsmd.getColumnCount()];

			if (return_column_names == true)
			{
				String[] columnRow = new String[rsmd.getColumnCount()];
				for (int i = 0; columnRow.length > i; i++)
				{
					columnRow[i] = rsmd.getColumnLabel(i + 1);
				}
				data.add(columnRow);

				return_column_names = false;
			}

			for (int i = 0; dataRow.length > i; i++)
			{
				dataRow[i] = resultSet.getObject(i + 1);
			}

			data.add(dataRow);
		}

		return data;
	}

	private static ArrayList<ArrayList<String[]>> executeGetDataMultiple(PreparedStatement preparedStatement, boolean return_column_names) throws SQLException
	{
		ArrayList<ArrayList<String[]>> data = new ArrayList<ArrayList<String[]>>();

		boolean has_results = preparedStatement.execute();
		while (has_results)
		{
			ArrayList<String[]> resultSetData = new ArrayList<String[]>();
			return_column_names = true;

			ResultSet resultSet = preparedStatement.getResultSet();

			while (resultSet.next())
			{
				// ResultSetMetaData, allows for retrieving information about the statement dynamically without having to know exactly what the statement is.
				// Puts results into a string array and then a final array to be returned.

				ResultSetMetaData rsmd = resultSet.getMetaData();
				String[] dataRow = new String[rsmd.getColumnCount()];

				if (return_column_names == true)
				{
					String[] columnRow = new String[rsmd.getColumnCount()];
					for (int i = 0; columnRow.length > i; i++)
					{
						columnRow[i] = rsmd.getColumnLabel(i + 1);
					}
					resultSetData.add(columnRow);

					return_column_names = false;
				}

				for (int i = 0; dataRow.length > i; i++)
				{
					dataRow[i] = resultSet.getString(i + 1);
				}
				resultSetData.add(dataRow);
			}

			data.add(resultSetData);

			has_results = preparedStatement.getMoreResults();
		}

		return data;
	}

	private static String[] executeGetDataFirstRow(PreparedStatement preparedStatement, boolean return_column_names) throws SQLException
	{
		ResultSet resultSet = null;
		String[] data = null;

		resultSet = preparedStatement.executeQuery();

		if (return_column_names == true)
		{
			// We will only return the first row
			if (resultSet.next())
			{
				// metadata output of ResultSetObject, allows for retrieving information about the statement dynamically
				// without having to know exactly what the statement is. Puts results into a string array and then a final array to be returned.
				ResultSetMetaData rsmd = resultSet.getMetaData();
				data = new String[rsmd.getColumnCount()];

				for (int i = 0; data.length > i; i++)
				{
					data[i] = rsmd.getColumnName(i + 1);
				}
			}
		}
		else
		{
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

		return data;
	}

	private static String executeGetDataFirstRowFirstColumn(PreparedStatement preparedStatement) throws SQLException
	{
		ResultSet resultSet = null;
		String data = null;

		resultSet = preparedStatement.executeQuery();

		if (resultSet.next())
		{
			// metadata output of ResultSetObject, allows for retrieving information about the statement dynamically
			// without having to know exactly what the statement is. Puts results into a string array and then a final array to be returned.
			ResultSetMetaData rsmd = resultSet.getMetaData();
			if (rsmd.getColumnCount() > 0)
				data = resultSet.getString(1);
		}

		return data;
	}

	private static int executeUpdateData(PreparedStatement preparedStatement)
	{
		int row_count = 0;

		try
		{
			// executeUpdate() automatically returns row count affected. If none were affected, zero is returned.
			row_count = preparedStatement.executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			// add exception enrichment
		}

		return row_count;
	}

	private static int executeInsertReturnId(PreparedStatement preparedStatement) throws SQLException
	{
		ResultSet resultSet = null;
		int row_id = 0;

		// first execute the statement
		preparedStatement.executeUpdate();

		// second grab the generatedkeys (ie: row id )
		resultSet = preparedStatement.getGeneratedKeys();
		row_id = resultSet.getInt(1);

		return row_id;
	}

	public static String buildStatementString(String statement, HashMap<String, Object> parms)
	{
		String fullStatement = ICommonConstants.NONE;

		fullStatement += "SQL Statement: " + statement;
		if (parms != null)
		{
			fullStatement += "\nParms:\n";
			for (Entry<String, Object> entry : parms.entrySet())
			{
				String keyvalue = ICommonConstants.NONE;
				String key = entry.getKey();
				String value = entry.getValue().toString();

				keyvalue += key + ":" + value;
				fullStatement += keyvalue + "\n";
			}
		}

		return fullStatement;
	}
}