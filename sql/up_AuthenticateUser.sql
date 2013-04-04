
USE common
;

DROP PROCEDURE IF EXISTS up_AuthenticateUser
;

CREATE PROCEDURE up_AuthenticateUser(
	IN p_Email VARCHAR(255),
	IN p_UserPassword VARCHAR(255)) 
BEGIN 

CREATE TEMPORARY TABLE IF NOT EXISTS _Temp
SELECT UserId, FirstName, LastName, EmailAddress, UserPassword FROM Common.users WHERE IFNULL(p_Email, EmailAddress) = EmailAddress AND IFNULL(p_UserPassword, UserPassword) = UserPassword ORDER BY LastName;

IF (SELECT COUNT(*) FROM _Temp) = 0
	THEN UPDATE Common.users SET InvalidLoginAttempts = InvalidLoginAttempts + 1 WHERE IFNULL(p_Email, EmailAddress) = EmailAddress;
END IF;

SELECT UserId, FirstName, LastName, EmailAddress, UserPassword FROM _Temp;

DROP TEMPORARY TABLE IF EXISTS _Temp;

END
;

