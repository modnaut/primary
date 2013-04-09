package com.modnaut.common.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.jxpath.JXPathContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.framework.pools.JaxbPool;
import com.modnaut.framework.pools.XslPool;
import com.modnaut.framework.properties.viewmetadata.ViewMetaData;
import com.modnaut.framework.session.WebSession;
import com.modnaut.framework.utilities.ServerMethods;

/**
 * 
 * @author Jamie Lynn
 * @date 1/9/2013
 * 
 *       Used by all java classes that respond to HTTP Servlet requests and produce output. Takes request sent in from servlet, performs business logic and sets the response to the final output result.
 * 
 */
public class FrameworkCtrl
{
	protected HttpServletRequest request;
	protected HttpServletResponse response;

	private static final String VIEW_META_DATA_FILE = "ViewMetaData.xsl";
	private static final String VIEW_PATH = "WEB-INF/views";

	protected ViewMetaData viewMetaData;
	protected JXPathContext jxPathContext;

	/**
	 * 
	 * @param request
	 * @param response
	 */
	public FrameworkCtrl(WebSession webSession)
	{
		this.request = webSession.getRequest();
		this.response = webSession.getResponse();
	}

	/**
	 * Unmarshalls xmlfile into a viewmetadata object that will be used by the java classes to manipulate, mold, break down into smaller sub objects and inject data.
	 * 
	 * @param xmlFileName
	 * @return
	 */
	protected ViewMetaData unmarshall(String xmlFileName)
	{
		try
		{
			Collection<File> files = FileUtils.listFiles(new File(ServerMethods.getRealPath() + VIEW_PATH), null, true);
			String absoluteFilePath = ICommonConstants.NONE;
			Iterator<File> iterator = files.iterator();
			while (iterator.hasNext())
			{
				File file = iterator.next();
				if (file.getName().equals(xmlFileName))
					absoluteFilePath = file.getAbsolutePath();
			}
			if (!absoluteFilePath.isEmpty())
			{
				File file = new File(absoluteFilePath);
				viewMetaData = JaxbPool.unmarshal(ViewMetaData.class, file);
				if (viewMetaData != null)
					jxPathContext = JXPathContext.newContext(viewMetaData);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return viewMetaData;
	}

	/**
	 * Puts all pieces together and sets the response's output stream to the final output result which will be shown on screen.
	 * 
	 * @param viewMetaData
	 * @throws IOException
	 * @throws Exception
	 */
	public void marshall(ViewMetaData viewMetaData)
	{
		try
		{
			XslPool.marshalAndTransform(viewMetaData, response.getOutputStream(), VIEW_META_DATA_FILE, null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void marshallStoreJson(ArrayList<String[]> data)
	{
		try
		{
			marshallStoreJson(data, true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void marshallStoreJson(ArrayList<String[]> data, boolean useSqlColumnNames)
	{
		try
		{
			String[] columnNames = null;
			if (useSqlColumnNames)
				columnNames = data.remove(0);
			else
			{
				columnNames = new String[data.get(0).length];
				for (int i = 0; i < columnNames.length; i++)
					columnNames[i] = "column" + i;
			}

			marshallStoreJson(data, columnNames);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void marshallStoreJson(ArrayList<String[]> data, String[] columnNames)
	{
		try
		{
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonObject responseObject = new JsonObject();
			JsonArray responseArray = new JsonArray();
			responseObject.add("data", responseArray);

			for (String[] rowData : data)
			{
				JsonObject row = new JsonObject();
				responseArray.add(row);
				for (int i = 0; i < rowData.length; i++)
					row.addProperty(columnNames[i], rowData[i]);
			}

			String prettyJsonString = gson.toJson(responseObject);
			response.getOutputStream().write(prettyJsonString.getBytes());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
