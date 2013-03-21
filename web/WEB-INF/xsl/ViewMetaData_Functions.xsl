<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:mn="http://www.modnaut.com" xmlns:seu="java:org.apache.commons.lang3.StringEscapeUtils">
	<xsl:template name="comma-delimit">
		<xsl:if test="position() != last()">
			<xsl:text>,</xsl:text>
		</xsl:if>
	</xsl:template>

	<xsl:function name="mn:wrap-string" as="xs:string">
		<xsl:param name="string" as="xs:string?"/>
		<xsl:choose>
			<xsl:when test="normalize-space($string) ne $string or not((string($string) castable as xs:integer and not(starts-with(string($string),'0') and not($string = '0'))) or (string($string) castable as xs:decimal and not(starts-with($string,'-.')) and not(starts-with($string,'-0') and not(starts-with($string,'-0.'))) and not(ends-with($string,'.')) and not(starts-with($string,'0') and not(starts-with($string,'0.'))) )) and not($string = 'false') and not($string = 'true') and not($string = 'null')">
				<!--xsl:sequence select="concat(&apos;&quot;&apos;,seu:escapeEcmaScript($string),&apos;&quot;&apos;)"/>-->
				<xsl:sequence select="concat(&apos;&quot;&apos;,$string,&apos;&quot;&apos;)"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:sequence select="$string"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:function>
	
	<xsl:function name="mn:attribute">
		<xsl:param name="node" as="node()"/>
		<xsl:param name="attributeName" as="xs:string"/>
		<xsl:param name="extraString" as="xs:string"/>
		<xsl:choose>
			<xsl:when test="$node">
				<xsl:variable name="attributeValue" select="$node/@*[name() = $attributeName]"/>
				<xsl:choose>
					<xsl:when test="$attributeValue != '' ">
						<xsl:variable name="colon" select="':'"/>
						<xsl:sequence select="concat('&quot;',$attributeName,'&quot;',$colon,mn:wrap-string($attributeValue), $extraString)"/>
					</xsl:when>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:sequence select=" '' "/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:function>
	
	<xsl:function name="mn:eval-attribute">
		<xsl:param name="node" as="node()"/>
		<xsl:param name="attributeName" as="xs:string"/>
		<xsl:param name="extraString" as="xs:string"/>
		<xsl:choose>
			<xsl:when test="$node">
				<xsl:variable name="attributeValue" select="$node/@*[name() = $attributeName]"/>
				<xsl:choose>
					<xsl:when test="$attributeValue != '' ">
						<xsl:variable name="colon" select="':'"/>
						<xsl:sequence select="concat('&quot;',$attributeName,'&quot;',$colon,$attributeValue,$extraString)"/>
					</xsl:when>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:sequence select=" '' "/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:function>
	
	<xsl:function name="mn:childString">
		<xsl:param name="node" as="node()"/>
		<xsl:param name="elementName" as="xs:string"/>
		<xsl:param name="extraString" as="xs:string?"/>
		<xsl:choose>
			<xsl:when test="$node">
				<xsl:variable name="stringValue" select="$node/*[local-name() = $elementName]/@stringCd"/>
				<xsl:choose>
					<xsl:when test="$stringValue != '' ">
						<xsl:variable name="colon" select="':'"/>
						<xsl:sequence select="concat('&quot;',$elementName,'&quot;',$colon,mn:wrap-string($stringValue), $extraString)"/>
					</xsl:when>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:sequence select=" '' "/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:function>
	
	<xsl:function name="mn:imageSpec">
		<xsl:param name="iconNode" as="node()?"/>
		<xsl:param name="attributeName" as="xs:string?"/>
		<xsl:param name="extraString" as="xs:string?"/>
		<xsl:if test="$iconNode">
			<xsl:choose>
				<xsl:when test="not($iconNode/@external = 'true')">
					<xsl:sequence select="concat('&quot;',$attributeName,'&quot;', ':&quot;', $imagePath, $iconNode/@path, '&quot;', $extraString)"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:sequence select="concat('&quot;',$attributeName,'&quot;', ':&quot;', $iconNode/@path, '&quot;', $extraString)"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:function>
	
</xsl:stylesheet>
