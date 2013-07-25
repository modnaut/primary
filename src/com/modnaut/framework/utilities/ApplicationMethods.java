package com.modnaut.framework.utilities;

import java.util.ArrayList;
import java.util.HashMap;

import com.modnaut.common.utilities.DatabaseMethods;
import com.modnaut.common.utilities.VmdMethods;
import com.modnaut.framework.database.SqlQueries.QUERY_FILE;
import com.modnaut.framework.properties.application.Application;

public class ApplicationMethods
{
	private static HashMap<Integer, Application> applications;

	private static void loadFromDB()
	{
		applications = new HashMap<Integer, Application>();

		ArrayList<String[]> applicationData = DatabaseMethods.getJustData("GET_ALL_APPLICATIONS", QUERY_FILE.COMMON);

		if (applicationData != null)
		{
			for (String[] application : applicationData)
			{
				Application app = new Application();
				app.setApplicationId(Integer.parseInt(application[0]));
				app.setApplicationFolder(application[1]);
				app.setPageTitle(VmdMethods.getStringObject(application[2]));
				app.setClazz(application[3]);
				applications.put(app.getApplicationId(), app);
			}
		}
	}

	public static void reload()
	{
		loadFromDB();
	}

	public static Application getApplication(int applicationId)
	{
		if (applications == null)
			loadFromDB();
		return applications.get(applicationId);
	}
}
