<?xml version="1.0" encoding="UTF-8"?><xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  
  	<xsl:output method="html" encoding="UTF-8"/> 
 	<!-- simply copy the message to the result tree -->  
  
	<xsl:template match="viewMetaData">
	  <html>
		<head>
			<!-- <meta http-equiv="Content-Type" content="text/html;charset=utf-8" /> -->
		</head>
		<body>
			<div id="container">
				<xsl:apply-templates/>
			</div>
		</body>
	  </html>
	</xsl:template>
	  
	  
	<xsl:template match="title">
		<h2>
			<xsl:value-of select="@value"/>
		</h2> <br/>
	</xsl:template>
	
	
	<xsl:template match="message">
		<label>
			<xsl:value-of select="@value"/>
		</label>
	</xsl:template> 
    
</xsl:stylesheet> 