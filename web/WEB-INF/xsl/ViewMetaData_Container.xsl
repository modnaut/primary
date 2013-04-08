<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:mn="http://www.modnaut.com" xmlns:json="http://json.org/">
	<xsl:output indent="no" omit-xml-declaration="yes" method="text" encoding="utf-8"/>

	<xsl:template match="item[@xsi:type='Container']">
		<xsl:call-template name="Container"/>
		"xtype": "container"
	</xsl:template>

	<xsl:template name="Container">
		<xsl:call-template name="Layout"/>
		<xsl:choose>
			<xsl:when test="string(@autoScroll) = '' ">
				<xsl:if test="not(layout[@xsi:type='BorderLayout'])">
					"autoScroll": true,
				</xsl:if>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="mn:attribute(., 'autoScroll', ',')"/>
			</xsl:otherwise>
		</xsl:choose>
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
		<xsl:value-of select="mn:attribute(., 'resizable', ',')"/>
		<xsl:value-of select="mn:attribute(., 'shrinkWrap', ',')"/>
		<xsl:apply-templates select="items"/>
	</xsl:template>
	
	<xsl:template name="Tools">
		<xsl:if test="tool">
			"tools": [
				<xsl:for-each select="tool">
					{
						<xsl:call-template name="Item"/>
						<xsl:value-of select="mn:attribute(., 'stopEvent', ',')"/>
						<xsl:value-of select="mn:childString(., 'tooltip', ',')"/>
						<xsl:value-of select="mn:attribute(., 'type', '')"/>
					}
					<xsl:call-template name="comma-delimit"/>
				</xsl:for-each>
			],
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="Toolbars">
		<xsl:for-each select="toolbar">
			<xsl:choose>
				<xsl:when test="@position = 'bottom' ">	
					<xsl:text>"bbar"</xsl:text>
				</xsl:when>
				<xsl:when test="@position = 'left' ">	
					<xsl:text>"lbar"</xsl:text>
				</xsl:when>
				<xsl:when test="@position = 'right' ">	
					<xsl:text>"rbar"</xsl:text>
				</xsl:when>
				<xsl:when test="@position = 'top' ">	
					<xsl:text>"tbar"</xsl:text>
				</xsl:when>
			</xsl:choose>
			: {
				<xsl:call-template name="Toolbar"/>
			},
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template name="Toolbar">
		<xsl:call-template name="Container"/>
		<xsl:value-of select="mn:attribute(., 'enableOverflow', ',')"/>
		<xsl:value-of select="mn:attribute(., 'vertical', ',')"/>
		"xtype": "toolbar"
	</xsl:template>
	
	<xsl:template match="item[@xsi:type = 'Toolbar']">
		<xsl:call-template name="Toolbar"/>
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
		<xsl:call-template name="Notifications"/>
		<xsl:call-template name="Tools"/>
		<xsl:call-template name="Toolbars"/>
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='Panel']">
		<xsl:call-template name="Panel"/>
		"xtype": "panel"
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
		"xtype": "tabpanel"
	</xsl:template>
	
	
	<xsl:template match="item[@xsi:type='CheckboxGroup']">
		<xsl:call-template name="Container"/>
		<xsl:call-template name="Labelable"/>
		<xsl:value-of select="mn:attribute(., 'allowBlank', ',')"/>
		<xsl:value-of select="mn:childString(., 'blankText', ',')"/>
		<xsl:value-of select="mn:eval-attribute(., 'columns', ',')"/>
		<xsl:value-of select="mn:attribute(., 'disabled', ',')"/>
		<xsl:value-of select="mn:attribute(., 'submitValue', ',')"/>
		<xsl:value-of select="mn:attribute(., 'validateOnChange', ',')"/>
		"xtype": "checkboxgroup"
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='RadioGroup']">
		<xsl:call-template name="Container"/>
		<xsl:call-template name="Labelable"/>
		<xsl:value-of select="mn:attribute(., 'allowBlank', ',')"/>
		<xsl:value-of select="mn:childString(., 'blankText', ',')"/>
		<xsl:value-of select="mn:eval-attribute(., 'columns', ',')"/>
		<xsl:value-of select="mn:attribute(., 'disabled', ',')"/>
		<xsl:value-of select="mn:attribute(., 'submitValue', ',')"/>
		<xsl:value-of select="mn:attribute(., 'validateOnChange', ',')"/>
		"xtype": "radiogroup"
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='ToolbarFill']">
		"xtype": "tbfill"
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='ToolbarSeparator']">
		"xtype": "tbseparator"
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='ToolbarSpacer']">
		"xtype": "tbspacer"
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='ToolbarText']">
		<xsl:value-of select="mn:childString(., 'text', ',')"/>
		"xtype": "tbtext"
	</xsl:template>
	
	<xsl:template name="Menu">
		<xsl:for-each select="menu">
			"menu": {
				<xsl:call-template name="Item"/>
				<xsl:call-template name="Panel"/>
				<xsl:value-of select="mn:attribute(., 'allowOtherMenus', ',')"/>
				<xsl:value-of select="mn:attribute(., 'enableKeyNav', ',')"/>
				<xsl:value-of select="mn:attribute(., 'ignoreParentClicks', ',')"/>
				<xsl:value-of select="mn:attribute(., 'plain', ',')"/>
				<xsl:value-of select="mn:attribute(., 'showSeparator', ',')"/>
				"_d": 0
			},
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template name="MenuItem">
		<xsl:value-of select="mn:attribute(., 'canActivate', ',')"/>
		<xsl:value-of select="mn:attribute(., 'clickHideDelay', ',')"/>
		<xsl:value-of select="mn:attribute(., 'destroyMenu', ',')"/>
		<xsl:value-of select="mn:attribute(., 'hideOnClick', ',')"/>
		<xsl:value-of select="mn:attribute(., 'href', ',')"/>
		<xsl:value-of select="mn:attribute(., 'hrefTarget', ',')"/>
		<xsl:value-of select="mn:imageSpec(icon, 'icon', ',')"/>
		<xsl:value-of select="mn:attribute(., 'iconCls', ',')"/>
		<xsl:call-template name="Menu"/>
		<xsl:value-of select="mn:attribute(., 'menuAlign', ',')"/>
		<xsl:value-of select="mn:attribute(., 'menuExpandDelay', ',')"/>
		<xsl:value-of select="mn:attribute(., 'menuHideDelay', ',')"/>
		<xsl:value-of select="mn:attribute(., 'plain', ',')"/>
		<xsl:value-of select="mn:childString(., 'text', ',')"/>
		<xsl:value-of select="mn:childString(., 'tooltip', ',')"/>
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='MenuItem']">
		<xsl:call-template name="MenuItem"/>
		"xtype": "menuitem"
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='MenuCheckItem']">
		<xsl:call-template name="MenuItem"/>
		<xsl:value-of select="mn:attribute(., 'checkChangeDisabled', ',')"/>
		<xsl:value-of select="mn:attribute(., 'checked', ',')"/>
		<xsl:value-of select="mn:attribute(., 'group', ',')"/>
		"xtype": "menucheckitem"
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='MenuSeparatorItem']">
		<xsl:call-template name="MenuItem"/>
		"xtype": "menuseparator"
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='DataView']">
		<xsl:for-each select="store">
			<xsl:apply-templates select="."/>,
		</xsl:for-each>
		<xsl:value-of select="mn:attribute(., 'itemSelector', ',')"/>
		"tpl": "<xsl:for-each select="tpl/*"><xsl:value-of select="mn:stringify-element(.)"/></xsl:for-each>",
		"xtype": "dataview"
	</xsl:template>
</xsl:stylesheet>
