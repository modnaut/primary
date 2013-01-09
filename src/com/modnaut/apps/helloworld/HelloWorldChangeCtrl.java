package com.modnaut.apps.helloworld;

import java.io.PrintStream;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;

import com.modnaut.common.framework.FrameworkCtrl;
import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.common.utilities.DatabaseMethods;

public class HelloWorldChangeCtrl extends FrameworkCtrl
{
	private static final String XML_FILE = "HelloWorld.xml";
	private static final Logger logger = LoggerFactory.getLogger("com.modnaut.apps.helloworld.HelloWorldChangeCtrl");

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
			logger.debug("Hello world.");

			// In order to utilize a different configuration than the default you must place the web/WEB-INF/lib/resource file in the build/classes folder (which is not versioned)
			// This is not very elegant, but it works for now.
			LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
			StatusPrinter.print(lc);

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
						logger.info(row[j]);
					}
				}
				logger.info(userList);
			}

			marshall(viewMetaData);

		}
		catch (Exception e)
		{
			logger.error("This a Logback ERROR meesage.  There was an error in default action of this class.");
			e.printStackTrace();
		}
	}
}
