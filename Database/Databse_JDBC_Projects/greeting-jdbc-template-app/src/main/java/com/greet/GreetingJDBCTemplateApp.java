package com.greet;

import com.greet.config.AppConfig;
import com.greet.model.Greeting;
import com.greet.model.User;
import com.greet.repository.GreetingRepository;
import com.greet.repository.UserRepository;
import com.greet.util.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Component
public class GreetingJDBCTemplateApp {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GreetingRepository greetingRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    private static final Scanner scanner = new Scanner(System.in);
    private User currentUser = null;

    public static void main(String[] args) {

        loadEnvironmentVariables();

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        GreetingJDBCTemplateApp app =
                context.getBean(GreetingJDBCTemplateApp.class);

        System.out.println("=== Greeting Spring JdbcTemplate Console ===");

        app.runLoop();

        context.close();
    }

    private static void loadEnvironmentVariables() {
        try {
            if (Files.exists(Paths.get(".env"))) {
                List<String> lines = Files.readAllLines(Paths.get(".env"));

                for (String line : lines) {
                    line = line.trim();

                    if (!line.isEmpty() && !line.startsWith("#")) {
                        String[] parts = line.split("=", 2);

                        if (parts.length == 2) {
                            System.setProperty(parts[0].trim(), parts[1].trim());
                        }
                    }
                }
            } else {
                System.out.println("Warning: .env file not found");
            }

        } catch (IOException e) {
            System.out.println("Failed to read .env file");
        }
    }

    private void runLoop() {
        while (true) {
            if (currentUser == null) {
                showAnonymousMenu();
            } else {
                showUserMenu();
            }
        }
    }

    private void showAnonymousMenu() {
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
                break;
            default:
                System.out.println("Invalid Option");
        }
    }

    private void showUserMenu() {
        System.out.println("\n--- Logged in as: " + currentUser.getUsername() + " (" + currentUser.getRole() + ") ---");

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
                System.out.println("Invalid Option");
        }
    }

    private boolean isAdmin() {
        return currentUser != null && currentUser.getRole().equals("ADMIN");
    }

    private void register() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter role (ADMIN/USER): ");
        String role = scanner.nextLine().toUpperCase();

        User user = new User(0, username, HashUtil.hashPassword(password), email, role);

        try {
            userRepository.save(user);
            System.out.println("Registration successful!");
        } catch (Exception e) {
            System.out.println("Registration failed");
        }
    }

    private void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = userRepository.findByUsername(username);

        if (user != null) {
            String hashedInput = HashUtil.hashPassword(password);

            if (user.getPassword().equals(hashedInput)) {
                currentUser = user;
                System.out.println("Login successful!");
            } else {
                System.out.println("Incorrect password");
            }
        } else {
            System.out.println("User not found");
        }
    }

    private void logout() {
        currentUser = null;
        System.out.println("Logged out");
    }

    private void viewGreetings() {
        List<Greeting> list = greetingRepository.findAll();

        for (Greeting g : list) {
            System.out.println(g);
        }
    }

    private void viewAuditLogs() {
        List<Map<String, Object>> logs = greetingRepository.getAuditLogs();

        for (Map<String, Object> log : logs) {
            System.out.println(log);
        }
    }

    private void viewUserGreetingCount() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        int count = greetingRepository.getGreetingCountForUser(username);

        System.out.println("User " + username + " has created " + count + " greetings");
    }

    private void createGreeting() {
        System.out.print("Enter greeting message: ");
        String message = scanner.nextLine();

        greetingRepository.save(message, currentUser.getId());
        System.out.println("Greeting created");
    }

    private void updateGreeting() {
        System.out.print("Enter Greeting ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter new message: ");
        String message = scanner.nextLine();

        transactionTemplate.execute(status -> {
            int rows = greetingRepository.update(id, message);

            if (rows > 0) {
                System.out.println("Greeting updated");
            } else {
                status.setRollbackOnly();
                System.out.println("Greeting not found");
            }

            return null;
        });
    }

    private void deleteGreeting() {
        System.out.print("Enter Greeting ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        int rows = greetingRepository.delete(id);

        if (rows > 0) {
            System.out.println("Greeting deleted");
        } else {
            System.out.println("Greeting not found");
        }
    }
}