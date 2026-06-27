CREATE TABLE Cars(
year INT,
brand VARCHAR(255),
model VARCHAR(245)
);

INSERT INTO Cars(year, brand, modeL)
VALUES
(2024, 'Toyota', 'Fortuner'),
(2023, 'BMW', 'X5'),
(2022, 'Tata', 'Nexon');

SELECT * From Cars;
--Equal to--
SELECT * FROM Cars WHERE brand = 'BMW';
SELECT * FROM Cars WHERE year < 2023;
SELECT * FROM cars WHERE year > 1975;
SELECT * FROM cars WHERE year <= 2023;
SELECT * FROM cars WHERE brand != 'BMW';

SELECT * FROM cars
WHERE brand = 'Tata' OR year = 1975;

SELECT * FROM cars WHERE brand = 'BMW' AND year = 2023;

SELECT * FROM cars WHERE brand IN ('Volvo', 'Tata', 'BMW');
SELECT * FROM cars WHERE model LIKE 'F%';

SELECT * FROM cars where year BETWEEN 2000 And 2025;
SELECT * FROM cars where model IS NULL;
