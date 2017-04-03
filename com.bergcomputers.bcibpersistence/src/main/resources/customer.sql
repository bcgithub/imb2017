drop table if exists Customer;
drop table if exists BaseEntity;

CREATE TABLE `BaseEntity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` INT  NOT NULL default 0,
  `deleted` bit NOT NULL default 0,
  `creationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   dtype VARCHAR(30) NOT NULL DEFAULT 'BaseEntity',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into BaseEntity(dtype) values('BaseEntity');
SELECT * FROM BaseEntity;

CREATE TABLE `customer` (
  `id` bigint(20) NOT NULL,
  `FirstName` varchar(30) COLLATE utf8_bin NOT NULL,
  `LastName` varchar(30) COLLATE utf8_bin NOT NULL,
  `LoginID` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `Password` varchar(45) COLLATE utf8_bin NOT NULL,
  foreign key(od) references BaseEntity(id),
  UNIQUE KEY `LoginID_UNIQUE` (`LoginID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into BaseEntity (dtype) values ('customer');
insert into Customer(id, FirstName, LastName, LoginID, Password) values (LAST_INSERT_ID(), "Ana", "Popescu", "ana934", "123456");
