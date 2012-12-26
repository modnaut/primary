package com.modnaut.common.framework;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamSource;

import com.modnaut.common.utilities.XslPool;

public class ApplicationCtrl {

    private HttpServletRequest request;
    private HttpServletResponse response;

    public ApplicationCtrl(HttpServletRequest request, HttpServletResponse response) {
	this.request = request;
	this.response = response;
    }

    public void defaultAction() throws IOException, Exception {
	XslPool.transform(new StreamSource(), response.getOutputStream(), "Application.xsl", null);
    }
}
