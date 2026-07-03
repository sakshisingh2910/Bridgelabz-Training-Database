package com.lms;

import com.lms.config.DatabaseConnection;
import com.lms.dao.AdminDAO;
import com.lms.dao.CandidateDAO;

import java.sql.Connection;

public class LMSApplication {
    public static void main(String[] args) {

        try {

            Connection con = DatabaseConnection.getConnection();
            System.out.println("Database Connected Successfully");
            con.close();

        }
        catch(Exception e) {
            e.printStackTrace();
        }

        AdminDAO adminDAO = new AdminDAO();
        boolean login = adminDAO.authenticateAdmin("admin@gmail.com", "Kiran@12");
        System.out.println(login);

        CandidateDAO candidateDAO = new CandidateDAO();
    }
}