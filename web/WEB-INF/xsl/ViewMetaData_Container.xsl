<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:mn="http://www.modnaut.com" xmlns:json="http://json.org/">
	<xsl:output indent="no" omit-xml-declaration="yes" method="text" encoding="utf-8"/>

	<xsl:template match="item[@xsi:type='Container']">
		<xsl:call-template name="Container"/>
		xtype: "container"
	</xsl:template>

	<xsl:template name="Container">
		<xsl:call-template name="Layout"/>
		<xsl:value-of select="mn:attribute(., 'autoScroll', ',')"/>
		<xsl:value-of select="mn:attribute(., 'border', ',')"/>
		<xsl:value-of select="mn:attribute(., 'cls', ',')"/>
		<xsl:value-of select="mn:attribute(., 'columnWidth', ',')"/>
		<xsl:value-of select="mn:attribute(., 'html', ',')"/>
		<xsl:value-of select="mn:attribute(., 'margin', ',')"/>
		<xsl:value-of select="mn:attribute(., 'minHeight', ',')"/>
		<xsl:value-of select="mn:attribute(., 'maxHeight', ',')"/>
		<xsl:value-of select="mn:attribute(., 'minWidth', ',')"/>
		<xsl:value-of select="mn:attribute(., 'maxWidth', ',')"/>
		<xsl:value-of select="mn:attribute(., 'padding', ',')"/>
		<xsl:value-of select="mn:attribute(., 'region', ',')"/>
		<xsl:value-of select="mn:attribute(., 'resizable', ',')"/>
		<xsl:value-of select="mn:attribute(., 'shrinkWrap', ',')"/>
		<xsl:apply-templates select="items"/>
	</xsl:template>
	
	<xsl:template name="Panel">
		<xsl:call-template name="Container"/>
		<xsl:value-of select="mn:attribute(., 'animCollapse', ',')"/>
		<xsl:value-of select="mn:attribute(., 'bodyPadding', ',')"/>
		<xsl:value-of select="mn:attribute(., 'collapseDirection', ',')"/>
		<xsl:value-of select="mn:attribute(., 'collapseFirst', ',')"/>
		<xsl:value-of select="mn:attribute(., 'collapseMode', ',')"/>
		<xsl:value-of select="mn:attribute(., 'collapsed', ',')"/>
		<xsl:value-of select="mn:attribute(., 'collapsible', ',')"/>
		<xsl:value-of select="mn:attribute(., 'headerPosition', ',')"/>
		<xsl:value-of select="mn:attribute(., 'hideCollapseTool', ',')"/>
		<xsl:value-of select="mn:attribute(., 'hideHeader', ',')"/>
		<xsl:value-of select="mn:imageSpec(icon, 'icon', ',')"/>
		<xsl:value-of select="mn:attribute(., 'iconCls', ',')"/>
		<xsl:value-of select="mn:childString(., 'title', ',')"/>
		<xsl:value-of select="mn:attribute(., 'titleAlign', ',')"/>
		<xsl:value-of select="mn:attribute(., 'titleCollapse', ',')"/>
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='Panel']">
		<xsl:call-template name="Panel"/>
		xtype: "panel"
	</xsl:template>
	
	<xsl:template name="TablePanel">
		<xsl:call-template name="Panel"/>
		<xsl:value-of select="mn:attribute(., 'allowDeselect', ',')"/>
		columns: [
		],
		<xsl:value-of select="mn:attribute(., 'columnLines', ',')"/>
		<xsl:value-of select="mn:attribute(., 'disableSelection', ',')"/>
		<xsl:value-of select="mn:childString(., 'emptyText', ',')"/>
		<xsl:value-of select="mn:attribute(., 'enableColumnHide', ',')"/>
		<xsl:value-of select="mn:attribute(., 'enableColumnMove', ',')"/>
		<xsl:value-of select="mn:attribute(., 'enableColumnResize', ',')"/>
		<xsl:value-of select="mn:attribute(., 'enableLocking', ',')"/>
		<xsl:value-of select="mn:attribute(., 'hideHeaders', ',')"/>
		<xsl:value-of select="mn:attribute(., 'rowLines', ',')"/>
		<xsl:value-of select="mn:attribute(., 'scroll', ',')"/>
		<xsl:value-of select="mn:attribute(., 'sortableColumns', ',')"/>
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='GridPanel']">
		<xsl:call-template name="TablePanel"/>
		xtype: "grid"
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='TabPanel']">
		<xsl:call-template name="Panel"/>
		<xsl:value-of select="mn:attribute(., 'activeTab', ',')"/>
		<xsl:value-of select="mn:attribute(., 'deferredRender', ',')"/>
		<xsl:value-of select="mn:attribute(., 'minTabWidth', ',')"/>
		<xsl:value-of select="mn:attribute(., 'maxTabWidth', ',')"/>
		<xsl:value-of select="mn:attribute(., 'plain', ',')"/>
		<xsl:value-of select="mn:attribute(., 'removePanelHeader', ',')"/>
		<xsl:value-of select="mn:attribute(., 'tabPosition', ',')"/>
		xtype: "tabpanel"
	</xsl:template>
</xsl:stylesheet>
