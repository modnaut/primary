package com.modnaut.apps.farmarkets;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.common.controllers.ExtJsScreenCtrl;
import com.modnaut.common.utilities.DatabaseMethods;
import com.modnaut.framework.database.SqlQueries.QUERY_FILE;
import com.modnaut.framework.session.WebSession;

public class MarketDetailCtrl extends ExtJsScreenCtrl
{
	private static final String XML_FILE = "MarketDetail.xml";
	private static final Logger LOGGER = LoggerFactory.getLogger(MarketDetailCtrl.class);

	public MarketDetailCtrl(WebSession webSession)
	{
		super(webSession);
		unmarshall(XML_FILE);
	}

	public void defaultAction()
	{
		String marketId = getParameter("MarketId");
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put("MarketId", marketId);
		String[] marketDetails = DatabaseMethods.getJustDataFirstRow("GET_MARKET_DETAILS", QUERY_FILE.MARKET_LINK, parms);
		populateData("MarketName", marketDetails[0]);
		populateData("Address", marketDetails[1]);
		populateData("City", marketDetails[2]);
		populateData("State", marketDetails[3]);
		populateData("ZipCode", marketDetails[4]);
		populateData("Url", marketDetails[5]);
		marshall(viewMetaData);
	}
}
