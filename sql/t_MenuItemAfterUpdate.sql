USE common
;
DROP TRIGGER IF EXISTS t_MenuItemAfterUpdate
;

CREATE TRIGGER t_MenuItemAfterUpdate
AFTER UPDATE ON MenuItem FOR EACH ROW
BEGIN
	Call Common.up_RebuildMenuItemOrdinal();
END
;