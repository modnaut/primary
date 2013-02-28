
/**

Job that deletes Sessions that are more than 24 hours old.

SELECT * FROM Common.`session`;
SHOW Processlist;
SET GLOBAL event_scheduler = ON;
SHOW EVENTS;

*/

USE Session;

DELIMITER $$
CREATE EVENT IF NOT EXISTS `SessionCleanerEvent`
	ON SCHEDULE
      EVERY 1 HOUR
	DO BEGIN
		DELETE FROM Session.sessionobject WHERE dateEntered < DATE_SUB(NOW(), INTERVAL 24 HOUR);
	END $$
DELIMITER ;
