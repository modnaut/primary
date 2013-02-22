package com.modnaut.apps.helloworld;

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
	private static final Logger LOGGER = LoggerFactory.getLogger("com.modnaut.apps.helloworld.HelloWorldChangeCtrl");

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
			LOGGER.debug("Hello world.");

			// In order to utilize a different configuration than the default you must place the web/WEB-INF/lib/resource file in the build/classes folder (which is not versioned)
			// This is not very elegant, but it works for now.
			LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
			StatusPrinter.print(lc);

			// NOTE: need to run the SP located in the new "primary/sql" folder in MySql before testing
			ArrayList<String[]> data = DatabaseMethods.getJustData(GET_ALL_USERS_ALPHABETICALLY, ICommonConstants.COMMON);

			populateData("users", data);

			marshall(viewMetaData);

		}
		catch (Exception e)
		{
			LOGGER.error("This a Logback ERROR meesage.  There was an error in default action of this class.");
			e.printStackTrace();
		}
	}

	public void getUsers() throws Exception
	{
		// NOTE: need to run the SP located in the new "primary/sql" folder in MySql before testing
		ArrayList<String[]> data = DatabaseMethods.getJustData(GET_ALL_USERS_ALPHABETICALLY, ICommonConstants.COMMON);

		// data.add(0, new String[] { "UserId", "UserName", "FirstName", "LastName", "EmailAddress", "UserPassword" });

		try
		{
			Thread.sleep(1500);
		}
		catch (Exception e)
		{
		}
		marshallStoreJson(data, false);
	}

	public void getUserCombo() throws Exception
	{
		LOGGER.info("userType: " + getParameter("userType"));
		ArrayList<String[]> data = new ArrayList<String[]>()
		{
			{
				add(new String[] { "id", "name" });
				add(new String[] { "5", "danny" });
				add(new String[] { "6", "ben" });
			}
		};

		marshallStoreJson(data, true);
	}
}
