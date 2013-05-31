USE common
;
DROP FUNCTION IF EXISTS uf_GetEntityTypeIdByName
;

CREATE FUNCTION uf_GetEntityTypeIdByName (
	p_EntityTypeName VARCHAR(50)
) RETURNS INT
BEGIN
	DECLARE EntityTypeId INT;
	
	SELECT
		et.EntityTypeId
	INTO
		EntityTypeId
	FROM
		Common.EntityType et
	WHERE
		et.EntityTypeName = p_EntityTypeName;
		
	RETURN EntityTypeId;

END 
;