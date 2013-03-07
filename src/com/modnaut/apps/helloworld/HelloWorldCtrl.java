package com.modnaut.apps.helloworld;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.common.controllers.ScreenCtrl;
import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.common.utilities.CommonMethods;
import com.modnaut.common.utilities.DatabaseMethods;
import com.modnaut.common.utilities.VmdMethods;
import com.modnaut.framework.properties.viewmetadata.TextField;

public class HelloWorldCtrl extends ScreenCtrl
{
	private static final String XML_FILE = "HelloWorld.xml";
	private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldCtrl.class);

	private static final String USER_ID = "UserId";
	private static final String USER_NAME = "UserName";
	private static final String FIRST_NAME = "FirstName";
	private static final String LAST_NAME = "LastName";
	private static final String EMAIL_ADDRESS = "EmailAddress";
	private static final String HIRE_DATE = "HireDate";
	private static final String USER_TYPE_CD = "UserTypeCd";
	private static final String USER_PASSWORD = "UserPassword";

	// CONSTANTS
	private static final int ITERATION_NUMBER = 1000;
	private static final String PASSWORD_SALT = "MoDnaUt_SalT";

	private static final String GET_ALL_USERS_ALPHABETICALLY = "GET_ALL_USERS_ALPHABETICALLY";

	public HelloWorldCtrl(HttpServletRequest request, HttpServletResponse response)
	{
		super(request, response);
		unmarshall(XML_FILE);
	}

	public void defaultAction()
	{
		try
		{
			// In order to utilize a different configuration than the default you must place the web/WEB-INF/lib/resource file in the build/classes folder (which is not versioned)
			// This is not very elegant, but it works for now.
			// LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
			// StatusPrinter.print(lc);

			// NOTE: need to run the SP located in the new "primary/sql" folder in MySql before testing
			// ArrayList<String[]> data = DatabaseMethods.getJustData(GET_ALL_USERS_ALPHABETICALLY, ICommonConstants.COMMON);
			// populateData("users", data);
			populateUserTypesCombo();
			marshall(viewMetaData);
		}
		catch (Exception e)
		{
			LOGGER.error("This a Logback ERROR meesage.  There was an error in default action of this class.", e);
		}
	}

	public void getUsers() throws Exception
	{
		// NOTE: need to run the SP located in the new "primary/sql" folder in MySql before testing
		ArrayList<String[]> data = DatabaseMethods.getData(GET_ALL_USERS_ALPHABETICALLY, ICommonConstants.COMMON);
		marshallStoreJson(data, true);
	}

	public void userSelected()
	{
		HashMap<String, Object> parms = new HashMap<String, Object>();
		parms.put(USER_ID, getParameter("UserId-0"));
		String[] userData = DatabaseMethods.getJustDataFirstRow("GET_USER", ICommonConstants.COMMON, parms);
		populateData(USER_ID, userData[0]);
		populateData(USER_NAME, userData[1]);
		populateData(FIRST_NAME, userData[2]);
		populateData(LAST_NAME, userData[3]);
		populateData(EMAIL_ADDRESS, userData[4]);
		populateData(HIRE_DATE, userData[5]);
		populateData(USER_TYPE_CD, userData[6]);

		populateUserTypesCombo();

		marshall(viewMetaData);
	}

	public void userTypeChanged()
	{
		LOGGER.info("User type changed: " + getParameter(USER_TYPE_CD));
		userSelected();
	}

	public void saveUser()
	{
		String userId = getParameter(USER_ID);
		String userName = getParameter(USER_NAME);
		String firstName = getParameter(FIRST_NAME);
		String lastName = getParameter(LAST_NAME);
		String email = getParameter(EMAIL_ADDRESS);
		String hireDate = getParameter(HIRE_DATE);
		String userTypeCd = getParameter(USER_TYPE_CD);
		String userPassword = getParameter("UserPassword");

		boolean valid = true;
		valid = valid && validateFieldIsNotEmpty(USER_NAME, null);
		valid = valid && validateFieldIsNotEmpty(FIRST_NAME, null);
		valid = valid && validateFieldIsNotEmpty(LAST_NAME, null);
		valid = valid && validateFieldIsNotEmpty(EMAIL_ADDRESS, null);
		valid = valid && validateFieldIsNotEmpty(HIRE_DATE, null);
		valid = valid && validateFieldIsNotEmpty(USER_TYPE_CD, null);

		String encryptedPassword = ICommonConstants.NONE;
		if (!StringUtils.isEmpty(userPassword))
		{
			try
			{
				encryptedPassword = CommonMethods.encryptString(userPassword, ITERATION_NUMBER, PASSWORD_SALT);
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				valid = false;
				((TextField) findById(USER_PASSWORD)).setActiveError(VmdMethods.getStringObject("Failed to encrypt password"));
				e.printStackTrace();
			}
		}

		populateData(USER_ID, userId);
		populateData(USER_NAME, userName);
		populateData(FIRST_NAME, firstName);
		populateData(LAST_NAME, lastName);
		populateData(EMAIL_ADDRESS, email);
		populateData(HIRE_DATE, hireDate);
		populateData(USER_TYPE_CD, userTypeCd);

		if (valid)
		{
			HashMap<String, Object> parms = new HashMap<String, Object>();
			parms.put(USER_ID, userId);
			parms.put(USER_NAME, userName);
			parms.put(FIRST_NAME, firstName);
			parms.put(LAST_NAME, lastName);
			parms.put(EMAIL_ADDRESS, email);
			parms.put(HIRE_DATE, hireDate);
			parms.put(USER_TYPE_CD, userTypeCd);
			if (!StringUtils.isEmpty(userPassword))
			{
				parms.put(USER_PASSWORD, encryptedPassword);
			}

			LOGGER.info(parms.toString());

			if (DatabaseMethods.updateData("UPDATE_USER", ICommonConstants.COMMON, parms) == 1)
				LOGGER.info("Saved!");
			else
				LOGGER.error("Failed to save");
		}
		populateUserTypesCombo();

		marshall(viewMetaData);
	}

	private void populateUserTypesCombo()
	{
		populateData(USER_TYPE_CD, DatabaseMethods.getJustData("GET_USER_TYPES", ICommonConstants.COMMON));
	}
}
