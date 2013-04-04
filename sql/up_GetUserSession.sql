USE common
;
DROP PROCEDURE IF EXISTS `up_GetUserSession`
;
CREATE PROCEDURE up_GetUserSession(
	IN p_UserId VARCHAR(255)) 
BEGIN 

SELECT s.SessionId, s.SessionObject, s.CreatedDate, s.LastModifiedDate
FROM Common.`session` s
	INNER JOIN Common.`usersession` us ON us.SessionId = s.SessionId
	INNER JOIN Common.`users` u ON us.UserId = u.UserId
WHERE u.UserId = p_UserId
ORDER BY s.LastModifiedDate DESC;

END 
; 