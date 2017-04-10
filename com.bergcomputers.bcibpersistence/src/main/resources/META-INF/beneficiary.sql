drop table if exists beneficiary;
drop table if exists BaseEntity;

CREATE TABLE `BaseEntity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` INT  NOT NULL default 0,
  `deleted` bit NOT NULL default 0,
  `creationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   dtype VARCHAR(30) NOT NULL DEFAULT 'BaseEntity',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `beneficiary` (
 `id` bigint(20) NOT NULL,
  `iban` varchar(45) COLLATE utf8_bin NOT NULL,
  `name` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `details` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `accountHolder` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  FOREIGN KEY (id) references BaseEntity(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into BaseEntity(dtype) values('BaseEntity');
INSERT INTO beneficiary (id, iban, name, details, accountHolder)
VALUES (last_insert_id(), 'a123', 'Ioana', 'green', 'abc');

select * from beneficiary