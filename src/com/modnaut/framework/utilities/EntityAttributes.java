package com.modnaut.framework.utilities;

import java.util.HashMap;

import com.modnaut.common.utilities.DatabaseMethods;
import com.modnaut.framework.database.SqlQueries.QUERY_FILE;

public abstract class EntityAttributes {

	protected static String getEntityAttribute(String entityTypeDescription, int entityId, int attributeId)
	{
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put("entityTypeDescription", entityTypeDescription);
		parms.put("entityId", entityId);
		parms.put("attributeId", attributeId);
		return DatabaseMethods.getJustDataFirstRowFirstColumn("GET_ENTITY_ATTRIBUTE_VALUE", QUERY_FILE.COMMON, parms);
	}
}
