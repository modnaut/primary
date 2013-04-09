package com.modnaut.framework.utilities;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.common.utilities.CommonMethods;
import com.modnaut.common.utilities.DatabaseMethods;
import com.modnaut.common.utilities.StringMethods;
import com.modnaut.framework.session.UserSession;

/**
 * @author Ben
 * 
 */
public class SessionMethods
{
	private static final Logger LOGGER = LoggerFactory.getLogger(SessionMethods.class);

	// SQL Parms
	private static final String USER_ID = "UserId";
	private static final String SESSION_ID = "SessionId";
	private static final String SESSION_OBJECT = "SessionObject";
	private static final String EMAIL = "Email";
	private static final String PASSWORD = "Password";

	// SQL QUERIES
	private static final String GET_SESSION = "GET_SESSION";
	private static final String INSERT_UPDATE_SESSION = "INSERT_UPDATE_SESSION";
	private static final String AUTHENTICATE_USER = "AUTHENTICATE_USER";

	// CONSTANTS
	private static final int ITERATION_NUMBER = 1000;
	private static final String PASSWORD_SALT = "MoDnaUt_SalT";

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
	public static UserSession getSession(long session_id)
	{
		LOGGER.trace("Testing different logging levels on a clas-by-class basis.");

		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put(SESSION_ID, session_id);

		Object object = null;

		ArrayList<Object[]> objects = DatabaseMethods.getJustDataObjects(GET_SESSION, ICommonConstants.COMMON, parms);
		if (objects != null && objects.size() > 0)
		{
			object = ((Object[]) objects.get(0))[0];
			if (object instanceof UserSession)
			{
				return (UserSession) object;
			}
		}

		return null;
	}

	/**
	 * This method will create a new, empty UserSession.
	 * 
	 * @return
	 */
	public static UserSession createNewSession()
	{
		long new_id = SessionMethods.generateSessionId();
		UserSession userSession = new UserSession(new_id); // create new Session object.
		return userSession;
	}

	/**
	 * @param session_id
	 * @param session
	 * @return
	 */
	public static long saveSession(UserSession session)
	{
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put(USER_ID, session.getUserId());
		parms.put(SESSION_ID, session.getSessionId());
		parms.put(SESSION_OBJECT, session);

		long row_count = DatabaseMethods.updateData(INSERT_UPDATE_SESSION, ICommonConstants.COMMON, parms);

		return row_count;
	}

	public static UserSession authenticate(String email, String password)
	{
		return authenticate(email, password, null);
	}

	public static UserSession authenticate(String email, String password, UserSession userSession)
	{
		if (email != null && password != null)
		{
			// The word 'password' will equal 'kLxNpX+0w9lWcamR3wSZ8O/828A=' after it has been salted and hashed
			String saltedPassword = CommonMethods.encryptString(password, ITERATION_NUMBER, PASSWORD_SALT);

			HashMap<String, Object> parms = new HashMap<String, Object>();
			parms.put(EMAIL, email);
			parms.put(PASSWORD, saltedPassword);

			// See if this email/password combination exists in our database. If not, the stored procedure will increment the invalid login attempts.
			String[] data = DatabaseMethods.getJustDataFirstRow(AUTHENTICATE_USER, ICommonConstants.COMMON, parms); // [0]UserId, [1]FirstName, [2]LastName, [3]EmailAddress, [4]UserPassword
			if (data != null && data.length > 3)
			{
				if (userSession == null)
				{
					// create new Session object.
					long new_id = SessionMethods.generateSessionId();
					userSession = new UserSession(new_id);
				}

				// Set the UserSession properties.
				int userId = StringMethods.StringToInt(data[0]);
				userSession.setUserId(userId);
				userSession.setEmail(email);
				userSession.setIsAuthenticated(true);
				userSession.setFirstName(StringUtils.trimToEmpty(data[1]));
				userSession.setLastName(StringUtils.trimToEmpty(data[2]));
				// Insert into database.
				SessionMethods.saveSession(userSession);

				return userSession;
			}
			else
			{
				// TODO - increment the invalid login count...

				// If the user has not given a valid Email and Password combination, pass them to the invalid Login Page
				// response.sendRedirect(INVALID_LOGIN_PAGE);
				return userSession;
			}
		}

		return userSession;
	}
}
