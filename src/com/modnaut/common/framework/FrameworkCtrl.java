package com.modnaut.common.framework;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import com.modnaut.common.properties.viewmetadata.ViewMetaData;
import com.modnaut.common.servlet.ApplicationServlet;
import com.modnaut.common.utilities.JaxbPool;
import com.modnaut.common.utilities.XslPool;

public class FrameworkCtrl
{

	protected HttpServletRequest request;
	protected HttpServletResponse response;

	public FrameworkCtrl(HttpServletRequest request, HttpServletResponse response)
	{
		this.request = request;
		this.response = response;
	}

	protected ViewMetaData viewMetaData;

	protected ViewMetaData unmarshall(String xmlFileName)
	{
		try
		{
			Collection<File> files = FileUtils.listFiles(new File(ApplicationServlet.getRealPath() + "WEB-INF/views"), null, true);
			String absoluteFilePath = "";
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

	public void marshall(ViewMetaData viewMetaData) throws IOException, Exception
	{
		XslPool.marshalAndTransform(viewMetaData, response.getOutputStream(), "ViewMetaData.xsl", null, true);
	}

	protected String getParameter(String name)
	{
		return this.request.getParameter(name);
	}
}
