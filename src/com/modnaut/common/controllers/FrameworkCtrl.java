package com.modnaut.common.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.apps.login.InsufficientPrivilegeException;
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
	public FrameworkCtrl(WebSession webSession)
	{
		this(webSession, false);
	}

	/**
	 * 
	 * @param webSession
	 * @param needs_authentication
	 */
	public FrameworkCtrl(WebSession webSession, boolean needs_authentication) throws InsufficientPrivilegeException
	{
		this.request = webSession.getRequest();
		this.response = webSession.getResponse();
		this.userSession = webSession.getUserSession();
		this.needs_authentication = needs_authentication;
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
}
