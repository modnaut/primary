package com.modnaut.apps.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.common.controllers.ExtJsScreenCtrl;
import com.modnaut.framework.session.UserSession;
import com.modnaut.framework.session.WebSession;
import com.modnaut.framework.utilities.SessionMethods;

public class LoginCtrl extends ExtJsScreenCtrl
{
	private static final String XML_FILE = "Login.xml";
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginCtrl.class);

	public LoginCtrl(WebSession webSession)
	{
		super(webSession);
		unmarshall(XML_FILE);
	}

	public void defaultAction()
	{
		marshall(viewMetaData);
	}

	public void submit()
	{
		authenticate();
		marshall(viewMetaData);
	}

	private void authenticate()
	{
		String email = getParameter("Email");
		String password = getParameter("Password");

		// TODO - Don't want to do this. Need to keep current session alive, not overwrite it.
		UserSession userSession = SessionMethods.authenticate(email, password, this.userSession);
		if (userSession != null && userSession.isAuthenticated())
		{
			response.setHeader("LoginSuccessful", String.valueOf(true));
			SessionMethods.saveSession(userSession);
		}
		else
		{
			addModalAlert("Invalid email or password.  Please try again.");
		}
	}
}
