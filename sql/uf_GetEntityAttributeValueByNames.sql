USE common
;
DROP FUNCTION IF EXISTS uf_GetEntityAttributeValueByNames
;

CREATE FUNCTION uf_GetEntityAttributeValueByNames (
	p_EntityTypeName VARCHAR(50)
	,p_EntityName VARCHAR(20000)
	,p_AttributeName VARCHAR(255)
) RETURNS VARCHAR(20000)
BEGIN
	DECLARE EntityTypeId INT;
	DECLARE EntityId INT;
	DECLARE AttributeId INT;
	
	SELECT
		Common.uf_GetEntityTypeIdByName(p_EntityTypeName)
	INTO
		EntityTypeId;
	
	SELECT
		Common.uf_GetEntityIdByName(EntityTypeId, p_EntityName)
	INTO
		EntityId;
	
	SELECT
		Common.uf_GetAttributeIdByName(p_AttributeName)
	INTO
		AttributeId;
		
	RETURN Common.uf_GetEntityAttributeValue(EntityTypeId, EntityId, AttributeId);

END
;