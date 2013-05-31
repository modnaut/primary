USE common
;
DROP FUNCTION IF EXISTS uf_GetEntityIdByName
;

CREATE FUNCTION uf_GetEntityIdByName (
	p_EntityTypeId INT
	,p_EntityName VARCHAR(20000)
) RETURNS INT
BEGIN
	DECLARE EntityId INT;
	
	CASE p_EntityTypeId
		WHEN 1 THEN
			SELECT Common.uf_GetEnvironmentIdByName(p_EntityName) INTO EntityId;
		ELSE SELECT -1 INTO EntityId;
	END CASE;	
		
	RETURN EntityId;

END 
;