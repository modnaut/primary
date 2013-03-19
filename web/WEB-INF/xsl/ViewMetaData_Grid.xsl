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
		<xsl:choose>
			<xsl:when test="store"><!--If we have a store use the normal store template-->
				<xsl:for-each select="store">
					<xsl:apply-templates select="."/>,
				</xsl:for-each>
			</xsl:when>
			<xsl:when test="data"><!--if we're just defining inline data, use the inline data template-->
				<xsl:for-each select="data">
					<xsl:apply-templates select="." mode="inlineGrid"/>,
				</xsl:for-each>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="Columns">
		<xsl:if test="column">
			"columns": [
				<xsl:for-each select="column">
				{
					<xsl:apply-templates select="." mode="columnType"/>
					<xsl:value-of select="mn:attribute(., 'align', ',')"/>
					<xsl:call-template name="Columns"/>
					"dataIndex":	<xsl:choose>
										<xsl:when test="@dataIndex != '' ">
											<xsl:value-of select="mn:wrap-string(@dataIndex)"/>
										</xsl:when>
										<xsl:otherwise>
											"column<xsl:value-of select="position() - 1"/>"
										</xsl:otherwise>
									</xsl:choose>,
					<xsl:value-of select="mn:attribute(., 'draggable', ',')"/>
					<xsl:if test="editor">
						"editor": {
						
							<xsl:for-each select="editor/item">
								<xsl:call-template name="Item"/>
								<xsl:apply-templates select="."/>
							</xsl:for-each>

						},
					</xsl:if>
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
					"_d": 0
				}<xsl:call-template name="comma-delimit"/>
				</xsl:for-each>
			],
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="column[@xsi:type='RowNumbererColumn']" mode="columnType">
		"xtype": "rownumberer",
	</xsl:template>
	
	<xsl:template match="column[@xsi:type='ActionColumn']" mode="columnType">
		"xtype": "actioncolumn",
	</xsl:template>
	
	<xsl:template match="column[@xsi:type='BooleanColumn']" mode="columnType">
		<xsl:value-of select="mn:childString(., 'falseText', ',')"/>
		<xsl:value-of select="mn:childString(., 'trueText', ',')"/>
		<xsl:value-of select="mn:childString(., 'undefinedText', ',')"/>
		"xtype": "booleancolumn",
	</xsl:template>
	
	<xsl:template match="column[@xsi:type='DateColumn']" mode="columnType">
		<xsl:value-of select="mn:attribute(., 'format', ',')"/>
		"xtype": "datecolumn",
	</xsl:template>

	<xsl:template match="column[@xsi:type='NumberColumn']" mode="columnType">
		<xsl:value-of select="mn:attribute(., 'format', ',')"/>
		"xtype": "numbercolumn",
	</xsl:template>

	<xsl:template match="item[@xsi:type='GridPanel']">
		<xsl:call-template name="TablePanel"/>
		<xsl:call-template name="GridPlugins"/>
		"xtype": "grid"
	</xsl:template>
	
	<xsl:template name="GridPlugins">
		<xsl:if test="plugin">
			"plugins": [
				<xsl:for-each select="plugin">
					{
						<xsl:apply-templates select="."/>
					}<xsl:call-template name="comma-delimit"/>
				</xsl:for-each>
			],
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="GridEditingPlugin">
		<xsl:value-of select="mn:attribute(., 'clicksToEdit', ',')"/>
		<xsl:value-of select="mn:attribute(., 'pluginId', ',')"/>
		<xsl:value-of select="mn:attribute(., 'triggerEvent', ',')"/>
	</xsl:template>
	
	<xsl:template match="plugin[@xsi:type='CellEditing']">
		<xsl:call-template name="GridEditingPlugin"/>
		"ptype": "cellediting"
	</xsl:template>
	
	<xsl:template match="plugin[@xsi:type='RowEditing']">
		<xsl:call-template name="GridEditingPlugin"/>
		<xsl:value-of select="mn:attribute(., 'autoCancel', ',')"/>
		<xsl:value-of select="mn:attribute(., 'clicksToMoveEditor', ',')"/>
		<xsl:value-of select="mn:attribute(., 'errorSummary', ',')"/>
		"ptype": "rowediting"
	</xsl:template>
	
	<xsl:template match="plugin[@xsi:type='BufferedRendererPlugin']">
		<xsl:value-of select="mn:attribute(., 'leadingBufferZone', ',')"/>
		<xsl:value-of select="mn:attribute(., 'numFromEdge', ',')"/>
		<xsl:value-of select="mn:attribute(., 'pluginId', ',')"/>
		<xsl:value-of select="mn:attribute(., 'scrollToLoadBuffer', ',')"/>
		<xsl:value-of select="mn:attribute(., 'synchronousRender', ',')"/>
		<xsl:value-of select="mn:attribute(., 'trailingBufferZone', ',')"/>
		<xsl:value-of select="mn:attribute(., 'variableRowHeight', ',')"/>
		"ptype": "bufferedrenderer"
	</xsl:template>

</xsl:stylesheet>
