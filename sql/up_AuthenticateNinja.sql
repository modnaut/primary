
USE common
;

DROP PROCEDURE IF EXISTS up_AuthenticateNinja
;

CREATE PROCEDURE up_AuthenticateNinja(
	IN p_Email VARCHAR(255),
	IN p_Password VARCHAR(255)) 
BEGIN 

CREATE TEMPORARY TABLE IF NOT EXISTS _Temp
SELECT NinjaId, FirstName, LastName, EmailAddress, Password FROM Common.Ninja WHERE IFNULL(p_Email, EmailAddress) = EmailAddress AND IFNULL(p_Password, Password) = Password ORDER BY LastName;

IF (SELECT COUNT(*) FROM _Temp) = 0
	THEN UPDATE Common.Ninja SET InvalidLoginAttempts = InvalidLoginAttempts + 1 WHERE IFNULL(p_Email, EmailAddress) = EmailAddress;
END IF;

SELECT NinjaId, FirstName, LastName, EmailAddress, Password FROM _Temp;

DROP TEMPORARY TABLE IF EXISTS _Temp;

END
;

