package greet;

import greet.model.Greeting;
import greet.model.User;
import greet.util.DBUtil;
import greet.util.HashUtil;

import java.sql.*;
import java.util.Scanner;

public class GreetingJDBCApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;

    public static void main(String[] args) {
        System.out.println("=== Greeting JDBC Application Console ===");

        while (true) {
            if (currentUser == null) {
                showAnonymousMenu();
            } else {
                showUserMenu();
            }
        }
    }

    private static void showAnonymousMenu() {
        System.out.println("\n1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Select Option: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                login();
                break;
            case "2":
                register();
                break;
            case "3":
                System.out.println("Goodbye!");
                System.exit(0);
            default:
                System.out.println("Invalid Option.");
        }
    }

    private static void showUserMenu() {
        System.out.println("\n--- Logged in as: " + currentUser.getUsername()
                + " (" + currentUser.getRole() + ") ---");

        System.out.println("1. View All Greetings");
        System.out.println("2. View Audit Logs");
        System.out.println("3. Get User Greeting Count");

        if (currentUser.getRole().equals("ADMIN")) {
            System.out.println("4. Create Greeting");
            System.out.println("5. Update Greeting");
            System.out.println("6. Delete Greeting");
        }

        System.out.println("7. Logout");
        System.out.print("Select Option: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                viewGreetings();
                break;
            case "2":
                viewAuditLogs();
                break;
            case "3":
                viewUserGreetingCount();
                break;
            case "4":
                if (isAdmin()) createGreeting();
                break;
            case "5":
                if (isAdmin()) updateGreeting();
                break;
            case "6":
                if (isAdmin()) deleteGreeting();
                break;
            case "7":
                logout();
                break;
            default:
                System.out.println("Invalid Option.");
        }
    }

    private static boolean isAdmin() {
        return currentUser != null && currentUser.getRole().equals("ADMIN");
    }

    private static void register() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter role (ADMIN/USER): ");
        String role = scanner.nextLine().toUpperCase();

        if (!role.equals("ADMIN") && !role.equals("USER")) {
            System.out.println("Invalid role choice.");
            return;
        }

        String hashedPassword = HashUtil.hashPassword(password);

        String sql = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, email);
            pstmt.setString(4, role);

            pstmt.executeUpdate();
            System.out.println("Registration successful!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        String hashedPassword = HashUtil.hashPassword(password);
        System.out.println("Input Password = " + password);
        System.out.println("Input Hash = " + hashedPassword);

        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String dbPassword = rs.getString("password");

                    if (dbPassword.equals(hashedPassword)) {
                        currentUser = new User(
                                rs.getInt("id"),
                                rs.getString("username"),
                                dbPassword,
                                rs.getString("email"),
                                rs.getString("role")
                        );

                        System.out.println("Login successful! Welcome " + currentUser.getUsername());
                    } else {
                        System.out.println("Incorrect password.");
                    }
                } else {
                    System.out.println("Username not found.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void logout() {
        System.out.println("Logged out successfully.");
        currentUser = null;
    }

    private static void viewGreetings() {
        String sql = "SELECT g.id, g.message, g.created_by, u.username AS creator " +
                "FROM greetings g " +
                "LEFT JOIN users u ON g.created_by = u.id " +
                "ORDER BY g.id ASC";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- GREETINGS ---");

            while (rs.next()) {
                Greeting greeting = new Greeting(
                        rs.getInt("id"),
                        rs.getString("message"),
                        rs.getInt("created_by"),
                        rs.getString("creator")
                );

                System.out.println(greeting);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewAuditLogs() {
        String sql = "SELECT * FROM greeting_audit ORDER BY changed_at DESC";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- AUDIT LOGS ---");

            while (rs.next()) {
                System.out.println(
                        rs.getInt("id") + " | " +
                                rs.getInt("greeting_id") + " | " +
                                rs.getString("action_type") + " | " +
                                rs.getString("changed_by")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void viewUserGreetingCount() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        String sql = "{ ? = call get_user_greeting_count(?) }";

        try (Connection conn = DBUtil.getConnection();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.registerOutParameter(1, Types.INTEGER);
            cstmt.setString(2, username);
            cstmt.execute();

            int count = cstmt.getInt(1);

            System.out.println("User '" + username + "' has created " + count + " greeting(s).");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createGreeting() {
        System.out.print("Enter greeting message: ");
        String message = scanner.nextLine();

        String sql = "INSERT INTO greetings (message, created_by) VALUES (?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, message);
            pstmt.setInt(2, currentUser.getId());

            pstmt.executeUpdate();
            System.out.println("Greeting created successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateGreeting() {
        System.out.print("Enter Greeting ID to update: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter new greeting message: ");
        String message = scanner.nextLine();

        String sql = "UPDATE greetings SET message = ? WHERE id = ?";

        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, message);
                pstmt.setInt(2, id);

                int rows = pstmt.executeUpdate();

                if (rows > 0) {
                    conn.commit();
                    System.out.println("Greeting updated successfully.");
                } else {
                    conn.rollback();
                    System.out.println("Greeting ID not found.");
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteGreeting() {
        System.out.print("Enter Greeting ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());

        String sql = "DELETE FROM greetings WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Greeting deleted successfully.");
            } else {
                System.out.println("Greeting ID not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}