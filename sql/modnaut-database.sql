DROP DATABASE IF EXISTS `common`;
CREATE DATABASE `common`;
USE `common`;


CREATE TABLE `Users` (
	`UserId` INT(10) NOT NULL AUTO_INCREMENT,
	`UserName` VARCHAR(50) NOT NULL,
	`FirstName` VARCHAR(50) NOT NULL,
	`LastName` VARCHAR(50) NOT NULL,
	`EmailAddress` VARCHAR(100) NOT NULL,
PRIMARY KEY (`UserId`),
UNIQUE INDEX `EmailAddress` (`EmailAddress`),
UNIQUE INDEX `UserName` (`UserName`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB;


INSERT INTO `users` (`UserName`, `FirstName`, `LastName`, `EmailAddress`) VALUES ('jlamarche11', 'Jamie', 'LaMarche', 'jlamarche@modnaut.com');
INSERT INTO `users` (`UserName`, `FirstName`, `LastName`, `EmailAddress`) VALUES ('dcohn33', 'Danny', 'Cohn', 'dcohn@modnaut.com');
INSERT INTO `users` (`UserName`, `FirstName`, `LastName`, `EmailAddress`) VALUES ('bdalgaard22', 'Ben', 'Dalgaard', 'bdalgaard@modnaut.com');

