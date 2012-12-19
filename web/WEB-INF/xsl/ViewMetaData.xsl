<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="UTF-8"/>
	<!-- simply copy the message to the result tree -->
	<xsl:template match="viewMetaData">
		<html>
			<head>
				<!-- <meta http-equiv="Content-Type" content="text/html;charset=utf-8" /> -->
			</head>
			<body>
				danny was here
				<xsl:value-of select="viewport/item/text/@stringId"/>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
