package com.modnaut.common.utilities;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.modnaut.common.properties.Query;
import com.modnaut.common.properties.SqlMetaData;
import com.modnaut.common.properties.StatementType;

public class UtilityDatabaseMethods {

    private static final String FILE_PATH = "M:/development/primary/xml/";
    
    public static ArrayList<String[]> getJustData(String queryName, String queryFile) {

	try {     
            System.out.println("Loading driver...");     
            Class.forName("com.mysql.jdbc.Driver");     
            System.out.println("Driver loaded!"); 
    	} 
    	catch (ClassNotFoundException e) {     
    	    throw new RuntimeException("Cannot find the driver in the classpath!", e); 
    	} 

  	Connection con = null;
  	Statement st = null;
	ResultSet rs = null;
		
	String url = "jdbc:mysql://localhost:3306/common";
	String user = "root";
	String password = "Mkyyxz2g";

	ArrayList<String[]> data = new ArrayList<String[]>();
	
        try {
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            
            File file = new File(FILE_PATH + queryFile + ".xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(SqlMetaData.class);
		
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            SqlMetaData sqlmetadata = (SqlMetaData) jaxbUnmarshaller.unmarshal(file);
		
            List<Query> queryList = sqlmetadata.getQuery();
            if (queryList != null) {
        	for (int i = 0; queryList.size() > i; i++) {
        	    Query q = (Query)queryList.get(i);
        	    if (q.getName().equals(queryName)) {
        		StatementType statement = (StatementType)q.getStatement();

        		rs = st.executeQuery(statement.getValue());
        		while (rs.next()) {
        		    String[] t = {rs.getString(2),  rs.getString(5)};
        		    data.add(t);
        		}
        	    }
        	}
            }
        } catch (Exception ex) {
        	ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }
            } 
            catch (Exception ex) {
            	ex.printStackTrace();
            }
        }
        
        return data;
    }
}
