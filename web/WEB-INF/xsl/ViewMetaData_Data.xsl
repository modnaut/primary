<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:mn="http://www.modnaut.com" xmlns:json="http://json.org/">
	<xsl:output indent="no" omit-xml-declaration="yes" method="text" encoding="utf-8"/>
	
	<xsl:template name="AbstractStore">
		<xsl:param name="valueField" as="xs:string?"/>
		<xsl:param name="displayField" as="xs:string?"/>
		<xsl:choose>
			<xsl:when test="@autoDestroy != '' ">
				<xsl:value-of select="mn:attribute(., 'autoDestroy', ',')"/>
			</xsl:when>
			<xsl:otherwise>
				"autoDestroy": true,
			</xsl:otherwise>
		</xsl:choose>
		<xsl:value-of select="mn:attribute(., 'autoLoad', ',')"/>
		<xsl:value-of select="mn:attribute(., 'autoSync', ',')"/>
		<xsl:value-of select="mn:attribute(., 'batchUpdateMode', ',')"/>
		<xsl:value-of select="mn:attribute(., 'defaultSortDirection', ',')"/>
		<xsl:value-of select="mn:attribute(., 'filterOnLoad', ',')"/>
		<xsl:value-of select="mn:attribute(., 'remoteFilter', ',')"/>
		<xsl:value-of select="mn:attribute(., 'remoteSort', ',')"/>
		<xsl:value-of select="mn:attribute(., 'sortOnLoad', ',')"/>
		<xsl:value-of select="mn:attribute(., 'storeId', ',')"/>
		<xsl:call-template name="Model">
			<xsl:with-param name="valueField" select="$valueField"/>
			<xsl:with-param name="displayField" select="$displayField"/>
		</xsl:call-template>
		<xsl:call-template name="Proxy"/>
	</xsl:template>
	
	<xsl:template match="store[@xsi:type = 'ExistingStore']">
			"store": Ext.getStore(<xsl:value-of select="mn:wrap-string(@sourceStoreId)"/>)
	</xsl:template>
	
	<xsl:template match="store[@xsi:type = 'Store'] ">
		<xsl:param name="valueField" as="xs:string?"/>
		<xsl:param name="displayField" as="xs:string?"/>
		"store": {
			<xsl:call-template name="AbstractStore">
				<xsl:with-param name="valueField" select="$valueField"/>
				<xsl:with-param name="displayField" select="$displayField"/>
			</xsl:call-template>
			<xsl:for-each select="data">
				"pageSize": <xsl:value-of select="count(record)"/>,
				"proxy": {
					"type": "memory"
				},
				<xsl:choose>
					<xsl:when test="../../@xsi:type = 'GridPanel' ">
						<xsl:call-template name="RecordSet">
							<xsl:with-param name="mode" select=" 'grid' "/>
						</xsl:call-template>
					</xsl:when>
					<xsl:when test="../../@xsi:type='ComboBox' ">
						<xsl:call-template name="RecordSet">
							<xsl:with-param name="mode" select=" 'combo' "/>
							<xsl:with-param name="valueField" select="$valueField"/>
							<xsl:with-param name="displayField" select="$displayField"/>
						</xsl:call-template>
					</xsl:when>
				</xsl:choose>
			</xsl:for-each>
			<xsl:value-of select="mn:attribute(., 'buffered', ',')"/>
			<xsl:value-of select="mn:attribute(., 'clearOnPageLoad', ',')"/>
			<xsl:value-of select="mn:attribute(., 'clearRemovedOnLoad', ',')"/>
			<xsl:value-of select="mn:attribute(., 'groupDir', ',')"/>
			<xsl:value-of select="mn:attribute(., 'groupField', ',')"/>
			<xsl:value-of select="mn:attribute(., 'leadingBufferZone', ',')"/>
			<xsl:choose>
				<xsl:when test="string(@pageSize) = ''">
					"pageSize": 1000000,
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="mn:attribute(., 'pageSize', ',')"/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:value-of select="mn:attribute(., 'purgePageCount', ',')"/>
			<xsl:value-of select="mn:attribute(., 'remoteGroup', ',')"/>
			<xsl:value-of select="mn:attribute(., 'sortOnFilter', ',')"/>
			<xsl:value-of select="mn:attribute(., 'trailingBufferZone', ',')"/>
			"autoLoad": true
		}
	</xsl:template>

	<xsl:template name="Model">
		<xsl:param name="valueField" as="xs:string?"/>
		<xsl:param name="displayField" as="xs:string?"/>
		<xsl:choose>
			<xsl:when test="model">
				<xsl:for-each select="model">
					"fields": [
						<xsl:for-each select="field">
							<xsl:call-template name="ModelField"/>
							<xsl:call-template name="comma-delimit"/>
						</xsl:for-each>
					],
				</xsl:for-each>
			</xsl:when>
			<xsl:when test="@modelName != '' ">
				"model": <xsl:value-of select="mn:wrap-string(@modelName)"/>,
			</xsl:when>
			<xsl:when test="../@xsi:type = 'GridPanel' ">
				"fields": [
					<xsl:for-each select="../column">
						{
							<xsl:choose>
								<xsl:when test="@dataIndex != ''">
									"name": <xsl:value-of select="mn:wrap-string(@dataIndex)"/>
								</xsl:when>
								<xsl:otherwise>
									"name": "column<xsl:value-of select="position() - 1"/>"
								</xsl:otherwise>
							</xsl:choose>
							<xsl:if test="@type != ''">
								,<xsl:value-of select="mn:attribute(., 'type', '')"/>
							</xsl:if>
						}<xsl:call-template name="comma-delimit"/>
					</xsl:for-each>
				],
			</xsl:when>
			<xsl:when test="../@xsi:type='ComboBox' ">
				"fields": [<xsl:value-of select="mn:wrap-string($valueField)"/>, <xsl:value-of select="mn:wrap-string($displayField)"/>],
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="ModelField">
		{
			"name": <xsl:value-of select="mn:wrap-string(@name)"/>
			<xsl:if test="@type != '' ">
				,<xsl:value-of select="mn:attribute(., 'type', '')"/>
			</xsl:if>
		}
	</xsl:template>
	
	<xsl:template name="Proxy">
		<xsl:for-each select="proxy">		
			<xsl:apply-templates select="."/>,
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template name="AbstractProxy">
	</xsl:template>
	
	<xsl:template name="ServerProxy">
		<xsl:call-template name="AbstractProxy"/>
		<xsl:value-of select="mn:attribute(., 'directionParam', ',')"/>
		<xsl:value-of select="mn:attribute(., 'filterParam', ',')"/>
		<xsl:value-of select="mn:attribute(., 'groupDirectionParam', ',')"/>
		<xsl:value-of select="mn:attribute(., 'groupParam', ',')"/>
		<xsl:value-of select="mn:attribute(., 'idParam', ',')"/>
		<xsl:value-of select="mn:attribute(., 'limit', ',')"/>
		<xsl:value-of select="mn:attribute(., 'noCache', ',')"/>
		<xsl:value-of select="mn:attribute(., 'pageParam', ',')"/>
		<xsl:value-of select="mn:attribute(., 'simpleGroupMode', ',')"/>
		<xsl:value-of select="mn:attribute(., 'simpleSortMode', ',')"/>
		<xsl:value-of select="mn:attribute(., 'sortParam', ',')"/>
		<xsl:value-of select="mn:attribute(., 'startParam', ',')"/>
		<xsl:value-of select="mn:attribute(., 'timeout', ',')"/>
		<xsl:choose>
			<xsl:when test="@url != '' ">
				<xsl:value-of select="mn:attribute(., 'url', ',')"/>
			</xsl:when>
			<xsl:otherwise>
				"url": "ApplicationServlet",
			</xsl:otherwise>
		</xsl:choose>
		"extraParams": {
			<xsl:if test="@xsi:type = 'AjaxProxy' ">
				<xsl:for-each select="extraParam">
					<xsl:call-template name="Parameter"/>,
				</xsl:for-each>
				<xsl:value-of select="mn:attribute(., 'Class', ',')"/>
				<xsl:value-of select="mn:attribute(., 'Method', '')"/>
			</xsl:if>
		},
		"reader": {
			"type": "json",
			<xsl:if test="../model/@idProperty != '' ">
				"idProperty": <xsl:value-of select="mn:wrap-string(../model/@idProperty)"/>,
			</xsl:if>
			"root": "data"
		},
	</xsl:template>
	
	<xsl:template match="proxy[@xsi:type='AjaxProxy']">
		"proxy": {
			<xsl:call-template name="ServerProxy"/>
			"type": "ajax"
		}
	</xsl:template>
	
	<xsl:template name="ClientProxy">
	</xsl:template>
	
	<xsl:template match="proxy[@xsi:type='MemoryProxy']">
		"proxy": {
			<xsl:call-template name="ClientProxy"/>
			"type": "memory"
		}
	</xsl:template>
	
	<xsl:template match="data" mode="inlineGrid">
		"store": {
			"autoDestroy": true,
			"pageSize": 1000000,
			"fields": [
				<xsl:for-each select="../column">
					{
						<xsl:choose>
							<xsl:when test="@dataIndex != ''">
								"name": <xsl:value-of select="mn:wrap-string(@dataIndex)"/>
							</xsl:when>
							<xsl:otherwise>
								"name": "column<xsl:value-of select="position() - 1"/>"
							</xsl:otherwise>
						</xsl:choose>
						<xsl:if test="@type != ''">
							,<xsl:value-of select="mn:attribute(., 'type', '')"/>
						</xsl:if>
					}<xsl:call-template name="comma-delimit"/>
				</xsl:for-each>
			],
			<xsl:call-template name="RecordSet">
				<xsl:with-param name="mode" select=" 'grid' "/>
			</xsl:call-template>
			"proxy": {
				"type": "memory"
			}
		}
	</xsl:template>
	
	<xsl:template match="data" mode="inlineCombo">
		<xsl:param name="valueField" as="xs:string"/>
		<xsl:param name="displayField" as="xs:string"/>
		"store": {
			"autoDestroy": true,
			<xsl:call-template name="RecordSet">
				<xsl:with-param name="mode" select=" 'combo' "/>
				<xsl:with-param name="valueField" select="$valueField"/>
				<xsl:with-param name="displayField" select="$displayField"/>
			</xsl:call-template>
			"fields": [<xsl:value-of select="mn:wrap-string($valueField)"/>, <xsl:value-of select="mn:wrap-string($displayField)"/>]
		}
	</xsl:template>
	
	<xsl:template name="RecordSet">
		<xsl:param name="mode" as="xs:string?"/>
		<xsl:param name="valueField" as="xs:string?"/>
		<xsl:param name="displayField" as="xs:string?"/>
		"data": [
				<xsl:for-each select="record">
					{
						<xsl:for-each select="field">
							<xsl:choose>
								<xsl:when test="@name != '' ">
									<xsl:value-of select="mn:wrap-string(@name)"/>
								</xsl:when>
								<xsl:when test="$mode = 'grid' ">
									"column<xsl:value-of select="position() - 1"/>"
								</xsl:when>
								<xsl:when test="$mode = 'combo' ">
									<xsl:choose>
										<xsl:when test="position() = 1">
											<xsl:value-of select="mn:wrap-string($valueField)"/> 
										</xsl:when>
										<xsl:when test="position() = 2">
											<xsl:value-of select="mn:wrap-string($displayField)"/> 
										</xsl:when>
									</xsl:choose>
								</xsl:when>
							</xsl:choose>
							: <xsl:value-of select="mn:wrap-string(@value)"/><xsl:call-template name="comma-delimit"/>
						</xsl:for-each>
					}<xsl:call-template name="comma-delimit"/>
				</xsl:for-each>
			],
	</xsl:template>
</xsl:stylesheet>