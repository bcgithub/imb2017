drop database bcib;
drop user bcib;
create database bcib;
CREATE USER 'bcib'@'localhost' IDENTIFIED BY 'bcib';
GRANT ALL ON bcib.* TO bcib@'localhost' IDENTIFIED BY 'bcib';

USE bcib;

DROP TABLE IF EXISTS Currency;
DROP TABLE IF EXISTS BaseEntity;

CREATE TABLE BaseEntity(id BIGINT NOT NULL AUTO_INCREMENT, version INTEGER DEFAULT 0, deleted BIT DEFAULT 0, creationDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
dtype VARCHAR(31) DEFAULT 'BaseEntity',
PRIMARY KEY (id));


CREATE TABLE Currency(id BIGINT,symbol VARCHAR(10),exchangerate DOUBLE,
FOREIGN KEY (id)
	REFERENCES BaseEntity(id)
	ON DELETE CASCADE);


insert into BaseEntity (dtype) values('Currency');
insert into Currency values(LAST_INSERT_ID(), 'EUR', 1.0);
insert into BaseEntity (dtype) values('Currency');
insert into Currency values(LAST_INSERT_ID(), 'USD', 1.0);
