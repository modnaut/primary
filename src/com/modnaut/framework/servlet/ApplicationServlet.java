package com.modnaut.framework.servlet;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.modnaut.apps.login.InsufficientPrivilegeException;
import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.common.utilities.EnrichableException;
import com.modnaut.framework.properties.application.Application;
import com.modnaut.framework.session.NinjaSession;
import com.modnaut.framework.session.WebSession;
import com.modnaut.framework.session.WebSessionController;
import com.modnaut.framework.utilities.ApplicationMethods;
import com.modnaut.framework.utilities.EnvironmentMethods;
import com.modnaut.framework.utilities.RequestParameterParser;
import com.modnaut.framework.utilities.SessionMethods;
import com.modnaut.framework.utilities.UrlMethods;

/**
 * 
 * @author Jamie LaMarche
 * @date 1/9/2013
 * 
 *       Servlet class used by all applications. Will be called every time there is a request from the browser. Uses java annotation, removes need to specify directly within web.xml (ie WebServlet("/"))
 * 
 */

@WebServlet("/ApplicationServlet/*")
public class ApplicationServlet extends HttpServlet
{
	private static final String CLASS_NAME_PATH = ApplicationServlet.class.getCanonicalName();
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationServlet.class);
	private static final String METHOD_NAME = "doGet";
	private static Pattern HASHPATH_PATTERN = Pattern.compile("([^?]+)\\?(.*)");
	private static final String INVOKE = "invoke";
	private static final String HASH_PATH = "hashPath";
	private static final long serialVersionUID = 1L;
	private RequestParameterParser requestParameterParser;

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
		EnvironmentMethods.initializeServer(config.getServletContext());
		super.init(config);
	}

	private String getParameter(String name, HttpServletRequest request)
	{
		return requestParameterParser.getParameter(name);
	}

	/**
	 * Method used every time this class is called from the browser. Retrieves the class name, method and parameters contained within the servlet request. Uses reflection to create the java class instance based on class name, which then invokes the method for that class instance with the parameters (if any).
	 * 
	 * @return void
	 * 
	 * @param HttpServletRequest
	 * @param HttpServletResponse
	 * @param RestVerb
	 * 
	 * @exception ServletException
	 * @exception IOException
	 * 
	 */
	public void service(HttpServletRequest request, HttpServletResponse response, RestVerb verb) throws ServletException, IOException
	{

		requestParameterParser = new RequestParameterParser(request, verb);

		// sets output response shown in browser to html
		response.setContentType(ICommonConstants.CONTENT_TYPE_HTML);// Set to HTML by default. ExtJS screens will set it to JSON

		// This is where we will intercept every request and check for a valid, unexpired session.
		WebSessionController wsController = new WebSessionController(request, response);
		NinjaSession ninjaSession = wsController.getNinjaSession();

		if (ninjaSession == null)
			LOGGER.error("ninjaSession is null.");

		WebSession webSession = new WebSession(request, response, ninjaSession, requestParameterParser);

		try
		{
			String className = ICommonConstants.NONE;
			String methodName = ICommonConstants.NONE;
			HashMap<String, String> extraParameters = null;

			String invoke = StringUtils.trimToEmpty(getParameter(INVOKE, request));
			String hashPath = StringUtils.trimToEmpty(getParameter(HASH_PATH, request));

			if (!hashPath.isEmpty())
			{
				Matcher matcher = HASHPATH_PATTERN.matcher(hashPath);
				if (matcher.find())
				{
					String[] classAndMethod = UrlMethods.retrieveClassAndMethod(matcher.group(1));
					if (classAndMethod != null && !StringUtils.isEmpty(classAndMethod[0]) && !StringUtils.isEmpty(classAndMethod[1]))
					{
						className = classAndMethod[0];
						methodName = classAndMethod[1];

						String extraParametersString = matcher.group(2);
						if (!StringUtils.isEmpty(extraParametersString))
						{
							String[] nameValueStrings = extraParametersString.split(ICommonConstants.AMPERSAND);
							if (nameValueStrings.length > 0)
							{
								extraParameters = new HashMap<String, String>();
								for (String nameValueString : nameValueStrings)
								{
									String[] parts = nameValueString.split(ICommonConstants.EQUALS);
									if (parts.length == 2)
									{
										extraParameters.put(parts[0], parts[1]);
									}
									else if (parts.length == 1)
									{
										extraParameters.put(parts[0], ICommonConstants.NONE);
									}
								}
							}
						}
					}

				}
			}

			if (StringUtils.isEmpty(className) && StringUtils.isEmpty(methodName) && !invoke.isEmpty())
			{
				String decodedInvoke = new String(Base64.decodeBase64(invoke));
				LOGGER.debug("Decoded Invoke: " + decodedInvoke);
				String[] invokeComponents = decodedInvoke.split(Pattern.quote(ICommonConstants.PIPE));
				className = invokeComponents[0];
				methodName = invokeComponents[1];
			}

			if (StringUtils.isEmpty(className) && StringUtils.isEmpty(methodName))
			{
				className = StringUtils.trimToEmpty(getParameter(ICommonConstants.CLASS, request));
				methodName = StringUtils.trimToEmpty(getParameter(ICommonConstants.METHOD, request));
			}

			// TODO: Load default class and method from server properties
			if (StringUtils.isEmpty(className) && StringUtils.isEmpty(methodName))
			{
				Application application = ApplicationMethods.getApplication(EnvironmentMethods.getApplicationId());
				className = application.getClazz();
				methodName = "buildPage";
			}

			if (!className.equals(ICommonConstants.NONE) && !methodName.equals(ICommonConstants.NONE))
			{
				className = UrlMethods.decrypt(className);
				methodName = UrlMethods.decrypt(methodName);

				if (extraParameters != null)
					requestParameterParser.setExtraParameters(extraParameters);

				Class<?> clazz = Class.forName(className);

				Object instance;
				Constructor<?> constructor = clazz.getConstructor(new Class[] { WebSession.class });
				if (constructor != null)
					instance = constructor.newInstance(webSession);
				else
					instance = clazz.newInstance();

				Method method = null;
				try
				{
					method = clazz.getDeclaredMethod(methodName);
				}
				catch (NoSuchMethodException e)
				{
					method = clazz.getMethod(methodName);
				}

				method.invoke(instance);

				UrlMethods.retrievePrettyUrl(webSession, className, methodName);
			}
			else
			{
				throw new EnrichableException(CLASS_NAME_PATH, METHOD_NAME, ICommonConstants.SERVLET_LOG, ICommonConstants.ERROR, "Invalid or missing required parameters: className: " + className + " methodName: " + methodName);
			}
		}
		catch (EnrichableException e)
		{
			LOGGER.error(e.toString(), e);
			sendErrorResponse(response, "An error has occurred", 2);
		}
		catch (InvocationTargetException e)// when method called by reflection throws an Exception it ends up here
		{
			// Screen needs permission, but ninja is not logged in.
			if (e.getCause() instanceof InsufficientPrivilegeException)
			{
				// TODO
				sendErrorResponse(response, "You must login first.", 1);
			}
			else
			{
				LOGGER.error(e.getTargetException().toString(), e.getTargetException());
				sendErrorResponse(response, "An error has occurred", 3);
			}

		}
		catch (Exception e)
		{
			// SHOULD NEVER HAPPEN - will need more advanced handling as we go live
			// In the event of an error while trying to load the page the following will occur:
			// 1. Java stack trace containing more detailed error messages will be printed to the console
			// 2. A basic html error page will be shown on screen by the PrintWriter object to ninja to let them know an error has occurred.
			LOGGER.error(e.toString(), e);
			sendErrorResponse(response, "An error has occurred", 3);
		}

		SessionMethods.saveSession(ninjaSession);
	}

	/**
	 * Calls service method for processing and logic.
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
		service(request, response, RestVerb.Post);
	}

	/**
	 * Calls service method for processing and logic.
	 * 
	 * @param HttpServletRequest
	 * @param HttpServletResponse
	 * 
	 * @exception ServletException
	 *                , IOException
	 * 
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		service(request, response, RestVerb.Get);
	}

	/**
	 * Calls service method for processing and logic.
	 * 
	 * @param HttpServletRequest
	 * @param HttpServletResponse
	 * 
	 * @exception ServletException
	 *                , IOException
	 * 
	 */
	@Override
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		service(request, response, RestVerb.Put);
	}

	/**
	 * Calls service method for processing and logic.
	 * 
	 * @param HttpServletRequest
	 * @param HttpServletResponse
	 * 
	 * @exception ServletException
	 *                , IOException
	 * 
	 */
	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		service(request, response, RestVerb.Delete);
	}

	private void sendErrorResponse(HttpServletResponse response, String errorMessage, int warning_code)
	{
		StringBuilder builder = new StringBuilder();

		if (errorMessage == null)
		{
			errorMessage = "An error has occurred";
		}
		if (response.getContentType().equals(ICommonConstants.CONTENT_TYPE_JSON))
		{
			builder.append("{success: true, html: \"");
			builder.append(StringEscapeUtils.escapeEcmaScript(errorMessage));
			builder.append("\"}");
		}
		else
		{
			builder.append("<html>");
			builder.append("<body>");
			builder.append("<p>An error occurred while trying to load this page. Please try again.</p>");
			builder.append("<p>");
			builder.append(errorMessage);
			builder.append("</p>");
			builder.append("</body>");
			builder.append("</html>");
		}

		try
		{
			response.addHeader("Warning", errorMessage);
			response.addHeader("WarningCode", warning_code + ICommonConstants.NONE);
			response.getOutputStream().write(builder.toString().getBytes(ICommonConstants.UTF_8));
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}