package com.modnaut.apps.login;

import com.modnaut.common.utilities.EnrichableException;

public class InsufficientPrivilegeException extends EnrichableException
{
	public static final long serialVersionUID = -1;

	public InsufficientPrivilegeException(String errorClass, String errorMethod, String errorCode, String errorLevel, String errorDetail)
	{
		super(errorClass, errorMethod, errorCode, errorLevel, errorDetail);
		// TODO Auto-generated constructor stub
	}

}