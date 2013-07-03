package com.modnaut.framework.database;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.jolbox.bonecp.BoneCPDataSource;
import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.common.utilities.EnrichableException;
import com.modnaut.framework.pools.JaxbPool;
import com.modnaut.framework.properties.dbproperties.Database;
import com.modnaut.framework.properties.dbproperties.DatabaseMetaData;
import com.modnaut.framework.utilities.EnvironmentMethods;

/**
 * 
 * @author Jamie Lynn
 * @date 1/9/2013
 * 
 *       Database is configured as a JNDI resource in both context.xml, which stores the database connection properties, and web.xml which stores a reference to the information in context.xml and so that it can be accessed by java. Creates a datasource object based on the above to get a pooled dconnection to the mysql database.
 * 
 */
public class JdbcConnection
{
	private static final String CLASS_NAME_PATH = JdbcConnection.class.getCanonicalName();
	private static final String CONNECTION_METHOD = "getConnection()";
	private static final String CONFIGURATION_ERROR_MESSAGE = "Error with database configuration file settings. Check configuration settings and files (JdbcConnection.java, context.xml, web.xml).";
	private static final String CONNECTION_ERROR_MESSAGE = "Error accessing connection with database. Make sure database server is running. Check configuration settings and files (JdbcConnection.java, context.xml, web.xml).";

	private static boolean INITIALIZED = false;
	private static BoneCPDataSource dataSource = null;

	private static final String XML_EXTENSION = ".xml";
	private static final String XML_PATH = "../xml";
	
	/**
	 * Uses datasource to get and return a pooled connection.
	 * 
	 * @return
	 */
	public static Connection getConnection()
	{
		if (!INITIALIZED)
			initialize();

		Connection con = null;

		try
		{
			con = dataSource.getConnection();
		}
		catch (SQLException e)
		{
			// e.printStackTrace();
			throw new EnrichableException(CLASS_NAME_PATH, CONNECTION_METHOD, ICommonConstants.DB_LOG, ICommonConstants.FATAL, CONNECTION_ERROR_MESSAGE, e);
		}

		return con;
	}

	private static void initialize()
	{
		try
		{
			String filePath = ICommonConstants.NONE;
			
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			if (classLoader.getResource(XML_PATH) != null)
				filePath = classLoader.getResource(XML_PATH).getPath();
			
			File file = new File(filePath + "DatabaseProperties" + XML_EXTENSION);
			DatabaseMetaData databasemetadata = JaxbPool.unmarshal(DatabaseMetaData.class, file);
			
			List databases = databasemetadata.getDatabase();
			if (databases != null && databases.size() > 0)
			{
				for (int i = 0; databases.size() > i; i++)
				{
					Database db = (Database) databases.get(i);

					if (db.getName().equalsIgnoreCase(EnvironmentMethods.getServerName()))
					{
						Class.forName(db.getDriverclassname().getValue());
						dataSource = new BoneCPDataSource();
						dataSource.setJdbcUrl(db.getUrl().getValue());
						dataSource.setUsername(db.getUsername().getValue());
						dataSource.setPassword(db.getPassword().getValue());
					}
				}
			}

			INITIALIZED = true;
		}
		catch (ClassNotFoundException e)
		{
			// e.printStackTrace();
			throw new EnrichableException(CLASS_NAME_PATH, CONNECTION_METHOD, ICommonConstants.DB_LOG, ICommonConstants.FATAL, CONFIGURATION_ERROR_MESSAGE, e);
		}

	}
}
