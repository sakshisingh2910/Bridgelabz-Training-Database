package com.payroll.repository;

import com.payroll.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Repository
public class EmployeeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addEmployee(Employee emp) {
        String sql = "INSERT INTO employees(name,profile_image,gender,salary,start_date,notes,created_by) VALUES(?,?,?,?,?,?,1)";

        jdbcTemplate.update(sql,
                emp.getName(),
                emp.getProfileImage(),
                emp.getGender(),
                emp.getSalary(),
                Date.valueOf(emp.getStartDate()),
                emp.getNotes()
        );
    }

    public List<Employee> findAll() {
        String sql = "SELECT * FROM employees";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Employee emp = new Employee();
            emp.setId(rs.getInt("id"));
            emp.setName(rs.getString("name"));
            emp.setProfileImage(rs.getString("profile_image"));
            emp.setGender(rs.getString("gender"));
            emp.setSalary(rs.getBigDecimal("salary"));
            emp.setStartDate(rs.getDate("start_date").toLocalDate());
            emp.setNotes(rs.getString("notes"));
            return emp;
        });
    }

    public void updateEmployee(int id, BigDecimal salary, String notes) {
        String sql = "UPDATE employees SET salary=?, notes=? WHERE id=?";
        jdbcTemplate.update(sql, salary, notes, id);
    }

    public void deleteEmployee(int id) {
        String sql = "DELETE FROM employees WHERE id=?";
        jdbcTemplate.update(sql, id);
    }
    public void addDepartment(int empId, String dept) {
        String sql = "INSERT INTO employee_departments(employee_id, department) VALUES(?,?)";
        jdbcTemplate.update(sql, empId, dept);
    }
    public String getDepartmentByEmployeeId(int empId) {
        String sql = "SELECT department FROM employee_departments WHERE employee_id=? LIMIT 1";
        return jdbcTemplate.queryForObject(sql, String.class, empId);
    }

    public BigDecimal getDeptPayroll(String dept) {
        String sql = "SELECT get_total_payroll_by_dept(?)";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, dept);
    }


    public List<String> getAuditLogs() {
        String sql = "SELECT * FROM payroll_audit";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                "EmpID: " + rs.getInt("employee_id") +
                        " | Action: " + rs.getString("action_type") +
                        " | Old Salary: " + rs.getBigDecimal("old_salary") +
                        " | New Salary: " + rs.getBigDecimal("new_salary")
        );
    }

    public Integer getLastEmployeeId() {
        String sql = "SELECT MAX(id) FROM employees";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
    public void addDepartments(int empId, List<String> departments) {
        String sql = "INSERT INTO employee_departments(employee_id, department) VALUES(?, ?)";

        for (String dept : departments) {
            jdbcTemplate.update(sql, empId, dept);
        }
    }

    public List<String> getDepartmentsByEmployeeId(int empId) {
        String sql = "SELECT department FROM employee_departments WHERE employee_id=?";

        return jdbcTemplate.query(sql,
                (rs, rowNum) -> rs.getString("department"),
                empId);
    }

}