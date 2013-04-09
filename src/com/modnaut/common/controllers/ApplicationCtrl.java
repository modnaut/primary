package com.modnaut.common.controllers;

import java.io.File;
import java.util.HashMap;

import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.common.utilities.EnrichableException;
import com.modnaut.framework.pools.JaxbPool;
import com.modnaut.framework.pools.XslPool;
import com.modnaut.framework.properties.application.Applications;
import com.modnaut.framework.session.WebSession;
import com.modnaut.framework.utilities.ServerMethods;

/**
 * 
 * @author Danny Cohn
 * @date 1/9/2013
 * 
 */
public class ApplicationCtrl extends FrameworkCtrl
{
	// exception handling
	private static final String CLASS_NAME_PATH = FrameworkCtrl.class.getCanonicalName();
	private static final String METHOD_NAME = "defaultAction";

	private static final String APPLICATION_ID = "applicationId";
	private static final String APPLICATION_XSL = "Application.xsl";
	private static final String APPLICATION_XML = "WEB-INF/xml/Application.xml";

	/**
	 * 
	 * @param request
	 * @param response
	 */
	public ApplicationCtrl(WebSession userSessionObject)
	{
		super(userSessionObject);
	}

	/**
	 * 
	 */
	public void defaultAction()
	{
		try
		{
			Applications apps = JaxbPool.unmarshal(Applications.class, new File(ServerMethods.getRealPath() + APPLICATION_XML));
			HashMap<String, Object> parms = new HashMap<String, Object>();
			parms.put(APPLICATION_ID, 1);
			XslPool.marshalAndTransform(apps, response.getOutputStream(), APPLICATION_XSL, parms);
		}
		catch (Exception e)
		{
			throw new EnrichableException(CLASS_NAME_PATH, METHOD_NAME, ICommonConstants.POOL_LOG, ICommonConstants.FATAL, "", e);
		}
	}
}
