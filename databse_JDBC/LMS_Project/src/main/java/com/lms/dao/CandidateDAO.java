package com.lms.dao;

import com.lms.config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CandidateDAO {
    public CandidateDAO() {
        try {
            Connection con = DatabaseConnection.getConnection();
            String jobsTable = "CREATE TABLE IF NOT EXISTS jobs(" + "id SERIAL PRIMARY KEY," + "title VARCHAR(100)," + "department VARCHAR(100))";
            con.prepareStatement(jobsTable).execute();

            String candidatesTable = "CREATE TABLE IF NOT EXISTS candidates(" + "id SERIAL PRIMARY KEY," + "name VARCHAR(100)," + "email VARCHAR(100) UNIQUE," + "phone VARCHAR(20)," + "status VARCHAR(50))";
            con.prepareStatement(candidatesTable).execute();

            String interviewersTable = "CREATE TABLE IF NOT EXISTS interviewers(" + "id SERIAL PRIMARY KEY," + "name VARCHAR(100)," + "email VARCHAR(100) UNIQUE)";
            con.prepareStatement(interviewersTable).execute();

            String interviewsTable =
                    "CREATE TABLE IF NOT EXISTS interviews(" +
                            "id SERIAL PRIMARY KEY," +
                            "candidate_id INT," +
                            "interviewer_id INT," +
                            "score INT," +
                            "feedback VARCHAR(200))";

            con.prepareStatement(interviewsTable).execute();

            String skillsTable =
                    "CREATE TABLE IF NOT EXISTS candidate_skills(" +
                            "id SERIAL PRIMARY KEY," +
                            "candidate_id INT," +
                            "skill_name VARCHAR(100))";

            con.prepareStatement(skillsTable).execute();

            String experienceTable =
                    "CREATE TABLE IF NOT EXISTS candidate_experience(" +
                            "id SERIAL PRIMARY KEY," +
                            "candidate_id INT," +
                            "company_name VARCHAR(100)," +
                            "years_of_experience INT)";

            con.prepareStatement(experienceTable).execute();

            System.out.println("Candidate Tables Ready");

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Apply Job
    public void applyJob(String name,
                         String email,
                         String phone) {

        try {

            Connection con = DatabaseConnection.getConnection();

            String query =
                    "INSERT INTO candidates(name,email,phone,status) VALUES(?,?,?,?)";

            PreparedStatement ps =
                    con.prepareStatement(query);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, "Applied");

            ps.executeUpdate();

            System.out.println("Application Submitted");

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Show Jobs
    public void listOpenJobs() {

        try {

            Connection con = DatabaseConnection.getConnection();

            String query = "SELECT * FROM jobs";

            PreparedStatement ps =
                    con.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                System.out.println(
                        rs.getInt("id") + " | " +
                                rs.getString("title") + " | " +
                                rs.getString("department"));
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Show Candidates
    public void listAllCandidates() {

        try {

            Connection con = DatabaseConnection.getConnection();

            String query = "SELECT * FROM candidates";

            PreparedStatement ps =
                    con.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                System.out.println(
                        rs.getInt("id") + " | " +
                                rs.getString("name") + " | " +
                                rs.getString("email") + " | " +
                                rs.getString("status"));
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Add Skill
    public void addSkill(int candidateId,
                         String skill) {

        try {

            Connection con = DatabaseConnection.getConnection();

            String query =
                    "INSERT INTO candidate_skills(candidate_id,skill_name) VALUES(?,?)";

            PreparedStatement ps =
                    con.prepareStatement(query);

            ps.setInt(1, candidateId);
            ps.setString(2, skill);

            ps.executeUpdate();

            System.out.println("Skill Added");

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Add Experience
    public void addExperience(int candidateId,
                              String company,
                              int years) {

        try {

            Connection con = DatabaseConnection.getConnection();

            String query =
                    "INSERT INTO candidate_experience(candidate_id,company_name,years_of_experience) VALUES(?,?,?)";

            PreparedStatement ps =
                    con.prepareStatement(query);

            ps.setInt(1, candidateId);
            ps.setString(2, company);
            ps.setInt(3, years);

            ps.executeUpdate();

            System.out.println("Experience Added");

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Schedule Interview
    public void scheduleInterview(int candidateId,
                                  int interviewerId) {

        try {

            Connection con = DatabaseConnection.getConnection();

            String query =
                    "INSERT INTO interviews(candidate_id,interviewer_id,score,feedback) VALUES(?,?,0,'Pending')";

            PreparedStatement ps =
                    con.prepareStatement(query);

            ps.setInt(1, candidateId);
            ps.setInt(2, interviewerId);

            ps.executeUpdate();

            System.out.println("Interview Scheduled");

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Submit Score
    public void submitInterviewScore(int interviewId,
                                     int score,
                                     String feedback) {

        try {

            Connection con = DatabaseConnection.getConnection();

            String query =
                    "UPDATE interviews SET score=?,feedback=? WHERE id=?";

            PreparedStatement ps =
                    con.prepareStatement(query);

            ps.setInt(1, score);
            ps.setString(2, feedback);
            ps.setInt(3, interviewId);

            ps.executeUpdate();

            System.out.println("Score Updated");

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Show Interviews
    public void listInterviews() {

        try {

            Connection con = DatabaseConnection.getConnection();

            String query = "SELECT * FROM interviews";

            PreparedStatement ps =
                    con.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                System.out.println(
                        rs.getInt("id") + " | " +
                                rs.getInt("candidate_id") + " | " +
                                rs.getInt("interviewer_id") + " | " +
                                rs.getInt("score") + " | " +
                                rs.getString("feedback"));
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Show Interviewers
    public void listInterviewers() {

        try {

            Connection con = DatabaseConnection.getConnection();

            String query = "SELECT * FROM interviewers";

            PreparedStatement ps =
                    con.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                System.out.println(
                        rs.getInt("id") + " | " +
                                rs.getString("name") + " | " +
                                rs.getString("email"));
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}