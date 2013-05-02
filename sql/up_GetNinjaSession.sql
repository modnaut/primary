USE common
;
DROP PROCEDURE IF EXISTS `up_GetNinjaSession`
;
CREATE PROCEDURE up_GetNinjaSession(
	IN p_NinjaId VARCHAR(255)) 
BEGIN 

SELECT s.SessionId, s.SessionObject, s.CreatedDate, s.LastModifiedDate
FROM Common.`session` s
	INNER JOIN Common.`ninjasession` ns ON ns.SessionId = s.SessionId
	INNER JOIN Common.`ninja` n ON ns.NinjaId = n.NinjaId
WHERE n.NinjaId = p_NinjaId
ORDER BY s.LastModifiedDate DESC;

END 
; 