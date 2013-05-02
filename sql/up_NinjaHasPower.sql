USE common
;
DROP PROCEDURE IF EXISTS up_NinjaHasPower
;
CREATE PROCEDURE up_NinjaHasPower(
	IN p_NinjaId INT,
	IN p_PowerId INT) 
BEGIN 

SELECT Common.uf_NinjaHasPower(p_NinjaId, p_PowerId);

END 
;