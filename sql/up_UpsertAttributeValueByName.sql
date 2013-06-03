USE common
;

DROP PROCEDURE IF EXISTS up_UpsertAttributeValueByName
;

CREATE PROCEDURE up_UpsertAttributeValueByName (
	IN p_EntityTypeName VARCHAR(1000)
	,IN p_EntityId INT
	,IN p_AttributeName VARCHAR(1000)
	,IN p_AttributeValue VARCHAR(20000))
BEGIN 

	DECLARE EntityTypeId INT;
	DECLARE AttributeId INT;
	
	SELECT
		Common.uf_GetEntityTypeIdByName(p_EntityTypeName)
	INTO
		EntityTypeId;
	
	SELECT
		Common.uf_GetAttributeIdByName(p_AttributeName)
	INTO
		AttributeId;
		
	IF(EntityTypeId IS NOT NULL AND AttributeId IS NOT NULL)
	THEN
		INSERT INTO
			Common.EntityAttributeValue
			(EntityTypeId, EntityId, AttributeId, AttributeValue)
		VALUES
			(EntityTypeId, p_EntityId, AttributeId, p_AttributeValue)
		ON DUPLICATE KEY UPDATE
			AttributeValue = p_AttributeValue;
	END IF;

END
;