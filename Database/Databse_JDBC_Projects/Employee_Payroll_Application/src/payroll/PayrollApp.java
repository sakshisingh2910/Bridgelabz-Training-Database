package payroll;

import payroll.model.User;
import payroll.util.DBUtil;
import payroll.util.HashUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class PayrollApp {

    private static User currentUser = null;
    private static final Scanner scanner = new Scanner(System.in);

    private static final List<String> AVAILABLE_PROFILES = Arrays.asList(
            "ellipse-1.png",
            "ellipse-2.png",
            "ellipse-3.png",
            "ellipse-4.png"
    );

    public static void main(String[] args) {
        while (true) {
            if (currentUser == null) {
                showAnonymousMenu();
            } else if (currentUser.getRole().equalsIgnoreCase("ADMIN")) {
                showAdminMenu();
            } else {
                showUserMenu();
            }
        }
    }

    private static void showAnonymousMenu() {
        System.out.println("\n===== Employee Payroll System =====");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");

        int choice = getIntInput();

        switch (choice) {
            case 1: login(); break;
            case 2: register(); break;
            case 3: System.exit(0);
            default: System.out.println("Invalid Choice!");
        }
    }

    private static void showAdminMenu() {
        System.out.println("\n===== ADMIN MENU =====");
        System.out.println("1. Add Employee");
        System.out.println("2. View All Employees");
        System.out.println("3. Edit Employee");
        System.out.println("4. Delete Employee");
        System.out.println("5. Department Wise Payroll");
        System.out.println("6. Payroll Audit Logs");
        System.out.println("7. Logout");

        int choice = getIntInput();

        switch (choice) {
            case 1: addEmployee(); break;
            case 2: viewAllEmployees(); break;
            case 3: editEmployee(); break;
            case 4: deleteEmployee(); break;
            case 5: getDeptPayroll(); break;
            case 6: viewAuditLogs(); break;
            case 7: logout(); break;
            default: System.out.println("Invalid Choice!");
        }
    }

    private static void showUserMenu() {
        System.out.println("\n===== USER MENU =====");
        System.out.println("1. View My Details");
        System.out.println("2. Logout");

        int choice = getIntInput();

        switch (choice) {
            case 1: viewMyDetails(); break;
            case 2: logout(); break;
            default: System.out.println("Invalid Choice!");
        }
    }

    private static void login() {
        try (Connection conn = DBUtil.getConnection()) {

            System.out.print("Username: ");
            String username = scanner.next();

            System.out.print("Password: ");
            String password = scanner.next();

            String sql = "SELECT * FROM users WHERE username=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String dbPassword = rs.getString("password");
                String hashedPassword = HashUtil.hashPassword(password);

                if (dbPassword.equals(hashedPassword)) {
                    currentUser = new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("email"),
                            rs.getString("role")
                    );
                    System.out.println("Login Successful!");
                } else {
                    System.out.println("Wrong Password!");
                }
            } else {
                System.out.println("User Not Found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void register() {
        try (Connection conn = DBUtil.getConnection()) {

            System.out.print("Username: ");
            String username = scanner.next();

            System.out.print("Password: ");
            String password = scanner.next();

            System.out.print("Email: ");
            String email = scanner.next();

            System.out.print("Role (ADMIN/USER): ");
            String role = scanner.next();

            String hashedPassword = HashUtil.hashPassword(password);

            String sql = "INSERT INTO users(username,password,email,role) VALUES(?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, email);
            pstmt.setString(4, role);

            pstmt.executeUpdate();
            System.out.println("Registration Successful!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void logout() {
        currentUser = null;
        System.out.println("Logged Out Successfully!");
    }

    private static int getIntInput() {
        while (true) {
            try {
                System.out.print("Enter Choice: ");
                return Integer.parseInt(scanner.next());
            } catch (Exception e) {
                System.out.println("Invalid Number!");
            }
        }
    }

    private static BigDecimal getDecimalInput() {
        while (true) {
            try {
                return new BigDecimal(scanner.next());
            } catch (Exception e) {
                System.out.println("Invalid Amount!");
            }
        }
    }

    private static void addEmployee() {
        try (Connection conn = DBUtil.getConnection()) {

            scanner.nextLine();
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();

            System.out.println("Available Profiles: " + AVAILABLE_PROFILES);
            System.out.print("Choose Profile Image: ");
            String profileImage = scanner.next();

            System.out.print("Enter Gender (Male/Female): ");
            String gender = scanner.next();

            scanner.nextLine();
            System.out.print("Enter Departments (comma separated): ");
            String deptInput = scanner.nextLine();

            List<String> departments = Arrays.asList(deptInput.split(","));

            System.out.print("Enter Salary: ");
            BigDecimal salary = getDecimalInput();

            System.out.print("Enter Start Date (YYYY-MM-DD): ");
            String startDate = scanner.next();

            scanner.nextLine();
            System.out.print("Enter Notes: ");
            String notes = scanner.nextLine();

            conn.setAutoCommit(false);

            String empSql = "INSERT INTO employees(name, profile_image, gender, salary, start_date, notes, created_by) VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement empStmt = conn.prepareStatement(empSql, Statement.RETURN_GENERATED_KEYS);

            empStmt.setString(1, name);
            empStmt.setString(2, profileImage);
            empStmt.setString(3, gender);
            empStmt.setBigDecimal(4, salary);
            empStmt.setDate(5, Date.valueOf(startDate));
            empStmt.setString(6, notes);
            empStmt.setInt(7, currentUser.getId());

            empStmt.executeUpdate();

            ResultSet rs = empStmt.getGeneratedKeys();
            int empId = 0;

            if (rs.next()) {
                empId = rs.getInt(1);
            }

            String deptSql = "INSERT INTO employee_departments(employee_id, department) VALUES (?, ?)";

            PreparedStatement deptStmt = conn.prepareStatement(deptSql);

            for (String dept : departments) {
                deptStmt.setInt(1, empId);
                deptStmt.setString(2, dept.trim());
                deptStmt.executeUpdate();
            }

            conn.commit();
            System.out.println("Employee Added Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void viewAllEmployees() {
        try (Connection conn = DBUtil.getConnection()) {

            String sql = "SELECT e.id, e.name, e.gender, STRING_AGG(d.department, ', ') AS departments, e.salary, e.start_date " +
                    "FROM employees e LEFT JOIN employee_departments d ON e.id = d.employee_id " +
                    "GROUP BY e.id ORDER BY e.id";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println("-------------------------");
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Gender: " + rs.getString("gender"));
                System.out.println("Departments: " + rs.getString("departments"));
                System.out.println("Salary: " + rs.getBigDecimal("salary"));
                System.out.println("Start Date: " + rs.getDate("start_date"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void editEmployee() {
        try (Connection conn = DBUtil.getConnection()) {

            System.out.print("Employee ID: ");
            int empId = getIntInput();

            System.out.print("New Salary: ");
            BigDecimal salary = getDecimalInput();

            scanner.nextLine();
            System.out.print("New Notes: ");
            String notes = scanner.nextLine();

            String sql = "UPDATE employees SET salary=?, notes=? WHERE id=?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setBigDecimal(1, salary);
            pstmt.setString(2, notes);
            pstmt.setInt(3, empId);

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Employee Updated Successfully!");
            } else {
                System.out.println("Employee Not Found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void deleteEmployee() {
        try (Connection conn = DBUtil.getConnection()) {

            System.out.print("Enter Employee ID: ");
            int empId = getIntInput();

            String sql = "DELETE FROM employees WHERE id=?";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, empId);

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Employee Deleted Successfully!");
            } else {
                System.out.println("Employee Not Found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getDeptPayroll() {
        try (Connection conn = DBUtil.getConnection()) {

            scanner.nextLine();
            System.out.print("Enter Department Name: ");
            String dept = scanner.nextLine();

            CallableStatement cstmt =
                    conn.prepareCall("{ ? = call get_total_payroll_by_dept(?) }");

            cstmt.registerOutParameter(1, Types.NUMERIC);
            cstmt.setString(2, dept);
            cstmt.execute();

            BigDecimal total = cstmt.getBigDecimal(1);

            System.out.println("Total Payroll: " + total);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void viewAuditLogs() {
        try (Connection conn = DBUtil.getConnection()) {

            String sql = "SELECT * FROM payroll_audit ORDER BY id";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println("------------------------");
                System.out.println("Audit ID: " + rs.getInt("id"));
                System.out.println("Employee ID: " + rs.getInt("employee_id"));
                System.out.println("Action: " + rs.getString("action_type"));
                System.out.println("Old Salary: " + rs.getBigDecimal("old_salary"));
                System.out.println("New Salary: " + rs.getBigDecimal("new_salary"));
                System.out.println("Changed By: " + rs.getString("changed_by"));
                System.out.println("Changed At: " + rs.getTimestamp("changed_at"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void viewMyDetails() {
        try (Connection conn = DBUtil.getConnection()) {

            String sql = "SELECT * FROM employees LIMIT 1";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Gender: " + rs.getString("gender"));
                System.out.println("Salary: " + rs.getBigDecimal("salary"));
                System.out.println("Start Date: " + rs.getDate("start_date"));
                System.out.println("Notes: " + rs.getString("notes"));
            } else {
                System.out.println("No Details Found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
