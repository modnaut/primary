package com.modnaut.framework.utilities;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.common.utilities.CommonMethods;
import com.modnaut.common.utilities.DatabaseMethods;
import com.modnaut.common.utilities.StringMethods;
import com.modnaut.framework.database.SqlQueries.QUERY_FILE;
import com.modnaut.framework.session.NinjaSession;

/**
 * @author Ben
 * 
 */
public class SessionMethods
{
	private static final Logger LOGGER = LoggerFactory.getLogger(SessionMethods.class);

	// SQL Parms
	private static final String NINJA_ID = "NinjaId";
	private static final String SESSION_ID = "SessionId";
	private static final String SESSION_OBJECT = "SessionObject";
	private static final String EMAIL = "Email";
	private static final String PASSWORD = "Password";

	// SQL QUERIES
	private static final String GET_SESSION = "GET_SESSION";
	private static final String INSERT_UPDATE_SESSION = "INSERT_UPDATE_SESSION";
	private static final String AUTHENTICATE_NINJA = "AUTHENTICATE_NINJA";
	private static final String INCREMENT_LOGIN_ATTEMPTS = "INCREMENT_LOGIN_ATTEMPTS";

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

		try
		{
			HashMap<String, Object> parms = new HashMap<String, Object>();
			parms.put(SESSION_ID, session_id);

			ArrayList<Object[]> data = DatabaseMethods.getJustDataObjects(GET_SESSION, QUERY_FILE.COMMON, parms);
			if (data != null && data.size() > 0)
			{
				object = data.get(0)[0];

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
					LOGGER.error("GET_SESSION did not return an instanceof serializing.NinjaSession. Object is of class {}", object.getClass().getCanonicalName());
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
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
		PreparedStatement st = null;
		int row_count = 0;

		try
		{
			HashMap<String, Object> parms = new HashMap<String, Object>();
			parms.put(NINJA_ID, session.getNinjaId());
			parms.put(SESSION_ID, session.getSessionId());
			parms.put(SESSION_OBJECT, session);

			row_count = DatabaseMethods.updateData(INSERT_UPDATE_SESSION, QUERY_FILE.COMMON, parms);

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
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
				DatabaseMethods.updateData(INCREMENT_LOGIN_ATTEMPTS, QUERY_FILE.COMMON, parms);

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
			LOGGER.error("Could not deserialize byte aray.", ioe);
			return null;
		}
		catch (ClassNotFoundException cnfe)
		{
			LOGGER.error("Could not deserialize byte aray.", cnfe);
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
