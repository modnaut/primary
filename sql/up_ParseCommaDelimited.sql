
USE common
;

DROP PROCEDURE IF EXISTS up_ParseCommaDelimited
;


CREATE PROCEDURE up_ParseCommaDelimited(
	IN p_CommaDelimitedList VARCHAR(20000),
	IN p_Delimiter VARCHAR(50),
	IN p_TableName VARCHAR(1000),
	IN p_ColumnName VARCHAR(1000))
BEGIN

DECLARE part VARCHAR(10000);

IF(p_Delimiter IS NULL) THEN
	SET p_Delimiter = ',';
END IF;



If(p_CommaDelimitedList <> '') Then
	SET @sql_text := CONCAT('Insert Into ', p_TableName, ' (', p_ColumnName, ') Values ');
	simple_loop: LOOP
		SET part = SUBSTRING_INDEX(p_CommaDelimitedList,p_Delimiter,1);
		
		IF part = '' THEN
			LEAVE simple_loop;
		END IF;
		
		SET @sql_text := CONCAT(@sql_text, '(''', REPLACE(part, '''', ''''''), '''),');
		
		Set p_CommaDelimitedList = SUBSTRING(p_CommaDelimitedList, LENGTH(part) + LENGTH(p_Delimiter) + 1);
	END LOOP simple_loop;
	
	SET @sql_text := SUBSTRING(@sql_text, 1, LENGTH(@sql_text) - 1);
   
	PREPARE
		insertStatment
	FROM
		@sql_text;
		
	EXECUTE insertStatment;
End If;



END
;