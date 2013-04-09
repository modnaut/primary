package com.modnaut.apps.farmarkets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.common.controllers.ExtJsScreenCtrl;
import com.modnaut.framework.session.WebSession;

public class MarketListCtrl extends ExtJsScreenCtrl
{
	private static final String XML_FILE = "MarketList.xml";
	private static final Logger LOGGER = LoggerFactory.getLogger(MarketListCtrl.class);

	public MarketListCtrl(WebSession webSession)
	{
		super(webSession);
		unmarshall(XML_FILE);
	}

	public void defaultAction()
	{
		marshall(viewMetaData);
	}
}
