USE common
;
DROP TRIGGER IF EXISTS t_MenuItemBeforeInsert
;

CREATE TRIGGER t_MenuItemBeforeInsert
BEFORE INSERT ON MenuItem FOR EACH ROW
BEGIN
	CALL Common.up_CheckMenuItemDisplayOrder(NEW.ParentMenuItemId, NEW.MenuItemId, NEW.MenuId, NEW.DisplayOrder);
END
;