USE common
;
DROP PROCEDURE IF EXISTS `up_GetUser`
;
CREATE PROCEDURE up_GetUser(
	IN p_UserId INT)
BEGIN 

		SELECT
			UserId
			,Username
			,FirstName
			,LastName
			,EmailAddress
			,CAST(HireDate as DATE)
			,UserTypeCd
		FROM
			Common.users
		WHERE
			UserId = p_UserId; 
END 
; 