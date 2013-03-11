USE common
;
DROP PROCEDURE IF EXISTS `GetUserMultipleResultTest`
;
CREATE PROCEDURE GetUserMultipleResultTest(
	IN UserIdParm VARCHAR(255)) 
BEGIN 
SELECT UserId, FirstName, LastName, EmailAddress, UserPassword FROM Common.users WHERE IFNULL(UserIdParm, UserId) = UserId ORDER BY LastName; 
SELECT UserId, FirstName, LastName, EmailAddress, UserPassword FROM Common.users WHERE IFNULL(UserIdParm, UserId) = UserId ORDER BY LastName;
END 
; 