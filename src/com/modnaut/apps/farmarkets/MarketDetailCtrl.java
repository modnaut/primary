package com.modnaut.apps.farmarkets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.common.controllers.ExtJsScreenCtrl;

public class MarketDetailCtrl extends ExtJsScreenCtrl
{
	private static final String XML_FILE = "MarketDetail.xml";
	private static final Logger LOGGER = LoggerFactory.getLogger(MarketDetailCtrl.class);

	public MarketDetailCtrl(HttpServletRequest request, HttpServletResponse response)
	{
		super(request, response);
		unmarshall(XML_FILE);
	}

	public void defaultAction()
	{
		String marketId = getParameter("MarketId");
		LOGGER.info("Getting detail for MarketId " + marketId);
		marshall(viewMetaData);
	}
}
