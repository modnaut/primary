package com.modnaut.common.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class JdbcConnection {

	// constants
	private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/common";
	private static final String MYSQL_USER = "root";
	private static final String MYSQL_PASSWORD = "Mkyyxz2g";


	public static Connection getConnection() {

		try {
			System.out.println("Loading driver...");
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver loaded!");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Cannot find the driver in the classpath!", e);
		}

		Connection con = null;

		try {
			con = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return con;
	}
}
