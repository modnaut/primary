USE common
;
DROP FUNCTION IF EXISTS uf_IsNumeric
;

CREATE FUNCTION uf_IsNumeric (p_value varchar(20000)) RETURNS INT
BEGIN
SET @match = '^(([0-9+-.$]{1})|([+-]?[$]?[0-9]*(([.]{1}[0-9]*)|([.]?[0-9]+))))$';

RETURN IF(p_value REGEXP @match, 1, 0);

END 
;