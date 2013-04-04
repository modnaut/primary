USE common
;
DROP PROCEDURE IF EXISTS `up_GetAllUsersAlphabetically`
;
CREATE PROCEDURE up_GetAllUsersAlphabetically() 
BEGIN
SELECT `UserId`, `UserName`, `FirstName`, `LastName`, `EmailAddress`, `UserPassword`, `HireDate`, `UserTypeCd` FROM Common.users ORDER BY LastName; 
END 
; 