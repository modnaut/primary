package com.modnaut.common.utilities;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.common.properties.sqlmetadata.Query;
import com.modnaut.common.properties.sqlmetadata.StatementType;
import com.modnaut.framework.database.JdbcConnection;
import com.modnaut.framework.database.SqlQueries;
import com.modnaut.framework.session.WebSession;
import com.mysql.jdbc.exceptions.jdbc4.MySQLDataException;

/**
 * @author Ben
 * 
 */
public class SessionMethods
{
	private static final Logger logger = LoggerFactory.getLogger(SessionMethods.class);

	// CONSTANTS
	private static MessageDigest MESSAGE_DIGEST = null;
	private static final int ITERATION_NUMBER = 1000;
	private static final String PASSWORD_SALT = "MoDnaUt_SalT";

	// SQL QUERIES
	private static final String GET_SESSION = "GET_SESSION";
	private static final String INSERT_SESSION = "INSERT_SESSION";
	private static final String GET_USER = "GET_USER";

	// SQL Parms
	private static final String EMAIL = "Email";
	private static final String PASSWORD = "Password";

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

	public static String encryptPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException, IOException
	{
		// The word 'password' will equal 'kLxNpX+0w9lWcamR3wSZ8O/828A=' after it has been salted and hashed
		return byteToBase64(getHash(ITERATION_NUMBER, password, base64ToByte(PASSWORD_SALT)));
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
	private static byte[] getHash(int iterationNb, String password, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException
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
