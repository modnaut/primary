package com.modnaut.common.utilities;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Danny Cohn
 * 
 */

public class JsonMethods
{
	private static final String CLASS_NAME = JsonMethods.class.getCanonicalName();
	private static Logger LOGGER = LoggerFactory.getLogger(JsonMethods.class);

	public static boolean populateData(Map json, String id, List data)
	{
		boolean found = false;

		Object element = findJsonElement(json, id);
		if (element != null)
		{
			found = true;
			if (element instanceof List)
			{
				populateData((List) element, data);
			}
		}

		return found;
	}

	public static Object findJsonElement(Map json, String id)
	{
		Object element = null;

		if (json != null && !StringUtils.isEmpty(id))
		{
			if (json.containsKey(id))
				element = json.get(id);
			else
			{
				for (Object key : json.keySet())
				{
					Object member = json.get(key);
					if (member instanceof Map)
					{
						element = findJsonElement((Map) member, id);
						if (element != null)
						{
							break;
						}
					}
					else if (member instanceof List)
					{
						element = findJsonElement((List) member, id);
					}
				}
			}
		}

		return element;
	}

	public static Object findJsonElement(List list, String id)
	{
		Object element = null;

		for (Object member : list)
		{
			if (member instanceof Map)
			{
				element = findJsonElement((Map) member, id);
				if (element != null)
					break;
			}
			else if (member instanceof List)
			{
				element = findJsonElement((List) member, id);
				if (element != null)
					break;
			}
		}

		return element;
	}

	public static void populateData(List list, List<Object[]> data)
	{
		if (list.size() > 0 && list.get(list.size() - 1) instanceof LinkedHashMap)
		{
			LinkedHashMap templateMember = (LinkedHashMap) list.remove(list.size() - 1);
			for (Object[] row : data)
			{
				LinkedHashMap record = new LinkedHashMap();
				int i = 0;
				for (Object key : templateMember.keySet())
				{
					record.put(key, row[i++]);
				}
				list.add(record);
			}
		}
	}
}
