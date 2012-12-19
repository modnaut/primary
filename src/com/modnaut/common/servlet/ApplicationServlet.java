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

@WebServlet("/")
public class ApplicationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static String realPath = "";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApplicationServlet() {
	super();
    }

    public void init(ServletConfig config) throws ServletException {
	realPath = config.getServletContext().getRealPath("/");
	super.init(config);
    }

    public static String getRealPath() {
	return realPath;
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	response.setContentType("text/html");

	// no paramater
	Class noparams[] = {};

	Class params[] = { HttpServletRequest.class, HttpServletResponse.class };

	try {

	    String className = StringUtils.trimToEmpty(request.getParameter("class"));
	    String methodName = StringUtils.trimToEmpty(request.getParameter("method"));

	    if (!className.equals(ICommonConstants.NONE) && !methodName.equals(ICommonConstants.NONE)) {
		Class clazz = Class.forName(className);

		Object instance;
		Constructor constructor = clazz.getConstructor(params);
		if (constructor != null)
		    instance = constructor.newInstance(request, response);
		else
		    instance = clazz.newInstance();

		Method method = clazz.getDeclaredMethod(methodName);
		method.invoke(instance, request, response);
	    } else {

		Class clazz = Class.forName("com.modnaut.apps.helloworld.HelloWorldCtrl");
		Object instance = clazz.getConstructor(params).newInstance(request, response);

		Method method = clazz.getDeclaredMethod("defaultAction");
		method.invoke(instance, request, response);
	    }

	} catch (Exception e) {

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
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	doGet(request, response);
    }

}