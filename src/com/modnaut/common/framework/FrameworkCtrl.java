package com.modnaut.common.framework;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.jxpath.JXPathContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.common.properties.viewmetadata.ViewMetaData;
import com.modnaut.common.servlet.ApplicationServlet;
import com.modnaut.common.utilities.JaxbPool;
import com.modnaut.common.utilities.VmdMethods;
import com.modnaut.common.utilities.XslPool;

/**
 * 
 * @author Jamie Lynn
 * @date 1/9/2013
 * 
 *       Used by all java classes that produce output. Takes request sent in from servlet, performs unmarshalling which allows the injection of data, searching on sub objects, etc and then marshalling which puts all the pieces together and sets the response to the final output result.
 * 
 */
public class FrameworkCtrl
{
	private static final String VIEW_META_DATA_FILE = "ViewMetaData.xsl";
	private static final String VIEW_PATH = "WEB-INF/views";

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected ViewMetaData viewMetaData;
	protected JXPathContext jxPathContext;

	/**
	 * 
	 * @param request
	 * @param response
	 */
	public FrameworkCtrl(HttpServletRequest request, HttpServletResponse response)
	{
		this.request = request;
		this.response = response;
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
			Collection<File> files = FileUtils.listFiles(new File(ApplicationServlet.getRealPath() + VIEW_PATH), null, true);
			String absoluteFilePath = ICommonConstants.NONE;
			Iterator<File> iterator = files.iterator();
			while (iterator.hasNext())
			{
				File file = (File) iterator.next();
				if (file.getName().equals(xmlFileName))
					absoluteFilePath = file.getAbsolutePath();
			}
			if (!absoluteFilePath.isEmpty())
			{
				File file = new File(absoluteFilePath);
				viewMetaData = (ViewMetaData) JaxbPool.unmarshal(ViewMetaData.class, file);
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
	public void marshall(ViewMetaData viewMetaData) throws IOException, Exception
	{
		XslPool.marshalAndTransform(viewMetaData, response.getOutputStream(), VIEW_META_DATA_FILE, null);
	}

	public void marshallStoreJson(ArrayList<String[]> data) throws IOException
	{
		marshallStoreJson(data, true);
	}

	public void marshallStoreJson(ArrayList<String[]> data, boolean useSqlColumnNames) throws IOException
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

	public void marshallStoreJson(ArrayList<String[]> data, String[] columnNames) throws IOException
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

	/**
	 * Returns a specific value from request based on name of the value in the request. For example, if the attribute name of a html element on screen is 'username', then it will return the value of this html element. Can also be used to retrieve the value off a url that contains parameters and hidden values on screen.
	 * 
	 * @param name
	 * @return
	 */
	protected String getParameter(String name)
	{
		return this.request.getParameter(name);
	}

	protected Object findById(String id)
	{
		return VmdMethods.getSingleById(viewMetaData, jxPathContext, id);
	}

	protected List findMultipleById(String id)
	{
		return VmdMethods.getMultipleById(viewMetaData, jxPathContext, id);
	}

	protected boolean populateData(String id, List data)
	{
		return VmdMethods.populateData(viewMetaData, jxPathContext, id, data);
	}

	protected boolean populateData(String id, String data)
	{
		return VmdMethods.populateData(viewMetaData, jxPathContext, id, data);
	}

	protected void deleteElement(String id)
	{
		VmdMethods.deleteElement(viewMetaData, jxPathContext, id);
	}
}
