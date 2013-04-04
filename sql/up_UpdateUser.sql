USE common
;
DROP PROCEDURE IF EXISTS `up_UpdateUser`
;
CREATE PROCEDURE up_UpdateUser(
	IN p_UserId INT
	,IN p_Username VARCHAR(50)
	,IN p_FirstName VARCHAR(50)
	,IN p_LastName VARCHAR(50)
	,IN p_EmailAddress VARCHAR(100)
	,IN p_HireDate DATE
	,IN p_UserTypeCd CHAR(1)
	,IN p_UserPassword VARCHAR(100))
BEGIN 

		UPDATE
			Common.Users
		SET
			FirstName = p_FirstName
			,FirstName = p_FirstName
			,LastName = p_LastName
			,EmailAddress = p_EmailAddress
			,UserPassword = IFNULL(p_UserPassword, UserPassword)
			/*,HireDate = p_HireDate*/
			,UserTypeCd = p_UserTypeCd
		WHERE
			UserId = p_UserId;
			
END 
; 