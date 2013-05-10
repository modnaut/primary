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
	<xsl:include href="ViewMetaData_Chart.xsl"/>
	
	<xsl:template match="viewMetaData">
		{
			<xsl:call-template name="Notifications"/>
			<xsl:call-template name="ModalWindows"/>
			"items": [
				<xsl:call-template name="Items"/>
			],
			"success": true
		}
	</xsl:template>
	
	<xsl:template name="ModalWindows">
		<xsl:if test="window">
			"windows": [
				<xsl:for-each select="window">
					{
						<xsl:apply-templates select="."/>
						_d: 0
					}
					<xsl:call-template name="comma-delimit"/>
				</xsl:for-each>
			],
		</xsl:if>
	</xsl:template>

	
	<xsl:template name="MessageBox">
		<xsl:value-of select="mn:childString(., 'text', ',')"/>
		<xsl:value-of select="mn:childString(., 'title', ',')"/>
		<xsl:value-of select="mn:attribute(., 'icon', ',')"/>
		<xsl:call-template name="Listeners"/>
	</xsl:template>
	
	<xsl:template match="window[@xsi:type='Alert']">
		<xsl:call-template name="MessageBox"/>
		"type": "alert",
	</xsl:template>
	
	<xsl:template match="window[@xsi:type='Confirm']">
		<xsl:call-template name="MessageBox"/>
		"type": "confirm",
	</xsl:template>
	
	<xsl:template match="window[@xsi:type='Prompt']">
		<xsl:call-template name="MessageBox"/>
		<xsl:value-of select="mn:childString(., 'defaultValue', ',')"/>
		<xsl:value-of select="mn:attribute(., 'multiline', ',')"/>
		"type": "prompt",
	</xsl:template>
	
	<xsl:template name="Notifications">
		<xsl:if test="notification">
			"dockedItems": [
				<xsl:for-each select="notification">
					<xsl:apply-templates select="."/>
					<xsl:call-template name="comma-delimit"/>
				</xsl:for-each>
			],
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="notification">
		{
			<xsl:value-of select="mn:attribute(., 'hideAfterMs', ',')"/>
			<xsl:value-of select="mn:childString(., 'text', ',')"/>
			<xsl:value-of select="mn:attribute(., 'type', ',')"/>
			"xtype": "notificationBar"
		}
	</xsl:template>
	
	<xsl:template match="items">
		"items": [
			<xsl:call-template name="Items"/>
		],
	</xsl:template>
	
	<xsl:template name="Items">
		<xsl:for-each select="item">
				{
					<xsl:call-template name="Item"/>
					<xsl:apply-templates select="."/>
				}
				<xsl:call-template name="comma-delimit"/>
			</xsl:for-each>
	</xsl:template>

	<xsl:template name="Item">
		<xsl:if test="@id != '' ">
			"itemId": <xsl:value-of select="mn:wrap-string(@id)"/>,
		</xsl:if>
		<xsl:if test="@anchor != '' ">
			"anchor": "<xsl:value-of select="@anchor"/>",<!--Anchor value MUST be output as string, even if it's a valid number value-->
		</xsl:if>
		<xsl:value-of select="mn:attribute(., 'colspan', ',')"/>
		<xsl:value-of select="mn:attribute(., 'cellId', ',')"/>
		<xsl:value-of select="mn:attribute(., 'cellCls', ',')"/>
		<xsl:value-of select="mn:attribute(., 'disabled', ',')"/>
		<xsl:value-of select="mn:attribute(., 'flex', ',')"/>
		<xsl:value-of select="mn:attribute(., 'height', ',')"/>
		<xsl:value-of select="mn:attribute(., 'hidden', ',')"/>
		<xsl:value-of select="mn:attribute(., 'hideMode', ',')"/>
		<xsl:value-of select="mn:attribute(., 'region', ',')"/>
		<xsl:value-of select="mn:attribute(., 'rowspan', ',')"/>
		<xsl:value-of select="mn:attribute(., 'split', ',')"/>
		<xsl:value-of select="mn:attribute(., 'splitterResize', ',')"/>
		<xsl:value-of select="mn:attribute(., 'width', ',')"/>
		<xsl:value-of select="mn:attribute(., 'x', ',')"/>
		<xsl:value-of select="mn:attribute(., 'y', ',')"/>
		<xsl:call-template name="Listeners"/>
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='Xtype']">
		<xsl:for-each select="parameter">
			<xsl:call-template name="Parameter"/>,
		</xsl:for-each>
		<xsl:value-of select="mn:attribute(., 'xtype', '')"/>
	</xsl:template>
	
	<xsl:template name="Parameter">
		<xsl:value-of select="mn:wrap-string(@name)"/>: <xsl:value-of select="mn:wrap-string(@value)"/>
	</xsl:template>
	
	<xsl:template name="Listeners">
		<xsl:if test="listener">
			"listeners": {
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
					<xsl:if test="@Class != ''">
						"Class": <xsl:value-of select="mn:wrap-string(@Class)"/>
						<xsl:if test="@Method != '' or parameter">,</xsl:if>
					</xsl:if>
					<xsl:if test="@Method != ''">
						"Method": <xsl:value-of select="mn:wrap-string(@Method)"/>
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
