package com.payroll;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.payroll.config.AppConfig;
import com.payroll.model.Employee;
import com.payroll.model.User;
import com.payroll.repository.EmployeeRepository;
import com.payroll.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

public class PayrollApp {

    private static User currentUser = null;
    private static final Scanner scanner = new Scanner(System.in);

    private static UserRepository userRepository;
    private static EmployeeRepository employeeRepository;

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        userRepository = context.getBean(UserRepository.class);
        employeeRepository = context.getBean(EmployeeRepository.class);

        while (true) {
            if (currentUser == null) {
                showAnonymousMenu();
            } else {
                if ("ADMIN".equals(currentUser.getRole())) {
                    showAdminMenu();
                } else {
                    showUserMenu();
                }
            }
        }
    }

    private static void showAnonymousMenu() {
        System.out.println("\n===== Employee Payroll App =====");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1 -> login();
            case 2 -> register();
            case 3 -> System.exit(0);
            default -> System.out.println("Invalid choice");
        }
    }

    private static void showAdminMenu() {
        System.out.println("\n===== ADMIN MENU =====");
        System.out.println("1. Add Employee");
        System.out.println("2. View Employees");
        System.out.println("3. Edit Employee");
        System.out.println("4. Delete Employee");
        System.out.println("5. Department Payroll");
        System.out.println("6. Audit Logs");
        System.out.println("7. Logout");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1 -> addEmployee();
            case 2 -> viewEmployees();
            case 3 -> editEmployee();
            case 4 -> deleteEmployee();
            case 5 -> getDeptPayroll();
            case 6 -> viewAuditLogs();
            case 7 -> logout();
            default -> System.out.println("Invalid choice");
        }
    }

    private static void showUserMenu() {
        System.out.println("\n===== USER MENU =====");
        System.out.println("1. View My Payroll");
        System.out.println("2. Logout");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1 -> viewMyPayroll();
            case 2 -> logout();
            default -> System.out.println("Invalid choice");
        }
    }

    private static void login() {
        System.out.print("Username: ");
        String username = scanner.next();

        System.out.print("Password: ");
        String password = scanner.next();

        User user = userRepository.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            System.out.println("Login Successful!");
        } else {
            System.out.println("Invalid Credentials!");
        }
    }

    private static void register() {
        System.out.print("Enter Username: ");
        String username = scanner.next();

        System.out.print("Enter Password: ");
        String password = scanner.next();

        System.out.print("Enter Email: ");
        String email = scanner.next();

        userRepository.registerUser(username, password, email, "USER");

        System.out.println("Registration Successful!");
    }

    private static void addEmployee() {
        scanner.nextLine();

        System.out.print("Enter Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Profile Image: ");
        String image = scanner.nextLine();

        System.out.print("Enter Gender: ");
        String gender = scanner.nextLine();

        System.out.print("Enter Salary: ");
        BigDecimal salary = scanner.nextBigDecimal();
        scanner.nextLine();

        System.out.println("Available Departments:");
        System.out.println("1. HR");
        System.out.println("2. Sales");
        System.out.println("3. IT");
        System.out.println("4. Finance");
        System.out.println("5. Marketing");
        System.out.println("6. Engineering");
        System.out.println("7. Support");

        System.out.print("Enter Departments (comma separated, example: HR,Sales,IT): ");
        String deptInput = scanner.nextLine();

        System.out.print("Enter Start Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        System.out.print("Enter Notes: ");
        String notes = scanner.nextLine();

        Employee emp = new Employee();
        emp.setName(name);
        emp.setProfileImage(image);
        emp.setGender(gender);
        emp.setSalary(salary);
        emp.setStartDate(LocalDate.parse(date));
        emp.setNotes(notes);

        employeeRepository.addEmployee(emp);
        Integer empId = employeeRepository.getLastEmployeeId();
        java.util.List<String> departments = java.util.Arrays.asList(deptInput.split(","));
        employeeRepository.addDepartments(empId, departments);
        System.out.println("Employee Added Successfully!");
    }

    private static void viewEmployees() {
        var employees = employeeRepository.findAll();

        System.out.println("\n===== EMPLOYEE DETAILS =====");

        for (var emp : employees) {
            System.out.println("----------------------------");
            System.out.println("ID         : " + emp.getId());
            System.out.println("Name       : " + emp.getName());
            System.out.println("Gender     : " + emp.getGender());

            var departments = employeeRepository.getDepartmentsByEmployeeId(emp.getId());
            System.out.println("Departments: " + String.join(", ", departments));
            System.out.println("Salary     : " + emp.getSalary());
            System.out.println("Start Date : " + emp.getStartDate());
            System.out.println("Notes      : " + emp.getNotes());
            System.out.println("----------------------------");
        }
    }

    private static void editEmployee() {
        System.out.print("Enter Employee ID: ");
        int id = scanner.nextInt();

        System.out.print("Enter New Salary: ");
        BigDecimal salary = scanner.nextBigDecimal();
        scanner.nextLine();

        System.out.print("Enter Notes: ");
        String notes = scanner.nextLine();

        employeeRepository.updateEmployee(id, salary, notes);

        System.out.println("Employee Updated Successfully!");
    }

    private static void deleteEmployee() {
        System.out.print("Enter Employee ID: ");
        int id = scanner.nextInt();

        employeeRepository.deleteEmployee(id);

        System.out.println("Employee Deleted Successfully!");
    }

    private static void viewMyPayroll() {
        var employees = employeeRepository.findAll();

        System.out.println("\n===== MY PAYROLL =====");

        for (var emp : employees) {
            if (emp.getName().toLowerCase().contains(currentUser.getUsername().toLowerCase())) {

                System.out.println("----------------------------");
                System.out.println("ID         : " + emp.getId());
                System.out.println("Name       : " + emp.getName());
                System.out.println("Salary     : " + emp.getSalary());
                System.out.println("Start Date : " + emp.getStartDate());
                System.out.println("----------------------------");
                return;
            }
        }

        System.out.println("No payroll found for current user.");
    }
    private static void getDeptPayroll() {
        scanner.nextLine();

        System.out.print("Enter Department Name: ");
        String dept = scanner.nextLine();

        BigDecimal total = employeeRepository.getDeptPayroll(dept);

        System.out.println("Total Payroll of " + dept + " = " + total);
    }

    private static void viewAuditLogs() {
        var logs = employeeRepository.getAuditLogs();

        System.out.println("\n===== AUDIT LOGS =====");

        for (String log : logs) {
            System.out.println(log);
        }
    }

    private static void logout() {
        currentUser = null;
        System.out.println("Logged out successfully.");
    }
}