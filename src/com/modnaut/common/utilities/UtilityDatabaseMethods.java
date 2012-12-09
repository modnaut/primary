package com.modnaut.common.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.modnaut.common.database.SQLQueries;
import com.modnaut.common.properties.Query;
import com.modnaut.common.properties.StatementType;

public class UtilityDatabaseMethods {

	// constants
	private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/common";
	private static final String MYSQL_USER = "root";
	private static final String MYSQL_PASSWORD = "Mkyyxz2g";
	private static final String SP = "SP";
	private static final String CALL = "CALL ";

	public static ArrayList<String[]> getJustData(String queryName, String queryFile) {

		try {
			System.out.println("Loading driver...");
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver loaded!");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Cannot find the driver in the classpath!", e);
		}

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		ArrayList<String[]> data = new ArrayList<String[]>();

		try {
			con = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
			st = con.createStatement();

			Query q = SQLQueries.getQuery(queryName, queryFile);

			StatementType statement = q.getStatement();

			if (q.getType().equals(SP))
				rs = st.executeQuery(CALL + statement.getValue());
			else
				rs = st.executeQuery(statement.getValue());

			while (rs.next()) {
				String[] t = {rs.getString(2), rs.getString(5)};
				data.add(t);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		return data;
	}
}
