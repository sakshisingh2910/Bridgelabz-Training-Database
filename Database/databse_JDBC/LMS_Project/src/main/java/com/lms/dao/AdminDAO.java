package com.lms.dao;

import com.lms.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminDAO {

    public AdminDAO() {
        try {
            Connection con = DatabaseConnection.getConnection();
            String createTable = "CREATE TABLE IF NOT EXISTS admins(" +
                            "id SERIAL PRIMARY KEY," +
                            "name VARCHAR(100)," +
                            "email VARCHAR(100) UNIQUE," +
                            "password VARCHAR(100))";

            PreparedStatement ps = con.prepareStatement(createTable);
            ps.execute();

            String checkAdmin = "SELECT * FROM admins WHERE email=?";
            PreparedStatement checkPs = con.prepareStatement(checkAdmin);
            checkPs.setString(1, "admin@gmail.com");
            ResultSet rs = checkPs.executeQuery();
            if (!rs.next()) {
                String insertAdmin = "INSERT INTO admins(name,email,password) VALUES(?,?,?)";
                PreparedStatement insertPs = con.prepareStatement(insertAdmin);

                insertPs.setString(1, "User Admin");
                insertPs.setString(2, "admin@gmail.com");
                insertPs.setString(3, "Kiran@12");
                insertPs.executeUpdate();
                System.out.println("Default Admin Added");
            }
            System.out.println("Admin Table Ready");
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean authenticateAdmin(String email, String password) {
        try {
            Connection con = DatabaseConnection.getConnection();
            String query = "SELECT * FROM admins WHERE email=? AND password=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                con.close();
                return true;
            }
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addJob(String title, String department) {
        try {
            Connection con = DatabaseConnection.getConnection();
            String query = "INSERT INTO jobs(title,department) VALUES(?,?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, title);
            ps.setString(2, department);
            ps.executeUpdate();
            System.out.println("Job Added Successfully");
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addCourse(String title, String description) {
        try {
            Connection con = DatabaseConnection.getConnection();
            String query = "INSERT INTO courses(title,description) VALUES(?,?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, title);
            ps.setString(2, description);
            ps.executeUpdate();
            System.out.println("Course Added Successfully");
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addInterviewer(String name, String email) {
        try {
            Connection con = DatabaseConnection.getConnection();
            String query = "INSERT INTO interviewers(name,email) VALUES(?,?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.executeUpdate();
            System.out.println("Interviewer Added Successfully");
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewHiringPipeline() {
        try {
            Connection con = DatabaseConnection.getConnection();
            String query = "SELECT id,name,email,status FROM candidates";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getInt("id") + " | " +
                                rs.getString("name") + " | " +
                                rs.getString("email") + " | " +
                                rs.getString("status"));
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewOnboardingSummary() {
        try {
            Connection con = DatabaseConnection.getConnection();
            String query = "SELECT * FROM onboardings";

            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("Candidate ID : " + rs.getInt("candidate_id"));
                System.out.println("Status : " + rs.getString("status"));
            }
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}