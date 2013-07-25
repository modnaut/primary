-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.6.12-log - MySQL Community Server (GPL)
-- Server OS:                    Win64
-- HeidiSQL Version:             8.0.0.4396
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Dumping database structure for common
DROP DATABASE IF EXISTS `common`;
CREATE DATABASE IF NOT EXISTS `common` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `common`;


-- Dumping structure for table common.address
DROP TABLE IF EXISTS `address`;
CREATE TABLE IF NOT EXISTS `address` (
  `AddressId` int(10) NOT NULL AUTO_INCREMENT,
  `Address1` varchar(500) DEFAULT NULL,
  `Address2` varchar(500) DEFAULT NULL,
  `CityId` int(11) DEFAULT NULL,
  `ZipCode` varchar(15) DEFAULT NULL,
  `Location` point DEFAULT NULL,
  `CreatedById` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedById` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`AddressId`),
  KEY `FK_address_city` (`CityId`),
  CONSTRAINT `FK_address_city` FOREIGN KEY (`CityId`) REFERENCES `city` (`CityId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Dumping data for table common.address: ~2 rows (approximately)
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address` (`AddressId`, `Address1`, `Address2`, `CityId`, `ZipCode`, `Location`, `CreatedById`, `CreatedDate`, `LastModifiedById`, `LastModifiedDate`) VALUES
	(1, '201 Market Street', NULL, 1, '23462', _binary 0x000000000101000000C399092FC381C2A90853C38019C3BF3EC3A3C3826B4240, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(2, '5960 Stewart Parkway', NULL, 2, '30135', _binary 0x0000000001010000003E7958C2A8353155C380C38E1951C39A1BC39C4040, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00');
/*!40000 ALTER TABLE `address` ENABLE KEYS */;


-- Dumping structure for table common.application
DROP TABLE IF EXISTS `application`;
CREATE TABLE IF NOT EXISTS `application` (
  `ApplicationId` int(10) NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) NOT NULL,
  `ApplicationFolder` varchar(500) DEFAULT NULL,
  `PageTitle` varchar(500) DEFAULT NULL,
  `Class` varchar(500) DEFAULT NULL,
  `CreatedById` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedById` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ApplicationId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- Dumping data for table common.application: ~1 rows (approximately)
/*!40000 ALTER TABLE `application` DISABLE KEYS */;
INSERT INTO `application` (`ApplicationId`, `Name`, `ApplicationFolder`, `PageTitle`, `Class`, `CreatedById`, `CreatedDate`, `LastModifiedById`, `LastModifiedDate`) VALUES
	(1, 'LocalFoodConnection Backend', 'localFoodConnection', 'Local Food Connection', 'com.modnaut.apps.localfoodconnection.ApplicationCtrl', 0, '0000-00-00 00:00:00', 0, '2013-07-24 22:21:57');
/*!40000 ALTER TABLE `application` ENABLE KEYS */;


-- Dumping structure for table common.attribute
DROP TABLE IF EXISTS `attribute`;
CREATE TABLE IF NOT EXISTS `attribute` (
  `AttributeId` int(10) NOT NULL AUTO_INCREMENT,
  `AttributeName` varchar(255) NOT NULL,
  `AttributeTypeId` int(10) NOT NULL,
  `CreatedById` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedById` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`AttributeId`),
  KEY `FK_attribute_attributetype` (`AttributeTypeId`),
  CONSTRAINT `FK_attribute_attributetype` FOREIGN KEY (`AttributeTypeId`) REFERENCES `attributetype` (`AttributeTypeId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Dumping data for table common.attribute: ~2 rows (approximately)
/*!40000 ALTER TABLE `attribute` DISABLE KEYS */;
INSERT INTO `attribute` (`AttributeId`, `AttributeName`, `AttributeTypeId`, `CreatedById`, `CreatedDate`, `LastModifiedById`, `LastModifiedDate`) VALUES
	(1, 'CACHE_XSL', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(2, 'APPLICATION_ID', 2, 0, '2013-07-24 21:28:52', 0, '2013-07-24 21:28:53');
/*!40000 ALTER TABLE `attribute` ENABLE KEYS */;


-- Dumping structure for table common.attributetype
DROP TABLE IF EXISTS `attributetype`;
CREATE TABLE IF NOT EXISTS `attributetype` (
  `AttributeTypeId` int(10) NOT NULL AUTO_INCREMENT,
  `Description` varchar(50) NOT NULL,
  `CreatedById` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedById` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`AttributeTypeId`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- Dumping data for table common.attributetype: ~4 rows (approximately)
/*!40000 ALTER TABLE `attributetype` DISABLE KEYS */;
INSERT INTO `attributetype` (`AttributeTypeId`, `Description`, `CreatedById`, `CreatedDate`, `LastModifiedById`, `LastModifiedDate`) VALUES
	(1, 'Text', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(2, 'Integer', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(3, 'Float', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(4, 'Date', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00');
/*!40000 ALTER TABLE `attributetype` ENABLE KEYS */;


-- Dumping structure for table common.city
DROP TABLE IF EXISTS `city`;
CREATE TABLE IF NOT EXISTS `city` (
  `CityId` int(10) NOT NULL AUTO_INCREMENT,
  `Name` varchar(500) NOT NULL,
  `StateId` int(11) NOT NULL,
  `CreatedById` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedById` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`CityId`),
  KEY `FK_City_state` (`StateId`),
  CONSTRAINT `FK_City_state` FOREIGN KEY (`StateId`) REFERENCES `state` (`StateId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Dumping data for table common.city: ~2 rows (approximately)
/*!40000 ALTER TABLE `city` DISABLE KEYS */;
INSERT INTO `city` (`CityId`, `Name`, `StateId`, `CreatedById`, `CreatedDate`, `LastModifiedById`, `LastModifiedDate`) VALUES
	(1, 'Virginia Beach', 46, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(2, 'Douglasville', 10, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00');
/*!40000 ALTER TABLE `city` ENABLE KEYS */;


-- Dumping structure for table common.clan
DROP TABLE IF EXISTS `clan`;
CREATE TABLE IF NOT EXISTS `clan` (
  `ClanId` int(10) NOT NULL AUTO_INCREMENT,
  `ClanDescription` varchar(50) NOT NULL,
  `CreatedById` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedById` varchar(45) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ClanId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- Dumping data for table common.clan: ~1 rows (approximately)
/*!40000 ALTER TABLE `clan` DISABLE KEYS */;
INSERT INTO `clan` (`ClanId`, `ClanDescription`, `CreatedById`, `CreatedDate`, `LastModifiedById`, `LastModifiedDate`) VALUES
	(1, 'TestGroup', 0, '0000-00-00 00:00:00', '', '0000-00-00 00:00:00');
/*!40000 ALTER TABLE `clan` ENABLE KEYS */;


-- Dumping structure for table common.clanpower
DROP TABLE IF EXISTS `clanpower`;
CREATE TABLE IF NOT EXISTS `clanpower` (
  `ClanId` int(10) NOT NULL,
  `PowerId` int(10) NOT NULL,
  `CreatedById` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedById` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ClanId`,`PowerId`),
  KEY `FK_ClanPower_power` (`PowerId`),
  CONSTRAINT `FK_ClanPower_clan` FOREIGN KEY (`ClanId`) REFERENCES `clan` (`ClanId`),
  CONSTRAINT `FK_ClanPower_power` FOREIGN KEY (`PowerId`) REFERENCES `power` (`PowerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table common.clanpower: ~2 rows (approximately)
/*!40000 ALTER TABLE `clanpower` DISABLE KEYS */;
INSERT INTO `clanpower` (`ClanId`, `PowerId`, `CreatedById`, `CreatedDate`, `LastModifiedById`, `LastModifiedDate`) VALUES
	(1, 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(1, 2, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00');
/*!40000 ALTER TABLE `clanpower` ENABLE KEYS */;


-- Dumping structure for table common.country
DROP TABLE IF EXISTS `country`;
CREATE TABLE IF NOT EXISTS `country` (
  `CountryId` int(10) NOT NULL DEFAULT '0',
  `Name` varchar(500) NOT NULL,
  `Abbreviation` varchar(50) NOT NULL,
  `DefaultLanguageId` smallint(11) NOT NULL,
  `CreatedById` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedById` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`CountryId`),
  KEY `FK_country_language` (`DefaultLanguageId`),
  CONSTRAINT `FK_country_language` FOREIGN KEY (`DefaultLanguageId`) REFERENCES `language` (`LanguageId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table common.country: ~1 rows (approximately)
/*!40000 ALTER TABLE `country` DISABLE KEYS */;
INSERT INTO `country` (`CountryId`, `Name`, `Abbreviation`, `DefaultLanguageId`, `CreatedById`, `CreatedDate`, `LastModifiedById`, `LastModifiedDate`) VALUES
	(1, 'United States of America', 'USA', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00');
/*!40000 ALTER TABLE `country` ENABLE KEYS */;


-- Dumping structure for table common.entityattributevalue
DROP TABLE IF EXISTS `entityattributevalue`;
CREATE TABLE IF NOT EXISTS `entityattributevalue` (
  `EntityAttributeValueId` int(10) NOT NULL AUTO_INCREMENT,
  `EntityTypeId` int(10) NOT NULL,
  `EntityId` int(10) NOT NULL,
  `AttributeId` int(10) NOT NULL,
  `AttributeValue` varchar(20000) DEFAULT NULL,
  `CreatedById` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedById` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`EntityAttributeValueId`),
  UNIQUE KEY `Index 4` (`EntityId`,`AttributeId`,`EntityTypeId`),
  KEY `FK_EntityAttributeValue_entitytype` (`EntityTypeId`),
  KEY `FK_EntityAttributeValue_attribute` (`AttributeId`),
  CONSTRAINT `FK_EntityAttributeValue_attribute` FOREIGN KEY (`AttributeId`) REFERENCES `attribute` (`AttributeId`),
  CONSTRAINT `FK_EntityAttributeValue_entitytype` FOREIGN KEY (`EntityTypeId`) REFERENCES `entitytype` (`EntityTypeId`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- Dumping data for table common.entityattributevalue: ~3 rows (approximately)
/*!40000 ALTER TABLE `entityattributevalue` DISABLE KEYS */;
INSERT INTO `entityattributevalue` (`EntityAttributeValueId`, `EntityTypeId`, `EntityId`, `AttributeId`, `AttributeValue`, `CreatedById`, `CreatedDate`, `LastModifiedById`, `LastModifiedDate`) VALUES
	(1, 1, 1, 1, 'Y', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(5, 1, 2, 1, 'N', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(6, 1, 1, 2, '1', 0, '2013-07-24 21:29:34', 0, '2013-07-24 21:29:35');
/*!40000 ALTER TABLE `entityattributevalue` ENABLE KEYS */;


-- Dumping structure for table common.entitytype
DROP TABLE IF EXISTS `entitytype`;
CREATE TABLE IF NOT EXISTS `entitytype` (
  `EntityTypeId` int(10) NOT NULL AUTO_INCREMENT,
  `EntityTypeName` varchar(50) NOT NULL,
  `Table` varchar(255) NOT NULL,
  `CreatedById` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedById` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`EntityTypeId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- Dumping data for table common.entitytype: ~1 rows (approximately)
/*!40000 ALTER TABLE `entitytype` DISABLE KEYS */;
INSERT INTO `entitytype` (`EntityTypeId`, `EntityTypeName`, `Table`, `CreatedById`, `CreatedDate`, `LastModifiedById`, `LastModifiedDate`) VALUES
	(1, 'Environment', 'Common.Environment', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00');
/*!40000 ALTER TABLE `entitytype` ENABLE KEYS */;


-- Dumping structure for table common.environment
DROP TABLE IF EXISTS `environment`;
CREATE TABLE IF NOT EXISTS `environment` (
  `EnvironmentId` int(10) NOT NULL AUTO_INCREMENT,
  `ParentEnvironmentId` int(10) DEFAULT NULL,
  `EnvironmentName` varchar(200) NOT NULL,
  `EnvironmentDescription` varchar(200) NOT NULL,
  `CreatedById` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedById` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`EnvironmentId`),
  KEY `FK_environment_environment` (`ParentEnvironmentId`),
  CONSTRAINT `FK_environment_environment` FOREIGN KEY (`ParentEnvironmentId`) REFERENCES `environment` (`EnvironmentId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Dumping data for table common.environment: ~2 rows (approximately)
/*!40000 ALTER TABLE `environment` DISABLE KEYS */;
INSERT INTO `environment` (`EnvironmentId`, `ParentEnvironmentId`, `EnvironmentName`, `EnvironmentDescription`, `CreatedById`, `CreatedDate`, `LastModifiedById`, `LastModifiedDate`) VALUES
	(1, NULL, 'localhost', 'Local development PC', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(2, 1, 'localhost-web', 'Local development PC running web server', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00');
/*!40000 ALTER TABLE `environment` ENABLE KEYS */;


-- Dumping structure for table common.hashpath
DROP TABLE IF EXISTS `hashpath`;
CREATE TABLE IF NOT EXISTS `hashpath` (
  `HashPathId` int(10) NOT NULL AUTO_INCREMENT,
  `HashPath` varchar(5000) NOT NULL,
  `Class` varchar(200) NOT NULL,
  `Method` varchar(200) NOT NULL,
  `CreatedById` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedById` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`HashPathId`),
  FULLTEXT KEY `Index 2` (`HashPath`,`Class`,`Method`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Dumping data for table common.hashpath: 2 rows
/*!40000 ALTER TABLE `hashpath` DISABLE KEYS */;
INSERT INTO `hashpath` (`HashPathId`, `HashPath`, `Class`, `Method`, `CreatedById`, `CreatedDate`, `LastModifiedById`, `LastModifiedDate`) VALUES
	(1, 'Market/List', 'com.modnaut.apps.farmarkets.MarketListCtrl', 'defaultAction', 0, '2013-07-02 06:55:53', 0, '2013-07-02 06:56:17'),
	(2, 'Market/Detail', 'com.modnaut.apps.farmarkets.MarketDetailCtrl', 'defaultAction', 0, '2013-07-02 06:55:53', 0, '2013-07-02 06:56:17');
/*!40000 ALTER TABLE `hashpath` ENABLE KEYS */;


-- Dumping structure for table common.language
DROP TABLE IF EXISTS `language`;
CREATE TABLE IF NOT EXISTS `language` (
  `LanguageId` smallint(3) NOT NULL AUTO_INCREMENT,
  `Name` varchar(100) NOT NULL,
  `LocalName` varchar(100) DEFAULT NULL,
  `FlagFileName` varchar(100) DEFAULT NULL,
  `IsoLanguageCd` varchar(2) NOT NULL,
  `CreatedById` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedById` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`LanguageId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- Dumping data for table common.language: ~3 rows (approximately)
/*!40000 ALTER TABLE `language` DISABLE KEYS */;
INSERT INTO `language` (`LanguageId`, `Name`, `LocalName`, `FlagFileName`, `IsoLanguageCd`, `CreatedById`, `CreatedDate`, `LastModifiedById`, `LastModifiedDate`) VALUES
	(1, 'English', 'English', NULL, 'en', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(2, 'Spanish', 'Espa', NULL, 'es', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(3, 'Hebrew', '?????', NULL, 'he', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00');
/*!40000 ALTER TABLE `language` ENABLE KEYS */;


-- Dumping structure for table common.menu
DROP TABLE IF EXISTS `menu`;
CREATE TABLE IF NOT EXISTS `menu` (
  `MenuId` int(10) NOT NULL AUTO_INCREMENT,
  `ApplicationId` int(10) NOT NULL,
  `MenuDescription` varchar(250) NOT NULL,
  PRIMARY KEY (`MenuId`),
  KEY `FK_Menu_application` (`ApplicationId`),
  CONSTRAINT `FK_Menu_application` FOREIGN KEY (`ApplicationId`) REFERENCES `application` (`ApplicationId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- Dumping data for table common.menu: ~1 rows (approximately)
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` (`MenuId`, `ApplicationId`, `MenuDescription`) VALUES
	(1, 1, 'LocalFoodConnection Backend Top Menu');
/*!40000 ALTER TABLE `menu` ENABLE KEYS */;


-- Dumping structure for table common.menuitem
DROP TABLE IF EXISTS `menuitem`;
CREATE TABLE IF NOT EXISTS `menuitem` (
  `MenuItemId` int(10) NOT NULL AUTO_INCREMENT,
  `MenuId` int(10) NOT NULL,
  `ParentMenuItemId` int(10) DEFAULT NULL,
  `Description` varchar(255) NOT NULL,
  `DisplayText` varchar(255) NOT NULL,
  `IconCls` varchar(255) DEFAULT NULL,
  `Class` varchar(255) DEFAULT NULL,
  `Method` varchar(255) DEFAULT NULL,
  `URL` varchar(255) DEFAULT NULL,
  `ActiveFlag` char(1) NOT NULL DEFAULT 'Y',
  `PowerId` int(11) DEFAULT NULL,
  PRIMARY KEY (`MenuItemId`),
  KEY `FK_MenuItem_menu` (`MenuId`),
  KEY `FK_MenuItem_menuitem` (`ParentMenuItemId`),
  CONSTRAINT `FK_MenuItem_menu` FOREIGN KEY (`MenuId`) REFERENCES `menu` (`MenuId`),
  CONSTRAINT `FK_MenuItem_menuitem` FOREIGN KEY (`ParentMenuItemId`) REFERENCES `menuitem` (`MenuItemId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- Dumping data for table common.menuitem: ~1 rows (approximately)
/*!40000 ALTER TABLE `menuitem` DISABLE KEYS */;
INSERT INTO `menuitem` (`MenuItemId`, `MenuId`, `ParentMenuItemId`, `Description`, `DisplayText`, `IconCls`, `Class`, `Method`, `URL`, `ActiveFlag`, `PowerId`) VALUES
	(1, 1, NULL, 'Dashboard', 'Dashboard', NULL, NULL, NULL, NULL, 'Y', NULL);
/*!40000 ALTER TABLE `menuitem` ENABLE KEYS */;


-- Dumping structure for table common.ninja
DROP TABLE IF EXISTS `ninja`;
CREATE TABLE IF NOT EXISTS `ninja` (
  `NinjaId` int(10) NOT NULL AUTO_INCREMENT,
  `FirstName` varchar(50) NOT NULL,
  `LastName` varchar(50) NOT NULL,
  `EmailAddress` varchar(100) NOT NULL,
  `Password` varchar(100) NOT NULL,
  `NinjaTypeCd` char(1) NOT NULL DEFAULT 'C',
  `InvalidLoginAttempts` int(2) DEFAULT NULL,
  `CreatedById` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedById` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`NinjaId`),
  UNIQUE KEY `EmailAddress` (`EmailAddress`),
  KEY `FK_Ninja_ninjatype` (`NinjaTypeCd`),
  CONSTRAINT `FK_Ninja_ninjatype` FOREIGN KEY (`NinjaTypeCd`) REFERENCES `ninjatype` (`NinjaTypeCd`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- Dumping data for table common.ninja: ~4 rows (approximately)
/*!40000 ALTER TABLE `ninja` DISABLE KEYS */;
INSERT INTO `ninja` (`NinjaId`, `FirstName`, `LastName`, `EmailAddress`, `Password`, `NinjaTypeCd`, `InvalidLoginAttempts`, `CreatedById`, `CreatedDate`, `LastModifiedById`, `LastModifiedDate`) VALUES
	(1, 'guest', 'guest', '', 'kLxNpX+0w9lWcamR3wSZ8O/828A=', 'C', 0, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(2, 'Jamie', 'LaMarche', 'jlamarche@modnaut.com', 'kLxNpX+0w9lWcamR3wSZ8O/828A=', 'C', 0, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(3, 'Danny', 'Cohn', 'dcohn@modnaut.com', 'kLxNpX+0w9lWcamR3wSZ8O/828A=', 'C', 0, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(4, 'Ben', 'Dalgaardfasdfsa', 'bdalgaard@modnaut.com', 'kLxNpX+0w9lWcamR3wSZ8O/828A=', 'C', 0, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00');
/*!40000 ALTER TABLE `ninja` ENABLE KEYS */;


-- Dumping structure for table common.ninjaclan
DROP TABLE IF EXISTS `ninjaclan`;
CREATE TABLE IF NOT EXISTS `ninjaclan` (
  `NinjaId` int(10) NOT NULL,
  `ClanId` int(10) NOT NULL,
  `CreatedByNinjaId` int(10) NOT NULL DEFAULT '4',
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedById` varchar(45) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`NinjaId`,`ClanId`),
  KEY `FK_ninjaclan_clan` (`ClanId`),
  KEY `FK_ninjaclan_ninja_2` (`CreatedByNinjaId`),
  CONSTRAINT `FK_ninjaclan_clan` FOREIGN KEY (`ClanId`) REFERENCES `clan` (`ClanId`),
  CONSTRAINT `FK_ninjaclan_ninja` FOREIGN KEY (`NinjaId`) REFERENCES `ninja` (`NinjaId`),
  CONSTRAINT `FK_ninjaclan_ninja_2` FOREIGN KEY (`CreatedByNinjaId`) REFERENCES `ninja` (`NinjaId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table common.ninjaclan: ~1 rows (approximately)
/*!40000 ALTER TABLE `ninjaclan` DISABLE KEYS */;
INSERT INTO `ninjaclan` (`NinjaId`, `ClanId`, `CreatedByNinjaId`, `CreatedDate`, `LastModifiedById`, `LastModifiedDate`) VALUES
	(3, 1, 3, '0000-00-00 00:00:00', '', '2013-04-08 22:24:18');
/*!40000 ALTER TABLE `ninjaclan` ENABLE KEYS */;


-- Dumping structure for table common.ninjapower
DROP TABLE IF EXISTS `ninjapower`;
CREATE TABLE IF NOT EXISTS `ninjapower` (
  `NinjaId` int(10) NOT NULL,
  `PowerId` int(10) NOT NULL,
  `CreatedByNinjaId` int(10) NOT NULL DEFAULT '0',
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedById` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`NinjaId`,`PowerId`),
  KEY `FK_ninjapower_power` (`PowerId`),
  KEY `FK_ninjapower_ninja_2` (`CreatedByNinjaId`),
  CONSTRAINT `FK_ninjapower_ninja` FOREIGN KEY (`NinjaId`) REFERENCES `ninja` (`NinjaId`),
  CONSTRAINT `FK_ninjapower_ninja_2` FOREIGN KEY (`CreatedByNinjaId`) REFERENCES `ninja` (`NinjaId`),
  CONSTRAINT `FK_ninjapower_power` FOREIGN KEY (`PowerId`) REFERENCES `power` (`PowerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table common.ninjapower: ~3 rows (approximately)
/*!40000 ALTER TABLE `ninjapower` DISABLE KEYS */;
INSERT INTO `ninjapower` (`NinjaId`, `PowerId`, `CreatedByNinjaId`, `CreatedDate`, `LastModifiedById`, `LastModifiedDate`) VALUES
	(1, 1, 1, '0000-00-00 00:00:00', 0, '2013-02-26 11:12:16'),
	(1, 2, 1, '0000-00-00 00:00:00', 0, '2013-02-26 11:12:59'),
	(4, 1, 4, '0000-00-00 00:00:00', 0, '2013-04-18 00:19:26');
/*!40000 ALTER TABLE `ninjapower` ENABLE KEYS */;


-- Dumping structure for table common.ninjasession
DROP TABLE IF EXISTS `ninjasession`;
CREATE TABLE IF NOT EXISTS `ninjasession` (
  `NinjaId` int(10) NOT NULL DEFAULT '1',
  `SessionId` bigint(19) NOT NULL,
  `CreatedById` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedById` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY `FK__session` (`SessionId`),
  KEY `FK_ninja` (`NinjaId`),
  CONSTRAINT `FK_ninjasession_ninja` FOREIGN KEY (`NinjaId`) REFERENCES `ninja` (`NinjaId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table common.ninjasession: ~242 rows (approximately)
/*!40000 ALTER TABLE `ninjasession` DISABLE KEYS */;
/*!40000 ALTER TABLE `ninjasession` ENABLE KEYS */;


-- Dumping structure for table common.ninjatype
DROP TABLE IF EXISTS `ninjatype`;
CREATE TABLE IF NOT EXISTS `ninjatype` (
  `NinjaTypeCd` char(1) NOT NULL,
  `NinjaTypeDescription` varchar(50) NOT NULL,
  `CreatedById` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedById` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`NinjaTypeCd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table common.ninjatype: ~2 rows (approximately)
/*!40000 ALTER TABLE `ninjatype` DISABLE KEYS */;
INSERT INTO `ninjatype` (`NinjaTypeCd`, `NinjaTypeDescription`, `CreatedById`, `CreatedDate`, `LastModifiedById`, `LastModifiedDate`) VALUES
	('A', 'Administrator', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	('C', 'Customer', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00');
/*!40000 ALTER TABLE `ninjatype` ENABLE KEYS */;


-- Dumping structure for table common.power
DROP TABLE IF EXISTS `power`;
CREATE TABLE IF NOT EXISTS `power` (
  `PowerId` int(10) NOT NULL AUTO_INCREMENT,
  `PowerDescription` varchar(100) NOT NULL,
  `PowerStatusCd` char(1) NOT NULL DEFAULT 'A',
  `CreatedById` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedById` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`PowerId`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- Dumping data for table common.power: ~2 rows (approximately)
/*!40000 ALTER TABLE `power` DISABLE KEYS */;
INSERT INTO `power` (`PowerId`, `PowerDescription`, `PowerStatusCd`, `CreatedById`, `CreatedDate`, `LastModifiedById`, `LastModifiedDate`) VALUES
	(1, 'Application Access', 'A', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(2, 'Admin Access', 'A', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00');
/*!40000 ALTER TABLE `power` ENABLE KEYS */;


-- Dumping structure for table common.session
DROP TABLE IF EXISTS `session`;
CREATE TABLE IF NOT EXISTS `session` (
  `SessionId` bigint(19) NOT NULL,
  `SessionObject` varbinary(65500) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`SessionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table common.session: ~4 rows (approximately)
/*!40000 ALTER TABLE `session` DISABLE KEYS */;
/*!40000 ALTER TABLE `session` ENABLE KEYS */;


-- Dumping structure for table common.state
DROP TABLE IF EXISTS `state`;
CREATE TABLE IF NOT EXISTS `state` (
  `StateId` int(10) NOT NULL AUTO_INCREMENT,
  `Name` varchar(500) DEFAULT NULL,
  `Abbreviation` varchar(50) DEFAULT NULL,
  `CountryId` int(10) NOT NULL,
  `CreatedById` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedById` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`StateId`),
  KEY `FK_state_country` (`CountryId`),
  CONSTRAINT `FK_state_country` FOREIGN KEY (`CountryId`) REFERENCES `country` (`CountryId`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8;

-- Dumping data for table common.state: ~50 rows (approximately)
/*!40000 ALTER TABLE `state` DISABLE KEYS */;
INSERT INTO `state` (`StateId`, `Name`, `Abbreviation`, `CountryId`, `CreatedById`, `CreatedDate`, `LastModifiedById`, `LastModifiedDate`) VALUES
	(1, 'Alabama', 'AL', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(2, 'Alaska', 'AK', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(3, 'Arizona', 'AZ', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(4, 'Arkansas', 'AR', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(5, 'California', 'CA', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(6, 'Colorado', 'CO', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(7, 'Connecticut', 'CT', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(8, 'Delaware', 'DE', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(9, 'Florida', 'FL', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(10, 'Georgia', 'GA', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(11, 'Hawaii', 'HI', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(12, 'Idaho', 'ID', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(13, 'Illinois', 'IL', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(14, 'Indiana', 'IN', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(15, 'Iowa', 'IA', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(16, 'Kansas', 'KS', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(17, 'Kentucky', 'KY', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(18, 'Louisiana', 'LA', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(19, 'Maine', 'ME', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(20, 'Maryland', 'MD', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(21, 'Massachusetts', 'MA', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(22, 'Michigan', 'MI', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(23, 'Minnesota', 'MN', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(24, 'Mississippi', 'MS', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(25, 'Missouri', 'MO', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(26, 'Montana', 'MT', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(27, 'Nebraska', 'NE', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(28, 'Nevada', 'NV', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(29, 'New Hampshire', 'NH', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(30, 'New Jersey', 'NJ', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(31, 'New Mexico', 'NM', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(32, 'New York', 'NY', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(33, 'North Carolina', 'NC', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(34, 'North Dakota', 'ND', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(35, 'Ohio', 'OH', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(36, 'Oklahoma', 'OK', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(37, 'Oregon', 'OR', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(38, 'Pennsylvania', 'PA', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(39, 'Rhode Island', 'RI', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(40, 'South Carolina', 'SC', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(41, 'South Dakota', 'SD', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(42, 'Tennessee', 'TN', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(43, 'Texas', 'TX', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(44, 'Utah', 'UT', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(45, 'Vermont', 'VT', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(46, 'Virginia', 'VA', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(47, 'Washington', 'WA', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(48, 'West Virginia', 'WV', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(49, 'Wisconsin', 'WI', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(50, 'Wyoming', 'WY', 1, 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00');
/*!40000 ALTER TABLE `state` ENABLE KEYS */;


-- Dumping structure for table common.status
DROP TABLE IF EXISTS `status`;
CREATE TABLE IF NOT EXISTS `status` (
  `StatusCd` char(1) NOT NULL,
  `StatusDescription` varchar(50) NOT NULL,
  `CreatedById` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedById` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`StatusCd`),
  UNIQUE KEY `StatusCd_UNIQUE` (`StatusCd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table common.status: ~2 rows (approximately)
/*!40000 ALTER TABLE `status` DISABLE KEYS */;
INSERT INTO `status` (`StatusCd`, `StatusDescription`, `CreatedById`, `CreatedDate`, `LastModifiedById`, `LastModifiedDate`) VALUES
	('A', 'Active', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	('D', 'Disabled', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00');
/*!40000 ALTER TABLE `status` ENABLE KEYS */;


-- Dumping structure for table common.string
DROP TABLE IF EXISTS `string`;
CREATE TABLE IF NOT EXISTS `string` (
  `StringId` int(10) NOT NULL AUTO_INCREMENT,
  `StringCd` varchar(500) NOT NULL,
  `CreatedById` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedById` varchar(45) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`StringId`),
  UNIQUE KEY `StringCd` (`StringCd`(255))
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- Dumping data for table common.string: ~5 rows (approximately)
/*!40000 ALTER TABLE `string` DISABLE KEYS */;
INSERT INTO `string` (`StringId`, `StringCd`, `CreatedById`, `CreatedDate`, `LastModifiedById`, `LastModifiedDate`) VALUES
	(1, 'Name', 0, '0000-00-00 00:00:00', '', '0000-00-00 00:00:00'),
	(2, 'Address', 0, '0000-00-00 00:00:00', '', '0000-00-00 00:00:00'),
	(3, 'City', 0, '0000-00-00 00:00:00', '', '0000-00-00 00:00:00'),
	(4, 'State', 0, '0000-00-00 00:00:00', '', '0000-00-00 00:00:00'),
	(5, 'Zip Code', 0, '0000-00-00 00:00:00', '', '0000-00-00 00:00:00');
/*!40000 ALTER TABLE `string` ENABLE KEYS */;


-- Dumping structure for table common.stringvalue
DROP TABLE IF EXISTS `stringvalue`;
CREATE TABLE IF NOT EXISTS `stringvalue` (
  `StringId` int(10) NOT NULL,
  `LanguageId` smallint(3) NOT NULL,
  `Value` varchar(5000) NOT NULL,
  `CreatedById` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedById` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`StringId`,`LanguageId`),
  KEY `FK__string` (`StringId`),
  KEY `FK__language` (`LanguageId`),
  CONSTRAINT `FK_stringvalue_string` FOREIGN KEY (`StringId`) REFERENCES `string` (`StringId`),
  CONSTRAINT `FK__language` FOREIGN KEY (`LanguageId`) REFERENCES `language` (`LanguageId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table common.stringvalue: ~10 rows (approximately)
/*!40000 ALTER TABLE `stringvalue` DISABLE KEYS */;
INSERT INTO `stringvalue` (`StringId`, `LanguageId`, `Value`, `CreatedById`, `CreatedDate`, `LastModifiedById`, `LastModifiedDate`) VALUES
	(1, 2, 'nombre ', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(1, 3, '??', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(2, 2, 'direcci', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(2, 3, '?????', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(3, 2, 'ciudad', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(3, 3, '???', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(4, 2, 'estado', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(4, 3, '?????', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(5, 2, 'c', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00'),
	(5, 3, '?????', 0, '0000-00-00 00:00:00', 0, '0000-00-00 00:00:00');
/*!40000 ALTER TABLE `stringvalue` ENABLE KEYS */;


-- Dumping database structure for localfoodconnection
DROP DATABASE IF EXISTS `localfoodconnection`;
CREATE DATABASE IF NOT EXISTS `localfoodconnection` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `localfoodconnection`;


-- Dumping structure for table localfoodconnection.category
DROP TABLE IF EXISTS `category`;
CREATE TABLE IF NOT EXISTS `category` (
  `CategoryId` int(11) NOT NULL AUTO_INCREMENT,
  `CategoryDescription` varchar(100) NOT NULL,
  `CategoryTypeId` int(11) NOT NULL,
  `CreatedByNinjaId` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedByNinjaId` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`CategoryId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table localfoodconnection.category: ~0 rows (approximately)
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
/*!40000 ALTER TABLE `category` ENABLE KEYS */;


-- Dumping structure for table localfoodconnection.collections
DROP TABLE IF EXISTS `collections`;
CREATE TABLE IF NOT EXISTS `collections` (
  `CollectionsId` int(11) NOT NULL AUTO_INCREMENT,
  `ProductId` int(11) NOT NULL,
  `CollectionsTypeId` varchar(45) NOT NULL,
  `CreatedByNinjaId` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedByNinjaId` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`CollectionsId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table localfoodconnection.collections: ~0 rows (approximately)
/*!40000 ALTER TABLE `collections` DISABLE KEYS */;
/*!40000 ALTER TABLE `collections` ENABLE KEYS */;


-- Dumping structure for table localfoodconnection.inventory
DROP TABLE IF EXISTS `inventory`;
CREATE TABLE IF NOT EXISTS `inventory` (
  `InventoryId` int(11) NOT NULL AUTO_INCREMENT,
  `StructureId` int(11) NOT NULL,
  `ProductId` int(11) NOT NULL,
  `CreatedByNinjaId` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedByNinjaId` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`InventoryId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table localfoodconnection.inventory: ~0 rows (approximately)
/*!40000 ALTER TABLE `inventory` DISABLE KEYS */;
/*!40000 ALTER TABLE `inventory` ENABLE KEYS */;


-- Dumping structure for table localfoodconnection.location
DROP TABLE IF EXISTS `location`;
CREATE TABLE IF NOT EXISTS `location` (
  `LocationId` int(11) NOT NULL AUTO_INCREMENT,
  `StructureId` int(11) NOT NULL,
  `LocationTypeId` int(11) NOT NULL,
  `LocationName` varchar(45) DEFAULT NULL,
  `Address1` varchar(45) NOT NULL,
  `Address2` varchar(45) DEFAULT NULL,
  `City` varchar(45) NOT NULL,
  `State` char(2) NOT NULL,
  `Zip` int(11) NOT NULL,
  `CreatedByNinjaId` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedByNinjaId` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`LocationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table localfoodconnection.location: ~0 rows (approximately)
/*!40000 ALTER TABLE `location` DISABLE KEYS */;
/*!40000 ALTER TABLE `location` ENABLE KEYS */;


-- Dumping structure for table localfoodconnection.locationtype
DROP TABLE IF EXISTS `locationtype`;
CREATE TABLE IF NOT EXISTS `locationtype` (
  `LocationTypeId` int(11) NOT NULL AUTO_INCREMENT,
  `LocationTypeDescription` varchar(45) NOT NULL,
  `CreatedByNinjaId` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedByNinjaId` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `LocationTypecol` varchar(45) DEFAULT 'CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP',
  PRIMARY KEY (`LocationTypeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table localfoodconnection.locationtype: ~0 rows (approximately)
/*!40000 ALTER TABLE `locationtype` DISABLE KEYS */;
/*!40000 ALTER TABLE `locationtype` ENABLE KEYS */;


-- Dumping structure for table localfoodconnection.options
DROP TABLE IF EXISTS `options`;
CREATE TABLE IF NOT EXISTS `options` (
  `ProductId` int(11) NOT NULL,
  `OptionTypeId` int(11) NOT NULL,
  `OptionValue` varchar(45) NOT NULL,
  `CreatedByNinjaId` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedByNinjaId` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table localfoodconnection.options: ~0 rows (approximately)
/*!40000 ALTER TABLE `options` DISABLE KEYS */;
/*!40000 ALTER TABLE `options` ENABLE KEYS */;


-- Dumping structure for table localfoodconnection.optiontype
DROP TABLE IF EXISTS `optiontype`;
CREATE TABLE IF NOT EXISTS `optiontype` (
  `OptionTypeId` int(11) NOT NULL,
  `OptionTypeDescription` varchar(45) NOT NULL,
  `CreatedByNinjaId` int(11) NOT NULL,
  `CreatedByDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedByNinjaId` int(11) NOT NULL,
  `LastModifiedByDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`OptionTypeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table localfoodconnection.optiontype: ~0 rows (approximately)
/*!40000 ALTER TABLE `optiontype` DISABLE KEYS */;
/*!40000 ALTER TABLE `optiontype` ENABLE KEYS */;


-- Dumping structure for table localfoodconnection.packagetype
DROP TABLE IF EXISTS `packagetype`;
CREATE TABLE IF NOT EXISTS `packagetype` (
  `PackageTypeId` int(11) NOT NULL AUTO_INCREMENT,
  `PackageDescription` varchar(45) NOT NULL,
  `CreatedByNinjaId` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedByNinjaId` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`PackageTypeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table localfoodconnection.packagetype: ~0 rows (approximately)
/*!40000 ALTER TABLE `packagetype` DISABLE KEYS */;
/*!40000 ALTER TABLE `packagetype` ENABLE KEYS */;


-- Dumping structure for table localfoodconnection.packaging
DROP TABLE IF EXISTS `packaging`;
CREATE TABLE IF NOT EXISTS `packaging` (
  `PackageId` int(11) NOT NULL AUTO_INCREMENT,
  `ProductId` int(11) NOT NULL,
  `PackageTypeId` int(11) NOT NULL,
  `Weight` float DEFAULT NULL,
  `Quantity` int(11) DEFAULT NULL,
  `CreatedByNinjaId` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedByNinjaId` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`PackageId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table localfoodconnection.packaging: ~0 rows (approximately)
/*!40000 ALTER TABLE `packaging` DISABLE KEYS */;
/*!40000 ALTER TABLE `packaging` ENABLE KEYS */;


-- Dumping structure for table localfoodconnection.price
DROP TABLE IF EXISTS `price`;
CREATE TABLE IF NOT EXISTS `price` (
  `PriceId` int(11) NOT NULL AUTO_INCREMENT,
  `PackageId` int(11) NOT NULL,
  `Price` double NOT NULL,
  `ValidDateFrom` datetime DEFAULT NULL,
  `ValidDateTo` datetime DEFAULT NULL,
  `Active` char(1) NOT NULL,
  `CreatedByNinjaId` int(11) NOT NULL,
  `CreatedByDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedByNinjaId` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`PriceId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table localfoodconnection.price: ~0 rows (approximately)
/*!40000 ALTER TABLE `price` DISABLE KEYS */;
/*!40000 ALTER TABLE `price` ENABLE KEYS */;


-- Dumping structure for table localfoodconnection.productcategory
DROP TABLE IF EXISTS `productcategory`;
CREATE TABLE IF NOT EXISTS `productcategory` (
  `ProductId` int(11) NOT NULL,
  `CategoryId` int(11) NOT NULL,
  `CreatedByNinjaId` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedByNinjaId` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table localfoodconnection.productcategory: ~0 rows (approximately)
/*!40000 ALTER TABLE `productcategory` DISABLE KEYS */;
/*!40000 ALTER TABLE `productcategory` ENABLE KEYS */;


-- Dumping structure for table localfoodconnection.productpackaging
DROP TABLE IF EXISTS `productpackaging`;
CREATE TABLE IF NOT EXISTS `productpackaging` (
  `PackageId` int(11) NOT NULL,
  `ProductId` int(11) NOT NULL,
  `CreatedByNinjaId` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedByNinjaId` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table localfoodconnection.productpackaging: ~0 rows (approximately)
/*!40000 ALTER TABLE `productpackaging` DISABLE KEYS */;
/*!40000 ALTER TABLE `productpackaging` ENABLE KEYS */;


-- Dumping structure for table localfoodconnection.products
DROP TABLE IF EXISTS `products`;
CREATE TABLE IF NOT EXISTS `products` (
  `ProductId` int(11) NOT NULL AUTO_INCREMENT,
  `ProductName` varchar(100) NOT NULL,
  `ProductDescription` varchar(300) DEFAULT NULL,
  `Unit` char(1) NOT NULL,
  `Visible` char(1) NOT NULL,
  `Active` char(1) NOT NULL,
  `SKU` varchar(15) DEFAULT NULL,
  `Barcode` int(11) DEFAULT NULL,
  `Image` blob,
  `CreatedByNinjaId` int(11) NOT NULL,
  `CreatedDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedByNinjaId` int(11) NOT NULL,
  `LastModifiedDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ProductId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table localfoodconnection.products: ~0 rows (approximately)
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
/*!40000 ALTER TABLE `products` ENABLE KEYS */;


-- Dumping structure for table localfoodconnection.structure
DROP TABLE IF EXISTS `structure`;
CREATE TABLE IF NOT EXISTS `structure` (
  `StructureId` int(11) NOT NULL,
  `StructureName` varchar(45) NOT NULL,
  `StructureTypeId` int(11) NOT NULL,
  `StructureDescription` varchar(45) DEFAULT NULL,
  `Website` varchar(45) DEFAULT NULL,
  `CreatedByNinjaId` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedByNinjaId` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`StructureId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table localfoodconnection.structure: ~0 rows (approximately)
/*!40000 ALTER TABLE `structure` DISABLE KEYS */;
/*!40000 ALTER TABLE `structure` ENABLE KEYS */;


-- Dumping structure for table localfoodconnection.structuretype
DROP TABLE IF EXISTS `structuretype`;
CREATE TABLE IF NOT EXISTS `structuretype` (
  `StructureTypeId` int(11) NOT NULL,
  `StructureTypeDescription` varchar(45) NOT NULL,
  `CreatedByNinjaId` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LastModifiedByNinjaId` int(11) NOT NULL,
  `LastModifiedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`StructureTypeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table localfoodconnection.structuretype: ~0 rows (approximately)
/*!40000 ALTER TABLE `structuretype` DISABLE KEYS */;
/*!40000 ALTER TABLE `structuretype` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
