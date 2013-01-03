package com.modnaut.apps.helloworld;

import java.io.PrintStream;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.modnaut.common.framework.FrameworkCtrl;
import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.common.utilities.DatabaseMethods;

public class HelloWorldChangeCtrl extends FrameworkCtrl
{
	private static final String XML_FILE = "HelloWorld.xml";
	private static final String GET_ALL_USERS_ALPHABETICALLY = "GET_ALL_USERS_ALPHABETICALLY";

	public HelloWorldChangeCtrl(HttpServletRequest request, HttpServletResponse response)
	{
		super(request, response);
		unmarshall(XML_FILE);
	}

	public void defaultAction()
	{
		try
		{
			String userList = ICommonConstants.NONE;

			// NOTE: need to run the SP located in the new "primary/sql" folder in MySql before testing
			ArrayList<String[]> data = DatabaseMethods.getJustData(GET_ALL_USERS_ALPHABETICALLY, ICommonConstants.COMMON);

			// PrintWriter writer = response.getWriter();
			PrintStream writer = System.out;
			if (data != null)
			{
				for (String[] row : data)
				{
					userList += row[0] + ICommonConstants.COMMA;
					for (int j = 0; row.length > j; j++)
					{
						writer.println(row[j]);
					}
				}
				writer.println(userList);
			}

			marshall(viewMetaData);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
