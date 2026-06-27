CREATE Table Cars(
year INT,
brand VARCHAR(232),
Model VARCHAR(222)
);

INSERT INTO Cars (brand, model, year)
VALUES ('Ford', 'Mustang', 1964);

SELECT * FROM CARS;

INSERT INTO Cars(year, brand, model)
VALUES
(2024, 'Toyota', 'Fortuner'),
(2023, 'BMW', 'X5'),
(2022, 'Tata', 'Nexon');

SELECT * FROM Cars;
SELECT brand , model from Cars ;



