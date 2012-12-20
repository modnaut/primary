<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="UTF-8"/>
	<!-- simply copy the message to the result tree -->
	<xsl:template match="/">
		<html>
			<head>
				<!-- <meta http-equiv="Content-Type" content="text/html;charset=utf-8" /> -->
			</head>
			<body>
				<xsl:apply-templates/>
			</body>
		</html>
	</xsl:template>
	<xsl:template match="viewMetaData">
		<xsl:apply-templates select="."/>
	</xsl:template>
	<xsl:template match="viewport">
		<xsl:for-each select="item">
			<xsl:for-each select="text">
				<xsl:value-of select="@stringId"/>
			</xsl:for-each>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
