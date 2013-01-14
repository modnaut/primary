package com.modnaut.common.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.modnaut.common.interfaces.ICommonConstants;

/**
 * 
 * @author Jamie LaMarche
 * @date 1/9/2013
 * 
 *       Servlet class used by all applications. Will be called every time there is a request from the browser.
 *       Uses java annotation, removes need to specify directly within web.xml (ie WebServlet("/"))
 * 
 */

@WebServlet("/")
public class ApplicationServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private static final String CLASS = "class";
	private static final String METHOD = "method";

	private static String realPath = ICommonConstants.NONE;

	/**
	 * Constructor method
	 */
	public ApplicationServlet()
	{
		super();
	}

	/**
	 * Called to obtain path of all source code, including views and view related code. Sets realPath static
	 * variable to be available for retrieving from all methods within this class.
	 */
	public void init(ServletConfig config) throws ServletException
	{
		realPath = config.getServletContext().getRealPath("/");
		super.init(config);
	}

	/**
	 * Called by outside classes and methods to get the actual path of all source code,
	 * including views and view related code. Makes finding necessary files within the code file system quick and easy.
	 * 
	 * @return String
	 */
	public static String getRealPath()
	{
		return realPath;
	}

	/**
	 * Method used every time this class is called from the browser.
	 * Retrieves the class name, method and parameters contained within the servlet request.
	 * Uses reflection to create the java class instance based on class name, which then invokes the method
	 * for that class instance with the parameters (if any).
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
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// sets output response shown in browser to html
		response.setContentType("text/html");

		Class<?> params[] = { HttpServletRequest.class, HttpServletResponse.class };

		try
		{
			String className = StringUtils.trimToEmpty(request.getParameter(CLASS));
			String methodName = StringUtils.trimToEmpty(request.getParameter(METHOD));

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
				// this is temporary for development purposes only. Will be removed from production environment.
				// is only called in the event the url does not contain the class and method parameters.
				Class<?> clazz = Class.forName("com.modnaut.apps.helloworld.HelloWorldChangeCtrl");
				Object instance = clazz.getConstructor(params).newInstance(request, response);

				Method method = clazz.getDeclaredMethod("defaultAction");
				method.invoke(instance);
			}

		}
		catch (Exception e)
		{
			// In the event of an error while trying to load the page the following will occur:
			// 1. Java stack trace containing more detailed error messages will be printed to the console
			// 2. A basic html error page will be shown on screen by the PrintWriter object to user to let them know an error has occurred.
			e.printStackTrace();

			PrintWriter pw = response.getWriter();
			pw.println("<html>");
			pw.println("<body>");
			pw.println("<p> An error occurred while trying to load this page. Please try again.</p>");
			pw.println("</body>");
			pw.println("</html>");
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
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}
}