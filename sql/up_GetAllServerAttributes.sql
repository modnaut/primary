USE common
;
DROP PROCEDURE IF EXISTS `up_GetAllServerAttributes`
;
CREATE PROCEDURE up_GetAllServerAttributes(
	IN p_ServerName VARCHAR(200)) 
BEGIN 

Select
	a.AttributeName
	,sav.AttributeValue
From
	Common.server s
	Inner Join Common.serverattributevalue sav on sav.ServerId = s.ServerId
	Inner Join Common.attribute a on a.AttributeId = sav.AttributeId
Where
	s.ServerName = p_ServerName;

END 
; 