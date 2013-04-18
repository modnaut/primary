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
import com.modnaut.framework.session.UserSession;
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

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected UserSession userSession;
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
		this.userSession = webSession.getUserSession();
		this.needs_authentication = needs_authentication;

		if (contentType != null && response != null)
			response.setContentType(contentType);

		LOGGER.debug("needs_authentication: " + needs_authentication + " isAuthenticated(): " + webSession.getUserSession().isAuthenticated());

		if (needs_authentication && !webSession.getUserSession().isAuthenticated())
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
		return this.request.getParameter(name);
	}

	protected boolean canPerformAction(int actionId)
	{
		boolean can_perform_action = false;
		if (userSession != null)
			can_perform_action = userSession.canPerformAction(actionId);
		return can_perform_action;
	}

	protected HashMap<Integer, Boolean> canPerformActions(List<Integer> actionIds)
	{
		HashMap<Integer, Boolean> permissions;
		if (userSession != null)
			permissions = userSession.canPerformActions(actionIds);
		else
			permissions = new HashMap<Integer, Boolean>();

		return permissions;
	}

	protected HashMap<Integer, Boolean> canPerformActions(int... actionId)
	{
		ArrayList<Integer> actionIds = new ArrayList<Integer>();
		for (int a : actionId)
			actionIds.add(a);

		return canPerformActions(actionIds);
	}
}
