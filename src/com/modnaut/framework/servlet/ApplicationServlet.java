package com.modnaut.framework.servlet;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.common.utilities.EnrichableException;
import com.modnaut.common.utilities.ServerMethods;
import com.modnaut.framework.session.WebSession;
import com.modnaut.framework.session.WebSessionController;

/**
 * 
 * @author Jamie LaMarche
 * @date 1/9/2013
 * 
 *       Servlet class used by all applications. Will be called every time there is a request from the browser. Uses java annotation, removes need to specify directly within web.xml (ie WebServlet("/"))
 * 
 */

@WebServlet("/")
public class ApplicationServlet extends HttpServlet
{
	private static final String CLASS_NAME_PATH = ApplicationServlet.class.getCanonicalName();
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationServlet.class);
	private static final String METHOD_NAME = "doGet";

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor method
	 */
	public ApplicationServlet()
	{
		super();
	}

	/**
	 * Called to obtain path of all source code, including views and view related code. Sets realPath static variable to be available for retrieving from all methods within this class.
	 */
	@Override
	public void init(ServletConfig config) throws ServletException
	{
		ServerMethods.initializeServer(config.getServletContext());
		super.init(config);
	}

	/**
	 * Method used every time this class is called from the browser. Retrieves the class name, method and parameters contained within the servlet request. Uses reflection to create the java class instance based on class name, which then invokes the method for that class instance with the parameters (if any).
	 * 
	 * @return void
	 * 
	 * @param HttpServletRequest
	 * @param HttpServletResponse
	 * 
	 * @exception ServletException
	 * @exception IOException
	 * 
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// sets output response shown in browser to html
		response.setContentType(ICommonConstants.CONTENT_TYPE_HTML);// Set to HTML by default. ExtJS screens will set it to JSON

		// This is where we will intercept every request and check for a valid, unexpired session.
		WebSessionController wsController = new WebSessionController(request, response);
		WebSession webSession = wsController.authenticate();

		if (webSession == null)
			response.sendRedirect(ICommonConstants.INVALID_LOGIN_PAGE);

		// TODO - we are not doing anything with the WebSession object yet. We will want to pass this to the Class that extends FrameworkCtrl.

		Class<?> params[] = { HttpServletRequest.class, HttpServletResponse.class };

		try
		{
			String className = StringUtils.trimToEmpty(request.getParameter(ICommonConstants.CLASS));
			String methodName = StringUtils.trimToEmpty(request.getParameter(ICommonConstants.METHOD));

			if (!className.equals(ICommonConstants.NONE) && !methodName.equals(ICommonConstants.NONE))
			{
				Class<?> clazz = Class.forName(className);

				Object instance;
				Constructor<?> constructor = clazz.getConstructor(params);
				if (constructor != null)
					instance = constructor.newInstance(request, response);
				else
					instance = clazz.newInstance();

				Method method = clazz.getDeclaredMethod(methodName);
				method.invoke(instance);
			}
			else
			{
				throw new EnrichableException(CLASS_NAME_PATH, METHOD_NAME, ICommonConstants.SERVLET_LOG, ICommonConstants.ERROR, "Invalid or missing required parameters: className: " + className + " methodName: " + methodName);
			}
		}
		catch (EnrichableException e)
		{
			LOGGER.error(e.toString(), e);
			sendErrorResponse(response, "An error has occurred");
		}
		catch (InvocationTargetException e)// when method called by reflection throws an Exception it ends up here
		{
			LOGGER.error(e.getTargetException().toString(), e.getTargetException());
			sendErrorResponse(response, "An error has occurred");
		}
		catch (Exception e)
		{
			// SHOULD NEVER HAPPEN - will need more advanced handling as we go live
			// In the event of an error while trying to load the page the following will occur:
			// 1. Java stack trace containing more detailed error messages will be printed to the console
			// 2. A basic html error page will be shown on screen by the PrintWriter object to user to let them know an error has occurred.
			LOGGER.error(e.toString(), e);
			sendErrorResponse(response, "An error has occurred");

		}
	}

	/**
	 * Calls doGet method for processing and logic.
	 * 
	 * @param HttpServletRequest
	 * @param HttpServletResponse
	 * 
	 * @exception ServletException
	 *                , IOException
	 * 
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}

	private void sendErrorResponse(HttpServletResponse response, String errorMessage)
	{
		StringBuilder builder = new StringBuilder();

		if (errorMessage == null)
		{
			errorMessage = "An error has occurred";
		}
		if (response.getContentType().equals(ICommonConstants.CONTENT_TYPE_JSON))
		{
			builder.append("{success: true, html: \"" + StringEscapeUtils.escapeEcmaScript(errorMessage) + "\"}");
		}
		else
		{
			builder.append("<html>");
			builder.append("<body>");
			builder.append("<p> An error occurred while trying to load this page. Please try again.</p>");
			builder.append("</body>");
			builder.append("</html>");
		}

		try
		{
			response.addHeader("Warning", errorMessage);
			response.getOutputStream().write(builder.toString().getBytes(ICommonConstants.UTF_8));
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}