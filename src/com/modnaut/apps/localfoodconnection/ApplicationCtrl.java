package com.modnaut.apps.localfoodconnection;

import com.google.gson.JsonObject;
import com.modnaut.framework.session.WebSession;

/**
 * 
 * @author Danny Cohn
 * @date 1/9/2013
 * 
 */
public class ApplicationCtrl extends com.modnaut.common.controllers.ApplicationCtrl
{
	// exception handling
	private static final String CLASS_NAME_PATH = ApplicationCtrl.class.getCanonicalName();

	/**
	 * 
	 * @param request
	 * @param response
	 */
	public ApplicationCtrl(WebSession ninjaSessionObject)
	{
		super(ninjaSessionObject);
	}

	protected JsonObject getConfig()
	{
		JsonObject config = new JsonObject();
		return config;
	}
}
