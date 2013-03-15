<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:mn="http://www.modnaut.com" xmlns:json="http://json.org/">
	<xsl:output indent="no" omit-xml-declaration="yes" method="text" encoding="utf-8"/>
	
	
	<xsl:template match="item[@xsi:type='Chart']">
		<xsl:call-template name="Axes"/>
		<xsl:apply-templates select="background"/>
		<xsl:apply-templates select="legend"/>
		<xsl:apply-templates select="series"/>
		<xsl:value-of select="mn:attribute(., 'animate', ',')"/>
		<xsl:value-of select="mn:attribute(., 'insetPadding', ',')"/>
		<xsl:value-of select="mn:attribute(., 'theme', ',')"/>
		
		
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
		"xtype": "chart"
	</xsl:template>
	
	<xsl:template name="Axes">
		"axes": [
			<xsl:for-each select="axis">
				{
					<xsl:apply-templates select="."/>
				}<xsl:call-template name="comma-delimit"/>
			</xsl:for-each>
		],
	</xsl:template>
	

	
	<xsl:template name="AbstractAxis">
			<xsl:if test="field">
				"fields": [
					<xsl:for-each select="field">
						<xsl:value-of select="mn:wrap-string(@name)"/>
						<xsl:call-template name="comma-delimit"/>
					</xsl:for-each>
				],
			</xsl:if>
			<xsl:for-each select="label">
				"label": {
					<xsl:value-of select="mn:attribute(., 'display', ',')"/>
					<xsl:value-of select="mn:attribute(., 'color', ',')"/>
					<xsl:value-of select="mn:attribute(., 'contrast', ',')"/>
					<xsl:value-of select="mn:attribute(., 'field', ',')"/>
					<xsl:value-of select="mn:attribute(., 'minMargin', ',')"/>
					<xsl:value-of select="mn:attribute(., 'font', ',')"/>
					<xsl:value-of select="mn:attribute(., 'orientation', ',')"/>
				},
			</xsl:for-each>
	</xsl:template>
	
	<xsl:template name="Axis">
		<xsl:call-template name="AbstractAxis"/>
		<xsl:value-of select="mn:attribute(., 'adjustEnd', ',')"/>
		<xsl:value-of select="mn:attribute(., 'dashSize', ',')"/>
		<xsl:value-of select="mn:attribute(., 'grid', ',')"/>
		<xsl:value-of select="mn:attribute(., 'length', ',')"/>
		<xsl:value-of select="mn:attribute(., 'majorTickSteps', ',')"/>
		<xsl:value-of select="mn:attribute(., 'minorTickSteps', ',')"/>
		<xsl:value-of select="mn:attribute(., 'position', ',')"/>
		<xsl:value-of select="mn:childString(., 'title', ',')"/>
		<xsl:value-of select="mn:attribute(., 'width', ',')"/>
	</xsl:template>
	
	<xsl:template match="axis[@xsi:type='CategoryAxis']">
		<xsl:call-template name="Axis"/>
		<xsl:value-of select="mn:attribute(., 'calculateCategoryAxis', ',')"/>
		<xsl:value-of select="mn:attribute(., 'categoryNames', ',')"/>
		"type": "category"
	</xsl:template>
	
	<xsl:template name="NumericAxis">
		<xsl:call-template name="Axis"/>
		<xsl:value-of select="mn:attribute(., 'adjustMaximumByMajorUnit', ',')"/>
		<xsl:value-of select="mn:attribute(., 'adjustMinimumByMajorUnit', ',')"/>
		<xsl:value-of select="mn:attribute(., 'decimals', ',')"/>
		<xsl:value-of select="mn:attribute(., 'maximum', ',')"/>
		<xsl:value-of select="mn:attribute(., 'minimum', ',')"/>
	</xsl:template>
	
	<xsl:template match="axis[@xsi:type='NumericAxis']">
		<xsl:call-template name="NumericAxis"/>
		"type": "numeric"
	</xsl:template>
	
	<xsl:template match="axis[@xsi:type='TimeAxis']">
		<xsl:call-template name="NumericAxis"/>
		<xsl:value-of select="mn:attribute(., 'constrain', ',')"/>
		<xsl:value-of select="mn:attribute(., 'dateFormat', ',')"/>
		<xsl:value-of select="mn:attribute(., 'fromDate', ',')"/>
		<xsl:value-of select="mn:eval-attribute(., 'step', ',')"/>
		<xsl:value-of select="mn:attribute(., 'toDate', ',')"/>
		"type": "time"
	</xsl:template>
	
	<xsl:template match="axis[@xsi:type='GaugeAxis']">
		<xsl:call-template name="AbstractAxis"/>
		<xsl:value-of select="mn:attribute(., 'margin', ',')"/>
		<xsl:value-of select="mn:attribute(., 'maximum', ',')"/>
		<xsl:value-of select="mn:attribute(., 'minimum', ',')"/>
		<xsl:value-of select="mn:attribute(., 'steps', ',')"/>
		<xsl:value-of select="mn:childString(., 'title', ',')"/>
		"type": "gauge"
	</xsl:template>
	
	<xsl:template match="axis[@xsi:type='RadialAxis']">
		<xsl:call-template name="AbstractAxis"/>
		<xsl:value-of select="mn:attribute(., 'maximum', ',')"/>
		<xsl:value-of select="mn:attribute(., 'steps', ',')"/>
		"type": "radial"
	</xsl:template>
	
</xsl:stylesheet>