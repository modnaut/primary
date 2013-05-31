USE common
;
DROP FUNCTION IF EXISTS uf_GetEnvironmentIdByName
;

CREATE FUNCTION uf_GetEnvironmentIdByName (
	p_EnvironmentName VARCHAR(20000)
) RETURNS INT
BEGIN
	DECLARE EnvironmentId INT;
	
	SELECT
		e.EnvironmentId
	INTO
		EnvironmentId
	FROM
		Common.Environment e
	WHERE
		EnvironmentName = p_EnvironmentName;
		
	RETURN EnvironmentId;

END 
;