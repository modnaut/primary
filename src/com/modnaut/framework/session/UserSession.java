package com.modnaut.framework.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.common.utilities.DatabaseMethods;
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
	private boolean is_authenticated = false;
	private String email = ICommonConstants.NONE;
	private String firstName = ICommonConstants.NONE;
	private String lastName = ICommonConstants.NONE;
	private HashMap<String, Object> map = new HashMap<String, Object>();

	private static final String GUEST = "guest";
	private static final String CAN_PERFORM_ACTION = "CAN_PERFORM_ACTION";
	private static final String CAN_PERFORM_ACTIONS = "CAN_PERFORM_ACTIONS";

	/**
	 * 
	 */
	public UserSession()
	{
		long session_id = SessionMethods.generateSessionId();
		new UserSession(session_id);
	}

	/**
	 * @param session_id
	 */
	public UserSession(long session_id)
	{
		this.session_id = session_id;
		this.firstName = GUEST;
		this.lastName = GUEST;
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
	 * @param firstName
	 */
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	/**
	 * @return firstName
	 */
	public String getFirstName()
	{
		return this.firstName;
	}

	/**
	 * @param lastName
	 */
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	/**
	 * @return lastName
	 */
	public String getLastName()
	{
		return this.lastName;
	}

	/**
	 * @param is_authenticated
	 */
	public void setIsAuthenticated(boolean is_authenticated)
	{
		this.is_authenticated = is_authenticated;
	}

	/**
	 * @return is_authenticated
	 */
	public boolean isAuthenticated()
	{
		return this.is_authenticated;
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

	public boolean canPerformAction(int actionId)
	{
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put(ICommonConstants.USER_ID, getUserId());
		parms.put(ICommonConstants.ACTION_ID, actionId);

		return ICommonConstants.LETTER_Y.equals(DatabaseMethods.getJustDataFirstRowFirstColumn(CAN_PERFORM_ACTION, ICommonConstants.COMMON, parms));
	}

	protected HashMap<Integer, Boolean> canPerformActions(int... actionId)
	{
		ArrayList<Integer> actionIds = new ArrayList<Integer>();
		for (int a : actionId)
			actionIds.add(a);

		return canPerformActions(actionIds);
	}

	public HashMap<Integer, Boolean> canPerformActions(List<Integer> actionIds)
	{
		HashMap<Integer, Boolean> permissions = new HashMap<Integer, Boolean>();

		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put(ICommonConstants.USER_ID, getUserId());
		parms.put(ICommonConstants.ACTION_IDS, StringUtils.join(actionIds, ICommonConstants.COMMA));

		ArrayList<String[]> permissionData = DatabaseMethods.getJustData(CAN_PERFORM_ACTIONS, ICommonConstants.COMMON, parms);
		if (permissionData != null)
		{
			for (String[] permissionDataRecord : permissionData)
			{
				permissions.put(Integer.parseInt(permissionDataRecord[0]), ICommonConstants.LETTER_Y.equals(permissionDataRecord[1]));
			}
		}

		return permissions;
	}
}
