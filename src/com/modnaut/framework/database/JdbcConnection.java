package com.modnaut.framework.database;

import java.sql.Connection;
import java.sql.SQLException;

import com.jolbox.bonecp.BoneCPDataSource;
import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.common.utilities.EnrichableException;

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
			// TODO - This needs to be initialized with properties from a file.
			Class.forName("com.mysql.jdbc.Driver"); // load the DB driver
			dataSource = new BoneCPDataSource(); // create a new datasource object
			dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/common"); // set the JDBC url
			dataSource.setUsername("modnaut00"); // set the username
			dataSource.setPassword("zp4X263tTSv06On"); // set the password

			INITIALIZED = true;
		}
		catch (ClassNotFoundException e)
		{
			// e.printStackTrace();
			throw new EnrichableException(CLASS_NAME_PATH, CONNECTION_METHOD, ICommonConstants.DB_LOG, ICommonConstants.FATAL, CONFIGURATION_ERROR_MESSAGE, e);
		}

	}
}
