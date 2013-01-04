package com.modnaut.common.database;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class JdbcConnection
{
	// constants
	private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/common";
	private static final String MYSQL_USER = "root";
	private static final String MYSQL_PASSWORD = "Mkyyxz2g";

	public static Connection getConnection()
	{
		Connection con = null;

		try
		{
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
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
			// try {
			// System.out.println("Loading driver...");
			// Class.forName("com.mysql.jdbc.Driver");
			// System.out.println("Driver loaded!");
			// } catch (ClassNotFoundException ex) {
			// throw new RuntimeException("Cannot find the driver in the classpath!", ex);
			// }
			//
			// try {
			// con = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
			//
			// } catch (Exception ex) {
			// ex.printStackTrace();
			// }
		}

		return con;
	}
}
