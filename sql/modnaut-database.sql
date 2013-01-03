-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.5.28 - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL version:             7.0.0.4053
-- Date/time:                    2013-01-03 00:54:23
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET FOREIGN_KEY_CHECKS=0 */;

-- Dumping database structure for common
DROP DATABASE IF EXISTS `common`;
CREATE DATABASE IF NOT EXISTS `common` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `common`;


-- Dumping structure for table common.application
DROP TABLE IF EXISTS `application`;
CREATE TABLE IF NOT EXISTS `application` (
  `ApplicationID` int(10) NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) NOT NULL,
  PRIMARY KEY (`ApplicationID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- Dumping data for table common.application: ~1 rows (approximately)
/*!40000 ALTER TABLE `application` DISABLE KEYS */;
INSERT INTO `application` (`ApplicationID`, `Name`) VALUES
	(1, 'Modnaut Demo');
/*!40000 ALTER TABLE `application` ENABLE KEYS */;


-- Dumping structure for procedure common.GetAllUsersAlphabetically
DROP PROCEDURE IF EXISTS `GetAllUsersAlphabetically`;
DELIMITER //
CREATE DEFINER=`modnaut00`@`%` PROCEDURE `GetAllUsersAlphabetically`()
BEGIN 
SELECT * FROM Common.users ORDER BY LastName; 
END//
DELIMITER ;


-- Dumping structure for table common.language
DROP TABLE IF EXISTS `language`;
CREATE TABLE IF NOT EXISTS `language` (
  `LanguageID` smallint(3) NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  `LocalName` varchar(100) DEFAULT NULL,
  `FlagFileName` varchar(100) DEFAULT NULL,
  `IsoLanguageCD` varchar(2) NOT NULL,
  PRIMARY KEY (`LanguageID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Dumping data for table common.language: ~2 rows (approximately)
/*!40000 ALTER TABLE `language` DISABLE KEYS */;
INSERT INTO `language` (`LanguageID`, `Name`, `LocalName`, `FlagFileName`, `IsoLanguageCD`) VALUES
	(1, 'English', 'English', NULL, 'en'),
	(2, 'Spanish', 'Español', NULL, 'es');
/*!40000 ALTER TABLE `language` ENABLE KEYS */;


-- Dumping structure for table common.string
DROP TABLE IF EXISTS `string`;
CREATE TABLE IF NOT EXISTS `string` (
  `StringID` int(10) NOT NULL,
  `StringCD` varchar(500) NOT NULL,
  PRIMARY KEY (`StringID`),
  UNIQUE KEY `Index 2` (`StringCD`(255))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table common.string: ~2 rows (approximately)
/*!40000 ALTER TABLE `string` DISABLE KEYS */;
INSERT INTO `string` (`StringID`, `StringCD`) VALUES
	(1, 'Hello World'),
	(2, 'Danny was here');
/*!40000 ALTER TABLE `string` ENABLE KEYS */;


-- Dumping structure for table common.stringvalue
DROP TABLE IF EXISTS `stringvalue`;
CREATE TABLE IF NOT EXISTS `stringvalue` (
  `StringID` int(10) NOT NULL,
  `LanguageID` smallint(3) NOT NULL,
  `Value` varchar(5000) NOT NULL,
  KEY `FK__string` (`StringID`),
  KEY `FK__language` (`LanguageID`),
  CONSTRAINT `FK__language` FOREIGN KEY (`languageID`) REFERENCES `language` (`languageID`),
  CONSTRAINT `FK__string` FOREIGN KEY (`stringID`) REFERENCES `string` (`StringID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table common.stringvalue: ~1 rows (approximately)
/*!40000 ALTER TABLE `stringvalue` DISABLE KEYS */;
INSERT INTO `stringvalue` (`StringID`, `LanguageID`, `Value`) VALUES
	(1, 1, 'Hello World');
/*!40000 ALTER TABLE `stringvalue` ENABLE KEYS */;


-- Dumping structure for table common.users
DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `UserId` int(10) NOT NULL AUTO_INCREMENT,
  `UserName` varchar(50) NOT NULL,
  `FirstName` varchar(50) NOT NULL,
  `LastName` varchar(50) NOT NULL,
  `EmailAddress` varchar(100) NOT NULL,
  PRIMARY KEY (`UserId`),
  UNIQUE KEY `EmailAddress` (`EmailAddress`),
  UNIQUE KEY `UserName` (`UserName`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- Dumping data for table common.users: ~3 rows (approximately)
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`UserId`, `UserName`, `FirstName`, `LastName`, `EmailAddress`) VALUES
	(1, 'jlamarche11', 'Jamie', 'LaMarche', 'jlamarche@modnaut.com'),
	(2, 'dcohn33', 'Danny', 'Cohn', 'dcohn@modnaut.com'),
	(3, 'bdalgaard22', 'Ben', 'Dalgaard', 'bdalgaard@modnaut.com');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
/*!40014 SET FOREIGN_KEY_CHECKS=1 */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
