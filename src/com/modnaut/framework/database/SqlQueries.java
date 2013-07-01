package com.modnaut.framework.database;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.common.utilities.EnrichableException;
import com.modnaut.framework.pools.JaxbPool;
import com.modnaut.framework.properties.sqlmetadata.Query;
import com.modnaut.framework.properties.sqlmetadata.SqlMetaData;

/**
 * 
 * @author Ben Dalgaard
 * @date 1/9/2013
 * 
 *       Loads all existing sql meta data files and queries so that it will be accessible as needed by the java code.
 * 
 */
public class SqlQueries
{
	// exception handling
	private static final String CLASS_NAME_PATH = SqlQueries.class.getCanonicalName();
	private static final String METHOD_NAME = "getQuery";
	private static final String QUERY_ERROR_MESSAGE = "Invalid query name. Please check spelling and sql meta data file being used.";
	private static final String FILE_ERROR_MESSAGE = "Invalid sql metadata file. Please check spelling or make sure file exists.";

	// constants
	private static final String XML_EXTENSION = ".xml";
	private static final String XML_PATH = "../xml";

	// private variables
	private static HashMap<QUERY_FILE, HashMap<String, Query>> allQueries = null;

	// sql meta data files
	public enum QUERY_FILE
	{
		COMMON("CommonSqlMetaData"),
		MARKET_LINK("MarketLinkSqlMetaData");

		private String fileName = ICommonConstants.NONE;

		private QUERY_FILE(String fileName)
		{
			this.fileName = fileName;
		}
	}

	/**
	 * Loops through every file that exists in the sqlFiles arraylist and builds a central repository hashmap that contains of sql queries within those sqlmetadata.xml files.
	 */
	private static void unmarshalSqlMetaData()
	{
		allQueries = new HashMap<QUERY_FILE, HashMap<String, Query>>();

		for (QUERY_FILE file : QUERY_FILE.values())
		{
			loadFile(file);
		}
	}

	/**
	 * For each sqlmetadata file passed in, the file is unmarshalled into a sqlmetadata object. From here this object can then be broken down further to get to each individual query and parameters contained within the file. The query / parameters are stored as a hashmap. Once all queries and parameters have been added to this hashmap the entire set is then stored in a hashmap with the key of the 'sqlmetadata.xml' file name.
	 * 
	 * 
	 * @param fileName
	 */
	private static void loadFile(QUERY_FILE queryFile)
	{
		String filePath = ICommonConstants.NONE;

		// TODO - This is used for batches, for now. This isn't a good solution. Should be in a properties file.
		try
		{
			filePath = new File(".").getCanonicalPath() + "/web/WEB-INF/xml/";
		}
		catch (IOException e)
		{
		}

		// This is used for Tomcat.
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader.getResource(XML_PATH) != null)
			filePath = classLoader.getResource(XML_PATH).getPath();

		HashMap<String, Query> hashMap = new HashMap<String, Query>();

		File file = new File(filePath + queryFile.fileName + XML_EXTENSION);
		SqlMetaData sqlmetadata = JaxbPool.unmarshal(SqlMetaData.class, file);

		List<Query> queryList = sqlmetadata.getQuery();
		if (queryList != null)
		{
			for (int i = 0; queryList.size() > i; i++)
			{
				Query q = queryList.get(i);
				hashMap.put(q.getName(), q);
			}
		}

		allQueries.put(queryFile, hashMap);
	}

	/**
	 * Checks to see if the queries have been loaded into the allQueries hashmap object. If not, it creates the hashmap and loads the existing queries. Returns query object that contains query string and parameters from specified applicationId (name of sqlmetadatafile) and the varcode (name of the query to be retrieved)
	 * 
	 * @param varCode
	 * @param applicationId
	 * @return
	 */
	public static Query getQuery(String queryName, QUERY_FILE queryFile)
	{
		allQueries = null;// TODO: This is for development so we dont have to restart server. Make this config-based

		if (allQueries == null)
			unmarshalSqlMetaData();

		Query query = null;
		try
		{
			query = allQueries.get(queryFile).get(queryName);
			if (query == null)
				throw new EnrichableException(CLASS_NAME_PATH, METHOD_NAME, ICommonConstants.DB_LOG, ICommonConstants.WARNING, QUERY_ERROR_MESSAGE);
		}
		catch (Exception e)
		{
			throw new EnrichableException(CLASS_NAME_PATH, METHOD_NAME, ICommonConstants.DB_LOG, ICommonConstants.WARNING, FILE_ERROR_MESSAGE, e);
		}

		return query;
	}
}
