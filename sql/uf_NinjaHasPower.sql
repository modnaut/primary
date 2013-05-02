
USE Common
;

DROP FUNCTION IF EXISTS uf_NinjaHasPower
;

CREATE FUNCTION uf_NinjaHasPower(
	p_NinjaId INT,
	p_PowerId INT
)
	 RETURNS CHAR(1) DETERMINISTIC
BEGIN 

	DECLARE v_has_power CHAR(1);
	DECLARE v_check_exists INT;
	SET v_has_power = 'N';
	SET v_check_exists = 0; 
	

	SELECT COUNT(1) INTO v_check_exists
	FROM Common.Ninja n
		LEFT OUTER JOIN Common.NinjaPower np ON np.NinjaId = n.NinjaId
		LEFT OUTER JOIN Common.NinjaClan nc ON nc.NinjaId = n.NinjaId
		LEFT OUTER JOIN Common.ClanPower cp ON cp.ClanId = nc.ClanId
	WHERE n.NinjaId = p_NinjaId
		AND (np.PowerId = p_PowerId OR cp.PowerId = p_PowerId);
 
	IF (v_check_exists > 0) THEN 
        SET v_has_power = 'Y'; 
    END IF;


	RETURN v_has_power;

END
;
