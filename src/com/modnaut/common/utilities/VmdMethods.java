package com.modnaut.common.utilities;

import java.util.List;

import org.apache.commons.jxpath.JXPathContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.common.properties.viewmetadata.AbstractStore;
import com.modnaut.common.properties.viewmetadata.GridPanel;
import com.modnaut.common.properties.viewmetadata.Record;
import com.modnaut.common.properties.viewmetadata.RecordField;
import com.modnaut.common.properties.viewmetadata.RecordSet;
import com.modnaut.common.properties.viewmetadata.Store;
import com.modnaut.common.properties.viewmetadata.ViewMetaData;

public class VmdMethods
{
	private static Logger LOGGER = LoggerFactory.getLogger(VmdMethods.class);

	public static List getMultipleById(ViewMetaData viewMetaData, String id)
	{
		long start = System.currentTimeMillis();
		JXPathContext context = JXPathContext.newContext(viewMetaData);
		long end = System.currentTimeMillis();
		LOGGER.info("context creation took " + (end - start));
		return getMultipleById(viewMetaData, context, id);
	}

	public static List getMultipleById(ViewMetaData viewMetaData, JXPathContext context, String id)
	{
		long start = System.currentTimeMillis();
		List list = context.selectNodes("//*[@id='" + id + "']");
		long end = System.currentTimeMillis();
		LOGGER.info("getMultipleById(" + id + ") took " + (end - start));
		return list;
	}

	public static Object getSingleById(ViewMetaData viewMetaData, String id)
	{
		long start = System.currentTimeMillis();
		JXPathContext context = JXPathContext.newContext(viewMetaData);
		long end = System.currentTimeMillis();
		LOGGER.info("context creation took " + (end - start));
		return getSingleById(viewMetaData, context, id);
	}

	public static Object getSingleById(ViewMetaData viewMetaData, JXPathContext context, String id)
	{
		long start = System.currentTimeMillis();
		Object object = context.selectSingleNode("//*[@id='" + id + "']");
		long end = System.currentTimeMillis();
		LOGGER.info("getSingleById(" + id + ") took " + (end - start));
		return object;
	}

	public static boolean populateData(ViewMetaData viewMetaData, String id, List data)
	{
		JXPathContext context = JXPathContext.newContext(viewMetaData);
		return populateData(viewMetaData, context, id, data);
	}

	public static boolean populateData(ViewMetaData viewMetaData, JXPathContext context, String id, List data)
	{
		boolean found = false;

		List elements = getMultipleById(viewMetaData, context, id);
		if (elements.size() > 0)
		{
			found = true;
			for (Object element : elements)
			{
				LOGGER.info("populating data on " + element.getClass().getCanonicalName());
				populateData(element, data);
			}
		}
		return found;
	}

	public static void populateData(Object element, List data)
	{
		if (element instanceof GridPanel)
			populateGrid((GridPanel) element, data);
	}

	public static void populateGrid(GridPanel gridPanel, List<Object[]> data)
	{
		AbstractStore abstractStore = gridPanel.getStore();
		RecordSet recordSet = null;
		if (abstractStore != null)
		{
			if (abstractStore instanceof Store)
			{
				Store store = (Store) abstractStore;
				recordSet = store.getData();
			}
		}

		if (recordSet == null)
		{
			recordSet = gridPanel.getRecords();
		}

		if (recordSet != null)
		{
			populateRecordSet(recordSet, data);
		}
	}

	public static void populateRecordSet(RecordSet recordSet, List<Object[]> data)
	{
		List<Record> records = recordSet.getRecord();
		if (records.size() > 0)
		{
			// use last record as template. This allows static records to be defined that will be before all dynamic records
			Record templateRecord = records.get(records.size() - 1);
			records.remove(templateRecord);

			for (Object[] recordData : data)
			{
				Record newRecord = new Record();
				for (int i = 0; i < templateRecord.getField().size(); i++)
				{
					RecordField templateField = templateRecord.getField().get(i);
					RecordField newField = new RecordField();
					newField.setName(templateField.getName());
					Integer populationIndex = templateField.getPopulationIndex();
					if (populationIndex == null)// if populationIndex not in XML, just use index of field within the record
						populationIndex = i;
					if (i < recordData.length)
						newField.setValue((String) recordData[populationIndex]);
					newRecord.getField().add(newField);
				}
				records.add(newRecord);
			}
		}
	}
}
