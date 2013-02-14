<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:mn="http://www.modnaut.com" xmlns:json="http://json.org/">
	<xsl:output indent="no" omit-xml-declaration="yes" method="text" encoding="utf-8"/>
	
	<xsl:template match="item[@xsi:type='Button']">
		<xsl:value-of select="mn:attribute(., 'allowDepress', ',')"/>
		<xsl:value-of select="mn:attribute(., 'arrowAlign', ',')"/>
		<xsl:value-of select="mn:attribute(., 'border', ',')"/>
		<xsl:value-of select="mn:attribute(., 'disabled', ',')"/>
		<xsl:value-of select="mn:attribute(., 'enableToggle', ',')"/>
		<xsl:value-of select="mn:attribute(., 'handleMouseEvents', ',')"/>
		<xsl:value-of select="mn:imageSpec(icon, 'icon', ',')"/>
		<xsl:value-of select="mn:attribute(., 'iconAlign', ',')"/>
		<xsl:value-of select="mn:attribute(., 'iconCls', ',')"/>
		<xsl:value-of select="mn:attribute(., 'menuAlign', ',')"/>
		<xsl:value-of select="mn:attribute(., 'overflowText', ',')"/>
		<xsl:value-of select="mn:attribute(., 'pressed', ',')"/>
		<xsl:value-of select="mn:attribute(., 'scale', ',')"/>
		<xsl:value-of select="mn:attribute(., 'shrinkWrap', ',')"/>
		<xsl:value-of select="mn:attribute(., 'tabIndex', ',')"/>
		<xsl:value-of select="mn:childString(., 'text', ',')"/>
		<xsl:value-of select="mn:attribute(., 'textAlign', ',')"/>
		<xsl:value-of select="mn:attribute(., 'toggleGroup', ',')"/>
		<xsl:value-of select="mn:childString(., 'tooltip', ',')"/>
		xtype: "button"
	</xsl:template>
	
	<xsl:template name="Labelable">
		<xsl:value-of select="mn:childString(., 'activeError', ',')"/>
		<xsl:value-of select="mn:attribute(., 'autoFitErrors', ',')"/>
		<xsl:value-of select="mn:childString(., 'fieldLabel', ',')"/>
		<xsl:value-of select="mn:attribute(., 'hideEmptyLabel', ',')"/>
		<xsl:value-of select="mn:attribute(., 'hideLabel', ',')"/>
		<xsl:value-of select="mn:attribute(., 'labelAlign', ',')"/>
		<xsl:value-of select="mn:attribute(., 'labelPad', ',')"/>
		<xsl:value-of select="mn:attribute(., 'labelSeparator', ',')"/>
		<xsl:value-of select="mn:attribute(., 'labelWidth', ',')"/>
		<xsl:value-of select="mn:attribute(., 'msgTarget', ',')"/>
		<xsl:value-of select="mn:attribute(., 'preventMark', ',')"/>
	</xsl:template>
	
	<xsl:template name="Field">
		<xsl:value-of select="mn:attribute(., 'disabled', ',')"/>
				<xsl:if test="@id != '' and @radioGroupName = ''">
			name: <xsl:value-of select="mn:wrap-string(@id)"/>,
		</xsl:if>
		<xsl:value-of select="mn:attribute(., 'submitValue', ',')"/>
		<xsl:value-of select="mn:attribute(., 'validateOnChange', ',')"/>
		<xsl:if test="@xsi:type != 'ComboBox' "><!--ComboBoxes must be dealt with specifically-->
			<xsl:value-of select="mn:attribute(., 'value', ',')"/>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="AbstractField">
		<xsl:call-template name="Field"/>
		<xsl:call-template name="Labelable"/>
		<xsl:value-of select="mn:childString(., 'invalidText', ',')"/>
		<xsl:value-of select="mn:attribute(., 'readOnly', ',')"/>
		<xsl:value-of select="mn:attribute(., 'tabIndex', ',')"/>
		<xsl:value-of select="mn:attribute(., 'validateOnBlur', ',')"/>
	</xsl:template>
	
	<xsl:template name="Checkbox">
		<xsl:call-template name="AbstractField"/>
		<xsl:value-of select="mn:childString(., 'boxLabel', ',')"/>
		<xsl:value-of select="mn:attribute(., 'boxLabelAlign', ',')"/>
		<xsl:value-of select="mn:attribute(., 'checked', ',')"/>
		<xsl:value-of select="mn:attribute(., 'inputValue', ',')"/>
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='Checkbox']">
		<xsl:call-template name="Checkbox"/>
		xtype: "checkbox"
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='Radio']">
		<xsl:call-template name="Checkbox"/>
		<xsl:if test="@radioGroupName != ''">
			name: <xsl:value-of select="mn:wrap-string(@radioGroupName)"/>,
		</xsl:if>
		xtype: "radio"
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='DisplayField']">
		<xsl:call-template name="AbstractField"/>
		xtype: "displayfield"
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='HiddenField']">
		<xsl:call-template name="AbstractField"/>
		xtype: "hidden"
	</xsl:template>
	
	<xsl:template name="TextField">
		<xsl:call-template name="AbstractField"/>
		<xsl:value-of select="mn:attribute(., 'allowBlank', ',')"/>
		<xsl:value-of select="mn:attribute(., 'allowOnlyWhitespace', ',')"/>
		<xsl:value-of select="mn:childString(., 'blankText', ',')"/>
		<xsl:value-of select="mn:attribute(., 'disableKeyFilter', ',')"/>
		<xsl:value-of select="mn:childString(., 'emptyText', ',')"/>		
		<xsl:value-of select="mn:attribute(., 'enforceMaxLength', ',')"/>
		<xsl:value-of select="mn:attribute(., 'grow', ',')"/>
		<xsl:value-of select="mn:attribute(., 'growMax', ',')"/>
		<xsl:value-of select="mn:attribute(., 'growMin', ',')"/>
		<xsl:value-of select="mn:attribute(., 'maxLength', ',')"/>
		<xsl:value-of select="mn:childString(., 'maxLengthText', ',')"/>
		<xsl:value-of select="mn:attribute(., 'minLength', ',')"/>
		<xsl:value-of select="mn:childString(., 'minLengthText', ',')"/>
		<xsl:value-of select="mn:attribute(., 'regex', ',')"/>
		<xsl:value-of select="mn:childString(., 'regexText', ',')"/>
		<xsl:value-of select="mn:attribute(., 'selectOnFocus', ',')"/>
		<xsl:value-of select="mn:attribute(., 'vtype', ',')"/>
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='TextField']">
		<xsl:call-template name="TextField"/>
		xtype: "textfield"
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='TextArea']">
		<xsl:call-template name="TextField"/>
		<xsl:value-of select="mn:attribute(., 'cols', ',')"/>
		<xsl:value-of select="mn:attribute(., 'enterIsSpecial', ',')"/>
		<xsl:value-of select="mn:attribute(., 'preventScrollbars', ',')"/>
		<xsl:value-of select="mn:attribute(., 'rows', ',')"/>
		xtype: "textarea"
	</xsl:template>
	
	<xsl:template name="TriggerField">
		<xsl:call-template name="TextField"/>
		<xsl:value-of select="mn:attribute(., 'editable', ',')"/>
		<xsl:value-of select="mn:attribute(., 'hideTrigger', ',')"/>
		<xsl:value-of select="mn:attribute(., 'selectOnFocus', ',')"/>
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='FileField']">
		<xsl:call-template name="TriggerField"/>
		<xsl:value-of select="mn:attribute(., 'buttonMargin', ',')"/>
		<xsl:value-of select="mn:attribute(., 'buttonOnly', ',')"/>
		<xsl:value-of select="mn:childString(., 'buttonText', ',')"/>		
		xtype: "filefield"
	</xsl:template>
	
	<xsl:template name="PickerField">
		<xsl:call-template name="TriggerField"/>
		<xsl:value-of select="mn:attribute(., 'matchFieldWidth', ',')"/>
		<xsl:value-of select="mn:attribute(., 'pickerAlign', ',')"/>
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='ComboBox']">
		<xsl:call-template name="PickerField"/>
		<xsl:value-of select="mn:attribute(., 'autoSelect', ',')"/>
		<xsl:value-of select="mn:attribute(., 'displayField', ',')"/>
		<xsl:choose>
			<xsl:when test="@forceSelection = '' ">
				forceSelection: true,
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="mn:attribute(., 'forceSelection', ',')"/>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:value-of select="mn:attribute(., 'growToLongestValue', ',')"/>
		<xsl:value-of select="mn:attribute(., 'minChars', ',')"/>
		<xsl:value-of select="mn:attribute(., 'multiSelect', ',')"/>
		<xsl:value-of select="mn:attribute(., 'pageSize', ',')"/>
		<xsl:value-of select="mn:attribute(., 'queryCaching', ',')"/>
		<xsl:value-of select="mn:attribute(., 'queryDelay', ',')"/>
		<xsl:value-of select="mn:attribute(., 'queryMode', ',')"/>
		<xsl:value-of select="mn:attribute(., 'queryParam', ',')"/>
		<xsl:value-of select="mn:attribute(., 'selectOnTab', ',')"/>
		<xsl:value-of select="mn:attribute(., 'triggerAction', ',')"/>
		<xsl:value-of select="mn:attribute(., 'typeAhead', ',')"/>
		<xsl:value-of select="mn:attribute(., 'typeAheadDelay', ',')"/>
		<xsl:value-of select="mn:attribute(., 'valueField', ',')"/>
		<xsl:variable name="valueField">
			<xsl:choose>
				<xsl:when test="@valueField != ''">
					<xsl:value-of select="@valueField"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="'text'"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="records/record[@selected='true']">
				value: [
					<xsl:for-each select="records/record[@selected='true']">
						<xsl:value-of select="mn:wrap-string(field[@name=$valueField]/@value)"/>
						<xsl:call-template name="comma-delimit"/>
					</xsl:for-each>
				],
			</xsl:when>
			<xsl:when test="store/data/record[@selected='true']">
				value: [
					<xsl:for-each select="store/data/record[@selected='true']">
						<xsl:value-of select="mn:wrap-string(field[@name=$valueField]/@value)"/>
						<xsl:call-template name="comma-delimit"/>
					</xsl:for-each>
				],
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="mn:attribute(., 'value', ',')"/>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:value-of select="mn:childString(., 'valueNotFoundText', ',')"/>
		<xsl:choose>
			<xsl:when test="records">
				store: {
					autoDestroy: true,
					fields: [
						<xsl:choose>
							<xsl:when test="@displayField">
								"<xsl:value-of select="@displayField"/>"
							</xsl:when>
							<xsl:otherwise>
								"text"
							</xsl:otherwise>
						</xsl:choose>
						<xsl:text>,</xsl:text>
						<xsl:choose>
							<xsl:when test="@valueField">
								"<xsl:value-of select="@valueField"/>"
							</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when test="@displayField">
										"<xsl:value-of select="@displayField"/>"
									</xsl:when>
									<xsl:otherwise>
										"text"
									</xsl:otherwise>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					],
					data: [
						<xsl:for-each select="records/record">
							{
								<xsl:for-each select="field">
									"<xsl:value-of select="@name"/>": <xsl:value-of select="mn:wrap-string(@value)"/>
									<xsl:call-template name="comma-delimit"/>
								</xsl:for-each>
							}<xsl:call-template name="comma-delimit"/>
						</xsl:for-each>
					]
				},
			</xsl:when>
			<xsl:when test="store">
				<xsl:for-each select="store">
					<xsl:apply-templates select="."/>,
				</xsl:for-each>
			</xsl:when>
		</xsl:choose>

		xtype: "combobox"
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='TimeField']">
		<xsl:call-template name="PickerField"/>
		<xsl:value-of select="mn:attribute(., 'format', ',')"/>
		<xsl:value-of select="mn:attribute(., 'increment', ',')"/>
		<xsl:value-of select="mn:childString(., 'maxText', ',')"/>
		<xsl:value-of select="mn:attribute(., 'maxValue', ',')"/>
		<xsl:value-of select="mn:childString(., 'minText', ',')"/>
		<xsl:value-of select="mn:attribute(., 'minValue', ',')"/>
		<xsl:value-of select="mn:attribute(., 'pickerMaxHeight', ',')"/>
		<xsl:value-of select="mn:attribute(., 'selectOnTab', ',')"/>
		<xsl:value-of select="mn:attribute(., 'snapToIncrement', ',')"/>
		<xsl:value-of select="mn:attribute(., 'submitFormat', ',')"/>
		xtype: "timefield"
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='DateField']">
		<xsl:call-template name="PickerField"/>
		<xsl:value-of select="mn:eval-attribute(., 'disabledDates', ',')"/>
		<xsl:value-of select="mn:childString(., 'disabledDatesText', ',')"/>
		<xsl:value-of select="mn:eval-attribute(., 'disabledDays', ',')"/>
		<xsl:value-of select="mn:childString(., 'disabledDayText', ',')"/>
		<xsl:value-of select="mn:attribute(., 'format', ',')"/>
		<xsl:value-of select="mn:childString(., 'maxText', ',')"/>
		<xsl:value-of select="mn:attribute(., 'maxValue', ',')"/>
		<xsl:value-of select="mn:childString(., 'minText', ',')"/>
		<xsl:value-of select="mn:attribute(., 'minValue', ',')"/>
		<xsl:value-of select="mn:attribute(., 'showToday', ',')"/>
		<xsl:value-of select="mn:attribute(., 'startDay', ',')"/>
		<xsl:value-of select="mn:attribute(., 'submitFormat', ',')"/>
		<xsl:value-of select="mn:attribute(., 'useStrict', ',')"/>
		xtype: "datefield"
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='NumberField']">
		<xsl:call-template name="TriggerField"/>
		<xsl:value-of select="mn:attribute(., 'allowDecimals', ',')"/>
		<xsl:value-of select="mn:attribute(., 'autoStripChars', ',')"/>
		<xsl:value-of select="mn:attribute(., 'decimalPrecison', ',')"/>
		<xsl:value-of select="mn:attribute(., 'decimalSeparator', ',')"/>
		<xsl:value-of select="mn:childString(., 'maxText', ',')"/>
		<xsl:value-of select="mn:attribute(., 'maxValue', ',')"/>
		<xsl:value-of select="mn:childString(., 'minText', ',')"/>
		<xsl:value-of select="mn:attribute(., 'minValue', ',')"/>
		<xsl:value-of select="mn:childString(., 'nanText', ',')"/>
		<xsl:value-of select="mn:childString(., 'negativeText', ',')"/>
		<xsl:value-of select="mn:attribute(., 'step', ',')"/>
		<xsl:value-of select="mn:attribute(., 'submitLocaleSeparator', ',')"/>
		xtype: "numberfield"
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='Slider']">
		<xsl:call-template name="AbstractField"/>
		<xsl:value-of select="mn:attribute(., 'animate', ',')"/>
		<xsl:value-of select="mn:attribute(., 'clickToChange', ',')"/>
		<xsl:value-of select="mn:attribute(., 'constrainThumbs', ',')"/>
		<xsl:value-of select="mn:attribute(., 'decimalPrecision', ',')"/>
		<xsl:value-of select="mn:attribute(., 'disableRounding', ',')"/>
		<xsl:value-of select="mn:attribute(., 'increment', ',')"/>
		<xsl:value-of select="mn:attribute(., 'keyIncrement', ',')"/>
		<xsl:value-of select="mn:attribute(., 'maxValue', ',')"/>
		<xsl:value-of select="mn:attribute(., 'minValue', ',')"/>
		<xsl:value-of select="mn:attribute(., 'useTips', ',')"/>
		<xsl:value-of select="mn:attribute(., 'vertical', ',')"/>
		<xsl:value-of select="mn:attribute(., 'zeroBasedSnapping', ',')"/>
		xtype: "slider"
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='HTMLEditor']">
		<xsl:call-template name="Field"/>
		<xsl:call-template name="Labelable"/>
		<xsl:value-of select="mn:attribute(., 'enableAlignments', ',')"/>
		<xsl:value-of select="mn:attribute(., 'enableColors', ',')"/>
		<xsl:value-of select="mn:attribute(., 'enableFonts', ',')"/>
		<xsl:value-of select="mn:attribute(., 'enableFontSize', ',')"/>
		<xsl:value-of select="mn:attribute(., 'enableFormat', ',')"/>
		<xsl:value-of select="mn:attribute(., 'enableLinks', ',')"/>
		<xsl:value-of select="mn:attribute(., 'enableLists', ',')"/>
		<xsl:value-of select="mn:attribute(., 'enableSourceEdit', ',')"/>
		xtype: "htmleditor"
	</xsl:template>
	
	<xsl:template match="item[@xsi:type='Image']">
		<xsl:value-of select="mn:childString(., 'altText', ',')"/>
		<xsl:value-of select="mn:imageSpec(image, 'image', ',')"/>
	</xsl:template>
</xsl:stylesheet>
