
--table created--
CREATE TABLE students(
    id INT,
    name VARCHAR(50)
);

--audit table created--
CREATE TABLE student_log(
    log_message TEXT
);
--create a trigger function--
CREATE OR REPLACE FUNCTION log_student_insert()
RETURNS TRIGGER
AS $$
BEGIN

    INSERT INTO student_log
    VALUES ('New student added');

    RETURN NEW;

END;
$$
LANGUAGE plpgsql;

--create trigger--
CREATE TRIGGER student_insert_trigger
AFTER INSERT
ON students
FOR EACH ROW
EXECUTE FUNCTION log_student_insert();

--insert value --
INSERT INTO students
VALUES(1,'Sakshi');

-- check --
SELECT * FROM student_log;