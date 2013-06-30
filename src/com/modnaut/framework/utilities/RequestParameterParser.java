package com.modnaut.framework.utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.framework.pools.XslPool;

public class RequestParameterParser
{
	private static Logger LOGGER = LoggerFactory.getLogger(RequestParameterParser.class);
	private static final String CLASS_NAME_PATH = XslPool.class.getCanonicalName();
	private static final String REQUEST_ID_ATTRIBUTE = CLASS_NAME_PATH + ".requestId";
	private static HashMap<String, HashMap<String, String>> MULTI_PART_FIELDS = new HashMap<String, HashMap<String, String>>();
	private static HashMap<String, List<FileItem>> FILES = new HashMap<String, List<FileItem>>();
	private static HashMap<String, HashMap<String, FileItem>> FILES_BY_NAME = new HashMap<String, HashMap<String, FileItem>>();
	private static HashMap<String, HashMap<String, String>> HEADER_PARAMETERS = new HashMap<String, HashMap<String, String>>();

	private HttpServletRequest request;
	private boolean isMultiPart = false;
	private HashMap<String, String> multiPartFields;
	private HashMap<String, String> headerParameters;
	private List<FileItem> files;
	private HashMap<String, FileItem> filesByName;
	private HashMap<String, String> extraParameters;

	public RequestParameterParser(HttpServletRequest request)
	{
		this.request = request;

		isMultiPart = ServletFileUpload.isMultipartContent(request);
		LOGGER.debug("Is multipart upload: {}", isMultiPart);

		String requestId = (String) request.getAttribute(REQUEST_ID_ATTRIBUTE);
		if (requestId != null)
		{
			multiPartFields = MULTI_PART_FIELDS.get(requestId);
			files = FILES.get(requestId);
			filesByName = FILES_BY_NAME.get(requestId);
		}
		else
		{
			requestId = UUID.randomUUID().toString();
			request.setAttribute(REQUEST_ID_ATTRIBUTE, requestId);
			multiPartFields = new HashMap<String, String>();
			files = new ArrayList<FileItem>();
			filesByName = new HashMap<String, FileItem>();
			headerParameters = new HashMap<String, String>();
			MULTI_PART_FIELDS.put(requestId, multiPartFields);
			FILES.put(requestId, files);
			FILES_BY_NAME.put(requestId, filesByName);
			HEADER_PARAMETERS.put(requestId, headerParameters);
			if (isMultiPart)
				parseMultiPartParameters();
			parseHeaderParameters();
		}
	}

	public void setExtraParameters(HashMap<String, String> extraParameters)
	{
		this.extraParameters = extraParameters;
	}

	public String getParameter(String name)
	{
		LOGGER.debug("Getting parameter {}", name);
		String value = request.getParameter(name);
		LOGGER.debug("Parameter {} from request: {}", name, value);

		if (value == null)
		{
			if (extraParameters != null)
			{
				value = extraParameters.get(name);
			}

			if (value == null)
			{
				if (isMultiPart)
				{
					value = getMultiPartParameter(name);
					LOGGER.debug("Parameter {} from multipart: {}", name, value);
				}
				else
				{
					value = headerParameters.get(name.toLowerCase());
					LOGGER.debug("Parameter {} from header: {}", name, value);
				}
			}
		}

		return value;
	}

	public FileItem getUploadedFile()
	{
		FileItem fileItem = null;

		LOGGER.info("Uploaded file count: {}", files.size());
		if (files.size() > 0)
			fileItem = files.get(0);

		return fileItem;
	}

	private void parseMultiPartParameters()
	{
		LOGGER.debug("Parsing multipart parameters");
		try
		{
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletContext servletContext = request.getServletContext();
			File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
			LOGGER.info("File upload repository: {}", repository.getAbsolutePath());
			factory.setRepository(repository);
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> items = upload.parseRequest(request);
			Iterator<FileItem> iterator = items.iterator();
			while (iterator.hasNext())
			{
				FileItem item = iterator.next();
				if (item.isFormField())
				{
					LOGGER.debug("item {} is formfield: {}", item.getFieldName(), item.getString());
					multiPartFields.put(item.getFieldName(), item.getString());
				}
				else
				{
					LOGGER.debug("item {} is not form field", item.getName());
					files.add(item);
					filesByName.put(item.getFieldName(), item);
					filesByName.put(item.getName(), item);
				}
			}
		}
		catch (FileUploadException e)
		{
			e.printStackTrace();
		}
	}

	private void parseHeaderParameters()
	{
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements())
		{
			String headerName = headerNames.nextElement();
			LOGGER.debug("Header parameter {}: {}", new String[] { headerName, request.getHeader(headerName) });
			if (headerName.startsWith("extraparam_"))
			{
				LOGGER.debug("Header parameter {}: {}", headerName.substring(11), request.getHeader(headerName));
				headerParameters.put(headerName.substring(11), request.getHeader(headerName));
			}
		}
	}

	private String getMultiPartParameter(String name)
	{
		return multiPartFields.get(name);
	}
}
