
USE common
;

DROP PROCEDURE IF EXISTS up_ParseCommaDelimited
;


CREATE PROCEDURE up_ParseCommaDelimited(
	IN p_CommaDelimitedList VARCHAR(20000),
	IN p_Delimiter VARCHAR(50))
BEGIN

DECLARE part VARCHAR(10000);

IF(p_Delimiter IS NULL) THEN
	SET p_Delimiter = ',';
END IF;

CREATE TEMPORARY TABLE DataSet (
	id INT NOT NULL AUTO_INCREMENT,
	StringPart VARCHAR(20000),
	PRIMARY KEY (`ID`)
);

If(p_CommaDelimitedList <> '') Then
      simple_loop: LOOP
         SET part = SUBSTRING_INDEX(p_CommaDelimitedList,p_Delimiter,1);
         IF part = '' THEN
            LEAVE simple_loop;
         END IF;
         insert into DataSet (StringPart) values (part);
         Set p_CommaDelimitedList = SUBSTRING(p_CommaDelimitedList, LENGTH(part) + LENGTH(p_Delimiter) + 1);
   END LOOP simple_loop;
End If;

Select StringPart From DataSet;

DROP TABLE DataSet;

END
;