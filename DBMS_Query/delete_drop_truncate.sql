--//converting year from int to varchar
Alter TABLE Cars
Alter column year type varchar(4);

--drop column--
Alter table cars
DROP COLUMN colors;
SELECT * FROM Cars;

DROP TABLE Cars;
SELECT * FROM Cars;

--delete --
DELETE From Cars where brand = 'Tata';
SELECT * FROM Cars;

--truncate--
TRUNCATE TABLE Cars;
select * from Cars;