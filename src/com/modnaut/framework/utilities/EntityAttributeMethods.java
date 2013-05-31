package com.modnaut.framework.utilities;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.common.utilities.DatabaseMethods;
import com.modnaut.framework.database.SqlQueries.QUERY_FILE;

public abstract class EntityAttributeMethods {

	private static final Logger LOGGER = LoggerFactory.getLogger(EntityAttributeMethods.class);
	private static final String CLASS_NAME_PATH = EntityAttributeMethods.class.getCanonicalName();
	private static final String GET_ENTITY_ATTRIBUTE_VALUE_BY_NAMES = "GET_ENTITY_ATTRIBUTE_VALUE_BY_NAMES";
	private static final String GET_ENTITY_ATTRIBUTE_VALUE = "GET_ENTITY_ATTRIBUTE_VALUE";
	private static final String UPSERT_ATTRIBUTE_VALUE = "UPSERT_ATTRIBUTE_VALUE";
	private static final String ENTITY_TYPE_NAME = "entityTypeName";
	private static final String ENTITY_NAME = "entityName";
	private static final String ENTITY_ID = "entityId";
	private static final String ATTRIBUTE_NAME = "attributeName";
	
	public static enum ENTITY_TYPES
	{
		ENVIRONMENT("Environment");
		
		private String name;
		private ENTITY_TYPES(String name)
		{
			this.name = name;
		}
		public String toString() {
			return this.name;
		}
	};
	
	public static enum ATTRIBUTES
	{
		CACHE_XSL
	};
	
	protected static String getEntityAttribute(ENTITY_TYPES entityType, String entityName, ATTRIBUTES attribute)
	{
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put(ENTITY_TYPE_NAME, entityType.toString());
		parms.put(ENTITY_NAME, entityName);
		parms.put(ATTRIBUTE_NAME, attribute.toString());
		return DatabaseMethods.getJustDataFirstRowFirstColumn(GET_ENTITY_ATTRIBUTE_VALUE_BY_NAMES, QUERY_FILE.COMMON, parms);
	}
	
	protected static String getEntityAttribute(ENTITY_TYPES entityType, int entityId, ATTRIBUTES attribute)
	{
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put(ENTITY_TYPE_NAME, entityType.toString());
		parms.put(ENTITY_ID, entityId);
		parms.put(ATTRIBUTE_NAME, attribute.toString());
		return DatabaseMethods.getJustDataFirstRowFirstColumn(GET_ENTITY_ATTRIBUTE_VALUE, QUERY_FILE.COMMON, parms);
	}
	
	protected static boolean getEntityAttributeBoolean(ENTITY_TYPES entityType, String entityName, ATTRIBUTES attribute)
	{
		String value = getEntityAttribute(entityType, entityName, attribute);
		return (value != null && value.equals(ICommonConstants.LETTER_Y));
	}
	
	protected static Integer getEntityAttributeInteger(ENTITY_TYPES entityType, String entityName, ATTRIBUTES attribute)
	{
		Integer value = null;
		String stringValue = getEntityAttribute(entityType, entityName, attribute);
		if(stringValue != null)
		{
			try
			{
				value = Integer.parseInt(stringValue);
			}
			catch(NumberFormatException e)
			{}
		}
		return value;
	}
	
	protected static Float getEntityAttributeFloat(ENTITY_TYPES entityType, String entityName, ATTRIBUTES attribute)
	{
		Float value = null;
		String stringValue = getEntityAttribute(entityType, entityName, attribute);
		if(stringValue != null)
		{
			try
			{
				value = Float.parseFloat(stringValue);
			}
			catch(NumberFormatException e)
			{}
		}
		return value;
	}
	
	protected static boolean getEntityAttributeBoolean(ENTITY_TYPES entityType, int entityId, ATTRIBUTES attribute)
	{
		String value = getEntityAttribute(entityType, entityId, attribute);
		return (value != null && value.equals(ICommonConstants.LETTER_Y));
	}
	
	protected static Integer getEntityAttributeInteger(ENTITY_TYPES entityType, int entityId, ATTRIBUTES attribute)
	{
		Integer value = null;
		String stringValue = getEntityAttribute(entityType, entityId, attribute);
		if(stringValue != null)
		{
			try
			{
				value = Integer.parseInt(stringValue);
			}
			catch(NumberFormatException e)
			{}
		}
		return value;
	}
	
	protected static Float getEntityAttributeFloat(ENTITY_TYPES entityType, int entityId, ATTRIBUTES attribute)
	{
		Float value = null;
		String stringValue = getEntityAttribute(entityType, entityId, attribute);
		if(stringValue != null)
		{
			try
			{
				value = Float.parseFloat(stringValue);
			}
			catch(NumberFormatException e)
			{}
		}
		return value;
	}
	
//	public static void upsertEntityAttribute(ENTITY_TYPES entityType, int entityId, ATTRIBUTES attribute)
//	{
//		HashMap<String, Object> parms = new HashMap<String, Object>();
//		parms.put(ENTITY_TYPE_NAME, entityType.toString());
//		parms.put(ENTITY_ID, entityId);
//		parms.put(ATTRIBUTE_NAME, attribute.toString());
//		DatabaseMethods.updateData(UPSERT_ATTRIBUTE_VALUE, QUERY_FILE.COMMON, parms);
//	}
}
