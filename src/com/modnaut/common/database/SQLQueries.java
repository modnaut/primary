package com.modnaut.common.database;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.common.properties.Query;
import com.modnaut.common.properties.SqlMetaData;

public class SQLQueries {
	// constants
	private static final String FILE_PATH = "M:/development/primary/xml/";
	private static final String XML_EXTENSION = ".xml";
	private static final ArrayList<String> sqlFiles = new ArrayList<String>() {
		private static final long serialVersionUID = -6540054895936607379L;

		{
			add(ICommonConstants.COMMON);
		}
	};

	// private variables
	private static HashMap<String, HashMap<String, Query>> allQueries = null;

	private static void unmarshalSqlMetaData() {
		for (String file : sqlFiles) {
			loadFile(file);
		}
	}

	private static void loadFile(String fileName) {
		try {
			allQueries = new HashMap<String, HashMap<String, Query>>();
			HashMap<String, Query> hashMap = new HashMap<String, Query>();
			
			File file = new File(FILE_PATH + fileName + XML_EXTENSION);
			JAXBContext jaxbContext = JAXBContext.newInstance(SqlMetaData.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			SqlMetaData sqlmetadata = (SqlMetaData) jaxbUnmarshaller.unmarshal(file);

			List<Query> queryList = sqlmetadata.getQuery();
			if (queryList != null) {
				for (int i = 0; queryList.size() > i; i++) {
					Query q = queryList.get(i);

					hashMap.put(q.getName(), q);
				}
			}
			
			allQueries.put(fileName, hashMap);
		} catch (JAXBException e) {
			System.out.println("Could not unmarshall " + fileName + " SqlMetaData.");
		} catch (NullPointerException npe) {
			System.out.println("Could not find SqlMetaData file: " + FILE_PATH + fileName + XML_EXTENSION);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Query getQuery(String varCode, String applicationId) {
		if (allQueries == null)
			unmarshalSqlMetaData();

		return allQueries.get(applicationId).get(varCode);
	}

}
