
USE Common
;

DROP FUNCTION IF EXISTS uf_CanPerformAction
;

CREATE FUNCTION uf_CanPerformAction(
	p_UserId INT,
	p_ActionId INT
)
	 RETURNS CHAR(1) DETERMINISTIC
BEGIN 

	DECLARE v_has_permission CHAR(1);
	DECLARE v_check_exists INT;
	SET v_has_permission = 'N';
   SET v_check_exists = 0; 
	

	SELECT COUNT(1) INTO v_check_exists
	FROM Common.Users m
		LEFT OUTER JOIN Common.UserAction ma ON ma.UserId = m.UserId
		LEFT OUTER JOIN Common.UserSecurityGroup msg ON msg.UserId = m.UserId
		LEFT OUTER JOIN Common.SecurityGroupAction sga ON sga.SecurityGroupId = msg.SecurityGroupId
	WHERE m.UserId = p_UserId
		AND (ma.ActionId = p_ActionId OR sga.ActionId = 1);
 
	IF (v_check_exists > 0) THEN 
        SET v_has_permission = 'Y'; 
    END IF;


	RETURN v_has_permission;

END
;
