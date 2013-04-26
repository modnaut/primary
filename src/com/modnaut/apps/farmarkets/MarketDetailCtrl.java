package com.modnaut.apps.farmarkets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.common.controllers.ExtJsScreenCtrl;
import com.modnaut.framework.properties.viewmetadata.Panel;
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
		String search = getParameter("search");
		Panel detailPanel = (Panel) findById("detailPanel");
		detailPanel.getTitle().setStringCd("Searched for: '" + search + "' Market ID " + marketId);
		marshall(viewMetaData);
	}
}
