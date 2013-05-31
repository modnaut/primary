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
	DECLARE ParentEntityId INT;
	
	SELECT
		av.AttributeValue
	INTO
		AttributeValue
	FROM
		Common.EntityAttributeValue av
	WHERE
		av.EntityTypeId = p_EntityTypeId
		AND
		av.EntityId = p_EntityId
		AND
		av.AttributeId = p_AttributeId;
	
	
	IF AttributeValue IS NULL
	THEN
		SELECT
			Common.uf_GetParentEntityId(p_EntityTypeId, p_EntityId)
		INTO
			ParentEntityId;
		
		WHILE (ParentEntityId IS NOT NULL AND AttributeValue IS NULL) DO
			SELECT
				av.AttributeValue
			INTO
				AttributeValue
			FROM
				Common.EntityAttributeValue av
			WHERE
				av.EntityTypeId = p_EntityTypeId
				AND
				av.EntityId = ParentEntityId
				AND
				av.AttributeId = p_AttributeId;
				
			SELECT
				Common.uf_GetParentEntityId(p_EntityTypeId, ParentEntityId)
			INTO
				ParentEntityId;
		END WHILE;
	END IF;
	
	RETURN AttributeValue;

END 
;