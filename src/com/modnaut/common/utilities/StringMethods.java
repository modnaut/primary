package com.modnaut.common.utilities;

import org.apache.commons.lang3.StringUtils;

public class StringMethods
{
	/**
	 * 
	 */
	public static Integer StringToInt(String value)
	{
		Integer returnValue = null;
		try
		{
			returnValue = Integer.parseInt(StringUtils.trimToEmpty(value));
		}
		catch (NumberFormatException nfe)
		{
			// Do nothing. This should be common.
		}
		return returnValue;
	}

	/**
	 * 
	 */
	public static Long StringToLong(String value)
	{
		Long returnValue = null;
		try
		{
			returnValue = Long.parseLong(StringUtils.trimToEmpty(value));
		}
		catch (NumberFormatException nfe)
		{
			// Do nothing. This should be common.
		}
		return returnValue;
	}
}
