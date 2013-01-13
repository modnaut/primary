<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:mn="http://www.modnaut.com" xmlns:json="http://json.org/">
	<xsl:output indent="no" omit-xml-declaration="yes" method="text" encoding="utf-8"/>
	
	<xsl:template match="item[@xsi:type='Button']">
		<xsl:value-of select="mn:attribute(., 'allowDepress', ',')"/>
		<xsl:value-of select="mn:attribute(., 'arrowAlign', ',')"/>
		<xsl:value-of select="mn:attribute(., 'border', ',')"/>
		<xsl:value-of select="mn:attribute(., 'disabled', ',')"/>
		<xsl:value-of select="mn:attribute(., 'enableToggle', ',')"/>
		<xsl:value-of select="mn:attribute(., 'handleMouseEvents', ',')"/>
		<xsl:value-of select="mn:imageSpec(icon, 'icon', ',')"/>
		<xsl:value-of select="mn:attribute(., 'iconAlign', ',')"/>
		<xsl:value-of select="mn:attribute(., 'iconCls', ',')"/>
		<xsl:value-of select="mn:attribute(., 'menuAlign', ',')"/>
		<xsl:value-of select="mn:attribute(., 'overflowText', ',')"/>
		<xsl:value-of select="mn:attribute(., 'pressed', ',')"/>
		<xsl:value-of select="mn:attribute(., 'scale', ',')"/>
		<xsl:value-of select="mn:attribute(., 'shrinkWrap', ',')"/>
		<xsl:value-of select="mn:attribute(., 'tabIndex', ',')"/>
		<xsl:value-of select="mn:childString(., 'text', ',')"/>
		<xsl:value-of select="mn:attribute(., 'textAlign', ',')"/>
		<xsl:value-of select="mn:attribute(., 'toggleGroup', ',')"/>
		<xsl:value-of select="mn:childString(., 'tooltip', ',')"/>
		xtype: "button"
	</xsl:template>
	
	<xsl:template name="Labelable">
		<xsl:value-of select="mn:childString(., 'activeError', ',')"/>
		<xsl:value-of select="mn:attribute(., 'autoFitErrors', ',')"/>
		<xsl:value-of select="mn:childString(., 'fieldLabel', ',')"/>
		<xsl:value-of select="mn:attribute(., 'hideEmptyLabel', ',')"/>
		<xsl:value-of select="mn:attribute(., 'hideLabel', ',')"/>
		<xsl:value-of select="mn:attribute(., 'labelAlign', ',')"/>
		<xsl:value-of select="mn:attribute(., 'labelPad', ',')"/>
		<xsl:value-of select="mn:attribute(., 'labelSeparator', ',')"/>
		<xsl:value-of select="mn:attribute(., 'labelWidth', ',')"/>
		<xsl:value-of select="mn:attribute(., 'msgTarget', ',')"/>
		<xsl:value-of select="mn:attribute(., 'preventMark', ',')"/>
	</xsl:template>
	
	<xsl:template name="AbstractField">
		<xsl:call-template name="Labelable"/>
		<xsl:value-of select="mn:attribute(., 'disabled', ',')"/>
		<xsl:value-of select="mn:childString(., 'invalidText', ',')"/>
		<xsl:if test="@id != '' and @radioGroupName = ''">
			name: <xsl:value-of select="mn:wrap-string(@id)"/>,
		</xsl:if>
		<xsl:value-of select="mn:attribute(., 'readOnly', ',')"/>
		<xsl:value-of select="mn:attribute(., 'submitValue', ',')"/>
		<xsl:value-of select="mn:attribute(., 'tabIndex', ',')"/>
		<xsl:value-of select="mn:attribute(., 'validateOnBlur', ',')"/>
		<xsl:value-of select="mn:attribute(., 'validateOnChange', ',')"/>
		<xsl:value-of select="mn:attribute(., 'value', ',')"/>
	</xsl:template>
	
	<xsl:template name="Checkbox">
		<xsl:call-template name="AbstractField"/>
		<xsl:value-of select="mn:childString(., 'boxLabel', ',')"/>
		<xsl:value-of select="mn:attribute(., 'boxLabelAlign', ',')"/>
		<xsl:value-of select="mn:attribute(., 'checked', ',')"/>
		<xsl:value-of select="mn:attribute(., 'inputValue', ',')"/>
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='Checkbox']">
		<xsl:call-template name="Checkbox"/>
		xtype: "checkbox"
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='Radio']">
		<xsl:call-template name="Checkbox"/>
		<xsl:if test="@radioGroupName != ''">
			name: <xsl:value-of select="mn:wrap-string(@radioGroupName)"/>,
		</xsl:if>
		xtype: "radio"
	</xsl:template>
</xsl:stylesheet>
