
USE common
;

DROP PROCEDURE IF EXISTS up_GetMultipleStringsForLanguage
;


CREATE PROCEDURE up_GetMultipleStringsForLanguage(
	IN p_CommaDelimitedList VARCHAR(20000),
	IN p_LanguageCd VARCHAR(3),
	IN p_Delimiter VARCHAR(10))
BEGIN


CREATE TEMPORARY TABLE TEMP_StringCds (
	StringCd varchar(500)
);

CALL Common.up_ParseCommaDelimited(p_CommaDelimitedList, p_Delimiter, 'TEMP_StringCds', 'StringCd');


SELECT	
	sc.StringCd
	,sv.Value
FROM
	TEMP_StringCds sc
	Left Join Common.string s on s.StringCd = sc.StringCd
	Left Join Common.language l on l.IsoLanguageCd = p_LanguageCd
	Left Join Common.stringvalue sv on sv.StringId = s.StringId and sv.LanguageId = l.LanguageId;

DROP TABLE TEMP_StringCds;

END
;