package com.modnaut.common.controllers;

import java.io.File;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.modnaut.framework.pools.JaxbPool;
import com.modnaut.framework.pools.XslPool;
import com.modnaut.framework.properties.application.Applications;
import com.modnaut.framework.servlet.ApplicationServlet;

/**
 * 
 * @author Danny Cohn
 * @date 1/9/2013
 * 
 */
public class ApplicationCtrl extends FrameworkCtrl
{
	private static final String APPLICATION_ID = "applicationId";
	private static final String APPLICATION_XSL = "Application.xsl";
	private static final String APPLICATION_XML = "WEB-INF/xml/Application.xml";

	/**
	 * 
	 * @param request
	 * @param response
	 */
	public ApplicationCtrl(HttpServletRequest request, HttpServletResponse response)
	{
		super(request, response);
	}

	/**
	 * 
	
	 */
	public void defaultAction()
	{
		try
		{
			Applications apps = JaxbPool.unmarshal(Applications.class, new File(ApplicationServlet.getRealPath() + APPLICATION_XML));
			HashMap<String, Object> parms = new HashMap<String, Object>();
			parms.put(APPLICATION_ID, 1);
			XslPool.marshalAndTransform(apps, response.getOutputStream(), APPLICATION_XSL, parms);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			// throw new EnrichableException("", "", "", e);
		}
	}
}