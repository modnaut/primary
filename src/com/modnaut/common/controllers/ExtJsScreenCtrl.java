package com.modnaut.common.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.common.utilities.StringMethods;
import com.modnaut.common.utilities.VmdMethods;
import com.modnaut.framework.pools.JaxbPool;
import com.modnaut.framework.pools.XslPool;
import com.modnaut.framework.properties.viewmetadata.AbstractField;
import com.modnaut.framework.properties.viewmetadata.Item;
import com.modnaut.framework.properties.viewmetadata.NotificationType;
import com.modnaut.framework.properties.viewmetadata.ViewMetaData;
import com.modnaut.framework.session.WebSession;
import com.modnaut.framework.utilities.ServerMethods;

/**
 * 
 * @author Jamie Lynn
 * @date 1/9/2013
 * 
 *       Used by all java classes that produce ExtJS-based screens. Takes request sent in from servlet, performs unmarshalling which allows the injection of data, searching on sub objects, etc and then marshalling which puts all the pieces together and sets the response to the final output result.
 * 
 */
public class ExtJsScreenCtrl extends FrameworkCtrl
{
	// logging
	private static final Logger LOGGER = LoggerFactory.getLogger(ExtJsScreenCtrl.class);

	private static final String VIEW_META_DATA_FILE = "ViewMetaData.xsl";
	private static final String VIEW_PATH = "WEB-INF/views";

	private static final String ALL_STRING_OBJECTS = "//*[@stringCd]";
	private static final String ALL_ITEMS_WITH_ACTION_IDS = "//*[@actionId != '']";
	private static final String ALL_ITEMS_REQUIRING_AUTHORIZATION = "//*[@requiresAuthorization=true]";

	protected ViewMetaData viewMetaData;
	protected JXPathContext jxPathContext;

	/**
	 * Implicitly sets the permission of the superclass to not needs authentication.
	 * 
	 * @param webSession
	 */
	public ExtJsScreenCtrl(WebSession webSession)
	{
		this(webSession, false);
	}

	/**
	 * Explicitly sets the permission of the super class whether or not a user must be authenticated.
	 * 
	 * @param webSession
	 * @param needs_authentication
	 */
	public ExtJsScreenCtrl(WebSession webSession, boolean needs_authentication)
	{
		super(webSession, needs_authentication, ICommonConstants.CONTENT_TYPE_JSON);
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
		getStringValues();
		applyPermissions();

		try
		{
			XslPool.marshalAndTransform(viewMetaData, response.getOutputStream(), VIEW_META_DATA_FILE, null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void getStringValues()
	{
		List<com.modnaut.framework.properties.string.String> list = jxPathContext.selectNodes(ALL_STRING_OBJECTS);

		if (list.size() > 0)
		{
			ArrayList<String> stringCds = new ArrayList<String>();
			for (com.modnaut.framework.properties.string.String string : list)
			{
				stringCds.add(string.getStringCd());
			}

			HashMap<String, String> strings = StringMethods.getStringValues(stringCds, "he");
			String stringValue = null;
			for (com.modnaut.framework.properties.string.String string : list)
			{
				stringValue = strings.get(string.getStringCd());
				if (stringValue != null && stringValue != StringMethods.STRING_NOT_FOUND)
					string.setStringCd(stringValue);
			}
		}
	}

	private void applyPermissions()
	{
		List<Item> list = jxPathContext.selectNodes(ALL_ITEMS_WITH_ACTION_IDS);

		if (list.size() > 0)
		{
			ArrayList<Integer> actionIds = new ArrayList<Integer>();
			for (Item item : list)
			{
				actionIds.add(item.getActionId());
			}

			HashMap<Integer, Boolean> permissions = canPerformActions(actionIds);
			for (Item item : list)
			{
				if (!permissions.get(item.getActionId()))
				{
					switch (item.getNoPermissionEffect())
					{
						case DISABLE:
							item.setDisabled(true);
							break;
						case HIDE:
							item.setHidden(true);
							break;
					}
				}
			}
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

	protected void addModalAlert(String alertTextStringCd)
	{
		addModalAlert(alertTextStringCd, null);
	}

	protected void addModalAlert(String alertTextStringCd, String alertTitleStringCd)
	{
		VmdMethods.addModalAlert(viewMetaData, jxPathContext, alertTextStringCd, alertTitleStringCd);
	}

	protected void addNotification(String notificationStringCd, NotificationType type, String panelId)
	{
		VmdMethods.addNotification(viewMetaData, jxPathContext, notificationStringCd, type, panelId);
	}

	protected void addNotification(String notificationStringCd, NotificationType type)
	{
		VmdMethods.addNotification(viewMetaData, jxPathContext, notificationStringCd, type);
	}

	protected boolean validateFieldIsNotEmpty(String fieldId)
	{
		return StringUtils.isNotEmpty(getParameter(fieldId));
	}

	protected boolean validateFieldIsNotEmpty(String fieldId, String error)
	{
		boolean isNotEmpty = validateFieldIsNotEmpty(fieldId);
		if (error == null)
			error = "This field is required";
		if (!isNotEmpty)
		{
			AbstractField field = (AbstractField) findById(fieldId);
			if (field != null)
				field.setActiveError(VmdMethods.getStringObject(error));
		}

		return isNotEmpty;
	}
}
