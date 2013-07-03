package com.modnaut.framework.utilities;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.common.utilities.StringMethods;

public class EnvironmentMethods
{
	private static final Logger LOGGER = LoggerFactory.getLogger(EnvironmentMethods.class);
	private static final String CLASS_NAME_PATH = EnvironmentMethods.class.getCanonicalName();
	private static boolean INITIALIZED = false;
	private static String ENVIRONMENT_NAME = null;
	private static String REAL_PATH = null;
	private static String SERVER_NAME = null;

	public static void initializeServer(ServletContext context)
	{
		if (!INITIALIZED)
			initialize(context);
	}

	synchronized private static void initialize(ServletContext context)
	{
		REAL_PATH = context.getRealPath(ICommonConstants.SLASH);
		ENVIRONMENT_NAME = context.getInitParameter(ICommonConstants.ENVIRONMENT_NAME);
		SERVER_NAME = context.getInitParameter(ICommonConstants.SERVER_NAME);

		LOGGER.info("Real Path: {}", REAL_PATH);
		LOGGER.info("Environment Name: {}", ENVIRONMENT_NAME);
		LOGGER.info("Server Name: {}", SERVER_NAME);

		StringMethods.cacheAllStringValues();

		INITIALIZED = true;
	}

	public static String getRealPath()
	{
		return REAL_PATH;
	}

	public static String getEnvironmentName()
	{
		return ENVIRONMENT_NAME;
	}
	
	public static String getServerName()
	{
		return SERVER_NAME;
	}
}
