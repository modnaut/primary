package com.modnaut.common.session;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.common.utilities.DatabaseMethods;
import com.modnaut.common.utilities.SessionMethods;

/**
 * 
 * @author Ben
 * 
 *         This class is in charge of authentication and creating WebSessions. TODO - Should create a controller factory.
 * 
 */
public class WebSessionController
{
	// SQL
	private static final String AUTHENTICATE_USER = "AUTHENTICATE_USER";

	// SQL Parms
	private static final String EMAIL = "Email";
	private static final String PASSWORD = "Password";
	private static final String SESSION_ID = "sessionId";

	// CONSTANTS
	HttpServletRequest request = null;
	HttpServletResponse response = null;

	public WebSessionController(HttpServletRequest request, HttpServletResponse response)
	{
		this.request = request;
		this.response = response;
	}

	/**
	 * 
	 * @param email
	 * @param password
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public WebSession authenticate() throws IOException
	{
		try
		{
			HttpSession httpSession = request.getSession(true);// create if it doesn't exist

			// We will always continue session if it is not new, even if they went through the login page again.
			// TODO - Is this smart? Should we check for login credentials first? Probably. Right now, this lets them in with a valid session and incorrect email/password.
			if (httpSession != null && !httpSession.isNew())
			{
				// User already has a WebSession. Get it from the database.
				if (httpSession.getAttribute(SESSION_ID) != null)
				{
					long session_id = (long) httpSession.getAttribute(SESSION_ID);

					// TODO - remove printlines.
					System.out.println("##########     checkForValidSession     ##########");
					System.out.println("session.isNew(): " + httpSession.isNew());
					System.out.println("session.getLastAccessedTime(): " + httpSession.getLastAccessedTime());
					System.out.println("session.getCreationTime(): " + httpSession.getCreationTime());
					System.out.println("session.getMaxInactiveInterval(): " + httpSession.getMaxInactiveInterval());
					System.out.println("session has been set already: " + session_id);

					// TODO - check session age

					// Get from database and Deserialize Session object.
					return SessionMethods.getSession(session_id);

				}
				else
				{
					// TODO
					// Session is not new, but the sessionId cannot be found?
					// Invalidate session and create a new session?
				}
			}

			// HttpSession exists, but and is new.
			if (httpSession != null)
			{
				String email = request.getParameter(EMAIL);
				String password = request.getParameter(PASSWORD);

				if (email != null && password != null)
				{
					// The word 'password' will equal 'kLxNpX+0w9lWcamR3wSZ8O/828A=' after it has been salted and hashed
					String saltedPassword = SessionMethods.encryptPassword(password);

					HashMap<String, String> parms = new HashMap<String, String>();
					parms.put(EMAIL, email);
					parms.put(PASSWORD, saltedPassword);

					// See if this email/password combination exists in our database.
					String[] data = DatabaseMethods.getJustDataFirstRow(AUTHENTICATE_USER, ICommonConstants.COMMON, parms);
					if (data != null && data.length > 1)
					{
						// Found the user in the database. Create a new WebSession.
						long new_id = SessionMethods.generateSessionId();
						// Store the sessionId in the httpSession object. This way we do not have to have a parameter on the page.
						// This will only work if we do not have different session per request. No Session branching right now.
						// TODO - still must make sure this cannot be intercepted - Man in the Middle attacks.
						httpSession.setAttribute(SESSION_ID, new_id);

						// create new Session object.
						WebSession webSession = new WebSession(new_id);
						// Insert into database.
						SessionMethods.saveSession(webSession);

						return webSession;
					}
					else
					{
						// TODO - increment the invalid login count...

						// If the user has not given a valid Email and Password combination, pass them to the invalid Login Page
						// response.sendRedirect(INVALID_LOGIN_PAGE);
						return null;
					}
				}

			}

			return null;
		}
		catch (NoSuchAlgorithmException e)
		{
			return null;
		}

	}

}
