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

			// grabs database settings (username, password, etc) from the context.xml file
			// based on the res-ref-name in web.xml (ie: 'jdbc/modnaut')
			DataSource ds = (DataSource) envCtx.lookup("jdbc/modnaut");

			try
			{
				con = ds.getConnection();
			}
			catch (SQLException s)
			{
				s.printStackTrace();
			}

		}
		catch (NamingException n)
		{
			n.printStackTrace();
		}

		return con;
	}
}
