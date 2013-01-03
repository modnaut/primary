<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:mn="http://www.modnaut.com">

	<xsl:output indent="no" omit-xml-declaration="yes" method="text" encoding="utf-8"/>
	<xsl:include href="StringFunctions.xsl"/>

	<xsl:template match="viewMetaData/viewport">
		
	</xsl:template>
	<xsl:template match="viewMetaData/items">
		{
			success: true,
			items: [<xsl:call-template name="items"/>
			]
		}
	</xsl:template>
	<xsl:template match="items" name="items">
		<xsl:for-each select="item">
			{
				<xsl:apply-templates select="."/>
			}
			<xsl:if test="position() != last()">,</xsl:if>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="layout">
		<xsl:if test="layout">
			layout: {
				type: "<xsl:value-of select="layout/@type"/>"
			}
		</xsl:if>
	</xsl:template>
	<xsl:template match="item[@xsi:type='Panel']">
		xtype: "panel",
		<xsl:call-template name="layout"/>
		<xsl:if test="title">
			title: <xsl:value-of select="mn:wrap-string(title/@stringId)"/>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
