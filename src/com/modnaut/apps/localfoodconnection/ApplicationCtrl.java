package com.modnaut.apps.localfoodconnection;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.common.utilities.DatabaseMethods;
import com.modnaut.framework.database.SqlQueries.QUERY_FILE;
import com.modnaut.framework.session.WebSession;

/**
 * 
 * @author Danny Cohn
 * @date 1/9/2013
 * 
 */
public class ApplicationCtrl extends com.modnaut.common.controllers.ApplicationCtrl
{
	// exception handling
	private static final String CLASS_NAME_PATH = ApplicationCtrl.class.getCanonicalName();
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationCtrl.class);

	/**
	 * 
	 * @param request
	 * @param response
	 */
	public ApplicationCtrl(WebSession ninjaSessionObject)
	{
		super(ninjaSessionObject);
	}

	protected JsonObject getConfig()
	{
		JsonObject config = new JsonObject();

		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put(ICommonConstants.MENU_ID, 1);
		parms.put(ICommonConstants.NINJA_ID, ninjaSession.getNinjaId());

		// [0]MenuItemId [1]ParentMenuItemId [2]Text [3]IconCls [4]Class [5]Method [6]URL
		ArrayList<String[]> menuData = DatabaseMethods.getJustData("GET_MENU", QUERY_FILE.COMMON, parms);
		JsonArray menu = new JsonArray();
		String currentMenuItemId = null;
		for (String[] menuItem : menuData)
		{
			JsonObject menuItemObject = new JsonObject();
			menuItemObject.addProperty("text", menuItem[2]);
			if (!StringUtils.isEmpty(menuItem[3]))
				menuItemObject.addProperty("iconCls", menuItem[3]);
			if (!StringUtils.isEmpty(menuItem[4]))
				menuItemObject.addProperty("Class", menuItem[4]);
			if (!StringUtils.isEmpty(menuItem[5]))
				menuItemObject.addProperty("Class", menuItem[5]);
			if (!StringUtils.isEmpty(menuItem[6]))
				menuItemObject.addProperty("Class", menuItem[6]);
			LOGGER.debug("Menu Item {}", menuItem);
		}
		return config;
	}
}
