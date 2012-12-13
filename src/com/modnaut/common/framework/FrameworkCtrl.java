package com.modnaut.common.framework;

import java.io.File;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.modnaut.common.properties.viewmetadata.ViewMetaData;
import com.modnaut.common.utilities.JAXBCache;

public class FrameworkCtrl {
    
    public ViewMetaData viewMetaData;
  
    public ViewMetaData unmarshall(String xmlFile) {
	
	try {
	    
	    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    String filePath = classLoader.getResource("../web/helloworld/xml").getPath();
	    
	    File file = new File(filePath  + xmlFile + ".xml");
	    viewMetaData = (ViewMetaData) JAXBCache.unmarshal(ViewMetaData.class, file);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	
	return viewMetaData;
    }
    
    public void marshall(HttpServletResponse res) {
	try {
	    //JAXBContext context = JAXBContext.newInstance(ViewMetaData.class);
			
	    //Marshaller m = context.createMarshaller();
	    //m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    //m.marshal(viewMetaData, res.getOutputStream());	
	    
	    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    String filePath = classLoader.getResource("../../WEB-INF").getPath();
	    

	    File xsltFile = new File(filePath.replace("%20", " ") + "/xsl/Simple.xsl");
	    
	    File xmlFile = new File(filePath.replace("%20", " ") + "web/helloworld/xml/HelloWorld.xml");

    
	    Source xmlSource = new StreamSource(xmlFile);
	    Source xsltSource = new StreamSource(xsltFile);

	    // create an instance of TransformerFactory
	    TransformerFactory transFact = TransformerFactory.newInstance();
	    Transformer trans = transFact.newTransformer(xsltSource);
	    Result result = new StreamResult(res.getOutputStream());

	    
	    trans.transform(xmlSource, result); 
	    
	 } catch (Exception e) {
	     e.printStackTrace();
	 }
    }    
}
