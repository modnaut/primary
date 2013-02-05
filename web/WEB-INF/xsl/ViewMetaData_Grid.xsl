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
		<xsl:call-template name="Store"/>
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
					<xsl:value-of select="mn:attribute(., 'flex', ',')"/>
					<xsl:value-of select="mn:attribute(., 'hidden', ',')"/>
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
					<xsl:value-of select="mn:attribute(., 'width', ',')"/>
					<xsl:value-of select="mn:attribute(., 'dataIndex', '')"/>
				}<xsl:call-template name="comma-delimit"/>
				</xsl:for-each>
			],
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="Store">
		store: {
			fields: [
				<xsl:choose>
					<xsl:when test="store">
						<xsl:for-each select="store/model/field">
							{
								name: <xsl:value-of select="mn:wrap-string(@name)"/>
								<xsl:if test="@type != '' ">
									,type: <xsl:value-of select="mn:wrap-string(@type)"/>
								</xsl:if>
							}
							<xsl:call-template name="comma-delimit"/>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise><!--Shortcut for grids, no need to define a model, we'll pull the fields from the columns-->
						<xsl:for-each select="column">
							{
								name: <xsl:value-of select="mn:wrap-string(@dataIndex)"/>
								<xsl:if test="@type != '' ">
									,type: <xsl:value-of select="mn:wrap-string(@type)"/>
								</xsl:if>
							}
							<xsl:call-template name="comma-delimit"/>
						</xsl:for-each>
					</xsl:otherwise>
				</xsl:choose>
			],
			<xsl:choose>
				<xsl:when test="store[@xsi:type = 'JsonStore' ]">
					autoLoad: true,
					proxy: {
						type: "ajax",
						url: "ApplicationServlet",
						extraParams: {
							class: "<xsl:value-of select="store[@xsi:type = 'JsonStore' ]/proxy/@class"/>",
							method: "<xsl:value-of select="store[@xsi:type = 'JsonStore' ]/proxy/@method"/>"
							<xsl:if test="store[@xsi:type = 'JsonStore' ]/proxy/extraParam">
								,
								<xsl:for-each select="store[@xsi:type = 'JsonStore' ]/proxy/extraParam">
									"<xsl:value-of select="@name"/>" : <xsl:value-of select="mn:wrap-string(@value)"/>
									<xsl:call-template name="comma-delimit"/>
								</xsl:for-each>
							</xsl:if>
						},
						reader: {
							root: "data",
							type: "json"
						}
					}
				</xsl:when>
				<xsl:otherwise>
					data: [
						<xsl:choose>
							<xsl:when test="store">
								<xsl:apply-templates select="store/data"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:apply-templates select="records"/>
							</xsl:otherwise>
						</xsl:choose>
					]
				</xsl:otherwise>
			</xsl:choose>
		},
	</xsl:template>
	
	<xsl:template match="data">
		<xsl:call-template name="RecordSet"/>
	</xsl:template>
	
	<xsl:template match="records">
		<xsl:call-template name="RecordSet"/>
	</xsl:template>
	
	<xsl:template name="RecordSet">
		<xsl:for-each select="record">
			{
				<xsl:for-each select="field">
					<xsl:value-of select="@name"/> : <xsl:value-of select="mn:wrap-string(@value)"/>
					<xsl:call-template name="comma-delimit"/>
				</xsl:for-each>
			}
			<xsl:call-template name="comma-delimit"/>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='GridPanel']">
		<xsl:call-template name="TablePanel"/>
		xtype: "grid"
	</xsl:template>

</xsl:stylesheet>
