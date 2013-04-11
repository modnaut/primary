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
DECLARE numColumns int DEFAULT 0;
DECLARE currentColumn int DEFAULT 0;

IF(p_Delimiter IS NULL) THEN
	SET p_Delimiter = ',';
END IF;



SET @sql_text := CONCAT('Insert Into ', p_TableName, ' (');


--, p_ColumnName, ') Values ');

--build columns list
columns_loop: LOOP
	SET part = SUBSTRING_INDEX(p_ColumnName,p_Delimiter,1);
	IF part = '' THEN
		LEAVE columns_loop;
	END IF;
	
	SET @sql_text := CONCAT(@sql_text, part, ',');
	SET numColumns = numColumns + 1;
	Set p_ColumnName = SUBSTRING(p_ColumnName, LENGTH(part) + LENGTH(p_Delimiter) + 1);
END LOOP columns_loop;



--remove last comma
SET @sql_text := SUBSTRING(@sql_text, 1, LENGTH(@sql_text) - 1);

--add ) values 
SET @sql_text := CONCAT(@sql_text, ') VALUES ');


values_loop: LOOP
	SET part = SUBSTRING_INDEX(p_CommaDelimitedList,p_Delimiter,1);
	IF part = '' THEN
		IF currentColumn <> 0 and currentColumn < numColumns THEN
			WHILE currentColumn < numColumns DO
				SET @sql_text := CONCAT(@sql_text, '\'\',');
				SET currentColumn = currentColumn + 1;
			END WHILE;
			SET @sql_text := SUBSTRING(@sql_text, 1, LENGTH(@sql_text) - 1);
			SET @sql_text := CONCAT(@sql_text, '),');
		END IF;
		LEAVE values_loop;
	END IF;
	
	IF currentColumn = 0 THEN
		SET @sql_text := CONCAT(@sql_text, '(');
	END IF;
	
	SET currentColumn = currentColumn + 1;

	SET @sql_text := CONCAT(@sql_text, QUOTE(part));
	
	IF currentColumn < numColumns THEN
		SET @sql_text := CONCAT(@sql_text, ',');
	END IF;
	
	IF currentColumn = numColumns THEN
		SET @sql_text := CONCAT(@sql_text, '),');
		SET currentColumn = 0;
	END IF;
	
	Set p_CommaDelimitedList = SUBSTRING(p_CommaDelimitedList, LENGTH(part) + LENGTH(p_Delimiter) + 1);
END LOOP values_loop;

SET @sql_text := SUBSTRING(@sql_text, 1, LENGTH(@sql_text) - 1);



PREPARE
	insertStatment
FROM
	@sql_text;

EXECUTE insertStatment;

END
;