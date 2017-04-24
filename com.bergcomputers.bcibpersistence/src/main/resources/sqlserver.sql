DROP TABLE TransactionTbl;
DROP TABLE Account;
DROP TABLE Device;
DROP TABLE Customer;
DROP TABLE Role;
DROP TABLE Currency;
DROP TABLE Beneficiary;
DROP TABLE BaseEntity;

CREATE TABLE BaseEntity(id int not null identity(1,1) primary key, version INTEGER DEFAULT 0, deleted BIT DEFAULT 0, creationDate datetime DEFAULT getdate(),
dtype VARCHAR(31) DEFAULT 'BaseEntity');

CREATE TABLE Beneficiary(id int primary key,iban VARCHAR(255),name VARCHAR(255),details VARCHAR(255),accountholder VARCHAR(255),
FOREIGN KEY (id)
	REFERENCES BaseEntity(id)
	ON DELETE CASCADE);

CREATE TABLE Currency(id int primary key, symbol VARCHAR(10),exchangerate float,
FOREIGN KEY (id)
	REFERENCES BaseEntity(id)
	ON DELETE CASCADE);

CREATE TABLE Role (id int NOT NULL primary key, name VARCHAR(255),
FOREIGN KEY (id)
REFERENCES BaseEntity(id)
ON DELETE CASCADE
);



CREATE TABLE Customer(id int NOT NULL primary key, roleid int NOT NULL, firstName VARCHAR(255), lastName VARCHAR(255), login VARCHAR(255), password VARCHAR(255),
FOREIGN KEY (id)
REFERENCES BaseEntity(id)
ON DELETE CASCADE,
FOREIGN KEY (roleid)
REFERENCES Role(id)
ON DELETE NO ACTION
);

CREATE TABLE Device(id int NOT NULL primary key, deviceid int NOT NULL, name VARCHAR(255), customerid int NOT NULL,
FOREIGN KEY (id)
REFERENCES BaseEntity(id)
ON DELETE CASCADE,
FOREIGN KEY (customerid)
REFERENCES Customer(id)
ON DELETE NO ACTION
);



CREATE TABLE Account (id int NOT NULL primary key, customerid int NOT NULL, currencyid int NOT NULL, iban VARCHAR(255), amount float,
FOREIGN KEY (id)
REFERENCES BaseEntity(id)
ON DELETE CASCADE,
FOREIGN KEY (customerid)
REFERENCES Customer(id)
ON DELETE NO ACTION,
FOREIGN KEY (currencyid)
REFERENCES Currency(id)
);

CREATE TABLE TransactionTbl (id int NOT NULL primary key, accountid int NOT NULL, date DATETIME, type VARCHAR(255), amount float, sender VARCHAR(255), details VARCHAR(255), status VARCHAR(255),
FOREIGN KEY (id)
        REFERENCES BaseEntity(id)
        ON DELETE CASCADE,
FOREIGN KEY (accountid)
        REFERENCES Account(id)
        ON DELETE NO ACTION
);

insert into BaseEntity (dtype) values('Currency');
insert into Currency values(SCOPE_IDENTITY(), 'EUR', 1.0);
insert into BaseEntity (dtype) values('Role');
insert into Role values(SCOPE_IDENTITY(), 'customer');
insert into BaseEntity (dtype) values('Role');
insert into Role values(SCOPE_IDENTITY(), 'employee');
insert into BaseEntity(dtype) values('Customer');
insert into Customer values(SCOPE_IDENTITY(), 3, 'Ion', 'Popescu','ionp','ionp');
insert into BaseEntity(dtype) values('Customer');
insert into Customer values(SCOPE_IDENTITY(), 2, 'Alex', 'Ionescu','alexi','alexi');

insert into BaseEntity (dtype) values('Account');
insert into Account values(SCOPE_IDENTITY(), 4, 1, 'ROBUC012345', 100.0);
insert into BaseEntity (dtype) values('Account');
insert into Account values(SCOPE_IDENTITY(), 4, 1, 'ROBUC012346', 500.0);
insert into BaseEntity (dtype) values('Account');
insert into Account values(SCOPE_IDENTITY(), 4, 1, 'ROBUC012347', 800.0);
insert into BaseEntity (dtype) values('Account');
insert into Account values(SCOPE_IDENTITY(), 4, 1, 'ROBUC012348', 900.0);
insert into BaseEntity (dtype) values('Account');
insert into Account values(SCOPE_IDENTITY(), 4, 1, 'ROTIM012345', 960.0);
insert into BaseEntity (dtype) values('Account');
insert into Account values(SCOPE_IDENTITY(), 4, 1, 'ROTIM012349', 100.0);
insert into BaseEntity (dtype) values('Account');
insert into Account values(SCOPE_IDENTITY(), 4, 1, 'ROTIM012346', 300.0);

insert into BaseEntity (dtype) values('Transaction');
insert into TransactionTbl values(SCOPE_IDENTITY(),7, cast('2014-10-21T10:30:00' as datetime), 'PAYMENT', 200.0, 'Alex Ion', 'Plata factura 23434', 'NEW');
insert into BaseEntity (dtype) values('Transaction');
insert into TransactionTbl values(SCOPE_IDENTITY(),7, cast( '2014-10-22T11:00:00' as datetime), 'CREDIT', 1000.0, 'bCOMPUTERS', 'Plata salar', 'NEW');
insert into BaseEntity (dtype) values('Beneficiary');
insert into Beneficiary values(SCOPE_IDENTITY(), 'ROBUC012345', 'plata curent', 'detalii ordin de plata pt factura lunara de curent', 'ENEL Energie SA');
insert into BaseEntity (dtype) values('Beneficiary');
insert into Beneficiary values(SCOPE_IDENTITY(), 'ROBUC012346', 'plata rata casa', 'detalii ordin de plata pt rata la casa', 'BCR');
