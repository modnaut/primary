package com.modnaut.framework.session;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author bend
 * 
 *         This Object will contain the information about the ninja (stored in the NinjaSession Object) as well as the Request and Response information.
 * 
 */
public class WebSession
{
	// VARIABLES
	private HttpServletRequest request = null;
	private HttpServletResponse response = null;
	private NinjaSession ninjaSession = null;
	private HashMap<String, String> extraParameters = null;

	public WebSession(HttpServletRequest request, HttpServletResponse response, NinjaSession webSession)
	{
		this.request = request;
		this.response = response;
		this.ninjaSession = webSession;
	}

	public void setRequest(HttpServletRequest request)
	{
		this.request = request;
	}

	public HttpServletRequest getRequest()
	{
		return this.request;
	}

	public void setResponse(HttpServletResponse response)
	{
		this.response = response;
	}

	public HttpServletResponse getResponse()
	{
		return this.response;
	}

	public void setNinjaSession(NinjaSession ninjaSession)
	{
		this.ninjaSession = ninjaSession;
	}

	public NinjaSession getNinjaSession()
	{
		return this.ninjaSession;
	}

	public void setExtraParameters(HashMap<String, String> extraParameters)
	{
		this.extraParameters = extraParameters;
	}

	public HashMap<String, String> getExtraParameters()
	{
		return this.extraParameters;
	}

	public String getParameter(String name)
	{
		String value = null;

		if (name != null)
		{
			if (this.extraParameters != null)
			{
				value = this.extraParameters.get(name);
			}

			if (value == null && this.request != null)
				value = this.request.getParameter(name);
		}

		return value;
	}
}