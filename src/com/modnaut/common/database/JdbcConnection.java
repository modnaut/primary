package com.modnaut.common.database;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * 
 * @author Jamie Lynn
 * @date 1/9/2013
 * 
 *       Database is configured as a JNDI resource in both context.xml, which stores the database connection properties,
 *       and web.xml which stores a reference to the information in context.xml and so that it can be accessed by java.
 *       Creates a datasource object based on the above to get a pooled dconnection to the mysql database.
 * 
 */
public class JdbcConnection
{
	private static final String CLASS_NAME_PATH = "com.modnaut.common.database.JdbcConnection";
	private static final String CONNECTION_METHOD = "getConnection()";
	private static final String CONFIGURATION_ERROR_MESSAGE = "Error with database configuration file settings.";
	private static final String CONNECTION_ERROR_MESSAGE = "Error accessing connection with database.";

	/**
	 * Uses datasource to get and return a pooled connection.
	 * 
	 * @return
	 */

	public static Connection getConnection()
	{
		Connection con = null;

		try
		{
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");

			// grabs database settings (username, password, etc) from the context.xml file based on the res-ref-name in web.xml (ie: 'jdbc/modnaut')
			DataSource ds = (DataSource) envCtx.lookup("jdbc/modnaut");

			con = ds.getConnection();
		}
		catch (NamingException e)
		{
			e.printStackTrace();
			// throw new EnrichableException(CLASS_NAME_PATH, CONNECTION_METHOD, CONFIGURATION_ERROR_MESSAGE, e);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			// throw new EnrichableException(CLASS_NAME_PATH, CONNECTION_METHOD, CONNECTION_ERROR_MESSAGE, e);
		}

		return con;
	}
}
