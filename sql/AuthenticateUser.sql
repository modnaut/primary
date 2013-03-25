
USE common
;

DROP PROCEDURE IF EXISTS AuthenticateUser
;

CREATE PROCEDURE AuthenticateUser(
	IN EmailParm VARCHAR(255),
	IN UserPasswordParm VARCHAR(255)) 
BEGIN 

CREATE TEMPORARY TABLE IF NOT EXISTS _Temp
SELECT UserId, FirstName, LastName, EmailAddress, UserPassword FROM Common.users WHERE IFNULL(EmailParm, EmailAddress) = EmailAddress AND IFNULL(UserPasswordParm, UserPassword) = UserPassword ORDER BY LastName;

IF (SELECT COUNT(*) FROM _Temp) = 0
	THEN UPDATE Common.users SET InvalidLoginAttempts = InvalidLoginAttempts + 1 WHERE IFNULL(EmailParm, EmailAddress) = EmailAddress;
END IF;

SELECT UserId, FirstName, LastName, EmailAddress, UserPassword FROM _Temp;

DROP TEMPORARY TABLE IF EXISTS _Temp;

END
;

