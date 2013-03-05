package com.modnaut.common.utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.framework.database.JdbcConnection;
import com.modnaut.framework.database.SqlQueries;
import com.modnaut.framework.properties.sqlmetadata.Query;
import com.modnaut.framework.properties.sqlmetadata.StatementType;
import com.modnaut.framework.session.WebSession;
import com.mysql.jdbc.exceptions.jdbc4.MySQLDataException;

/**
 * @author Ben
 * 
 */
public class SessionMethods
{
	private static final Logger logger = LoggerFactory.getLogger(SessionMethods.class);

	// SQL QUERIES
	private static final String GET_SESSION = "GET_SESSION";
	private static final String INSERT_SESSION = "INSERT_SESSION";

	public static void main(String[] args)
	{
		WebSession session = new WebSession();
		session.setSessionId(1234);
	}

	/**
	 * @return generated id
	 */
	public static long generateSessionId()
	{
		// TODO - think this through. Need it to be unique, "random" and can't be zero.
		return (long) (System.nanoTime() * Math.random());
	}

	/**
	 * @param sessionId
	 * @return
	 */
	public static WebSession getSession(long session_id)
	{
		// use a prepared statement for sql queries and sps that have input and output parameters.
		PreparedStatement st = null;
		ResultSet rs = null;
		Connection con = null;

		try
		{
			con = JdbcConnection.getConnection();

			Query q = SqlQueries.getQuery(GET_SESSION, ICommonConstants.COMMON);
			StatementType statement = q.getStatement();
			String statementString = statement.getValue();
			st = con.prepareStatement(statementString);
			st.setString(1, ICommonConstants.NONE + session_id);
			rs = st.executeQuery();
			rs.next();
			Object object = rs.getObject(1);

			// TODO
			logger.debug("done de-serializing " + object.getClass().getName());

		}
		catch (MySQLDataException ex)
		{
			logger.error(ex.getMessage());
			logger.error(st.toString());
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
				if (st != null)
				{
					st.close();
				}

				if (con != null)
				{
					con.close();
				}

			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		return new WebSession();
	}

	/**
	 * @param session_id
	 * @param session
	 * @return
	 */
	public static long saveSession(WebSession session)
	{
		PreparedStatement st = null;
		Connection con = null;
		long row_count = 0;

		try
		{
			con = JdbcConnection.getConnection();
			Query q = SqlQueries.getQuery(INSERT_SESSION, ICommonConstants.COMMON);
			StatementType statement = q.getStatement();
			String statementString = statement.getValue();

			st = con.prepareStatement(statementString);
			st.setLong(1, session.getSessionId());
			st.setObject(2, session);

			// executeUpdate() automatically returns row count affected. If none were affected, zero is returned.
			row_count = st.executeUpdate();

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
				if (st != null)
				{
					st.close();
				}

				if (con != null)
				{
					con.close();
				}
			}
			catch (Exception ex)
			{

				ex.printStackTrace();
			}
		}

		return row_count;
	}
}
