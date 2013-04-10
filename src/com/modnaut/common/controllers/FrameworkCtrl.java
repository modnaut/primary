package com.modnaut.common.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected UserSession userSession;

	/**
	 * 
	 * @param request
	 * @param response
	 */
	public FrameworkCtrl(WebSession webSession)
	{
		this.request = webSession.getRequest();
		this.response = webSession.getResponse();
		this.userSession = webSession.getUserSession();
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
