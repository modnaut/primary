package com.modnaut.common.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
  
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ApplicationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    
    public void init (ServletConfig  config) throws ServletException {
	super.init (config);
    }
    
    
    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
	response.setContentType("text/html");

        //no paramater
	Class noparams[] = {};
	
	Class paramHttpRes[] = new Class[1];
	paramHttpRes[0] = HttpServletResponse.class;
	
	try {
	    
	    String className = StringUtils.trimToEmpty(request.getParameter("class"));
	    String methodName = StringUtils.trimToEmpty(request.getParameter("method"));
	    
	    if (!className.equals(ICommonConstants.NONE) && !methodName.equals(ICommonConstants.NONE)) {
		Class c = Class.forName(className);
		Object o = c.newInstance();
        
        	Method method = c.getDeclaredMethod(methodName, paramHttpRes);
        	method.invoke(o, response);
	    }
	    else {

		Class c = Class.forName("com.modnaut.apps.helloworld.HelloWorldCtrl");
		Object o = c.newInstance();
        
		//Method method = c.getDeclaredMethod(StringUtils.trimToEmpty(methodName.replace('"', ' ')), paramHttpRes);
        	Method method = c.getDeclaredMethod("defaultAction", paramHttpRes);
        	method.invoke(o, response);
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