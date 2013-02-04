package com.modnaut.common.session;

import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.common.utilities.SessionMethods;

/**
 * @author Ben
 * 
 */
public class Session implements java.io.Serializable
{
	/**
	 * generated serialVersionUID
	 */
	private static final long serialVersionUID = -4119815288345489125L;

	private long session_id = 0;
	private int user_id = 0;
	private String email = ICommonConstants.NONE;

	/**
	 * 
	 */
	public Session()
	{
		this.session_id = SessionMethods.generateSessionId();
	}

	/**
	 * @param session_id
	 */
	public Session(long session_id)
	{
		// TODO
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
	 * @return
	 */
	public String getEmail()
	{
		return this.email;
	}

}
