package com.modnaut.common.framework;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.common.properties.viewmetadata.ViewMetaData;
import com.modnaut.common.servlet.ApplicationServlet;
import com.modnaut.common.utilities.JaxbPool;
import com.modnaut.common.utilities.XslPool;

/**
 * 
 * @author Jamie Lynn
 * @date 1/9/2013
 * 
 *       Used by all java classes that produce output. Takes request sent in from servlet, performs unmarshalling
 *       which allows the injection of data, searching on sub objects, etc and then marshalling which puts all the pieces
 *       together and sets the response to the final output result.
 * 
 */
public class FrameworkCtrl
{
	private static final String VIEW_META_DATA_FILE = "ViewMetaData.xsl";
	private static final String VIEW_PATH = "WEB-INF/views";

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected ViewMetaData viewMetaData;

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
	 * Unmarshalls xmlfile into a viewmetadata object that will be used by the java classes to manipulate, mold, break down
	 * into smaller sub objects and inject data.
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
		XslPool.marshalAndTransform(viewMetaData, response.getOutputStream(), VIEW_META_DATA_FILE, null, true);
	}

	/**
	 * Returns a specific value from request based on name of the value in the request. For example, if the attribute name of a
	 * html element on screen is 'username', then it will return the value of this html element. Can also be used to retrieve the
	 * value off a url that contains parameters and hidden values on screen.
	 * 
	 * @param name
	 * @return
	 */
	protected String getParameter(String name)
	{
		return this.request.getParameter(name);
	}
}
