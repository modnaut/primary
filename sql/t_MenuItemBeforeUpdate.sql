USE common
;
DROP TRIGGER IF EXISTS t_MenuItemBeforeUpdate
;

CREATE TRIGGER t_MenuItemBeforeUpdate
BEFORE UPDATE ON MenuItem FOR EACH ROW
BEGIN
	IF NEW.MenuItemId = OLD.MenuItemId THEN
		CALL Common.up_CheckMenuItemDisplayOrder(NEW.ParentMenuItemId, NEW.MenuItemId, NEW.MenuId, NEW.DisplayOrder);
	END IF;
END
;