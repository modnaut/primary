package com.modnaut.apps.farmarkets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.common.controllers.ExtJsScreenCtrl;
import com.modnaut.framework.session.WebSession;

public class AddReviewCtrl extends ExtJsScreenCtrl
{
	private static final String XML_FILE = "AddReview.xml";
	private static final Logger LOGGER = LoggerFactory.getLogger(AddReviewCtrl.class);

	/**
	 * Constructor that requires ninja be authenticated to see page.
	 * 
	 * @param webSession
	 */
	public AddReviewCtrl(WebSession webSession)
	{
		super(webSession, true); // this class needs authentication
		unmarshall(XML_FILE);
	}

	public void defaultAction()
	{
		String marketId = getParameter("MarketId");
		LOGGER.info("Getting detail for MarketId " + marketId);
		marshall(viewMetaData);
	}
}
