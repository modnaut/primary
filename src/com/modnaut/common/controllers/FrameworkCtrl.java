package com.modnaut.common.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.apps.login.InsufficientPrivilegeException;
import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.framework.session.NinjaSession;
import com.modnaut.framework.session.WebSession;

/**
 * 
 * @author Jamie Lynn
 * @date 1/9/2013
 * 
 *       Used by all java classes that respond to HTTP Servlet requests and produce output. Takes request sent in from servlet, performs business logic and sets the response to the final output result.
 * 
 */
public class FrameworkCtrl
{
	// logging
	private static final Logger LOGGER = LoggerFactory.getLogger(ExtJsScreenCtrl.class);

	// exception handling
	private static final String CLASS_NAME_PATH = FrameworkCtrl.class.getCanonicalName();
	private static final String CONSTRUCTOR = "CONSTRUCTOR";
	private static final String NEEDS_AUTHENTICATION = "Needs authentication.";

	// constants
	protected static final String NINJA_ID = "NinjaId";
	protected static final String SESSION_ID = "SessionId";

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected NinjaSession ninjaSession;
	protected WebSession webSession;
	protected boolean needs_authentication = false;

	/**
	 * 
	 * @param webSession
	 */
	public FrameworkCtrl(WebSession webSession, String contentType)
	{
		this(webSession, false, contentType);
	}

	/**
	 * 
	 * @param webSession
	 * @param needs_authentication
	 */
	public FrameworkCtrl(WebSession webSession, boolean needs_authentication, String contentType) throws InsufficientPrivilegeException
	{
		this.request = webSession.getRequest();
		this.response = webSession.getResponse();
		this.ninjaSession = webSession.getNinjaSession();
		this.webSession = webSession;
		this.needs_authentication = needs_authentication;

		if (contentType != null && response != null)
			response.setContentType(contentType);

		LOGGER.debug("needs_authentication: " + needs_authentication + " isAuthenticated(): " + webSession.getNinjaSession().isAuthenticated());

		if (needs_authentication && !webSession.getNinjaSession().isAuthenticated())
		{
			LOGGER.debug("NEEDS TO AUTHENTICATE FIRST!!!!");
			throw new InsufficientPrivilegeException(CLASS_NAME_PATH, CONSTRUCTOR, ICommonConstants.AUTHORIZATION_LOG, ICommonConstants.WARNING, NEEDS_AUTHENTICATION);
		}
	}

	/**
	 * Returns a specific value from request based on name of the value in the request. For example, if the attribute name of a html element on screen is 'username', then it will return the value of this html element. Can also be used to retrieve the value off a url that contains parameters and hidden values on screen.
	 * 
	 * @param name
	 * @return
	 */
	protected String getParameter(String name)
	{
		return this.webSession.getParameter(name);
	}

	protected boolean hasPower(int powerId)
	{
		boolean ninja_has_power = false;
		if (ninjaSession != null)
			ninja_has_power = ninjaSession.hasPower(powerId);
		return ninja_has_power;
	}

	protected HashMap<Integer, Boolean> hasPowers(List<Integer> powerIds)
	{
		HashMap<Integer, Boolean> powers;
		if (ninjaSession != null)
			powers = ninjaSession.hasPowers(powerIds);
		else
			powers = new HashMap<Integer, Boolean>();

		return powers;
	}

	protected HashMap<Integer, Boolean> hasPowers(int... powerId)
	{
		ArrayList<Integer> powerIds = new ArrayList<Integer>();
		for (int p : powerId)
			powerIds.add(p);

		return hasPowers(powerIds);
	}

	protected HashMap<String, Object> getDefaultParms()
	{
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put(NINJA_ID, ninjaSession.getNinjaId());
		parms.put(SESSION_ID, ninjaSession.getSessionId());
		return parms;
	}
}
