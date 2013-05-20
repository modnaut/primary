package com.modnaut.apps.farmarkets;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.common.controllers.ExtJsScreenCtrl;
import com.modnaut.common.utilities.DatabaseMethods;
import com.modnaut.framework.database.SqlQueries.QUERY_FILE;
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
		ArrayList<String[]> allMarkets = DatabaseMethods.getJustData("GET_ALL_MARKETS", QUERY_FILE.MARKET_LINK);
		populateData("marketView", allMarkets);
		marshall(viewMetaData);
	}

	public void getMarketsJson()
	{
		unmarshallJson("Markets.json");
		ArrayList<String[]> allMarkets = DatabaseMethods.getJustData("GET_ALL_MARKETS", QUERY_FILE.MARKET_LINK);
		populateDataJson("markets", allMarkets);
		marshall(json);
	}
}
