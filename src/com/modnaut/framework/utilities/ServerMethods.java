package com.modnaut.framework.utilities;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.common.utilities.StringMethods;

public class ServerMethods
{
	private static Logger LOGGER = LoggerFactory.getLogger(ServerMethods.class);
	private static final String CLASS_NAME_PATH = ServerMethods.class.getCanonicalName();
	private static boolean INITIALIZED = false;
	private static String SERVER_NAME = null;
	private static String REAL_PATH = null;

	public static void initializeServer(ServletContext context)
	{
		if (!INITIALIZED)
			initialize(context);
	}

	synchronized private static void initialize(ServletContext context)
	{
		REAL_PATH = context.getRealPath(ICommonConstants.SLASH);
		SERVER_NAME = context.getInitParameter("serverName");

		LOGGER.info("Real Path: {}", REAL_PATH);
		LOGGER.info("Server Name: {}", SERVER_NAME);

		StringMethods.cacheAllStringValues();

		INITIALIZED = true;
	}

	public static String getRealPath()
	{
		return REAL_PATH;
	}

	public static String getServerName()
	{
		return SERVER_NAME;
	}
}
