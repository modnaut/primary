package com.modnaut.common.framework;

import java.io.File;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.modnaut.common.properties.ViewMetaData;

public class FrameworkCtrl {
    
    public ViewMetaData viewMetaData;
    private static final String FILE_PATH = "M:/development/primary";
    
    public ViewMetaData unmarshall(String xmlFile) {
	
	try {
	    File file = new File(FILE_PATH + "/web/helloworld/xml/" + xmlFile + ".xml");
	    JAXBContext jaxbContext = JAXBContext.newInstance(ViewMetaData.class);
		
	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	    viewMetaData = (ViewMetaData) jaxbUnmarshaller.unmarshal(file);
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
	    
	    File xmlFile = new File(FILE_PATH + "/web/helloworld/xml/helloWorld.xml");
	    File xsltFile = new File(FILE_PATH + "/xsl/simple.xsl");
    
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
