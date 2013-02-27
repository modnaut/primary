
USE Session;

--SHOW Processlist;

--SET GLOBAL event_scheduler = ON;

--SHOW EVENTS;

DELIMITER $$
CREATE EVENT IF NOT EXISTS `SessionCleanerEvent`
	ON SCHEDULE
      EVERY 1 HOUR
		--STARTS '2013-02-26 03:00:00'
	DO BEGIN
		-- Deletes Sessions that are more than 24 hours old.
		DELETE FROM Session.sessionobject WHERE dateEntered < DATE_SUB(NOW(), INTERVAL 24 HOUR);
	END */$$
DELIMITER ;
