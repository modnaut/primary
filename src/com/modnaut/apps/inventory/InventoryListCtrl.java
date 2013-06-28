package com.modnaut.apps.inventory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.apps.farmarkets.MarketListCtrl;
import com.modnaut.common.controllers.ExtJsScreenCtrl;
import com.modnaut.framework.session.WebSession;

public class InventoryListCtrl extends ExtJsScreenCtrl
{

	private static final String XML_FILE = "InventoryList.xml";
	private static final Logger LOGGER = LoggerFactory.getLogger(MarketListCtrl.class);

	public InventoryListCtrl(WebSession webSession)
	{
		super(webSession);
		unmarshall(XML_FILE);
	}
	
	public void defaultAction()
	{
		marshall(viewMetaData);
	}
}
