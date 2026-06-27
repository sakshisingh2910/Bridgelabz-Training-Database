CREATE TABLE categories (
    category_id SERIAL PRIMARY KEY,
    category_name VARCHAR(255),
    description VARCHAR(255)
);

INSERT INTO categories (category_name, description)
VALUES
('Beverages', 'Softdrinks, coffees, teas, beers, and juice'),
('Dairy Products', 'Cheeses, paneer'),
('Grains/Cereals', 'Breads, crackers, pasta, and cereal'),
('Confections', 'Desserts, candies, and sweet breads'),
('Meat/Poultry', 'Prepared meats'),
('Produce', 'Dried fruit and bean curd'),
('Seafood', 'Seaweed and fish');

SELECT * FROM categories;


--Create table orders)--
CREATE TABLE orders
(order_id SERIAL PRIMARY KEY,
CUSTOMER_ID INT,
ORDER_date DATE);

INSERT INTO orders(order_id, customer_id, order_date)
VALUES(
10234, 90, '2021-07-04'),
(10249, 81, '2021-07-05'),
(10252, 76, '2021-07-09'),
(10254, 14, '2021-07-11'),
(10255, 68, '2021-07-12'),
(10256, 88, '2021-07-15'),
(10257, 35, '2021-07-16'),
(10258, 20, '2021-07-17'),
(10259, 13, '2021-07-18'),
(10260, 55, '2021-07-19'),
(10261, 61, '2021-07-19');

SELECT * FROM orders;

--(customer table)--
CREATE TABLE customers (
  customer_id SERIAL NOT NULL PRIMARY KEY,
  customer_name VARCHAR(255),
  contact_name VARCHAR(255),
  address VARCHAR(255),
  city VARCHAR(255),
  postal_code VARCHAR(255),
  country VARCHAR(255)
);

INSERT INTO customers (customer_name, contact_name, address, city, postal_code, country)
VALUES
  ('Alfreds Futterkiste', 'Maria Anders', 'Obere Str. 57', 'Berlin', '12209', 'Germany'),
  ('Ana Trujillo Emparedados y helados', 'Ana Trujillo', 'Avda. de la Constitucion 2222', 'Mexico D.F.', '05021', 'Mexico'),
  ('Antonio Moreno Taquera', 'Antonio Moreno', 'Mataderos 2312', 'Mexico D.F.', '05023', 'Mexico'),
  ('Around the Horn', 'Thomas Hardy', '120 Hanover Sq.', 'London', 'WA1 1DP', 'UK'),
  ('Berglunds snabbkoep', 'Christina Berglund', 'Berguvsvegen 8', 'Lulea', 'S-958 22', 'Sweden'),
  ('Blauer See Delikatessen', 'Hanna Moos', 'Forsterstr. 57', 'Mannheim', '68306', 'Germany'),
  ('Blondel pere et fils', 'Frederique Citeaux', '24, place Kleber', 'Strasbourg', '67000', 'France'),
  ('Bolido Comidas preparadas', 'Martin Sommer', 'C/ Araquil, 67', 'Madrid', '28023', 'Spain'),
  ('Bon app', 'Laurence Lebihans', '12, rue des Bouchers', 'Marseille', '13008', 'France'),
  ('Bottom-Dollar Marketse', 'Elizabeth Lincoln', '23 Tsawassen Blvd.', 'Tsawassen', 'T2F 8M4', 'Canada'),
  ('Bs Beverages', 'Victoria Ashworth', 'Fauntleroy Circus', 'London', 'EC2 5NT', 'UK'),
  ('Cactus Comidas para llevar', 'Patricio Simpson', 'Cerrito 333', 'Buenos Aires', '1010', 'Argentina');
  SELECT * FROM customers;