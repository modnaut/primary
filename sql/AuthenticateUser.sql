USE common
;
DROP PROCEDURE IF EXISTS `AuthenticateUser`
;
CREATE PROCEDURE AuthenticateUser(
	IN EmailParm VARCHAR(255),
	IN UserPasswordParm VARCHAR(255)) 
BEGIN 
SELECT UserId, FirstName, LastName, EmailAddress, UserPassword FROM Common.users WHERE IFNULL(EmailParm, EmailAddress) = EmailAddress AND IFNULL(UserPasswordParm, UserPassword) = UserPassword ORDER BY LastName; 
END 
; 