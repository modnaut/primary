package com.modnaut.common.database;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.common.properties.sqlmetadata.Query;
import com.modnaut.common.properties.sqlmetadata.SqlMetaData;
import com.modnaut.common.utilities.JaxbPool;

public class SqlQueries {

	// constants
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
	
	    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    String filePath = classLoader.getResource("../xml").getPath();

	    try {
		
		allQueries = new HashMap<String, HashMap<String, Query>>();
		HashMap<String, Query> hashMap = new HashMap<String, Query>();

		File file = new File(filePath + fileName + XML_EXTENSION);
		SqlMetaData sqlmetadata = JaxbPool.unmarshal(SqlMetaData.class, file);

		List<Query> queryList = sqlmetadata.getQuery();
		if (queryList != null) {
		    for (int i = 0; queryList.size() > i; i++) {
			Query q = queryList.get(i);
			hashMap.put(q.getName(), q);
		    }
		}

		allQueries.put(fileName, hashMap);

		} catch (NullPointerException npe) {
		    System.out.println("Could not find SqlMetaData file: " + filePath + fileName + XML_EXTENSION);
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
