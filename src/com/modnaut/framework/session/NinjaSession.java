package com.modnaut.framework.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.common.utilities.DatabaseMethods;
import com.modnaut.framework.database.SqlQueries.QUERY_FILE;
import com.modnaut.framework.utilities.SessionMethods;

/**
 * @author Ben
 * 
 *         This Object contains specific information about the ninja and what they are currently doing.
 * 
 */
public class NinjaSession implements java.io.Serializable
{
	/**
	 * generated serialVersionUID
	 */
	private static final long serialVersionUID = -4119815288345489125L;

	private long session_id = 0;
	private int ninja_id = 0;
	private boolean is_authenticated = false;
	private String email = ICommonConstants.NONE;
	private String firstName = ICommonConstants.NONE;
	private String lastName = ICommonConstants.NONE;
	private HashMap<String, Object> map = new HashMap<String, Object>();

	private static final String GUEST = "guest";
	private static final String NINJA_HAS_POWER = "NINJA_HAS_POWER";
	private static final String NINJA_HAS_POWERS = "NINJA_HAS_POWERS";

	/**
	 * 
	 */
	public NinjaSession()
	{
		long session_id = SessionMethods.generateSessionId();
		new NinjaSession(session_id);
	}

	/**
	 * @param session_id
	 */
	public NinjaSession(long session_id)
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
	 * @param new_ninja_id
	 */
	public void setNinjaId(int new_ninja_id)
	{
		ninja_id = new_ninja_id;
	}

	/**
	 * @return
	 */
	public int getNinjaId()
	{
		return this.ninja_id;
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

	public boolean hasPower(int powerId)
	{
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put(ICommonConstants.NINJA_ID, getNinjaId());
		parms.put(ICommonConstants.POWER_ID, powerId);

		return ICommonConstants.LETTER_Y.equals(DatabaseMethods.getJustDataFirstRowFirstColumn(NINJA_HAS_POWER, QUERY_FILE.COMMON, parms));
	}

	protected HashMap<Integer, Boolean> hasPowers(int... powerId)
	{
		ArrayList<Integer> powerIds = new ArrayList<Integer>();
		for (int p : powerId)
			powerIds.add(p);

		return hasPowers(powerIds);
	}

	public HashMap<Integer, Boolean> hasPowers(List<Integer> powerIds)
	{
		HashMap<Integer, Boolean> powers = new HashMap<Integer, Boolean>();

		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put(ICommonConstants.NINJA_ID, getNinjaId());
		parms.put(ICommonConstants.POWER_IDS, StringUtils.join(powerIds, ICommonConstants.COMMA));

		ArrayList<String[]> powerData = DatabaseMethods.getJustData(NINJA_HAS_POWERS, QUERY_FILE.COMMON, parms);
		if (powerData != null)
		{
			for (String[] powerDataRecord : powerData)
			{
				powers.put(Integer.parseInt(powerDataRecord[0]), ICommonConstants.LETTER_Y.equals(powerDataRecord[1]));
			}
		}

		return powers;
	}
}
