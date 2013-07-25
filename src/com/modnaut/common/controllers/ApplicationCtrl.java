package com.modnaut.common.controllers;

import java.util.HashMap;

import com.google.gson.JsonObject;
import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.common.utilities.EnrichableException;
import com.modnaut.framework.pools.XslPool;
import com.modnaut.framework.properties.application.Application;
import com.modnaut.framework.session.WebSession;
import com.modnaut.framework.utilities.ApplicationMethods;
import com.modnaut.framework.utilities.EnvironmentMethods;

/**
 * 
 * @author Danny Cohn
 * @date 1/9/2013
 * 
 */
public abstract class ApplicationCtrl extends FrameworkCtrl
{
	// exception handling
	private static final String CLASS_NAME_PATH = ApplicationCtrl.class.getCanonicalName();
	private static final String METHOD_NAME = "defaultAction";

	private static final String APPLICATION_XSL = "Application.xsl";

	/**
	 * 
	 * @param request
	 * @param response
	 */
	public ApplicationCtrl(WebSession ninjaSessionObject)
	{
		super(ninjaSessionObject, null);
	}

	/**
	 * 
	 */
	public void buildPage()
	{
		try
		{
			Application application = ApplicationMethods.getApplication(EnvironmentMethods.getApplicationId());
			HashMap<String, Object> parms = new HashMap<String, Object>();
			parms.put("config", getConfig().toString());
			XslPool.marshalAndTransform(application, response.getOutputStream(), APPLICATION_XSL, parms);
		}
		catch (Exception e)
		{
			throw new EnrichableException(CLASS_NAME_PATH, METHOD_NAME, ICommonConstants.POOL_LOG, ICommonConstants.FATAL, "", e);
		}
	}

	protected abstract JsonObject getConfig();
}
