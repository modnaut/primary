<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:mn="http://www.modnaut.com" xmlns:json="http://json.org/">
	<xsl:output indent="no" omit-xml-declaration="yes" method="text" encoding="utf-8"/>
	
	<xsl:template name="Layout">
			<xsl:if test="Layout">
				"layout": {
					<xsl:apply-templates select="Layout"/>
				},
			</xsl:if>
	</xsl:template>

	<xsl:template match="Layout">
		<xsl:value-of select="mn:attribute(., 'itemCls', ',')"/>
		<xsl:value-of select="mn:attribute(., 'reserveScrollbar', ',')"/>
	</xsl:template>
	
	<xsl:template match="Layout[@xsi:type='AutoLayout']">
		"type": "auto"
	</xsl:template>
	
	<xsl:template name="AnchorLayout">
		<xsl:value-of select="mn:attribute(., 'defaultAnchor', ',')"/>
		<xsl:value-of select="mn:attribute(., 'manageOverflow', ',')"/>
	</xsl:template>
	
	<xsl:template match="Layout[@xsi:type='AnchorLayout']">
		<xsl:call-template name="AnchorLayout"/>
		"type": "anchor"
	</xsl:template>
	
	<xsl:template match="Layout[@xsi:type='AbsoluteLayout']">
		<xsl:call-template name="AnchorLayout"/>
		<xsl:value-of select="mn:attribute(., 'ignoreOnContentChange', ',')"/>
		"type": "absolute"
	</xsl:template>
	
	<xsl:template name="BoxLayout">
		<xsl:value-of select="mn:attribute(., 'align', ',')"/>
		<xsl:value-of select="mn:attribute(., 'constrainAlign', ',')"/>
		<xsl:value-of select="mn:attribute(., 'padding', ',')"/>
		<xsl:value-of select="mn:attribute(., 'pack', ',')"/>
	</xsl:template>
	
	<xsl:template match="Layout[@xsi:type='HBoxLayout']">
		<xsl:call-template name="BoxLayout"/>
		"type": "hbox"
	</xsl:template>
	
	<xsl:template match="Layout[@xsi:type='VBoxLayout']">
		<xsl:call-template name="BoxLayout"/>
		"type": "vbox"
	</xsl:template>
	
	<xsl:template match="Layout[@xsi:type='AccordionLayout']">
		<xsl:call-template name="BoxLayout"/>
		<xsl:value-of select="mn:attribute(., 'activeOnTop', ',')"/>
		<xsl:value-of select="mn:attribute(., 'animate', ',')"/>
		<xsl:value-of select="mn:attribute(., 'collapseFirst', ',')"/>
		<xsl:value-of select="mn:attribute(., 'fill', ',')"/>
		<xsl:value-of select="mn:attribute(., 'hideCollapseTool', ',')"/>
		<xsl:value-of select="mn:attribute(., 'multi', ',')"/>
		<xsl:value-of select="mn:attribute(., 'titleCollapse', ',')"/>
		"type": "accordion"
	</xsl:template>
	
	<xsl:template match="Layout[@xsi:type='ColumnLayout']">
		"type": "column"
	</xsl:template>
	
	<xsl:template match="Layout[@xsi:type='FitLayout']">
		"type": "fit"
	</xsl:template>
	
	<xsl:template match="Layout[@xsi:type='CenterLayout']">
		"type": "ux.center"
	</xsl:template>
	
</xsl:stylesheet>
