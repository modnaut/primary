package com.modnaut.framework.utilities;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.common.utilities.DatabaseMethods;
import com.modnaut.framework.session.WebSession;

public class UrlMethods
{

	public static void retrievePrettyUrl(WebSession webSession, String className, String methodName)
	{
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put(ICommonConstants.CLASS, className);
		parms.put(ICommonConstants.METHOD, methodName);

		String hashPath = DatabaseMethods.getJustDataFirstRowFirstColumn("GET_HASHPATH_BY_CLASS_AND_METHOD", ICommonConstants.COMMON, parms);

		if (!StringUtils.isEmpty(hashPath))
		{
			webSession.getResponse().setHeader("HashPath", hashPath);
		}
	}

	public static String[] retrieveClassAndMethod(String hashPath)
	{
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put("HashPath", hashPath);
		return DatabaseMethods.getJustDataFirstRow("GET_CLASS_AND_METHOD_FROM_HASHPATH", ICommonConstants.COMMON, parms);
	}
}
