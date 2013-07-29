USE common
;
DROP PROCEDURE IF EXISTS up_CheckMenuItemDisplayOrder
;
CREATE PROCEDURE up_CheckMenuItemDisplayOrder(
p_NewParentMenuItemId INT
,p_NewMenuItemId INT
,p_NewMenuId INT
,p_NewDisplayOrder INT
) 
BEGIN 

	/*
		We want to make sure there are no two items in the same parent with the
		same DisplayOrder. Since ParentMenuItemId is NULLable, the unique key
		doesn't enforce it at the root level. Before insert and before update triggers
		will call this proc to catch root level items with the same DisplayOrder
	*/
	SELECT
		COUNT(*) INTO @exists
	FROM
		Common.MenuItem m
	WHERE
		(
			(p_NewParentMenuItemId IS NULL AND m.ParentMenuItemId IS NULL)
			OR
			(p_NewParentMenuItemId = m.ParentMenuItemId)
		)
		AND
		p_NewDisplayOrder = m.DisplayOrder
		AND
		p_NewMenuId = m.MenuId
		AND
		p_NewMenuItemId <> m.MenuItemId;
		
	IF @exists > 0 THEN
		SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'DUPLICATE DisplayOrder IN SAME PARENT';
	END IF;

END 
;