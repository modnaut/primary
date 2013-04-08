package com.modnaut.apps.farmarkets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.common.controllers.ExtJsScreenCtrl;

public class MarketListCtrl extends ExtJsScreenCtrl
{
	private static final String XML_FILE = "MarketList.xml";
	private static final Logger LOGGER = LoggerFactory.getLogger(MarketListCtrl.class);

	public MarketListCtrl(HttpServletRequest request, HttpServletResponse response)
	{
		super(request, response);
		unmarshall(XML_FILE);
	}

	public void defaultAction()
	{
		marshall(viewMetaData);
	}
}
