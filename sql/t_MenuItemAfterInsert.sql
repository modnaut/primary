USE common
;
DROP TRIGGER IF EXISTS t_MenuItemAfterInsert
;

CREATE TRIGGER t_MenuItemAfterInsert
AFTER INSERT ON MenuItem FOR EACH ROW
BEGIN
	Call Common.up_RebuildMenuItemOrdinal();
END
;