drop table if exists device;
drop table if exists BaseEntity;
create table Device(
deviceID int,
nume varchar(50)
);



CREATE TABLE device(
`id` bigint(20),
nume varchar(10)default null,
deviceID varchar(10)default null,
foreign key(id) references BaseEntity(id)
);

CREATE TABLE `BaseEntity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` INT  NOT NULL default 0,
  `deleted` bit NOT NULL default 0,
  `creationDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   dtype VARCHAR(30) NOT NULL DEFAULT 'BaseEntity',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

insert into device (nume,deviceID) values('iphone',2);
Select * From device;
delete from device where deviceID=2;