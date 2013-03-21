<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:mn="http://www.modnaut.com" xmlns:json="http://json.org/">
	<xsl:output indent="no" omit-xml-declaration="yes" method="text" encoding="utf-8"/>
	
	
	<xsl:template match="item[@xsi:type='Chart']">
		<xsl:call-template name="Axes"/>
		<xsl:call-template name="ChartBackground"/>
		<xsl:call-template name="ChartLegend"/>
		<xsl:call-template name="Series"/>
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
					<xsl:apply-templates select="." mode="inlineChart"/>,
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
	
	<xsl:template name="ChartBackground">
		<xsl:for-each select="background">
			"background": {
				<xsl:choose>
					<xsl:when test="@fill != '' ">
						<xsl:value-of select="mn:attribute(., 'fill', '')"/>
					</xsl:when>
					<xsl:when test="image">
						<xsl:value-of select="mn:imageSpec(image, 'image', '')"/>
					</xsl:when>
					<xsl:when test="gradient">
						gradient: {
							<xsl:value-of select="mn:attribute(., 'id', ',')"/>
							<xsl:value-of select="mn:attribute(., 'angle', ',')"/>
							"stops": {
								<xsl:for-each select="stop">
									<xsl:value-of select="mn:wrap-string(@key)"/>:
									{
											<xsl:value-of select="mn:attribute(., 'color', '')"/>
									}<xsl:call-template name="comma-delimit"/>
								</xsl:for-each>
							}
						}
					</xsl:when>
				</xsl:choose>
			},
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template name="ChartLegend">
		<xsl:for-each select="legend">
			"legend": {
				<xsl:value-of select="mn:attribute(., 'boxFill', ',')"/>
				<xsl:value-of select="mn:attribute(., 'boxStroke', ',')"/>
				<xsl:value-of select="mn:attribute(., 'boxStrokeWidth', ',')"/>
				<xsl:value-of select="mn:attribute(., 'boxZIndex', ',')"/>
				<xsl:value-of select="mn:attribute(., 'itemSpacing', ',')"/>
				<xsl:value-of select="mn:attribute(., 'labelColor', ',')"/>
				<xsl:value-of select="mn:attribute(., 'labelFont', ',')"/>
				<xsl:value-of select="mn:attribute(., 'padding', ',')"/>
				<xsl:value-of select="mn:attribute(., 'position', ',')"/>
				<xsl:value-of select="mn:attribute(., 'update', ',')"/>
				<xsl:value-of select="mn:attribute(., 'visible', ',')"/>
				<xsl:value-of select="mn:attribute(., 'x', ',')"/>
				<xsl:value-of select="mn:attribute(., 'y', '')"/>
			},
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template name="Series">
		"series": [
			<xsl:for-each select="series">
				{
					<xsl:apply-templates select="."/>
				}<xsl:call-template name="comma-delimit"/>
			</xsl:for-each>
		],
	</xsl:template>
	
	<xsl:template name="AbstractSeries">	
		<xsl:value-of select="mn:attribute(., 'highlight', ',')"/>
		<xsl:call-template name="SeriesLabel"/>
		<xsl:value-of select="mn:attribute(., 'showInLegend', ',')"/>
		<xsl:value-of select="mn:childString(., 'title', ',')"/>
	</xsl:template>
	
	<xsl:template name="SeriesLabel">
		<xsl:for-each select="label">
			"label": {
				<xsl:value-of select="mn:attribute(., 'color', ',')"/>
				<xsl:value-of select="mn:attribute(., 'constrast', ',')"/>
				<xsl:value-of select="mn:attribute(., 'display', ',')"/>
				<xsl:if test="field">
					field: [
						<xsl:for-each select="field">
							<xsl:value-of select="mn:wrap-string(@name)"/>
							<xsl:call-template name="comma-delimit"/>
						</xsl:for-each>
					],
				</xsl:if>
				<xsl:value-of select="mn:attribute(., 'minMargin', ',')"/>
				<xsl:value-of select="mn:attribute(., 'font', ',')"/>
				<xsl:value-of select="mn:attribute(., 'orientation', '')"/>
				_d: null
			},
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template name="CartesianSeries">
		<xsl:call-template name="AbstractSeries"/>
		<xsl:value-of select="mn:attribute(., 'axis', ',')"/>
		<xsl:if test="xField">
			"xField": [
				<xsl:for-each select="xField">
					<xsl:value-of select="mn:wrap-string(@name)"/>
					<xsl:call-template name="comma-delimit"/>
				</xsl:for-each>
			],
		</xsl:if>
		<xsl:if test="yField">
			"yField": [
				<xsl:for-each select="yField">
					<xsl:value-of select="mn:wrap-string(@name)"/>
					<xsl:call-template name="comma-delimit"/>
				</xsl:for-each>
			],
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="series[@xsi:type='AreaSeries']">
		<xsl:call-template name="CartesianSeries"/>
		"type": "area"
	</xsl:template>
	
	<xsl:template match="series[@xsi:type='BarSeries']">
		<xsl:call-template name="CartesianSeries"/>
		<xsl:value-of select="mn:attribute(., 'column', ',')"/>
		<xsl:value-of select="mn:attribute(., 'groupGutter', ',')"/>
		<xsl:value-of select="mn:attribute(., 'gutter', ',')"/>
		<xsl:value-of select="mn:attribute(., 'stacked', ',')"/>
		<xsl:value-of select="mn:attribute(., 'xPadding', ',')"/>
		<xsl:value-of select="mn:attribute(., 'yPadding', ',')"/>
		"type": "bar"
	</xsl:template>
	
	<xsl:template match="series[@xsi:type='LineSeries']">
		<xsl:call-template name="CartesianSeries"/>
		<xsl:value-of select="mn:attribute(., 'fill', ',')"/>
		<xsl:value-of select="mn:attribute(., 'selectionTolerance', ',')"/>
		<xsl:value-of select="mn:attribute(., 'showMarkers', ',')"/>
		<xsl:value-of select="mn:attribute(., 'smooth', ',')"/>
		"type": "line"
	</xsl:template>
	
	<xsl:template match="series[@xsi:type='ScatterSeries']">
		<xsl:call-template name="CartesianSeries"/>
		"type": "scatter"
	</xsl:template>
	
	<xsl:template match="series[@xsi:type='GaugeSeries']">
		<xsl:call-template name="AbstractSeries"/>
		<xsl:if test="angleField">
			"angleField": <xsl:value-of select="mn:wrap-string(angleField/@name)"/>,
		</xsl:if>
		<xsl:value-of select="mn:attribute(., 'donut', ',')"/>
		<xsl:value-of select="mn:attribute(., 'highlightDuration', ',')"/>
		<xsl:value-of select="mn:attribute(., 'needle', ',')"/>
		"type": "gauge"
	</xsl:template>
	
	<xsl:template match="series[@xsi:type='PieSeries']">
		<xsl:call-template name="AbstractSeries"/>
		<xsl:if test="angleField">
			"angleField": <xsl:value-of select="mn:wrap-string(angleField/@name)"/>,
		</xsl:if>
		<xsl:value-of select="mn:attribute(., 'donut', ',')"/>
		<xsl:value-of select="mn:attribute(., 'highlightDuration', ',')"/>
		<xsl:if test="lengthField">
			"lengthField": <xsl:value-of select="mn:wrap-string(lengthField/@name)"/>,
		</xsl:if>
		"type": "pie"
	</xsl:template>
	
	<xsl:template match="series[@xsi:type='RadarSeries']">
		<xsl:call-template name="AbstractSeries"/>
		<xsl:value-of select="mn:attribute(., 'highlightDuration', ',')"/>
		<xsl:value-of select="mn:attribute(., 'showMarkers', ',')"/>
		<xsl:if test="xField">
			"xField": <xsl:value-of select="mn:wrap-string(xField/@name)"/>,
		</xsl:if>
		<xsl:if test="yField">
			"yField": <xsl:value-of select="mn:wrap-string(yField/@name)"/>,
		</xsl:if>
		"type": "radar"
	</xsl:template>
</xsl:stylesheet>