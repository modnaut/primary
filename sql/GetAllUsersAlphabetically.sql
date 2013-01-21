use common;
DROP PROCEDURE IF EXISTS `GetAllUsersAlphabetically`;
DELIMITER // 
CREATE PROCEDURE GetAllUsersAlphabetically() 
BEGIN 
SELECT `UserId`, `UserName`, `FirstName`, `LastName`, `EmailAddress`, `Password` FROM Common.users ORDER BY LastName; 
END // 
DELIMITER ; 