USE common
;
DROP PROCEDURE IF EXISTS up_RebuildMenuItemOrdinal
;
CREATE PROCEDURE up_RebuildMenuItemOrdinal() 
BEGIN 

	DROP TEMPORARY TABLE IF EXISTS TEMP_MIO;
	DROP TEMPORARY TABLE IF EXISTS TEMP_MIO2;
	
	CREATE TEMPORARY TABLE TEMP_MIO (
		MenuItemId INT
		,Level INT
		,Sorter VARCHAR(255) NULL DEFAULT NULL
		,Ordinal INT NULL DEFAULT NULL
	);
	
	CREATE TEMPORARY TABLE TEMP_MIO2 (
		MenuItemId INT
		,Level INT
		,Sorter VARCHAR(255) NULL DEFAULT NULL
		,Ordinal INT NULL DEFAULT NULL
	); 
	
	INSERT INTO TEMP_MIO (MenuItemId, Level, Sorter)
	SELECT
		MenuItemId
		,1
		,CAST(DisplayOrder AS CHAR)
	FROM
		Common.MenuItem
	WHERE
		ParentMenuItemId IS NULL;
	
	SET @Level = 1;
	
	theloop: LOOP
		SELECT
			COUNT(*) INTO @exists
		FROM
			Common.MenuItem m
			INNER JOIN TEMP_MIO t ON m.ParentMenuItemId = t.MenuItemId
		WHERE
			t.Level = @Level;
			
		IF @exists = 0 THEN
			LEAVE theloop;
		END IF;
			
		UPDATE
			TEMP_MIO
		SET
			Sorter = CONCAT(Sorter, '-');
			
		DELETE FROM TEMP_MIO2;
		INSERT INTO TEMP_MIO2
			(MenuItemId, Level, Sorter, Ordinal)
		SELECT
			MenuItemId
			,Level
			,Sorter
			,Ordinal
		FROM
			TEMP_MIO;
			
		INSERT INTO
			TEMP_MIO (MenuItemId, Level, Sorter)
		SELECT
			m.MenuItemId
			,(@Level + 1)
			,CONCAT(t.Sorter, m.DisplayOrder)
		FROM
			Common.MenuItem m
			INNER JOIN TEMP_MIO2 t ON m.ParentMenuItemId = t.MenuItemId AND t.Level = @Level;
			
		SET @Level = @Level + 1;
	END LOOP theloop;
	
	DELETE FROM Common.MenuItemOrdinal;
	
	INSERT INTO
		Common.MenuItemOrdinal (MenuItemId, Ordinal, Level)
	SELECT
		MenuItemId
		,@rownum:=@rownum+1
		,Level
	FROM
		TEMP_MIO t
		,(SELECT @rownum:=0) r
	ORDER BY
		t.Sorter ASC;
	
	DROP TEMPORARY TABLE TEMP_MIO;
	DROP TEMPORARY TABLE TEMP_MIO2;

END 
;