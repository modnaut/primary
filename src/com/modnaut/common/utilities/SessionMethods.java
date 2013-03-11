package com.modnaut.common.utilities;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.framework.session.WebSession;

/**
 * @author Ben
 * 
 */
public class SessionMethods
{
	private static final Logger logger = LoggerFactory.getLogger(SessionMethods.class);

	// parms
	private static final String USER_ID = "UserId";
	private static final String SESSION_ID = "SessionId";
	private static final String SESSION_OBJECT = "SessionObject";

	// SQL QUERIES
	private static final String GET_SESSION = "GET_SESSION";
	private static final String INSERT_UPDATE_SESSION = "INSERT_UPDATE_SESSION";

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
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put(SESSION_ID, session_id);

		Object object = null;

		ArrayList<Object[]> objects = DatabaseMethods.getJustDataObjects(GET_SESSION, ICommonConstants.COMMON, parms);
		if (objects != null)
			object = ((Object[]) objects.get(0))[0];

		return new WebSession();
	}

	/**
	 * @param session_id
	 * @param session
	 * @return
	 */
	public static long saveSession(WebSession session)
	{
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put(USER_ID, session.getUserId());
		parms.put(SESSION_ID, session.getSessionId());
		parms.put(SESSION_OBJECT, session);

		long row_count = DatabaseMethods.updateData(INSERT_UPDATE_SESSION, ICommonConstants.COMMON, parms);

		return row_count;
	}
}
