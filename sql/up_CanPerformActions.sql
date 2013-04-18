USE common
;
DROP PROCEDURE IF EXISTS up_CanPerformActions
;
CREATE PROCEDURE up_CanPerformActions(
	IN p_UserId INT,
	IN p_ActionIds VARCHAR(5000)) 
BEGIN 
	
DROP TABLE IF EXISTS TEMP_Actions;

CREATE TEMPORARY TABLE TEMP_Actions (
	ActionId VARCHAR(50),
	CanPerform CHAR(1)
);

CALL Common.up_ParseCommaDelimited(p_ActionIds, ',', 'TEMP_Actions', 'ActionId');

DELETE FROM TEMP_Actions WHERE Common.uf_IsInteger(ActionId) = 0;

UPDATE TEMP_Actions SET
	CanPerform = Common.uf_CanPerformAction(p_UserId, ActionId);
	
	
SELECT
	ActionId,
	CanPerform
FROM
	TEMP_Actions;
	
DROP TABLE TEMP_Actions;

END 
;