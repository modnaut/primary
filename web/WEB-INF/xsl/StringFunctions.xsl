<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:mn="http://www.modnaut.com" xmlns:seu="java:org.apache.commons.lang3.StringEscapeUtils">
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
</xsl:stylesheet>
