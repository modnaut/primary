<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:mn="http://www.modnaut.com" xmlns:json="http://json.org/">
	<xsl:output indent="no" omit-xml-declaration="yes" method="text" encoding="utf-8"/>

		<xsl:template name="TablePanel">
		<xsl:call-template name="Panel"/>
		<xsl:value-of select="mn:attribute(., 'allowDeselect', ',')"/>
		<xsl:call-template name="Columns"/>
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
	
	<xsl:template name="Columns">
		<xsl:if test="column">
			columns: [
				<xsl:for-each select="column">
				{
					<xsl:value-of select="mn:attribute(., 'align', ',')"/>
					<xsl:call-template name="Columns"/>
					<xsl:value-of select="mn:attribute(., 'draggable', ',')"/>
					<xsl:value-of select="mn:childString(., 'emptyCellText', ',')"/>
					<xsl:value-of select="mn:attribute(., 'groupable', ',')"/>
					<xsl:value-of select="mn:attribute(., 'hideable', ',')"/>
					<xsl:value-of select="mn:attribute(., 'lockable', ',')"/>
					<xsl:value-of select="mn:attribute(., 'locked', ',')"/>
					<xsl:value-of select="mn:attribute(., 'menuDisabled', ',')"/>
					<xsl:value-of select="mn:childString(., 'menuText', ',')"/>
					<xsl:value-of select="mn:attribute(., 'sealed', ',')"/>
					<xsl:value-of select="mn:attribute(., 'sortable', ',')"/>
					<xsl:value-of select="mn:attribute(., 'tdCls', ',')"/>
					<xsl:value-of select="mn:childString(., 'text', ',')"/>
					<xsl:value-of select="mn:childString(., 'tooltip', ',')"/>
					<xsl:value-of select="mn:attribute(., 'dataIndex', '')"/>
				}<xsl:call-template name="comma-delimit"/>
				</xsl:for-each>
			],
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='GridPanel']">
		<xsl:call-template name="TablePanel"/>
		xtype: "grid"
	</xsl:template>

</xsl:stylesheet>
