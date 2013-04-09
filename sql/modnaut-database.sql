-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.6.10-log - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL version:             7.0.0.4053
-- Date/time:                    2013-04-09 00:51:47
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
  `ActionId` int(10) NOT NULL AUTO_INCREMENT,
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

-- Dumping data for table common.application: ~0 rows (approximately)
/*!40000 ALTER TABLE `application` DISABLE KEYS */;
INSERT INTO `application` (`ApplicationId`, `Name`) VALUES
	(1, 'Modnaut Demo');
/*!40000 ALTER TABLE `application` ENABLE KEYS */;


-- Dumping structure for table common.attribute
DROP TABLE IF EXISTS `attribute`;
CREATE TABLE IF NOT EXISTS `attribute` (
  `AttributeId` int(10) NOT NULL AUTO_INCREMENT,
  `AttributeName` varchar(255) NOT NULL,
  `AttributeTypeId` int(10) NOT NULL,
  PRIMARY KEY (`AttributeId`),
  KEY `FK_attribute_attributetype` (`AttributeTypeId`),
  CONSTRAINT `FK_attribute_attributetype` FOREIGN KEY (`AttributeTypeId`) REFERENCES `attributetype` (`AttributeTypeId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- Dumping data for table common.attribute: ~0 rows (approximately)
/*!40000 ALTER TABLE `attribute` DISABLE KEYS */;
INSERT INTO `attribute` (`AttributeId`, `AttributeName`, `AttributeTypeId`) VALUES
	(1, 'CacheXsl', 1);
/*!40000 ALTER TABLE `attribute` ENABLE KEYS */;


-- Dumping structure for table common.attributetype
DROP TABLE IF EXISTS `attributetype`;
CREATE TABLE IF NOT EXISTS `attributetype` (
  `AttributeTypeId` int(10) NOT NULL AUTO_INCREMENT,
  `Description` varchar(50) NOT NULL,
  PRIMARY KEY (`AttributeTypeId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- Dumping data for table common.attributetype: ~5 rows (approximately)
/*!40000 ALTER TABLE `attributetype` DISABLE KEYS */;
INSERT INTO `attributetype` (`AttributeTypeId`, `Description`) VALUES
	(1, 'Text'),
	(2, 'Integer'),
	(3, 'Float'),
	(4, 'List'),
	(5, 'Date');
/*!40000 ALTER TABLE `attributetype` ENABLE KEYS */;


-- Dumping structure for table common.language
DROP TABLE IF EXISTS `language`;
CREATE TABLE IF NOT EXISTS `language` (
  `LanguageId` smallint(3) NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  `LocalName` varchar(100) DEFAULT NULL,
  `FlagFileName` varchar(100) DEFAULT NULL,
  `IsoLanguageCd` varchar(2) NOT NULL,
  PRIMARY KEY (`LanguageId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- Dumping data for table common.language: ~3 rows (approximately)
/*!40000 ALTER TABLE `language` DISABLE KEYS */;
INSERT INTO `language` (`LanguageId`, `Name`, `LocalName`, `FlagFileName`, `IsoLanguageCd`) VALUES
	(1, 'English', 'English', NULL, 'en'),
	(2, 'Spanish', 'Español', NULL, 'es'),
	(3, 'Hebrew', 'עברית', NULL, 'he');
/*!40000 ALTER TABLE `language` ENABLE KEYS */;


-- Dumping structure for table common.securitygroup
DROP TABLE IF EXISTS `securitygroup`;
CREATE TABLE IF NOT EXISTS `securitygroup` (
  `SecurityGroupId` int(10) NOT NULL AUTO_INCREMENT,
  `Description` varchar(50) NOT NULL,
  PRIMARY KEY (`SecurityGroupId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- Dumping data for table common.securitygroup: ~0 rows (approximately)
/*!40000 ALTER TABLE `securitygroup` DISABLE KEYS */;
INSERT INTO `securitygroup` (`SecurityGroupId`, `Description`) VALUES
	(1, 'TestGroup');
/*!40000 ALTER TABLE `securitygroup` ENABLE KEYS */;


-- Dumping structure for table common.securitygroupaction
DROP TABLE IF EXISTS `securitygroupaction`;
CREATE TABLE IF NOT EXISTS `securitygroupaction` (
  `SecurityGroupId` int(10) NOT NULL,
  `ActionId` int(10) NOT NULL,
  PRIMARY KEY (`SecurityGroupId`,`ActionId`),
  KEY `FK_SecurityGroupAction_ActionId` (`ActionId`),
  CONSTRAINT `FK_SecurityGroupAction_SecurityGroupId` FOREIGN KEY (`SecurityGroupId`) REFERENCES `securitygroup` (`SecurityGroupId`),
  CONSTRAINT `FK_SecurityGroupAction_ActionId` FOREIGN KEY (`ActionId`) REFERENCES `action` (`ActionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table common.securitygroupaction: ~2 rows (approximately)
/*!40000 ALTER TABLE `securitygroupaction` DISABLE KEYS */;
INSERT INTO `securitygroupaction` (`SecurityGroupId`, `ActionId`) VALUES
	(1, 1),
	(1, 2);
/*!40000 ALTER TABLE `securitygroupaction` ENABLE KEYS */;


-- Dumping structure for table common.server
DROP TABLE IF EXISTS `server`;
CREATE TABLE IF NOT EXISTS `server` (
  `ServerId` int(10) NOT NULL AUTO_INCREMENT,
  `ServerName` varchar(200) NOT NULL,
  `ServerDescription` varchar(200) NOT NULL,
  PRIMARY KEY (`ServerId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- Dumping data for table common.server: ~0 rows (approximately)
/*!40000 ALTER TABLE `server` DISABLE KEYS */;
INSERT INTO `server` (`ServerId`, `ServerName`, `ServerDescription`) VALUES
	(1, 'localhost', 'Local development PC');
/*!40000 ALTER TABLE `server` ENABLE KEYS */;


-- Dumping structure for table common.serverattributevalue
DROP TABLE IF EXISTS `serverattributevalue`;
CREATE TABLE IF NOT EXISTS `serverattributevalue` (
  `ServerId` int(10) NOT NULL,
  `AttributeId` int(10) NOT NULL,
  `AttributeValue` varchar(2000) NOT NULL,
  KEY `FK_ServerAttributeValue_server` (`ServerId`),
  KEY `FK_ServerAttributeValue_attribute` (`AttributeId`),
  CONSTRAINT `FK_ServerAttributeValue_attribute` FOREIGN KEY (`AttributeId`) REFERENCES `attribute` (`AttributeId`),
  CONSTRAINT `FK_ServerAttributeValue_server` FOREIGN KEY (`ServerId`) REFERENCES `server` (`ServerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table common.serverattributevalue: ~0 rows (approximately)
/*!40000 ALTER TABLE `serverattributevalue` DISABLE KEYS */;
INSERT INTO `serverattributevalue` (`ServerId`, `AttributeId`, `AttributeValue`) VALUES
	(1, 1, 'N');
/*!40000 ALTER TABLE `serverattributevalue` ENABLE KEYS */;


-- Dumping structure for table common.session
DROP TABLE IF EXISTS `session`;
CREATE TABLE IF NOT EXISTS `session` (
  `SessionId` bigint(19) NOT NULL,
  `SessionObject` blob NOT NULL,
  `CreatedDate` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`SessionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table common.session: ~0 rows (approximately)
/*!40000 ALTER TABLE `session` DISABLE KEYS */;
/*!40000 ALTER TABLE `session` ENABLE KEYS */;


-- Dumping structure for table common.status
DROP TABLE IF EXISTS `status`;
CREATE TABLE IF NOT EXISTS `status` (
  `StatusCd` char(1) NOT NULL,
  `StatusDescription` varchar(50) NOT NULL,
  PRIMARY KEY (`StatusCd`),
  UNIQUE KEY `StatusCd_UNIQUE` (`StatusCd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table common.status: ~2 rows (approximately)
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` (`StatusCd`, `StatusDescription`) VALUES
	('A', 'Active'),
	('D', 'Disabled');
/*!40000 ALTER TABLE `status` ENABLE KEYS */;


-- Dumping structure for table common.string
DROP TABLE IF EXISTS `string`;
CREATE TABLE IF NOT EXISTS `string` (
  `StringId` int(10) NOT NULL AUTO_INCREMENT,
  `StringCd` varchar(500) NOT NULL,
  PRIMARY KEY (`StringId`),
  UNIQUE KEY `Index 2` (`StringCd`(255))
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- Dumping data for table common.string: ~5 rows (approximately)
/*!40000 ALTER TABLE `string` DISABLE KEYS */;
INSERT INTO `string` (`StringId`, `StringCd`) VALUES
	(1, 'Name'),
	(2, 'Address'),
	(3, 'City'),
	(4, 'State'),
	(5, 'Zip Code');
/*!40000 ALTER TABLE `string` ENABLE KEYS */;


-- Dumping structure for table common.stringvalue
DROP TABLE IF EXISTS `stringvalue`;
CREATE TABLE IF NOT EXISTS `stringvalue` (
  `StringId` int(10) NOT NULL,
  `LanguageId` smallint(3) NOT NULL,
  `Value` varchar(5000) NOT NULL,
  KEY `FK__string` (`StringId`),
  KEY `FK__language` (`LanguageId`),
  CONSTRAINT `FK_stringvalue_string` FOREIGN KEY (`StringId`) REFERENCES `string` (`StringId`),
  CONSTRAINT `FK__language` FOREIGN KEY (`LanguageId`) REFERENCES `language` (`LanguageId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table common.stringvalue: ~10 rows (approximately)
/*!40000 ALTER TABLE `stringvalue` DISABLE KEYS */;
INSERT INTO `stringvalue` (`StringId`, `LanguageId`, `Value`) VALUES
	(1, 2, 'nombre '),
	(2, 2, 'dirección '),
	(3, 2, 'ciudad'),
	(4, 2, 'estado'),
	(5, 2, 'código postal'),
	(1, 3, 'שם'),
	(2, 3, 'כתובת'),
	(3, 3, 'עיר'),
	(4, 3, 'מדינה'),
	(5, 3, 'מיקוד');
/*!40000 ALTER TABLE `stringvalue` ENABLE KEYS */;


-- Dumping structure for table common.useraction
DROP TABLE IF EXISTS `useraction`;
CREATE TABLE IF NOT EXISTS `useraction` (
  `UserId` int(10) NOT NULL,
  `ActionId` int(10) NOT NULL,
  `CreatedByUserId` int(10) NOT NULL DEFAULT '0',
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`UserId`,`ActionId`),
  KEY `FK_UserAction_ActionId` (`ActionId`),
  KEY `FK_UserAction_CreatedByUserId` (`CreatedByUserId`),
  CONSTRAINT `FK_UserAction_ActionId` FOREIGN KEY (`ActionId`) REFERENCES `action` (`ActionId`) ON DELETE NO ACTION,
  CONSTRAINT `FK_UserAction_UserId` FOREIGN KEY (`UserId`) REFERENCES `user` (`UserId`) ON DELETE NO ACTION,
  CONSTRAINT `FK_UserAction_CreatedByUserId` FOREIGN KEY (`CreatedByUserId`) REFERENCES `user` (`UserId`) ON DELETE NO ACTION
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
  `InvalidLoginAttempts` int(2) DEFAULT NULL,
  PRIMARY KEY (`UserId`),
  UNIQUE KEY `EmailAddress` (`EmailAddress`),
  UNIQUE KEY `UserName` (`UserName`),
  KEY `FK_users_usertype` (`UserTypeCd`),
  CONSTRAINT `FK_users_usertype` FOREIGN KEY (`UserTypeCd`) REFERENCES `usertype` (`UserTypeCd`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- Dumping data for table common.users: ~4 rows (approximately)
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`UserId`, `UserName`, `FirstName`, `LastName`, `EmailAddress`, `UserPassword`, `HireDate`, `UserTypeCd`, `UserStatusCd`, `InvalidLoginAttempts`) VALUES
	(1, 'guest', 'guest', 'guest', '', 'kLxNpX+0w9lWcamR3wSZ8O/828A=', '2000-01-01 00:00:00', 'C', 'A', 0),
	(2, 'jlamarche11', 'Jamie', 'LaMarche', 'jlamarche@modnaut.com', 'kLxNpX+0w9lWcamR3wSZ8O/828A=', '2013-02-05 18:34:51', 'C', 'A', 0),
	(3, 'dcohn33', 'Danny', 'Cohn', 'dcohn@modnaut.com', 'kLxNpX+0w9lWcamR3wSZ8O/828A=', '2013-02-05 18:34:51', 'C', 'A', 0),
	(4, 'bdalgaard22', 'Ben', 'Dalgaardfasdfsa', 'bdalgaard@modnaut.com', 'kLxNpX+0w9lWcamR3wSZ8O/828A=', '2013-02-05 18:34:51', 'C', 'A', 0);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;


-- Dumping structure for table common.usersecuritygroup
DROP TABLE IF EXISTS `usersecuritygroup`;
CREATE TABLE IF NOT EXISTS `usersecuritygroup` (
  `UserId` int(10) NOT NULL,
  `SecurityGroupId` int(10) NOT NULL,
  `CreatedByUserId` int(10) NOT NULL DEFAULT '4',
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`UserId`,`SecurityGroupId`),
  KEY `FK_UserSecurityGroup_SecurityGroupId` (`SecurityGroupId`),
  KEY `FK_UserSecurityGroup_CreatedByUserId` (`CreatedByUserId`),
  CONSTRAINT `FK_UserSecurityGroup_UserId` FOREIGN KEY (`UserId`) REFERENCES `users` (`UserId`),
  CONSTRAINT `FK_UserSecurityGroup_SecurityGroupId` FOREIGN KEY (`SecurityGroupId`) REFERENCES `securitygroup` (`SecurityGroupId`),
  CONSTRAINT `FK_UserSecurityGroup_CreatedByUserId` FOREIGN KEY (`CreatedByUserId`) REFERENCES `users` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table common.usersecuritygroup: ~0 rows (approximately)
/*!40000 ALTER TABLE `usersecuritygroup` DISABLE KEYS */;
INSERT INTO `usersecuritygroup` (`UserId`, `SecurityGroupId`, `CreatedByUserId`, `CreatedDate`) VALUES
	(3, 1, 3, '2013-04-08 22:24:18');
/*!40000 ALTER TABLE `usersecuritygroup` ENABLE KEYS */;


-- Dumping structure for table common.usersession
DROP TABLE IF EXISTS `usersession`;
CREATE TABLE IF NOT EXISTS `usersession` (
  `UserId` int(10) NOT NULL DEFAULT '1',
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
