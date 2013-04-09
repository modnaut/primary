package com.modnaut.common.controllers;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.modnaut.common.interfaces.ICommonConstants;
import com.modnaut.common.utilities.VmdMethods;
import com.modnaut.framework.properties.viewmetadata.AbstractField;
import com.modnaut.framework.properties.viewmetadata.NotificationType;
import com.modnaut.framework.session.WebSession;

/**
 * 
 * @author Jamie Lynn
 * @date 1/9/2013
 * 
 *       Used by all java classes that produce ExtJS-based screens. Takes request sent in from servlet, performs unmarshalling which allows the injection of data, searching on sub objects, etc and then marshalling which puts all the pieces together and sets the response to the final output result.
 * 
 */
public class ExtJsScreenCtrl extends FrameworkCtrl
{
	/**
	 * 
	 * @param request
	 * @param response
	 */
	public ExtJsScreenCtrl(WebSession webSession)
	{
		super(webSession);
		response.setContentType(ICommonConstants.CONTENT_TYPE_JSON);
	}

	/**
	 * Returns a specific value from request based on name of the value in the request. For example, if the attribute name of a html element on screen is 'username', then it will return the value of this html element. Can also be used to retrieve the value off a url that contains parameters and hidden values on screen.
	 * 
	 * @param name
	 * @return
	 */
	protected String getParameter(String name)
	{
		return this.request.getParameter(name);
	}

	protected Object findById(String id)
	{
		return VmdMethods.getSingleById(viewMetaData, jxPathContext, id);
	}

	protected List findMultipleById(String id)
	{
		return VmdMethods.getMultipleById(viewMetaData, jxPathContext, id);
	}

	protected boolean populateData(String id, List data)
	{
		return VmdMethods.populateData(viewMetaData, jxPathContext, id, data);
	}

	protected boolean populateData(String id, String data)
	{
		return VmdMethods.populateData(viewMetaData, jxPathContext, id, data);
	}

	protected void deleteElement(String id)
	{
		VmdMethods.deleteElement(viewMetaData, jxPathContext, id);
	}

	protected void addModalAlert(String alertTextStringCd)
	{
		addModalAlert(alertTextStringCd, null);
	}

	protected void addModalAlert(String alertTextStringCd, String alertTitleStringCd)
	{
		VmdMethods.addModalAlert(viewMetaData, jxPathContext, alertTextStringCd, alertTitleStringCd);
	}

	protected void addNotification(String notificationStringCd, NotificationType type, String panelId)
	{
		VmdMethods.addNotification(viewMetaData, jxPathContext, notificationStringCd, type, panelId);
	}

	protected void addNotification(String notificationStringCd, NotificationType type)
	{
		VmdMethods.addNotification(viewMetaData, jxPathContext, notificationStringCd, type);
	}

	protected boolean validateFieldIsNotEmpty(String fieldId)
	{
		return StringUtils.isNotEmpty(getParameter(fieldId));
	}

	protected boolean validateFieldIsNotEmpty(String fieldId, String error)
	{
		boolean isNotEmpty = validateFieldIsNotEmpty(fieldId);
		if (error == null)
			error = "This field is required";
		if (!isNotEmpty)
		{
			AbstractField field = (AbstractField) findById(fieldId);
			if (field != null)
				field.setActiveError(VmdMethods.getStringObject(error));
		}

		return isNotEmpty;
	}
}
