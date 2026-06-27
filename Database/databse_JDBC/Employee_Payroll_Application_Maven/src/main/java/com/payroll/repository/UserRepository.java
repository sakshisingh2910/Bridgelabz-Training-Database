package com.payroll.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.payroll.model.User;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username=?";

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                    new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("email"),
                            rs.getString("role")
                    ), username);
        } catch (Exception e) {
            return null;
        }
    }

    public void registerUser(String username, String password, String email, String role) {
        String sql = "INSERT INTO users(username,password,email,role) VALUES(?,?,?,?)";
        jdbcTemplate.update(sql, username, password, email, role);
    }
}