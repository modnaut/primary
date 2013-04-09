
USE common
;

DROP PROCEDURE IF EXISTS up_GetTranslatedStrings
;


CREATE PROCEDURE up_GetTranslatedStrings(
	IN p_CommaDelimitedList VARCHAR(20000),
	IN p_LanguageCd VARCHAR(3))
BEGIN



CREATE TEMPORARY TABLE CommaDelimited (
	StringPart varchar(20000)
);

CALL Common.up_ParseCommaDelimited(p_CommaDelimitedList, ',');


SELECT	
	cd.StringPart
	,sv.Value
FROM
	CommaDelimited cd
	Left Join Common.string s on s.StringCd = cd.stringpart
	Left Join Common.language l on l.IsoLanguageCd = p_LanguageCd
	Left Join Common.stringvalue sv on sv.StringId = s.StringId and sv.LanguageId = l.LanguageId;

DROP TABLE CommaDelimited;

END
;