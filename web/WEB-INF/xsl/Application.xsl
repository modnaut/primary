<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions">
	<xsl:param name="staticPath" select=" 'static/' "/>
	<xsl:param name="applicationId"/>
	<xsl:output doctype-system="html" doctype-public="-//W3C//DTD HTML 4.01//EN"/>
	<xsl:template match="application[@applicationId = $applicationId]">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html" charset="UTF-8"/>
				<title><xsl:value-of select="pageTitle/@stringCd"/></title>
				<link rel="stylesheet" type="text/css">
					<xsl:attribute name="href"><xsl:value-of select="$staticPath"/>extjs/resources/css/ext-all-neptune-debug.css</xsl:attribute>
				</link>
				<link rel="stylesheet" type="text/css">
					<xsl:attribute name="href"><xsl:value-of select="$staticPath"/>css/icon_silk.css</xsl:attribute>
				</link>
				<link rel="stylesheet" type="text/css">
					<xsl:attribute name="href"><xsl:value-of select="$staticPath"/>css/general.css</xsl:attribute>
				</link>
				<script type="text/javascript">
					<xsl:attribute name="src"><xsl:value-of select="$staticPath"/>extjs/bootstrap.js</xsl:attribute>
				</script>
				<script type="text/javascript">
					<xsl:attribute name="src"><xsl:value-of select="$staticPath"/>extjs/ext-theme-neptune.js</xsl:attribute>
				</script>
				<script type="text/javascript">
					<xsl:attribute name="src"><xsl:value-of select="$staticPath"/>general.js</xsl:attribute>
				</script>
				<script type="text/javascript">
					<xsl:attribute name="src"><xsl:value-of select="$staticPath"/><xsl:value-of select="applicationFolder/text()"/>/app.js</xsl:attribute>
				</script>
			</head>
			<body>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>