-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.5.28 - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL version:             7.0.0.4053
-- Date/time:                    2013-02-28 00:58:55
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET FOREIGN_KEY_CHECKS=0 */;

-- Dumping database structure for common
DROP DATABASE IF EXISTS `common`;
CREATE DATABASE IF NOT EXISTS `common` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `common`;


-- Dumping structure for table common.action
DROP TABLE IF EXISTS `action`;
CREATE TABLE IF NOT EXISTS `action` (
  `ActionId` int(11) NOT NULL AUTO_INCREMENT,
  `ActionDescription` varchar(100) NOT NULL,
  `ActionStatusCd` char(1) NOT NULL DEFAULT 'A',
  PRIMARY KEY (`ActionId`),
  UNIQUE KEY `ActionId_UNIQUE` (`ActionId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Dumping data for table common.action: ~2 rows (approximately)
/*!40000 ALTER TABLE `action` DISABLE KEYS */;
INSERT INTO `action` (`ActionId`, `ActionDescription`, `ActionStatusCd`) VALUES
	(1, 'Application Access', 'A'),
	(2, 'Admin Access', 'A');
/*!40000 ALTER TABLE `action` ENABLE KEYS */;


-- Dumping structure for table common.application
DROP TABLE IF EXISTS `application`;
CREATE TABLE IF NOT EXISTS `application` (
  `ApplicationId` int(10) NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) NOT NULL,
  PRIMARY KEY (`ApplicationId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- Dumping data for table common.application: ~1 rows (approximately)
/*!40000 ALTER TABLE `application` DISABLE KEYS */;
INSERT INTO `application` (`ApplicationId`, `Name`) VALUES
	(1, 'Modnaut Demo');
/*!40000 ALTER TABLE `application` ENABLE KEYS */;


-- Dumping structure for table common.language
DROP TABLE IF EXISTS `language`;
CREATE TABLE IF NOT EXISTS `language` (
  `LanguageId` smallint(3) NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  `LocalName` varchar(100) DEFAULT NULL,
  `FlagFileName` varchar(100) DEFAULT NULL,
  `IsoLanguageCd` varchar(2) NOT NULL,
  PRIMARY KEY (`LanguageId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Dumping data for table common.language: ~2 rows (approximately)
/*!40000 ALTER TABLE `language` DISABLE KEYS */;
INSERT INTO `language` (`LanguageId`, `Name`, `LocalName`, `FlagFileName`, `IsoLanguageCd`) VALUES
	(1, 'English', 'English', NULL, 'en'),
	(2, 'Spanish', 'Español', NULL, 'es');
/*!40000 ALTER TABLE `language` ENABLE KEYS */;


-- Dumping structure for table common.session
DROP TABLE IF EXISTS `session`;
CREATE TABLE IF NOT EXISTS `session` (
  `SessionId` bigint(19) NOT NULL,
  `SessionObject` blob NOT NULL,
  `CreatedDate` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`SessionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table common.session: ~2 rows (approximately)
/*!40000 ALTER TABLE `session` DISABLE KEYS */;
INSERT INTO `session` (`SessionId`, `SessionObject`, `CreatedDate`, `LastModifiedDate`) VALUES
	(9829863113365, _binary 0xACED000573720025636F6D2E6D6F646E6175742E636F6D6D6F6E2E73657373696F6E2E57656253657373696F6EC6D379D2ADF1911B0200044A000A73657373696F6E5F6964490007757365725F69644C0005656D61696C7400124C6A6176612F6C616E672F537472696E673B4C00036D61707400134C6A6176612F7574696C2F486173684D61703B7870000008F0B17FC29500000000740000737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000078, '2013-02-27 23:12:29', '2013-02-27 23:12:29'),
	(13749790474425, _binary 0xACED000573720025636F6D2E6D6F646E6175742E636F6D6D6F6E2E73657373696F6E2E57656253657373696F6EC6D379D2ADF1911B0200044A000A73657373696F6E5F6964490007757365725F69644C0005656D61696C7400124C6A6176612F6C616E672F537472696E673B4C00036D61707400134C6A6176612F7574696C2F486173684D61703B787000000C815F6080B900000000740000737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000078, '2013-02-28 00:50:05', '2013-02-28 00:50:05');
/*!40000 ALTER TABLE `session` ENABLE KEYS */;


-- Dumping structure for table common.status
DROP TABLE IF EXISTS `status`;
CREATE TABLE IF NOT EXISTS `status` (
  `StatusCd` char(1) NOT NULL,
  `StatusDescription` varchar(50) NOT NULL,
  PRIMARY KEY (`StatusCd`),
  UNIQUE KEY `StatusCd_UNIQUE` (`StatusCd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table common.status: ~0 rows (approximately)
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
/*!40000 ALTER TABLE `status` ENABLE KEYS */;


-- Dumping structure for table common.string
DROP TABLE IF EXISTS `string`;
CREATE TABLE IF NOT EXISTS `string` (
  `StringId` int(10) NOT NULL,
  `StringCd` varchar(500) NOT NULL,
  PRIMARY KEY (`StringId`),
  UNIQUE KEY `Index 2` (`StringCd`(255))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table common.string: ~2 rows (approximately)
/*!40000 ALTER TABLE `string` DISABLE KEYS */;
INSERT INTO `string` (`StringId`, `StringCd`) VALUES
	(1, 'Hello World'),
	(2, 'Danny was here');
/*!40000 ALTER TABLE `string` ENABLE KEYS */;


-- Dumping structure for table common.stringvalue
DROP TABLE IF EXISTS `stringvalue`;
CREATE TABLE IF NOT EXISTS `stringvalue` (
  `StringId` int(10) NOT NULL,
  `LanguageId` smallint(3) NOT NULL,
  `Value` varchar(5000) NOT NULL,
  KEY `FK__string` (`StringId`),
  KEY `FK__language` (`LanguageId`),
  CONSTRAINT `FK__language` FOREIGN KEY (`LanguageId`) REFERENCES `language` (`LanguageId`),
  CONSTRAINT `FK__string` FOREIGN KEY (`StringId`) REFERENCES `string` (`StringId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table common.stringvalue: ~1 rows (approximately)
/*!40000 ALTER TABLE `stringvalue` DISABLE KEYS */;
INSERT INTO `stringvalue` (`StringId`, `LanguageId`, `Value`) VALUES
	(1, 1, 'Hello World');
/*!40000 ALTER TABLE `stringvalue` ENABLE KEYS */;


-- Dumping structure for table common.useraction
DROP TABLE IF EXISTS `useraction`;
CREATE TABLE IF NOT EXISTS `useraction` (
  `UserId` int(11) NOT NULL,
  `ActionId` int(11) NOT NULL,
  `CreatedByUserId` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`UserId`,`ActionId`),
  KEY `FK_useraction_action` (`ActionId`),
  KEY `FK_useraction_users_2` (`CreatedByUserId`),
  CONSTRAINT `FK_useraction_action` FOREIGN KEY (`ActionId`) REFERENCES `action` (`ActionId`) ON DELETE NO ACTION,
  CONSTRAINT `FK_useraction_users` FOREIGN KEY (`UserId`) REFERENCES `users` (`UserId`) ON DELETE NO ACTION,
  CONSTRAINT `FK_useraction_users_2` FOREIGN KEY (`CreatedByUserId`) REFERENCES `users` (`UserId`) ON DELETE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table common.useraction: ~2 rows (approximately)
/*!40000 ALTER TABLE `useraction` DISABLE KEYS */;
INSERT INTO `useraction` (`UserId`, `ActionId`, `CreatedByUserId`, `CreatedDate`) VALUES
	(1, 1, 1, '2013-02-26 11:12:16'),
	(1, 2, 1, '2013-02-26 11:12:59');
/*!40000 ALTER TABLE `useraction` ENABLE KEYS */;


-- Dumping structure for table common.users
DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `UserId` int(10) NOT NULL AUTO_INCREMENT,
  `UserName` varchar(50) NOT NULL,
  `FirstName` varchar(50) NOT NULL,
  `LastName` varchar(50) NOT NULL,
  `EmailAddress` varchar(100) NOT NULL,
  `UserPassword` varchar(100) NOT NULL,
  `HireDate` datetime NOT NULL,
  `UserTypeCd` char(1) NOT NULL DEFAULT 'C',
  `UserStatusCd` char(1) NOT NULL DEFAULT 'A',
  PRIMARY KEY (`UserId`),
  UNIQUE KEY `EmailAddress` (`EmailAddress`),
  UNIQUE KEY `UserName` (`UserName`),
  KEY `FK_users_usertype` (`UserTypeCd`),
  CONSTRAINT `FK_users_usertype` FOREIGN KEY (`UserTypeCd`) REFERENCES `usertype` (`UserTypeCd`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- Dumping data for table common.users: ~3 rows (approximately)
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`UserId`, `UserName`, `FirstName`, `LastName`, `EmailAddress`, `UserPassword`, `HireDate`, `UserTypeCd`, `UserStatusCd`) VALUES
	(1, 'jlamarche11', 'Jamie', 'LaMarche', 'jlamarche@modnaut.com', 'kLxNpX+0w9lWcamR3wSZ8O/828A=', '2013-02-05 18:34:51', 'C', 'A'),
	(2, 'dcohn33', 'Danny', 'Cohn', 'dcohn@modnaut.com', 't85j1nK7VVxAv3QtmvSHldXtCKE=', '2013-02-05 18:34:51', 'C', 'A'),
	(3, 'bdalgaard22', 'Ben', 'Dalgaard', 'bdalgaard@modnaut.com', 'kLxNpX+0w9lWcamR3wSZ8O/828A=', '2013-02-05 18:34:51', 'C', 'A');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;


-- Dumping structure for table common.usersession
DROP TABLE IF EXISTS `usersession`;
CREATE TABLE IF NOT EXISTS `usersession` (
  `UserId` int(10) NOT NULL,
  `SessionId` bigint(19) NOT NULL,
  KEY `FK__user` (`UserId`),
  KEY `FK__session` (`SessionId`),
  CONSTRAINT `FK__session` FOREIGN KEY (`SessionId`) REFERENCES `session` (`SessionId`),
  CONSTRAINT `FK__user` FOREIGN KEY (`UserId`) REFERENCES `users` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table common.usersession: ~0 rows (approximately)
/*!40000 ALTER TABLE `usersession` DISABLE KEYS */;
/*!40000 ALTER TABLE `usersession` ENABLE KEYS */;


-- Dumping structure for table common.usertype
DROP TABLE IF EXISTS `usertype`;
CREATE TABLE IF NOT EXISTS `usertype` (
  `UserTypeCd` char(1) NOT NULL,
  `Description` varchar(50) NOT NULL,
  PRIMARY KEY (`UserTypeCd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table common.usertype: ~2 rows (approximately)
/*!40000 ALTER TABLE `usertype` DISABLE KEYS */;
INSERT INTO `usertype` (`UserTypeCd`, `Description`) VALUES
	('A', 'Administrator'),
	('C', 'Customer');
/*!40000 ALTER TABLE `usertype` ENABLE KEYS */;
/*!40014 SET FOREIGN_KEY_CHECKS=1 */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
