CREATE TABLE students(
student_id INT PRIMARY KEY,
 name VARCHAR(50)
);

INSERT INTO students(student_id, name)
VALUES
(1,'SAKSHI'),
(2,'RAHUL'),
(3,'PRIYA'),
(5,'AMAN');
SELECT * FROM students;


--2nd table--
CREATE TABLE courses(
student_id INT ,
course VARCHAR(50)
);

INSERT INTO courses(student_id, course)
VALUES
(1,'JAVA'),
(2,'PYTHON'),
(4,'DBMS'),
(5,'SQL');


--(INNER JOIN)--
SELECT * FROM students s 
INNER JOIN courses c
ON s.student_id = c.student_id;


--LEFT JOIN--
SELECT * FROM students s
LEFT JOIN courses c
ON s.student_id = c.student_id;

--RIGHT JOIN--
SELECT * FROM students s
RIGHT JOIN courses c
ON s.student_id = c.student_id;


--FULL OUTER  JOIN--
SELECT * FROM students s
FULL OUTER JOIN courses c
ON s.student_id = c.student_id;

--CROSS JOIN--
SELECT * 
FROM students 
CROSS JOIN courses ;

--SELF JOIN--
CREATE TABLE employee(
    emp_id INT,
    emp_name VARCHAR(50),
    manager_id INT
);
INSERT INTO employee(emp_id,  emp_name, manager_id  )
VALUES
(1,'SAKSHI', 22);

SELECT 
e.emp_name AS Employee,
m.emp_name AS Manager
FROM employee e
LEFT JOIN employee m
ON e.manager_id = m.emp_id;













