USE common
;
DROP FUNCTION IF EXISTS uf_GetAttributeIdByName
;

CREATE FUNCTION uf_GetAttributeIdByName (
	p_AttributeName VARCHAR(255)
) RETURNS INT
BEGIN
	DECLARE AttributeId INT;
	
	SELECT
		a.AttributeId
	INTO
		AttributeId
	FROM
		Common.Attribute a
	WHERE
		a.AttributeName = p_AttributeName;
		
	RETURN AttributeId;

END 
;