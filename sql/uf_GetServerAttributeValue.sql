USE common
;
DROP FUNCTION IF EXISTS uf_GetServerAttributeValue
;

CREATE FUNCTION uf_GetServerAttributeValue (
	p_ServerId INT
	,p_AttributeId INT
) RETURNS VARCHAR(20000)
BEGIN
	DECLARE v_EntityTypeId INT;
	
	SELECT
		EntityTypeId INTO v_EntityTypeId
	FROM
		Common.EntityType
	WHERE
		Description = 'Server';
		
	RETURN common.uf_GetEntityAttributeValue(v_EntityTypeId, p_ServerId, p_AttributeId);
END 
;