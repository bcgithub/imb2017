
drop table if exists currency;
drop table if exists BaseEntity;

CREATE TABLE `BaseEntity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` INT  NOT NULL default 0,
  `deleted` bit NOT NULL default 0,
  `creationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   dtype VARCHAR(30) NOT NULL DEFAULT 'BaseEntity',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


SELECT * FROM BaseEntity;


CREATE TABLE `currency` (
 `id` bigint(20) NOT NULL ,
  `symbol` varchar(10)  NOT NULL,
  `exchangeRate` double NOT NULL,
  foreign key (id) references BaseEntity(id)
  
  
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


insert into BaseEntity(dtype) values('BaseEntity');

insert into currency (id, symbol , exchangeRate) values (LAST_INSERT_ID(),'B', 40.3);
insert into currency (id, symbol , exchangeRate) values (LAST_INSERT_ID(),'c', 21.3);
