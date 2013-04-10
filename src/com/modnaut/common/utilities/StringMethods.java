package com.modnaut.common.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.common.interfaces.ICommonConstants;

public class StringMethods
{
	private static Logger LOGGER = LoggerFactory.getLogger(StringMethods.class);
	private static ConcurrentHashMap<String, ConcurrentHashMap<String, String>> LANGUAGE_STRINGS = new ConcurrentHashMap<String, ConcurrentHashMap<String, String>>();
	private static final String STRING_CD_DELIMITER = "~~~";
	private static final String STRING_CDS = "stringCds";
	private static final String LANGUAGE_CD = "languageCd";
	private static final String DELIMITER = "delimiter";
	private static final String GET_MULTIPLE_STRINGS_FOR_LANGUAGE = "GET_MULTIPLE_STRINGS_FOR_LANGUAGE";
	public static final String STRING_NOT_FOUND = "STRING NOT FOUND";

	public static HashMap<String, String> getStringValues(List<String> stringCds, String languageCd)
	{
		StopWatch clock = new StopWatch();
		clock.start();

		stringCds = new ArrayList<String>(stringCds);// clone list so that we don't modify the original
		HashMap<String, String> strings = new HashMap<String, String>();

		ConcurrentHashMap<String, String> language = LANGUAGE_STRINGS.get(languageCd);
		if (language == null)
		{
			language = new ConcurrentHashMap<String, String>();
			LANGUAGE_STRINGS.put(languageCd, language);
		}

		for (String stringCd : stringCds)
		{
			if (language.containsKey(stringCd))
			{
				strings.put(stringCd, language.get(stringCd));
			}
		}

		stringCds.removeAll(strings.keySet());// remove any strings from the list that we already have cached translations for

		if (!stringCds.isEmpty())
		{
			HashMap<String, Object> parms = new HashMap<String, Object>();
			parms.put(STRING_CDS, StringUtils.join(stringCds, STRING_CD_DELIMITER));
			parms.put(LANGUAGE_CD, languageCd);
			parms.put(DELIMITER, STRING_CD_DELIMITER);
			ArrayList<String[]> stringValues = DatabaseMethods.getJustData(GET_MULTIPLE_STRINGS_FOR_LANGUAGE, ICommonConstants.COMMON, parms);

			for (String[] stringValue : stringValues)
			{
				if (stringValue[1] != null)
				{
					language.put(stringValue[0], stringValue[1]);
					strings.put(stringValue[0], stringValue[1]);
				}
				else
				{
					language.put(stringValue[0], STRING_NOT_FOUND);
					LOGGER.warn("stringCd not found in SQL: {}", stringValue[0]);
				}
			}
		}

		clock.stop();
		LOGGER.debug("Elapsed method time {}", clock.getTime());
		return strings;
	}

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
