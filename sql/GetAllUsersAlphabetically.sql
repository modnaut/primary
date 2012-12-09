DELIMITER // 
CREATE PROCEDURE GetAllUsersAlphabetically() 
BEGIN 
SELECT * FROM Common.users ORDER BY LastName; 
END // 
DELIMITER ; 