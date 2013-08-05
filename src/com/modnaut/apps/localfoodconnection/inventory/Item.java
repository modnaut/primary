package com.modnaut.apps.localfoodconnection.inventory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.modnaut.common.controllers.ExtJsScreenCtrl;
import com.modnaut.framework.session.WebSession;

public class Item extends ExtJsScreenCtrl
{
	private static Logger LOGGER = LoggerFactory.getLogger(Item.class);

	public Item(WebSession webSession)
	{
		super(webSession);
	}

	public void rest() throws IOException
	{
		switch (webSession.getRestVerb())
		{
			case Delete:
				delete();
				break;
			case Get:
				get();
				break;
			case Post:
				create();
				break;
			case Put:
				update();
				break;
		}
	}

	public void get()
	{
		LOGGER.debug("Item get()");
		marshallStoreJson(new ArrayList<String[]>()
		{
			{
				add(new String[] { "1", "corn" });
			}
		}, new String[] { "id", "product" });
	}

	public void delete() throws IOException
	{
		Integer itemId = webSession.getRestResourceId();
		Gson gson = new Gson();
		Map itemDetails = gson.fromJson(webSession.getRequestBody(), Map.class);
		LOGGER.debug("Item delete() {} {}", itemId, itemDetails);
	}

	public void create()
	{
		LOGGER.debug("Item create()");
	}

	public void update()
	{
		LOGGER.debug("Item update()");
	}
}
