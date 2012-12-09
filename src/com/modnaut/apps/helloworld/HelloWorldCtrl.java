package com.modnaut.apps.helloworld;

import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import com.modnaut.common.framework.FrameworkCtrl;
import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.common.properties.MessageType;
import com.modnaut.common.utilities.UtilityDatabaseMethods;

public class HelloWorldCtrl extends FrameworkCtrl {

    private static final String XML_FILE = "helloWorld";
    private static final String GET_USER_LIST = "GET_USER_LIST";
    HttpServletResponse res;
    
    public HelloWorldCtrl() {

    }

    public void printout(HttpServletResponse res) {
	
	this.res = res;
	viewMetaData = unmarshall(XML_FILE);
	
	try {
	    if (viewMetaData != null) {
		System.out.println(viewMetaData.getTitle().getValue());
	    }
	    
	    String userList = ICommonConstants.NONE;
	    ArrayList<String[]> data = UtilityDatabaseMethods.getJustData(GET_USER_LIST, ICommonConstants.COMMON);
	    
	    //NOTE: need to run the SP located in the new "primary/sql" folder in MySql before testing
//	    ArrayList<String[]> data = UtilityDatabaseMethods.getJustData("GET_ALL_USERS_ALPHABETICALLY", ICommonConstants.COMMON);
	    if (data != null) {
		for (int i = 0; data.size() > i; i++) {
		    String[] d = (String[]) data.get(i);
		    
		    userList += d[0] + ICommonConstants.COMMA;
		    for (int j = 0; d.length > j; j++) {
			System.out.println(d[j]);
		    }
		}
	    }

	    MessageType m = new MessageType();
	    m.setValue("This is our user list: " + userList + ".... and more to come..");
	    viewMetaData.getMessage().add(m);
	    
	    marshall(res);    

	 } catch (Exception e) {
	     e.printStackTrace();
	 }
    }
}
