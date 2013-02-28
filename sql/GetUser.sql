USE common
;
DROP PROCEDURE IF EXISTS `GetUser`
;
CREATE PROCEDURE GetUser(
	IN PUserId INT)
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
			UserId = PUserId; 
END 
; 