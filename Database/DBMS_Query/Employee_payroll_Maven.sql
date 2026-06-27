
-- =========================
-- USERS TABLE
-- =========================
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(64) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role VARCHAR(20) NOT NULL CHECK(role IN ('ADMIN','USER')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- =========================
-- EMPLOYEES TABLE
-- =========================
CREATE TABLE employees (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    profile_image VARCHAR(100) NOT NULL,
    gender VARCHAR(10) NOT NULL CHECK(gender IN ('Male','Female')),
    salary NUMERIC(10,2) NOT NULL CHECK(salary >= 0),
    start_date DATE NOT NULL,
    notes TEXT,
    created_by INTEGER REFERENCES users(id) ON DELETE SET NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- =========================
-- EMPLOYEE DEPARTMENTS
-- =========================
CREATE TABLE employee_departments (
    employee_id INTEGER REFERENCES employees(id) ON DELETE CASCADE,
    department VARCHAR(50) NOT NULL,
    PRIMARY KEY(employee_id, department)
);


-- =========================
-- PAYROLL AUDIT TABLE
-- =========================
CREATE TABLE payroll_audit (
    id SERIAL PRIMARY KEY,
    employee_id INTEGER NOT NULL,
    action_type VARCHAR(10) NOT NULL,
    old_salary NUMERIC(10,2),
    new_salary NUMERIC(10,2),
    changed_by VARCHAR(50),
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- =========================
-- INDEXES
-- =========================
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_emp_dept_id ON employee_departments(employee_id);


-- =========================
-- TRIGGER FUNCTION
-- =========================
CREATE OR REPLACE FUNCTION log_salary_change()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        INSERT INTO payroll_audit(employee_id, action_type, old_salary, new_salary, changed_by)
        VALUES (NEW.id, 'INSERT', NULL, NEW.salary, current_user);

        RETURN NEW;

    ELSIF TG_OP = 'UPDATE' THEN
        IF OLD.salary <> NEW.salary THEN
            INSERT INTO payroll_audit(employee_id, action_type, old_salary, new_salary, changed_by)
            VALUES (NEW.id, 'UPDATE', OLD.salary, NEW.salary, current_user);
        END IF;

        RETURN NEW;

    ELSIF TG_OP = 'DELETE' THEN
        INSERT INTO payroll_audit(employee_id, action_type, old_salary, new_salary, changed_by)
        VALUES (OLD.id, 'DELETE', OLD.salary, NULL, current_user);

        RETURN OLD;
    END IF;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;


-- =========================
-- TRIGGER
-- =========================
CREATE TRIGGER trg_log_salary_change
AFTER INSERT OR UPDATE OR DELETE
ON employees
FOR EACH ROW
EXECUTE FUNCTION log_salary_change();


-- =========================
-- DEPARTMENT PAYROLL FUNCTION
-- =========================
CREATE OR REPLACE FUNCTION get_total_payroll_by_dept(p_dept VARCHAR)
RETURNS NUMERIC AS $$
DECLARE total_salary NUMERIC;
BEGIN
    SELECT COALESCE(SUM(e.salary),0)
    INTO total_salary
    FROM employees e
    JOIN employee_departments ed
    ON e.id = ed.employee_id
    WHERE ed.department = p_dept;

    RETURN total_salary;
END;
$$ LANGUAGE plpgsql;


-- =========================
-- SEED USERS
-- =========================
INSERT INTO users (username, password, email, role)
VALUES
('sakshi', 'sakshi@123', 'sakshi@gmail.com', 'ADMIN'),
('sweta', 'sweta@123', 'sweta@gmail.com', 'USER');


-- =========================
-- SEED EMPLOYEE
-- =========================
INSERT INTO employees
(name, profile_image, gender, salary, start_date, notes, created_by)
VALUES
('sakshi singh', 'ellipse-1.png', 'Female', 50000, CURRENT_DATE, 'Seed Employee', 1);


-- =========================
-- SEED DEPARTMENTS
-- =========================
INSERT INTO employee_departments(employee_id, department)
VALUES
(1, 'Sales'),
(1, 'HR'),
(1, 'Finance');

SELECT * FROM users;
SELECT * FROM employees;
SELECT * FROM employee_departments;
SELECT * FROM payroll_audit;