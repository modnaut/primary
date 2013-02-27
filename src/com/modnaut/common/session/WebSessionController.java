package com.modnaut.common.session;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

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
	private static MessageDigest MESSAGE_DIGEST = null;
	private static final int ITERATION_NUMBER = 1000;
	private static final String PASSWORD_SALT = "MoDnaUt_SalT";

	// SQL
	private static final String GET_USER = "GET_USER";

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
					String saltedPassword = byteToBase64(getHash(ITERATION_NUMBER, password, base64ToByte(PASSWORD_SALT)));

					HashMap<String, String> parms = new HashMap<String, String>();
					parms.put(EMAIL, email);
					parms.put(PASSWORD, saltedPassword);

					// See if this email/password combination exists in our database.
					String[] data = DatabaseMethods.getJustDataFirstRow(GET_USER, ICommonConstants.COMMON, parms);
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

	/**
	 * From a password, a number of iterations and a salt, returns the corresponding digest
	 * 
	 * @param iterationNb
	 *            int The number of iterations of the algorithm
	 * @param password
	 *            String The password to encrypt
	 * @param salt
	 *            byte[] The salt
	 * @return byte[] The digested password
	 * @throws NoSuchAlgorithmException
	 *             If the algorithm doesn't exist
	 * @throws UnsupportedEncodingException
	 */
	private byte[] getHash(int iterationNb, String password, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		// Initialize the MessageDigest Object used for hashing
		if (MESSAGE_DIGEST == null)
		{
			MESSAGE_DIGEST = MessageDigest.getInstance(ICommonConstants.SHA_1);
		}
		MESSAGE_DIGEST.reset();
		MESSAGE_DIGEST.update(salt); // Add the salting to make DICTIONARY ATTACKS harder.
		byte[] input = MESSAGE_DIGEST.digest(password.getBytes(ICommonConstants.UTF_8));
		// Hash the password a few times. This is expensive, but makes the attack very hard from an attacker standpoint. We still only salt once though...
		for (int i = 0; i < iterationNb; i++)
		{
			MESSAGE_DIGEST.reset();
			input = MESSAGE_DIGEST.digest(input);
		}
		return input;
	}

	/**
	 * From a base 64 representation, returns the corresponding byte[]
	 * 
	 * @param data
	 *            String The base64 representation
	 * @return byte[]
	 * @throws IOException
	 */
	private static byte[] base64ToByte(String data) throws IOException
	{
		BASE64Decoder decoder = new BASE64Decoder();
		return decoder.decodeBuffer(data);
	}

	/**
	 * From a byte[] returns a base 64 representation
	 * 
	 * @param data
	 *            byte[]
	 * @return String
	 * @throws IOException
	 */
	private static String byteToBase64(byte[] data)
	{
		BASE64Encoder endecoder = new BASE64Encoder();
		return endecoder.encode(data);
	}
}
