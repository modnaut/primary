package com.modnaut.common.framework;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Jamie Lynn
 * @date 1/9/2013
 * 
 * Used by all java classes that respond to HTTP Servlet requests and produce output. Takes request sent in from servlet, performs business logic and sets the response to the final output result.
 * 
 */
public class FrameworkCtrl
{
	protected HttpServletRequest request;
	protected HttpServletResponse response;

	/**
	 * 
	 * @param request
	 * @param response
	 */
	public FrameworkCtrl(HttpServletRequest request, HttpServletResponse response)
	{
		this.request = request;
		this.response = response;
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
