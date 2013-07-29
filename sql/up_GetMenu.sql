
USE common
;

DROP PROCEDURE IF EXISTS up_GetMenu
;


CREATE PROCEDURE up_GetMenu(
	IN p_MenuId INT,
	IN p_NinjaId INT,
	IN p_LanguageCd VARCHAR(2))
BEGIN
	
DECLARE numOrphans INT DEFAULT 0;

DROP TEMPORARY TABLE IF EXISTS TEMP_MenuItems;
DROP TEMPORARY TABLE IF EXISTS TEMP_MenuItems2;

CREATE TEMPORARY TABLE TEMP_MenuItems (
	MenuItemId INT
	,ParentMenuItemId INT
);

CREATE TEMPORARY TABLE TEMP_MenuItems2 (
	MenuItemId INT
	,ParentMenuItemId INT
);

--Get all active menu items that user has access to
INSERT INTO
	TEMP_MenuItems
	(MenuItemId, ParentMenuItemId)
SELECT
	m.MenuItemId
	,m.ParentMenuItemId
FROM
	Common.MenuItem m
WHERE
	m.MenuId = p_MenuId
	AND
	(
		m.PowerId IS NULL
		OR
		Common.uf_NinjaHasPower(p_NinjaId, m.PowerId) = 'Y'
	)
	AND
	m.ActiveFlag = 'Y';

--Copy full set into second table
INSERT INTO
	TEMP_MenuItems2
	(MenuItemId, ParentMenuItemId)
SELECT
	MenuItemId, ParentMenuItemId
FROM
	TEMP_MenuItems;

--See if there are any orphans (items that user has access to but doesn't have access to parent
SELECT
	COUNT(1) INTO numOrphans
FROM
	TEMP_MenuItems m1
WHERE
	m1.ParentMenuItemId IS NOT NULL
	AND
	NOT EXISTS (SELECT 1 FROM TEMP_MenuItems2 m2 WHERE m2.MenuItemId = m1.ParentMenuItemId);
	
--While there are still orhpans
WHILE numOrphans > 0 DO

	--Delete orphans using secondary table as source
	DELETE FROM m1 USING TEMP_MenuItems AS m1 WHERE m1.ParentMenuItemId IS NOT NULL AND m1.ParentMenuItemId NOT IN (SELECT m2.MenuItemId FROM TEMP_MenuItems2 m2);
	
	
	--Repopulate secondary table
	TRUNCATE TABLE TEMP_MenuItems2;
	
	INSERT INTO
		TEMP_MenuItems2
		(MenuItemId, ParentMenuItemId)
	SELECT
		MenuItemId, ParentMenuItemId
	FROM
		TEMP_MenuItems;

	--Get new orphan count
	SELECT
		COUNT(1) INTO numOrphans
	FROM
		TEMP_MenuItems m1
	WHERE
		m1.ParentMenuItemId IS NOT NULL
		AND
		NOT EXISTS (SELECT 1 FROM TEMP_MenuItems2 m2 WHERE m2.MenuItemId = m1.ParentMenuItemId);


END WHILE;

--Select items, using MenuItemOrdinal table to sort properly
SELECT
	m.MenuItemId
	,m.ParentMenuItemId
	,IFNULL(sv.Value, m.DisplayTextStringCd)
	,m.IconCls
	,m.Class
	,m.Method
	,m.URL
FROM
	TEMP_MenuItems t
	INNER JOIN Common.MenuItem m ON m.MenuItemId = t.MenuItemId
	INNER JOIN Common.MenuItemOrdinal o ON o.MenuItemId = m.MenuItemId
	LEFT JOIN Common.Language l ON l.IsoLanguageCd = p_LanguageCd
	LEFT JOIN Common.String s ON s.StringCd = m.DisplayTextStringCd
	LEFT JOIN Common.StringValue sv ON sv.StringId = s.StringId AND sv.LanguageId = l.LanguageId
ORDER BY
	o.Ordinal ASC;

DROP TEMPORARY TABLE TEMP_MenuItems;
DROP TEMPORARY TABLE TEMP_MenuItems2;

END
;