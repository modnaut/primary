USE common
;

DROP PROCEDURE IF EXISTS `up_InsertUpdateSession`
;

CREATE PROCEDURE up_InsertUpdateSession (
	IN p_NinjaId INT
	,IN p_SessionId BIGINT
	,IN p_SessionObject BLOB)
BEGIN 

	INSERT INTO Common.Session(SessionId, SessionObject, CreatedDate, LastModifiedDate) 
	VALUES (p_SessionId, p_SessionObject, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP())
	ON DUPLICATE KEY UPDATE SessionObject = p_SessionObject, LastModifiedDate = CURRENT_TIMESTAMP();

	INSERT IGNORE INTO Common.NinjaSession(NinjaId, SessionId) 
	VALUES (p_NinjaId, p_SessionId);

END
;