USE common
;
DROP PROCEDURE IF EXISTS `GetAllUsersAlphabetically`
;
CREATE PROCEDURE GetAllUsersAlphabetically() 
BEGIN
SELECT `UserId`, `UserName`, `FirstName`, `LastName`, `EmailAddress`, `UserPassword`, `HireDate` FROM Common.users ORDER BY LastName; 
END 
; 