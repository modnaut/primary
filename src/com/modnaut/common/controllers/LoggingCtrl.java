package com.modnaut.common.controllers;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.framework.session.WebSession;

/**
 * 
 * @author Danny Cohn
 * @date 4/15/2013
 * 
 *       Used by all javascript code to log using logback to file or SQL
 * 
 */
public class LoggingCtrl extends FrameworkCtrl
{
	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingCtrl.class);
	private static final String MESSAGE = "message";
	private static final String DATA = "data";

	public LoggingCtrl(WebSession webSession)
	{
		super(webSession, ICommonConstants.CONTENT_TYPE_HTML);
	}

	private String getMessage()
	{
		return StringUtils.trimToEmpty(getParameter(MESSAGE));
	}

	private String getData()
	{
		return StringUtils.trimToEmpty(getParameter(DATA));
	}

	public void trace()
	{
		LOGGER.trace("{} - {}", getMessage(), getData());
	}

	public void debug()
	{
		LOGGER.debug("{} - {}", getMessage(), getData());
	}

	public void info()
	{
		LOGGER.info("{} - {}", getMessage(), getData());
	}

	public void warn()
	{
		LOGGER.warn("{} - {}", getMessage(), getData());
	}

	public void error()
	{
		LOGGER.error("{} - {}", getMessage(), getData());
	}
}
