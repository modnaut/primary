package com.modnaut.framework.session;

import java.util.HashMap;

import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.framework.utilities.SessionMethods;

/**
 * @author Ben
 * 
 *         This Object contains specific information about the user and what they are currently doing.
 * 
 */
public class UserSession implements java.io.Serializable
{
	/**
	 * generated serialVersionUID
	 */
	private static final long serialVersionUID = -4119815288345489125L;

	private long session_id = 0;
	private int user_id = 0;
	private String email = ICommonConstants.NONE;
	private HashMap<String, Object> map = new HashMap<String, Object>();

	/**
	 * 
	 */
	public UserSession()
	{
		this.session_id = SessionMethods.generateSessionId();
	}

	/**
	 * @param session_id
	 */
	public UserSession(long session_id)
	{
		this.session_id = session_id;
	}

	/**
	 * @param new_session_id
	 */
	public void setSessionId(long new_session_id)
	{
		session_id = new_session_id;
	}

	/**
	 * @return
	 */
	public long getSessionId()
	{
		return this.session_id;
	}

	/**
	 * @param new_user_id
	 */
	public void setUserId(int new_user_id)
	{
		user_id = new_user_id;
	}

	/**
	 * @return
	 */
	public int getUserId()
	{
		return this.user_id;
	}

	/**
	 * @param newEmail
	 */
	public void setEmail(String newEmail)
	{
		email = newEmail;
	}

	/**
	 * @return email
	 */
	public String getEmail()
	{
		return this.email;
	}

	/**
	 * This is a method that allows objects to be stored in the session object using a HashMap. WARNING: Don't make a mess of this as it will increase the size of the session and decrease database speed. You have been warned!!!
	 * 
	 * @param key
	 *            - key to access object by
	 * @param value
	 *            - value of the object
	 * 
	 */
	public void addValue(String key, Object value)
	{
		map.put(key, value);
	}

	/**
	 * This is the method used to retrieve objects being stored in session.
	 * 
	 * @param key
	 * @return Object stored in HashMap.
	 */
	public Object getValue(String key)
	{
		return map.get(key);
	}
}
