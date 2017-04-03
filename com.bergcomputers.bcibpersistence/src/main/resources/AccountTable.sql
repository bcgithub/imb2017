drop table if exists Account;
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

CREATE TABLE `Account` (
  `id` bigint(20) NOT NULL,
  `iban` varchar(35) COLLATE utf8_bin NOT NULL,
  `amount` double NOT NULL,
  UNIQUE KEY `IBAN_UNIQUE` (`IBAN`),
  foreign key(id) references BaseEntity(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


insert into BaseEntity (dtype) values ('Account');
insert into Account (id,iban,amount) 
values (LAST_INSERT_ID(), 'RO0123456789', 1234);