package com.modnaut.framework.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import com.modnaut.framework.servlet.RestVerb;
import com.modnaut.framework.utilities.RequestParameterParser;

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
	private RequestParameterParser requestParameterParser = null;

	// private HashMap<String, String> extraParameters = null;

	public WebSession(HttpServletRequest request, HttpServletResponse response, NinjaSession webSession, RequestParameterParser requestParameterParser)
	{
		this.request = request;
		this.response = response;
		this.ninjaSession = webSession;
		this.requestParameterParser = requestParameterParser;
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

	public String getParameter(String name)
	{
		String value = null;
		if (name != null)
		{
			if (requestParameterParser != null)
			{
				value = requestParameterParser.getParameter(name);
			}
			if (value == null && request != null)
				value = request.getParameter(name);
		}

		return value;
	}

	public FileItem getUploadedFile()
	{
		return requestParameterParser.getUploadedFile();
	}

	public String getRequestBody()
	{
		return requestParameterParser.getRequestBody();
	}

	public RestVerb getRestVerb()
	{
		return requestParameterParser.getRestVerb();
	}

	public Integer getRestResourceId()
	{
		return requestParameterParser.getRestResourceId();
	}
}