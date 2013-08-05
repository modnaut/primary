package com.modnaut.apps.localfoodconnection.inventory;

import com.modnaut.common.controllers.ExtJsScreenCtrl;
import com.modnaut.framework.session.WebSession;

public class InventoryCtrl extends ExtJsScreenCtrl
{
	private static final String XML_FILE = "Inventory.xml";

	public InventoryCtrl(WebSession webSession)
	{
		super(webSession);
		unmarshall(XML_FILE);
	}

	public void defaultAction()
	{
		marshall(viewMetaData);
	}
}
