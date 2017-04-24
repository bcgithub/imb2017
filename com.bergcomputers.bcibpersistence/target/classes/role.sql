drop table if exists Role;
drop table if exists BaseEntity;


CREATE TABLE `BaseEntity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` INT  NOT NULL default 0,
  `deleted` bit NOT NULL default 0,
  `creationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   dtype VARCHAR(30) NOT NULL DEFAULT 'BaseEntity',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;




CREATE TABLE Role (
	`id` bigint(20) NOT NULL,
    Nume varchar(255),
    foreign key (id) references BaseEntity(id)
);

insert into BaseEntity(dtype) values('BaseEntity');
INSERT INTO Role( Id, Nume )
VALUES (LAST_INSERT_ID(),'Ion');


SELECT * FROM Role;