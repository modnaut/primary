package com.modnaut.apps.login;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.modnaut.common.framework.ApplicationCtrl;
import com.modnaut.common.framework.FrameworkCtrl;
import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.common.utilities.DatabaseMethods;

public class LoginCtrl extends FrameworkCtrl
{
	private static final Logger logger = LoggerFactory.getLogger("com.modnaut.apps.helloworld.HelloWorldChangeCtrl");

	private static final String INVALID_LOGIN_PAGE = "invalid_login.html";

	private static MessageDigest MESSAGE_DIGEST = null;
	private static final int ITERATION_NUMBER = 1000;
	private static final String PASSWORD_SALT = "MoDnaUt_SalT";

	// SQL
	private static final String GET_USER = "GET_USER";

	// SQL Parms
	private static final String EMAIL = "Email";
	private static final String PASSWORD = "Password";

	// Page Parameters

	// Page Values
	private String email = ICommonConstants.NONE;
	private String password = ICommonConstants.NONE;

	/**
	 * Constuctor for the login page.
	 * 
	 * @param request
	 * @param response
	 */
	public LoginCtrl(HttpServletRequest request, HttpServletResponse response)
	{
		super(request, response);
		// unmarshall(XML_FILE); No Unmarshalling. We will redirect to the Main Page or an Invalid Login page.
	}

	public void defaultAction() throws IOException
	{
		logger.debug("You made it to LoginCtrl!!!");

		try
		{
			getPageValues();

			if (authenticate(email, password))
			{
				// If the user has typed a valid Email and Password combination, pass them to the main page
				ApplicationCtrl applicationCtrl = new ApplicationCtrl(this.request, this.response);
				applicationCtrl.defaultAction();
			}
			else
			{
				// TODO - increment the invalid login count...
				// TODO - start Session...

				// If the user has not given a valid Email and Password combination, pass them to the invalid Login Page
				response.sendRedirect(INVALID_LOGIN_PAGE);
			}
		}
		catch (Exception e)
		{
			logger.error("This a Logback ERROR meesage.  There was an error in default action of this class.");
			e.printStackTrace();
			response.sendRedirect("invalid_login.html");
		}
	}

	/**
	 * Gets the values from the Request Object.
	 */
	private void getPageValues()
	{
		email = getParameter(EMAIL);
		password = getParameter(PASSWORD);
	}

	/**
	 * 
	 * @param email
	 * @param password
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	private boolean authenticate(String email, String password) throws IOException, NoSuchAlgorithmException
	{
		boolean authenticated = false;

		// Still testing...
		logger.debug(email, password);
		logger.debug(byteToBase64(base64ToByte(PASSWORD_SALT)));
		logger.debug(byteToBase64(getHash(ITERATION_NUMBER, password, base64ToByte(PASSWORD_SALT))));

		// The word 'password' will equal 'kLxNpX+0w9lWcamR3wSZ8O/828A=' after it has been salted and hashed
		String saltedPassword = byteToBase64(getHash(ITERATION_NUMBER, password, base64ToByte(PASSWORD_SALT)));

		HashMap<String, String> parms = new HashMap<String, String>();
		parms.put(EMAIL, email);
		parms.put(PASSWORD, saltedPassword);

		// See if this email/password combination exists in our database.
		String[] data = DatabaseMethods.getJustDataFirstRow(GET_USER, ICommonConstants.COMMON, parms);
		if (data != null && data.length > 1)
		{
			authenticated = true;
		}

		return authenticated;

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
