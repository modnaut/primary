package com.modnaut.common.utilities;

import java.util.List;

import org.apache.commons.jxpath.JXPathContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.common.properties.viewmetadata.AbstractStore;
import com.modnaut.common.properties.viewmetadata.ComboBox;
import com.modnaut.common.properties.viewmetadata.DisplayField;
import com.modnaut.common.properties.viewmetadata.GridPanel;
import com.modnaut.common.properties.viewmetadata.Record;
import com.modnaut.common.properties.viewmetadata.RecordField;
import com.modnaut.common.properties.viewmetadata.RecordSet;
import com.modnaut.common.properties.viewmetadata.Store;
import com.modnaut.common.properties.viewmetadata.TextArea;
import com.modnaut.common.properties.viewmetadata.TextField;
import com.modnaut.common.properties.viewmetadata.ViewMetaData;

/**
 * @author Danny Cohn
 * 
 */

public class VmdMethods
{
	private static Logger LOGGER = LoggerFactory.getLogger(VmdMethods.class);

	/**
	 * @param viewMetaData
	 * @param id
	 * @return
	 */
	public static List getMultipleById(ViewMetaData viewMetaData, String id)
	{
		long start = System.currentTimeMillis();
		JXPathContext context = JXPathContext.newContext(viewMetaData);
		long end = System.currentTimeMillis();
		LOGGER.info("context creation took " + (end - start));
		return getMultipleById(viewMetaData, context, id);
	}

	/**
	 * @param viewMetaData
	 * @param context
	 * @param id
	 * @return
	 */
	public static List getMultipleById(ViewMetaData viewMetaData, JXPathContext context, String id)
	{
		long start = System.currentTimeMillis();
		List list = context.selectNodes("//*[@id='" + id + "']");
		long end = System.currentTimeMillis();
		LOGGER.info("getMultipleById(" + id + ") took " + (end - start));
		return list;
	}

	/**
	 * @param viewMetaData
	 * @param id
	 * @return
	 */
	public static Object getSingleById(ViewMetaData viewMetaData, String id)
	{
		long start = System.currentTimeMillis();
		JXPathContext context = JXPathContext.newContext(viewMetaData);
		long end = System.currentTimeMillis();
		LOGGER.info("context creation took " + (end - start));
		return getSingleById(viewMetaData, context, id);
	}

	/**
	 * @param viewMetaData
	 * @param context
	 * @param id
	 * @return
	 */
	public static Object getSingleById(ViewMetaData viewMetaData, JXPathContext context, String id)
	{
		long start = System.currentTimeMillis();
		Object object = context.selectSingleNode("//*[@id='" + id + "']");
		long end = System.currentTimeMillis();
		LOGGER.info("getSingleById(" + id + ") took " + (end - start));
		return object;
	}

	/**
	 * @param viewMetaData
	 * @param id
	 * @param data
	 * @return
	 */
	public static boolean populateData(ViewMetaData viewMetaData, String id, List data)
	{
		JXPathContext context = JXPathContext.newContext(viewMetaData);
		return populateData(viewMetaData, context, id, data);
	}

	/**
	 * @param viewMetaData
	 * @param context
	 * @param id
	 * @param data
	 * @return
	 */
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

	/**
	 * @param viewMetaData
	 * @param context
	 * @param id
	 * @param data
	 * @return
	 */
	public static boolean populateData(ViewMetaData viewMetaData, JXPathContext context, String id, String data)
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

	/**
	 * @param element
	 * @param data
	 */
	public static void populateData(Object element, List data)
	{
		if (element instanceof GridPanel)
			populateGrid((GridPanel) element, data);
		else if (element instanceof ComboBox)
			populateComboBox((ComboBox) element, data);
	}

	/**
	 * @param element
	 * @param data
	 */
	public static void populateData(Object element, String data)
	{
		if (element instanceof ComboBox)
			populateComboBox((ComboBox) element, data);
		if (element instanceof TextField)
			populateTextField((TextField) element, data);
		if (element instanceof TextArea)
			populateTextArea((TextArea) element, data);
		if (element instanceof DisplayField)
			populateDisplayField((DisplayField) element, data);
	}

