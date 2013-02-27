<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:mn="http://www.modnaut.com" xmlns:json="http://json.org/">
	<xsl:output indent="no" omit-xml-declaration="yes" method="text" encoding="utf-8"/>
	<xsl:param name="staticPath" select=" 'static/' "/>
	<xsl:param name="imagePath" select="concat($staticPath, 'images/')"/>
	<xsl:include href="ViewMetaData_Functions.xsl"/>
	<xsl:include href="ViewMetaData_Layout.xsl"/>
	<xsl:include href="ViewMetaData_Container.xsl"/>
	<xsl:include href="ViewMetaData_Data.xsl"/>
	<xsl:include href="ViewMetaData_Form.xsl"/>
	<xsl:include href="ViewMetaData_Grid.xsl"/>
	
	<xsl:template match="viewMetaData">
		<xsl:if test="items">
			{
				<xsl:apply-templates select="items"/>
				"success": true
			}
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="items">
		"items": [
			<xsl:for-each select="item">
				{
					<xsl:call-template name="Item"/>
					<xsl:apply-templates select="."/>
				}
				<xsl:call-template name="comma-delimit"/>
			</xsl:for-each>
		],
	</xsl:template>

	<xsl:template name="Item">
		<xsl:if test="@id != '' ">
			"itemId": <xsl:value-of select="mn:wrap-string(@id)"/>,
		</xsl:if>
		<xsl:value-of select="mn:attribute(., 'flex', ',')"/>
		<xsl:value-of select="mn:attribute(., 'height', ',')"/>
		<xsl:value-of select="mn:attribute(., 'hidden', ',')"/>
		<xsl:value-of select="mn:attribute(., 'hideMode', ',')"/>
		<xsl:value-of select="mn:attribute(., 'region', ',')"/>
		<xsl:value-of select="mn:attribute(., 'split', ',')"/>
		<xsl:value-of select="mn:attribute(., 'width', ',')"/>
		
		<xsl:call-template name="Listeners"/>
	</xsl:template>
	
	<xsl:template name="Parameter">
		<xsl:value-of select="mn:wrap-string(@name)"/>: <xsl:value-of select="mn:wrap-string(@value)"/>
	</xsl:template>
	
	<xsl:template name="Listeners">
		<xsl:if test="listener">
			listeners: {
				<xsl:for-each select="listener">
					<xsl:call-template name="Listener"/>
					<xsl:call-template name="comma-delimit"/>
				</xsl:for-each>
			},
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="Listener">
		<xsl:value-of select="@event"/>: function() {
			return Globals.eventListener({
				arguments: arguments,
				component: this, 
				eventType: <xsl:value-of select="mn:wrap-string(@event)"/>,
				actionType: <xsl:value-of select="mn:wrap-string(@action)"/>,
				itemsToUpdate: <xsl:value-of select="mn:wrap-string(@itemsToUpdate)"/>,
				parameters: {
					<xsl:if test="@class != ''">
						"Class": <xsl:value-of select="mn:wrap-string(@class)"/>
						<xsl:if test="@method != '' or parameter">,</xsl:if>
					</xsl:if>
					<xsl:if test="@method != ''">
						"Method": <xsl:value-of select="mn:wrap-string(@method)"/>
						<xsl:if test="parameter">,</xsl:if>
					</xsl:if>
					<xsl:for-each select="parameter">
						<xsl:call-template name="Parameter"/>
						<xsl:call-template name="comma-delimit"/>
					</xsl:for-each>
					}
			});
		}
	</xsl:template>

</xsl:stylesheet>
