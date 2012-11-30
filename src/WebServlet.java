import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class WebServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
  
    public void init (ServletConfig  config) throws ServletException {
	super.init (config);
    }
    
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        
	res.setContentType("text/html");

        //no paramater
	Class noparams[] = {};
	
	Class paramHttpRes[] = new Class[1];
	paramHttpRes[0] = HttpServletResponse.class;
	
	try {
	    Class c = Class.forName("com.modnaut.apps.helloworld.HelloWorldCtrl");
	    Object o = c.newInstance();

	    Method method = c.getDeclaredMethod("printout", paramHttpRes);
	    method.invoke(o, res);
    
	} catch (Exception e) {
	    e.printStackTrace();
	}
	

        // ... output page to pw...
        
        /*PrintWriter pw = res.getWriter();
	pw.println("<html>");
        pw.println("<body>");
        pw.println("<p> this is the message : " + message + "</p>");
        pw.println("</body>");
        pw.println("</html>");*/
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
	doGet(req, res);
    }

}