<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="UTF-8"/>
	<!-- simply copy the message to the result tree -->
	<xsl:template match="viewMetaData/viewport">
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
			</head>
			<body>
				doing viewport
			</body>
		</html>
	</xsl:template>
	
	<xsl:template match="viewMetaData/items">
		doing items
	</xsl:template>
</xsl:stylesheet>
