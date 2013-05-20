package com.modnaut.framework.utilities;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.common.utilities.CommonMethods;
import com.modnaut.common.utilities.DatabaseMethods;
import com.modnaut.common.utilities.StringMethods;
import com.modnaut.framework.database.JdbcConnection;
import com.modnaut.framework.database.SqlQueries;
import com.modnaut.framework.database.SqlQueries.QUERY_FILE;
import com.modnaut.framework.properties.sqlmetadata.Query;
import com.modnaut.framework.properties.sqlmetadata.StatementType;
import com.modnaut.framework.session.NinjaSession;

/**
 * @author Ben
 * 
 */
public class SessionMethods
{
	private static final Logger LOGGER = LoggerFactory.getLogger(SessionMethods.class);

	// SQL Parms
	private static final String SESSION_OBJECT = "SessionObject";
	private static final String EMAIL = "Email";
	private static final String PASSWORD = "Password";

	// SQL QUERIES
	private static final String GET_SESSION = "GET_SESSION";
	private static final String INSERT_UPDATE_SESSION = "INSERT_UPDATE_SESSION";
	private static final String AUTHENTICATE_NINJA = "AUTHENTICATE_NINJA";

	// CONSTANTS
	private static final int ITERATION_NUMBER = 1000;
	private static final String PASSWORD_SALT = "MoDnaUt_SalT";

	/**
	 * @return generated id
	 */
	public static long generateSessionId()
	{
		// TODO - think this through. Need it to be Positive, unique, "random" and can't be zero.
		return (long) Math.abs(System.nanoTime() * Math.random());
	}

	/**
	 * @param sessionId
	 * @return
	 */
	public static NinjaSession getSession(long session_id)
	{
		LOGGER.trace("Testing different logging levels on a clas-by-class basis.");

		Object object = null;

		Connection con = null;

		try
		{
			con = JdbcConnection.getConnection();
			// Statements allow to issue SQL queries to the database

			PreparedStatement preparedStatement = con.prepareStatement("SELECT SessionObject, LastModifiedDate FROM Common.Session WHERE SessionId = ?");
			// Parameters start with 1
			preparedStatement.setLong(1, session_id);

			// Result set get the result of the SQL query
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet != null)
			{
				if (resultSet.next())
				{
					object = resultSet.getObject(1);

					if (object instanceof byte[])
					{
						object = deserialize((byte[]) object);
					}

					if (object instanceof com.modnaut.framework.session.NinjaSession)
					{
						return (com.modnaut.framework.session.NinjaSession) object;
					}
					else
					{
						System.out.println("GET_SESSION did not return an instanceof serializing.NinjaSession.");
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (con != null)
					con.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * This method will create a new, empty NinjaSession.
	 * 
	 * @return
	 */
	public static NinjaSession createNewSession()
	{
		long new_id = SessionMethods.generateSessionId();
		NinjaSession ninjaSession = new NinjaSession(new_id); // create new Session object.
		return ninjaSession;
	}

	/**
	 * @param session_id
	 * @param session
	 * @return
	 */
	public static int saveSession(NinjaSession session)
	{
		// HashMap<String, Object> parms = new HashMap<String, Object>();
		// parms.put(NINJA_ID, session.getNinjaId());
		// parms.put(SESSION_ID, session.getSessionId());
		// parms.put(SESSION_OBJECT, session);
		//
		// int row_count = DatabaseMethods.updateData(INSERT_UPDATE_SESSION, ICommonConstants.COMMON, parms);
		//
		// return row_count;
		//
		//

		PreparedStatement st = null;
		Connection con = null;
		int row_count = 0;

		try
		{
			con = JdbcConnection.getConnection();
			Query q = SqlQueries.getQuery(INSERT_UPDATE_SESSION, QUERY_FILE.COMMON);
			StatementType statement = q.getStatement();
			String statementString = "CALL " + statement.getValue() + ";";

			st = con.prepareStatement(statementString);
			st.setInt(1, session.getNinjaId());
			st.setLong(2, session.getSessionId());
			st.setObject(3, session);

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

			}
		}
		return row_count;

	}

	public static NinjaSession authenticate(String email, String password)
	{
		return authenticate(email, password, null);
	}

	public static NinjaSession authenticate(String email, String password, NinjaSession ninjaSession)
	{
		if (email != null && password != null)
		{
			// The word 'password' will equal 'kLxNpX+0w9lWcamR3wSZ8O/828A=' after it has been salted and hashed
			String saltedPassword = CommonMethods.encryptString(password, ITERATION_NUMBER, PASSWORD_SALT);

			HashMap<String, Object> parms = new HashMap<String, Object>();
			parms.put(EMAIL, email);
			parms.put(PASSWORD, saltedPassword);

			// See if this email/password combination exists in our database. If not, the stored procedure will increment the invalid login attempts.
			String[] data = DatabaseMethods.getJustDataFirstRow(AUTHENTICATE_NINJA, QUERY_FILE.COMMON, parms); // [0]NinjaId, [1]FirstName, [2]LastName, [3]EmailAddress, [4]Password
			if (data != null && data.length > 3)
			{
				if (ninjaSession == null)
				{
					// create new Session object.
					long new_id = SessionMethods.generateSessionId();
					ninjaSession = new NinjaSession(new_id);
				}

				// Set the NinjaSession properties.
				int ninjaId = StringMethods.StringToInt(data[0]);
				ninjaSession.setNinjaId(ninjaId);
				ninjaSession.setEmail(email);
				ninjaSession.setIsAuthenticated(true);
				ninjaSession.setFirstName(StringUtils.trimToEmpty(data[1]));
				ninjaSession.setLastName(StringUtils.trimToEmpty(data[2]));
				// Insert into database.
				SessionMethods.saveSession(ninjaSession);

				return ninjaSession;
			}
			else
			{
				// TODO - increment the invalid login count...

				// If the ninja has not given a valid Email and Password combination, pass them to the invalid Login Page
				// response.sendRedirect(INVALID_LOGIN_PAGE);
				return ninjaSession;
			}
		}

		return ninjaSession;
	}

	/**
	 * 
	 * @param serializedBytes
	 * @return
	 */
	public static Object deserialize(byte[] serializedBytes)
	{
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		try
		{
			bis = new ByteArrayInputStream(serializedBytes);
			ois = new ObjectInputStream(bis);

			Object o = null;
			o = ois.readObject();

			return o;
		}
		catch (IOException ioe)
		{
			LOGGER.info("Could not deserialize byte aray.");
			return null;
		}
		catch (ClassNotFoundException cnfe)
		{
			LOGGER.info("Could not deserialize byte aray.");
			return null;
		}
		finally
		{
			try
			{
				if (ois != null)
					ois.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
			}
		}
	}
}
