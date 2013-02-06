package com.modnaut.common.framework;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.modnaut.common.properties.application.Applications;
import com.modnaut.common.servlet.ApplicationServlet;
import com.modnaut.common.utilities.JaxbPool;
import com.modnaut.common.utilities.XslPool;

/**
 * 
 * @author Danny Cohn
 * @date 1/9/2013
 * 
 */
public class ApplicationCtrl
{
	private static final String APPLICATION_ID = "applicationId";
	private static final String APPLICATION_XSL = "Application.xsl";
	private static final String APPLICATION_XML = "WEB-INF/xml/Application.xml";

	private HttpServletRequest request;
	private HttpServletResponse response;

	/**
	 * 
	 * @param request
	 * @param response
	 */
	public ApplicationCtrl(HttpServletRequest request, HttpServletResponse response)
	{
		this.request = request;
		this.response = response;
	}

	/**
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	public void defaultAction() throws IOException, Exception
	{
		Applications apps = JaxbPool.unmarshal(Applications.class, new File(ApplicationServlet.getRealPath() + APPLICATION_XML));
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put(APPLICATION_ID, 1);
		XslPool.marshalAndTransform(apps, response.getOutputStream(), APPLICATION_XSL, parms);
	}
}
