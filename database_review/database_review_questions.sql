
DROP TABLE Orders;
DROP TABLE Product;
create Table Product(
p_id serial primary key,
p_name varchar(20) not null
);

create table orders(
o_id serial primary key,
p_id int REFERENCES Product(p_id), 
d_location VARCHAR(20),
Available_stock int
);

INSERT INTO product (p_name)
VALUES
('Laptop'),
('Mouse'),
('Keyboard'),
('Monitor'),
('Printer'),
('Scanner'),
('Speaker'),
('Webcam'),
('Hard Disk'),
('UPS');

INSERT INTO orders (p_id, d_location, Available_stock)
VALUES
(1,'delhi',850),
(1,'delhi',850),
(2,'china',920),
(3,'noida',1100),
(3,'noida',1100),
(4,'varanasi',875),
(5,'guragoan',980),
(5,'guragoan',980),
(6,'jaunpur',1200),
(7,'kanpur',1050),
(8, 'assam',890),
(9,'etah',1150),
(10,'delhi',1000);

SELECT * FROM Product;
SELECT * FROM orders;

ALTER TABLE Product
ADD COLUMN d_location VARCHAR(20);

ALTER TABLE Product
ADD COLUMN available_stock INT;


UPDATE Product p
SET d_location = o.d_location,
    available_stock = o.available_stock
FROM Orders o
WHERE p.p_id = o.p_id;

ALTER TABLE Orders
DROP COLUMN d_location;

ALTER TABLE Orders
DROP COLUMN available_stock;

SELECT COUNT(DISTINCT o_id) AS unique_orders
FROM Orders;



