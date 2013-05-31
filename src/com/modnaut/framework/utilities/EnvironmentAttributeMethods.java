package com.modnaut.framework.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnvironmentAttributeMethods extends EntityAttributeMethods {

	private static final Logger LOGGER = LoggerFactory.getLogger(EnvironmentAttributeMethods.class);
	private static final String CLASS_NAME_PATH = EnvironmentAttributeMethods.class.getCanonicalName();
	
	public static String getAttribute(ATTRIBUTES attribute)
	{
		return getEntityAttribute(ENTITY_TYPES.ENVIRONMENT, EnvironmentMethods.getEnvironmentName(), attribute);
	}
	
	public static boolean getAttributeBoolean(ATTRIBUTES attribute)
	{
		return getEntityAttributeBoolean(ENTITY_TYPES.ENVIRONMENT, EnvironmentMethods.getEnvironmentName(), attribute);
	}
	
	public static Integer getAttributeInteger(ATTRIBUTES attribute)
	{
		return getEntityAttributeInteger(ENTITY_TYPES.ENVIRONMENT, EnvironmentMethods.getEnvironmentName(), attribute);
	}
	
	public static Float getAttributeFloat(ATTRIBUTES attribute)
	{
		return getEntityAttributeFloat(ENTITY_TYPES.ENVIRONMENT, EnvironmentMethods.getEnvironmentName(), attribute);
	}
}
