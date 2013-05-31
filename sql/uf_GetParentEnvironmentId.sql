
USE common
;
DROP FUNCTION IF EXISTS uf_GetParentEnvironmentId
;

CREATE FUNCTION uf_GetParentEnvironmentId (
	p_EnvironmentId INT
) RETURNS INT
BEGIN
	DECLARE ParentEnvironmentId INT;
	
	SELECT
		e.ParentEnvironmentId
	INTO
		ParentEnvironmentId
	FROM
		Common.Environment e
	WHERE
		e.EnvironmentId = p_EnvironmentId;
		
	RETURN ParentEnvironmentId;

END 
;