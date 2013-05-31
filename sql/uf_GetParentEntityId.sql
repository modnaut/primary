USE common
;
DROP FUNCTION IF EXISTS uf_GetParentEntityId
;

CREATE FUNCTION uf_GetParentEntityId (
	p_EntityTypeId INT
	,p_EntityId INT
) RETURNS INT
BEGIN
	DECLARE ParentEntityId INT;
	
	CASE p_EntityTypeId
		WHEN 1 THEN
			SELECT Common.uf_GetParentEnvironmentId(p_EntityId) INTO ParentEntityId;
		ELSE SET ParentEntityId = NULL;
	END CASE;	
		
	RETURN ParentEntityId;

END 
;