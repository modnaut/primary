USE common
;
DROP PROCEDURE IF EXISTS up_CanPerformAction
;
CREATE PROCEDURE up_CanPerformAction(
	IN p_UserId INT,
	IN p_ActionId INT) 
BEGIN 

SELECT Common.uf_CanPerformAction(p_UserId, p_ActionId);

END 
;