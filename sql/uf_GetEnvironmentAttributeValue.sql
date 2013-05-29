USE common
;
DROP FUNCTION IF EXISTS uf_GetEnvironmentAttributeValue
;

CREATE FUNCTION uf_GetEnvironmentAttributeValue (
	p_EnvironmentId INT
	,p_AttributeId INT
) RETURNS VARCHAR(20000)
BEGIN
	DECLARE v_EntityTypeId INT;
	
	SELECT
		EntityTypeId INTO v_EntityTypeId
	FROM
		Common.EntityType
	WHERE
		Description = 'Environment';
		
	RETURN common.uf_GetEntityAttributeValue(v_EntityTypeId, p_EnvironmentId, p_AttributeId);
END 
;