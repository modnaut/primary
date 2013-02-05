USE common
;
DROP PROCEDURE IF EXISTS `GetUserSession`
;
CREATE PROCEDURE GetUserSession(
	IN UserIdParm VARCHAR(255)) 
BEGIN 

SELECT s.SessionId, s.SessionObject, s.LastModifiedDate
FROM Common.`session` s
	INNER JOIN Common.`usersession` us ON us.SessionId = s.SessionId
	INNER JOIN Common.`users` u ON us.UserId = u.UserId
WHERE u.UserId = UserIdParm
ORDER BY s.LastModifiedDate DESC;

END 
; 