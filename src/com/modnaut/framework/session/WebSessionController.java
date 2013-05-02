package com.modnaut.framework.session;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.common.utilities.StringMethods;
import com.modnaut.framework.utilities.SessionMethods;

/**
 * 
 * @author Ben
 * 
 *         This class is in charge of authentication and creating WebSessions. TODO - Should create a controller factory.
 * 
 */
public class WebSessionController
{
	private static final Logger LOGGER = LoggerFactory.getLogger(WebSessionController.class);

	// CONSTANTS
	private static final String MODNAUT_SESSION_ID = "MODNAUT_SESSION_ID";

	// CLASS VARIABLES
	private HttpServletRequest request = null;
	private HttpServletResponse response = null;

	public WebSessionController(HttpServletRequest request, HttpServletResponse response)
	{
		this.request = request;
		this.response = response;
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public NinjaSession getNinjaSession()
	{
		NinjaSession ninjaSession = null;
		long session_id = -1;
		Cookie cookie = null;
		Cookie[] cookies = request.getCookies();

		if (cookies != null)
		{
			for (int i = 0; i < cookies.length; i++)
			{
				if (cookies[i].getName().equals(MODNAUT_SESSION_ID))
				{
					cookie = cookies[i];
					session_id = StringMethods.StringToLong(cookie.getValue());
					break;
				}
			}
		}

		// Ninja already has a WebSession. Get it from the database.
		if (session_id > 0)
		{
			// TODO - check session age. Does session expire?

			// Get from database and Deserialize Session object.
			ninjaSession = SessionMethods.getSession(session_id);

			// Not found in database!!! Must have been cleared out.
			if (ninjaSession == null)
			{
				int ninjaId = 1; // guest ninja until they log in.

				// create new Session object.
				ninjaSession = SessionMethods.createNewSession();
				ninjaSession.setNinjaId(ninjaId);
				// Insert into database.
				SessionMethods.saveSession(ninjaSession);
			}
		}
		else
		{
			// No sessionId found in browser.
			int ninjaId = 1; // guest ninja until they log in.

			// create new Session object.
			ninjaSession = SessionMethods.createNewSession();
			ninjaSession.setNinjaId(ninjaId);
			// Insert into database.
			SessionMethods.saveSession(ninjaSession);
		}

		int maxAge;
		try
		{
			// maxAge = new Integer(request.getServletContext().getInitParameter("cookie-age")).intValue();
			maxAge = 86400; // 60 seconds * 60 minutes * 24 hours
		}
		catch (Exception e)
		{
			maxAge = -1;
		}

		cookie = new Cookie(MODNAUT_SESSION_ID, Long.toString(ninjaSession.getSessionId()));
		cookie.setPath(request.getContextPath());
		cookie.setMaxAge(maxAge); // time in seconds
		response.addCookie(cookie);

		return ninjaSession;
	}
}
