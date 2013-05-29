USE common
;
DROP PROCEDURE IF EXISTS up_NinjaHasPowers
;
CREATE PROCEDURE up_NinjaHasPowers(
	IN p_NinjaId INT,
	IN p_PowerIds VARCHAR(5000)) 
BEGIN 
	
DROP TABLE IF EXISTS TEMP_Powers;

CREATE TEMPORARY TABLE TEMP_Powers (
	PowerId VARCHAR(50),
	HasPower CHAR(1)
);

CALL common.up_ParseCommaDelimited(p_PowerIds, ',', 'TEMP_Powers', 'PowerId');

DELETE FROM TEMP_Powers WHERE common.uf_IsInteger(PowerId) = 0;

UPDATE TEMP_Powers SET
	HasPower = common.uf_NinjaHasPower(p_NinjaId, PowerId);
	
	
SELECT
	PowerId,
	HasPower
FROM
	TEMP_Powers;
	
DROP TABLE TEMP_Powers;

END 
;