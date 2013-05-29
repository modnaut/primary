USE common
;
DROP FUNCTION IF EXISTS uf_GetEntityAttributeValue
;

CREATE FUNCTION uf_GetEntityAttributeValue (
	p_EntityTypeId INT
	,p_EntityId INT
	,p_AttributeId INT
) RETURNS VARCHAR(20000)
BEGIN
	DECLARE AttributeValue VARCHAR(20000);
	
	SELECT av.AttributeValue INTO AttributeValue
	FROM
		Common.EntityAttributeValue av
	WHERE
		av.EntityTypeId = p_EntityTypeId
		AND
		av.EntityId = p_EntityId
		AND
		av.AttributeId = p_AttributeId;
		
	RETURN AttributeValue;

END 
;