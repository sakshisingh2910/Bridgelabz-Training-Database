--(where clause)--

SELECT * FROM customers
WHERE city = 'London';

SELECT * FROM customers
where customer_id = 11;

SELECT * FROM customers
where customer_id>11;

SELECT * FROM customers
where customer_id<11;

--(order by)--

SELECT * FROM orders
ORDER BY customer_id ;

Select * from orders
order by order_id DESC;

select * from categories
order by category_name;

--(LIMIT)--
SELECT * FROM customers
LIMIT(11);

SELECT * FROM customers
LIMIT(11) OFFSET(5);

--(min)--
SELECT MIN(price)
FROM products;

--(max)--
SELECT MAX(price)
FROM products;

--(COUNT)--
SELECT COUNT(customer_id)
from customers;

SELECT COUNT(customer_id)
from customers
WHERE city='London';

--(LIKE)--
--starts with A--
SELECT * FROM customers
WHERE customer_name Like 'A%';

--contains A--
SELECT * FROM customers
Where customer_name LIKE '%A%';


--ILIKE-insensitive search--
SELECT * FROM customers
Where customer_name ILIKE '%A%';

SELECT * FROM customers
WHERE city LIKE 'L_nd__';

--(IN)--
SELECT * FROM customers
WHERE country IN ('Germany', 'France','Mexico');

--(not in)--
SELECT * FROM customers
WHERE country NOT IN ('Germany', 'France', 'Mexico');

--(Between)--
SELECT * FROM orders
WHERE customer_id BETWEEN 76 AND 90;

SELECT * FROM orders
WHERE order_date BETWEEN '2021-07-04' AND '2021-07-18';