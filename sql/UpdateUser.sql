USE common
;
DROP PROCEDURE IF EXISTS `UpdateUser`
;
CREATE PROCEDURE UpdateUser(
	IN PUserId INT
	,IN PUsername VARCHAR(50)
	,IN PFirstName VARCHAR(50)
	,IN PLastName VARCHAR(50)
	,IN PEmailAddress VARCHAR(100)
	,IN PHireDate DATE
	,IN PUserTypeCd CHAR(1)
	,IN PUserPassword VARCHAR(100))
BEGIN 

		UPDATE
			Common.Users
		SET
			FirstName = PFirstName
			,FirstName = PFirstName
			,LastName = PLastName
			,EmailAddress = PEmailAddress
			,UserPassword = IFNULL(PUserPassword, UserPassword)
			/*,HireDate = PHireDate*/
			,UserTypeCd = PUserTypeCd
		WHERE
			UserId = PUserId;
			
END 
; 