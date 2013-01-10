<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:mn="http://www.modnaut.com" xmlns:json="http://json.org/">
	<xsl:output indent="no" omit-xml-declaration="yes" method="text" encoding="utf-8"/>
	<xsl:include href="StringFunctions.xsl"/>
	
	<xsl:template name="comma-delimit">
		<xsl:if test="position() != last()">
			<xsl:text>,</xsl:text>
		</xsl:if>
	</xsl:template>
		
	<xsl:function name="mn:attribute">
		<xsl:param name="node" as="node()"/>
		<xsl:param name="attributeName" as="xs:string"/>
		<xsl:choose>
			<xsl:when test="$node">
				<xsl:variable name="attributeValue" select="$node/@*[name() = $attributeName]"/>
				<xsl:choose>
					<xsl:when test="$attributeValue != '' ">
						<xsl:variable name="colon" select="':'"/>
						<xsl:sequence select="concat($attributeName,$colon,mn:wrap-string($attributeValue))"/>
					</xsl:when>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:sequence select=" '' "/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:function>
	
	<xsl:function name="mn:childString">
		<xsl:param name="node" as="node()"/>
		<xsl:param name="elementName" as="xs:string"/>
		<xsl:choose>
			<xsl:when test="$node">
				<xsl:variable name="stringValue" select="$node/*[local-name() = $elementName]/@stringCD"/>
				<xsl:choose>
					<xsl:when test="$stringValue != '' ">
						<xsl:variable name="colon" select="':'"/>
						<xsl:sequence select="concat($elementName,$colon,mn:wrap-string($stringValue))"/>
					</xsl:when>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:sequence select=" '' "/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:function>
	
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
				_d: 0
			}
			<xsl:call-template name="comma-delimit"/>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="Layout">
			layout: {
				<xsl:apply-templates select="Layout"/>
			},
	</xsl:template>

	<xsl:template match="Layout">
		<xsl:value-of select="mn:attribute(., 'itemCls')"/>,
		<xsl:value-of select="mn:attribute(., 'reserveScrollbar')"/>,
	</xsl:template>
	
	<xsl:template match="Layout[@xsi:type='AutoLayout']">
		type: "auto"
	</xsl:template>

	<xsl:template match="item">
		<xsl:value-of select="mn:attribute(., 'id')"/>,
		<xsl:value-of select="mn:attribute(., 'width')"/>,
		<xsl:value-of select="mn:attribute(., 'height')"/>,
	</xsl:template>

	<xsl:template match="item[@xsi:type='Container']">
		xtype: "container",
		<xsl:call-template name="Container"/>
	</xsl:template>

	<xsl:template name="Container">
		<xsl:call-template name="Layout"/>
		<xsl:if test="@autoScroll != '' ">
			<xsl:value-of select="mn:attribute(., 'autoScroll')"/>,
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='Panel']">
		xtype: "panel",
		<xsl:call-template name="Container"/>
		<xsl:value-of select="mn:childString(., 'title')"/>,
	</xsl:template>
</xsl:stylesheet>
