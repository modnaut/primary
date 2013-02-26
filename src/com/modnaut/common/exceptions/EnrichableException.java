package com.modnaut.common.exceptions;

import java.util.ArrayList;
import java.util.List;

public class EnrichableException extends RuntimeException
{
	public static final long serialVersionUID = -1;

	protected List<InfoItem> infoItems = new ArrayList<InfoItem>();

	protected class InfoItem
	{
		public int sessionId = 0;
		public String errorClass = null;
		public String errorMethod = null;
		public String errorCode = null;
		public String errorLevel = null;
		public String errorDetail = null;

		public InfoItem(int sessionId, String errorClass, String errorMethod, String errorCode, String errorLevel, String errorDetail)
		{
			this.sessionId = sessionId;
			this.errorClass = errorClass;
			this.errorMethod = errorMethod;
			this.errorCode = errorCode;
			this.errorLevel = errorLevel;
			this.errorDetail = errorDetail;
		}
	}

	public EnrichableException(String errorClass, String errorMethod, String errorCode, String errorLevel, String errorDetail)
	{
		addInfo(0, errorClass, errorMethod, errorCode, errorLevel, errorDetail);
	}

	public EnrichableException(String errorClass, String errorMethod, String errorCode, String errorLevel, String errorDetail, Throwable cause)
	{
		super(cause);
		addInfo(0, errorClass, errorMethod, errorCode, errorLevel, errorDetail);
	}

	public EnrichableException addInfo(int sessionId, String errorClass, String errorMethod, String errorCode, String errorLevel, String errorDetail)
	{
		this.infoItems.add(new InfoItem(sessionId, errorClass, errorMethod, errorCode, errorLevel, errorDetail));
		return this;
	}

	public String getCode()
	{
		StringBuilder builder = new StringBuilder();

		for (int i = this.infoItems.size() - 1; i >= 0; i--)
		{
			InfoItem info = this.infoItems.get(i);
			builder.append('[');
			builder.append(info.errorLevel);
			builder.append(':');
			builder.append(info.errorCode);
			builder.append(']');
		}

		return builder.toString();
	}

	public String toString()
	{
		StringBuilder builder = new StringBuilder();

		builder.append(getCode());
		builder.append('\n');

		// append additional context information.
		for (int i = this.infoItems.size() - 1; i >= 0; i--)
		{
			InfoItem info = this.infoItems.get(i);
			builder.append('[');
			builder.append(info.errorClass);
			builder.append(':');
			builder.append(info.errorMethod);
			builder.append(']');
			builder.append('\n');
			builder.append(info.errorDetail);
			if (i > 0)
				builder.append('\n');
		}

		// append root causes and text from this exception first.
		if (getMessage() != null)
		{
			builder.append('\n');
			if (getCause() == null)
			{
				builder.append(getMessage());
			}
			else if (!getMessage().equals(getCause().toString()))
			{
				builder.append(getMessage());
			}
		}
		appendException(builder, getCause());

		return builder.toString();
	}

	private void appendException(StringBuilder builder, Throwable throwable)
	{
		if (throwable == null)
			return;
		appendException(builder, throwable.getCause());
		builder.append(throwable.toString());
		builder.append('\n');
	}
}