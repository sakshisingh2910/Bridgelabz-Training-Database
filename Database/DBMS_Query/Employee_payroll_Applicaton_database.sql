

-- USERS TABLE
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(64) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role VARCHAR(20) NOT NULL CHECK(role IN ('ADMIN','USER')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
DELETE FROM users;

-- EMPLOYEES TABLE
CREATE TABLE employees (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    profile_image VARCHAR(100) NOT NULL,
    gender VARCHAR(10) NOT NULL CHECK(gender IN ('Male','Female')),
    salary NUMERIC(10,2) NOT NULL CHECK(salary >= 0),
    start_date DATE NOT NULL,
    notes TEXT,
    created_by INT REFERENCES users(id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- EMPLOYEE DEPARTMENTS TABLE
CREATE TABLE employee_departments (
    employee_id INT REFERENCES employees(id) ON DELETE CASCADE,
    department VARCHAR(50) NOT NULL,
    PRIMARY KEY(employee_id, department)
);

-- PAYROLL AUDIT TABLE
CREATE TABLE payroll_audit (
    id SERIAL PRIMARY KEY,
    employee_id INT NOT NULL,
    action_type VARCHAR(10) NOT NULL,
    old_salary NUMERIC(10,2),
    new_salary NUMERIC(10,2),
    changed_by VARCHAR(50),
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- INDEXES
CREATE UNIQUE INDEX idx_users_username ON users(username);
CREATE INDEX idx_emp_dept_id ON employee_departments(employee_id);

-- FUNCTION FOR PAYROLL BY DEPARTMENT
CREATE OR REPLACE FUNCTION get_total_payroll_by_dept(p_dept VARCHAR)
RETURNS NUMERIC AS $$
BEGIN
    RETURN (
        SELECT COALESCE(SUM(salary), 0)
        FROM employees e
        JOIN employee_departments d ON e.id = d.employee_id
        WHERE d.department = p_dept
    );
END;
$$ LANGUAGE plpgsql;

-- TRIGGER FUNCTION
CREATE OR REPLACE FUNCTION log_salary_change()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        INSERT INTO payroll_audit(employee_id, action_type, old_salary, new_salary, changed_by)
        VALUES (NEW.id, 'INSERT', NULL, NEW.salary, current_user);

    ELSIF TG_OP = 'UPDATE' THEN
        IF OLD.salary <> NEW.salary THEN
            INSERT INTO payroll_audit(employee_id, action_type, old_salary, new_salary, changed_by)
            VALUES (NEW.id, 'UPDATE', OLD.salary, NEW.salary, current_user);
        END IF;

    ELSIF TG_OP = 'DELETE' THEN
        INSERT INTO payroll_audit(employee_id, action_type, old_salary, new_salary, changed_by)
        VALUES (OLD.id, 'DELETE', OLD.salary, NULL, current_user);
    END IF;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- TRIGGER
CREATE TRIGGER trg_log_salary_change
AFTER INSERT OR UPDATE OR DELETE ON employees
FOR EACH ROW
EXECUTE FUNCTION log_salary_change();

-- SEED USERS
-- INSERT INTO users(username,password,email,role)
-- VALUES
-- ('admin','admin','admin@gmail.com','ADMIN'),
-- ('user','user','user@gmail.com','USER');

INSERT INTO users(username,password,email,role)
VALUES
(
'sakshii_29',
'8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918',
'sakshi@gmail.com',
'ADMIN'
),
(
'sakshii_2910',
'04f8996da763b7a969b1028ee3007569eaf3a635486ddab211d512c85b9df8fb',
'sweta@gmail.com',
'USER'
);

-- SEED EMPLOYEE
INSERT INTO employees(name,profile_image,gender,salary,start_date,notes,created_by)
VALUES
('Amarpa Keerthi Kumar','ellipse-1.png','Female',10000,'2019-10-29','Senior specialist account manager.',1);
UPDATE  employees
SET name  = 'Sweta singh'
WHERE name  = 'Amarpa Keerthi Kumar';

INSERT INTO employee_departments(employee_id,department)
VALUES
(1,'Sales'),
(1,'HR'),
(1,'Finance');


SELECT * FROM users;
SELECT * FROM employees;


