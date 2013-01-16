package com.modnaut.common.framework;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.modnaut.common.properties.application.Applications;
import com.modnaut.common.servlet.ApplicationServlet;
import com.modnaut.common.utilities.JaxbPool;
import com.modnaut.common.utilities.XslPool;

public class ApplicationCtrl
{

	private HttpServletRequest request;
	private HttpServletResponse response;

	public ApplicationCtrl(HttpServletRequest request, HttpServletResponse response)
	{
		this.request = request;
		this.response = response;
	}

	public void defaultAction() throws IOException, Exception
	{
		Applications apps = JaxbPool.unmarshal(Applications.class, new File(ApplicationServlet.getRealPath() + "WEB-INF/xml/Application.xml"));
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put("applicationId", 1);
		XslPool.marshalAndTransform(apps, response.getOutputStream(), "Application.xsl", parms, false);
	}
}
