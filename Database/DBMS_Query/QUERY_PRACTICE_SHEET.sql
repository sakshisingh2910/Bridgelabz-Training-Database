--department table--
CREATE TABLE Departments(
 department_id SERIAL PRIMARY KEY,
 department_name VARCHAR(100) NOT NULL UNIQUE
);

INSERT INTO Departments(department_name)
VALUES
('sALES'),
('Engineering'),
('Human Resourses');

--employeee table--
CREATE TABLE Employees(
employee_id SERIAL PRIMARY KEY,
first_name VARCHAR(50) NOT NULL,
LAST_NAME VARCHAR(50) NOT NULL,
Department_id INT,
salary DECIMAL(10,2),
joining_date DATE NOT NULL,
FOREIGN KEY (department_id)
REFERENCES Departments(department_id)
);


INSERT INTO Employees
(first_name, last_name, department_id, salary, joining_date)
VALUES
('Sakshi', 'Singh', 1, 50000, '2026-05-10'),
('Priya', 'Singh', 2, 60000, '2020-08-13'),
('Rishabh', 'Verma', 1, 45000, '2022-01-20'),
('Shreeya', 'Gupta', 3, 55000, '2020-11-05'),
('Navya', 'Yadav', 2, 58000, '2023-03-12');


--table project--
CREATE TABLE Projects (
    project_id SERIAL PRIMARY KEY,
    project_name VARCHAR(100) NOT NULL,
    department_id INT,
    status VARCHAR(30) DEFAULT 'ongoing',

    FOREIGN KEY (department_id)
    REFERENCES Departments(department_id)
);

INSERT INTO Projects
(project_name, department_id, status)
VALUES
('New Project', 1, 'ongoing'),
('Hotel management System', 2, 'completed'),
('JOBHUB Portal', 3, 'ongoing');

--Retrieve all employees--
SELECT * FROM Employees;

--Insert Human Resources Department--
INSERT INTO Departments(department_name)
VALUES ('Human Resources');

--update ssalry--
UPDATE Employees
SET salary = 60000
WHERE employee_id = 5;


--delete  aproejct--
DELETE FROM Projects
WHERE project_name = 'Outdated Project';

----employee name --
SELECT first_name, last_name
FROM Employees
Where joining_date > '2020-01-12';

--employee salary betwen --
SELECT *
FROM Employees
WHERE salary BETWEEN 40000 AND 70000;


--LIST ALL EMPLOYEES IN SALES DEPARTEMT --
SELECT *
FROM Employees e
JOIN Departments d
ON e.department_id = d.department_id
WHERE d.department_name ='sALES';

--First Name Starts With A--
SELECT * FROM Employees
WHERE first_name LIKE 'A%';


--TOP 3 HIGHEST SALARY--
SELECT * FROM Employees
ORDER BY salary desc
LIMIT 3;

--PROEJCT NOT COMPLET--
SELECT * FROM Projects
WHERE status != 'completed';

SELECT
e.employee_id,
e.first_name,
e.last_name,
d.department_name
FROM Employees e
JOIN Departments d
ON e.department_id = d.department_id;

SELECT p.project_name,
d.department_name
FROM Projects p
JOIN Departments d
ON p.department_id = d.department_id;

SELECT e.*
FROM Employees e
JOIN Projects p
ON e.department_id = p.department_id
WHERE p.project_name = 'New Project';

SELECT d.department_name,
SUM(e.salary)
FROM Employees e
JOIN Departments d
ON e.department_id = d.department_id
GROUP BY d.department_name; 

SELECT AVG(salary) 
FROM Employees;

SELECT d.department_name,
COUNT(*)
FROM Departments d
JOIN Employees e
ON d.department_id = e.Department_id
GROUP BY d.department_name;


--highest salry--

SELECT * 
FROM Employees
ORDER BY salary DESC
LIMIT 1;

SELECT
d.department_name,
COUNT(*) AS total_employees
FROM Employees e
JOIN Departments d
ON e.department_id = d.department_id
GROUP BY d.department_name
HAVING COUNT(*) > 5;

SELECT * from Employees
WHERE salary >(Select AVG(salary)
from Employees);

ALTER TABLE Projects
ADD CONSTRAINT unique_project_name
UNIQUE(project_name); 

ALTER TABLE Projects
DROP COLUMN status; 

SELECT * FROM Employees
WHERE department_id IS NULL;

SELECT *
FROM Employees
WHERE joining_date <= '2021-06-26';

SELECT d.department_name
FROM Departments d
LEFT JOIN Projects p
ON d.department_id = p.department_id
WHERE p.project_id IS NULL;

SELECT *
FROM Employees
WHERE last_name LIKE '%S%';

SELECT *
FROM Projects
WHERE project_name LIKE '%System';

SELECT *
FROM Employees
ORDER BY joining_date DESC;

SELECT *
FROM Employees
ORDER BY salary ASC;