	/**
	 * @param gridPanel
	 * @param data
	 */
	public static void populateGrid(GridPanel gridPanel, List<Object[]> data)
	{
		AbstractStore abstractStore = gridPanel.getStore();
		RecordSet recordSet = null;
		if (abstractStore instanceof Store)
		{
			Store store = (Store) abstractStore;
			recordSet = store.getData();
		}

		if (recordSet == null)
		{
			recordSet = gridPanel.getData();
		}

		if (recordSet != null)
		{
			populateRecordSet(recordSet, data);
		}
	}

	/**
	 * @param recordSet
	 * @param data
	 */
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
						newField.setValue(recordData[populationIndex].toString());
					newRecord.getField().add(newField);
				}
				records.add(newRecord);
			}
		}
	}

	/**
	 * @param element
	 * @param data
	 */
	public static void populateComboBox(ComboBox element, List<Object[]> data)
	{
		AbstractStore abstractStore = element.getStore();
		RecordSet recordSet = null;
		if (abstractStore instanceof Store)
		{
			Store store = (Store) abstractStore;
			recordSet = store.getData();
		}

		if (recordSet == null)
		{
			recordSet = element.getData();
		}

		if (recordSet != null)
		{
			populateRecordSet(recordSet, data);
		}
	}

	/**
	 * @param element
	 * @param data
	 */
	public static void populateComboBox(ComboBox element, String data)
	{
		element.setValue(data);
	}

	/**
	 * @param element
	 * @param data
	 */
	public static void populateTextField(TextField element, String data)
	{
		element.setMaxLength(2);
		element.setValue(data);
	}

	/**
	 * @param element
	 * @param data
	 */
	public static void populateTextArea(TextArea element, String data)
	{
		element.setValue(data);
	}

	/**
	 * @param element
	 * @param data
	 */
	public static void populateDisplayField(DisplayField element, String data)
	{
		element.setValue(data);
	}

	/**
	 * @param viewMetaData
	 * @param context
	 * @param id
	 */
	public static void deleteElement(ViewMetaData viewMetaData, JXPathContext context, String id)
	{
		long start = System.currentTimeMillis();

		context.removeAll("//*[@id='" + id + "']");

		long end = System.currentTimeMillis();
		LOGGER.info("deleteElement deleted " + id + " from page took " + (end - start));
	}

	/**
	 * @param viewMetaData
	 * @param context
	 * @param id
	 * @param data
	 * @param minTextLength
	 * @param maxTextLength
	 * @param set_disabled
	 * @return
	 */
	public static boolean populateTextFieldElement(ViewMetaData viewMetaData, JXPathContext context, String id, String data, int label_width, int label_height, int min_text_length, int max_text_length, boolean set_disabled)
	{
		boolean found = false;

		List elements = getMultipleById(viewMetaData, context, id);
		if (elements.size() > 0)
		{
			found = true;
			for (Object element : elements)
			{
				LOGGER.info("populating data on " + element.getClass().getCanonicalName());
				populateTextFieldElement((TextField) element, data, label_width, label_height, min_text_length, max_text_length, set_disabled);
			}
		}
		return found;
	}

	/**
	 * @param element
	 * @param data
	 * @param minTextLength
	 * @param maxTextLength
	 * @param set_disabled
	 */
	public static void populateTextFieldElement(TextField element, String data, int label_width, int label_height, int min_text_length, int max_text_length, boolean set_disabled)
	{
		element.setValue(data);

		if (label_height > 0)
			element.setHeight(label_height);

		if (label_width > 0)
			element.setLabelWidth(label_width);

		if (min_text_length > 0)
			element.setMinLength(min_text_length);

		if (max_text_length > 0)
			element.setMaxLength(max_text_length);

		element.setDisabled(set_disabled);
	}

	/**
	 * @param id
	 * @param data
	 * @param label_width
	 * @param label_height
	 * @param min_text_length
	 * @param max_text_length
	 * @param set_disabled
	 * @return
	 */
	public static TextField getTextFieldElement(String id, String data, int label_width, int label_height, int min_text_length, int max_text_length, boolean set_disabled)
	{
		TextField element = new TextField();

		element.setId(id);
		element.setValue(data);

		if (label_height > 0)
			element.setHeight(label_height);

		if (label_width > 0)
			element.setLabelWidth(label_width);

		if (min_text_length > 0)
			element.setMinLength(min_text_length);

		if (max_text_length > 0)
			element.setMaxLength(max_text_length);

		element.setDisabled(set_disabled);

		return element;
	}
}
