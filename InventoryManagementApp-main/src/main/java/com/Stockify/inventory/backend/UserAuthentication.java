package com.Stockify.inventory.backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.Stockify.inventory.db.DBConnection;

public class UserAuthentication {
    
    // Default admin credentials (you can extend this to use database)
    private static final String DEFAULT_ADMIN_USER = "admin";
    private static final String DEFAULT_ADMIN_PASS = "admin";
    
    // Additional demo users
    private static final String DEMO_USER = "demo";
    private static final String DEMO_PASS = "demo123";
    
    public static class User {
        private String username;
        private String role;
        private String fullName;
        
        public User(String username, String role, String fullName) {
            this.username = username;
            this.role = role;
            this.fullName = fullName;
        }
        
        // Getters
        public String getUsername() { return username; }
        public String getRole() { return role; }
        public String getFullName() { return fullName; }
    }
    
    public static User authenticate(String username, String password) {
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            return null;
        }
        
        username = username.trim();
        password = password.trim();
        
        // Check default admin
        if (DEFAULT_ADMIN_USER.equals(username) && DEFAULT_ADMIN_PASS.equals(password)) {
            return new User(username, "admin", "System Administrator");
        }
        
        // Check demo user
        if (DEMO_USER.equals(username) && DEMO_PASS.equals(password)) {
            return new User(username, "user", "Demo User");
        }
        
        // You can extend this to check against a users table in database
        // For now, return null for invalid credentials
        return null;
    }
    
    public static boolean isAdmin(User user) {
        return user != null && "admin".equals(user.getRole());
    }
    
    public static boolean isValidUser(User user) {
        return user != null && user.getUsername() != null && !user.getUsername().trim().isEmpty();
    }
    
    // Future enhancement: Database-based authentication
    private static User authenticateFromDatabase(String username, String password) {
        String sql = "SELECT username, role, full_name FROM users WHERE username = ? AND password = ? AND active = 1";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ps.setString(2, password); // In real app, this should be hashed
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getString("username"),
                        rs.getString("role"),
                        rs.getString("full_name")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Database authentication failed: " + e.getMessage());
        }
        
        return null;
    }
    
    // Create users table (for future database authentication)
    public static void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "user_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(50) UNIQUE NOT NULL," +
                    "password VARCHAR(255) NOT NULL," +
                    "full_name VARCHAR(100) NOT NULL," +
                    "role VARCHAR(20) DEFAULT 'user'," +
                    "active BOOLEAN DEFAULT TRUE," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
            System.out.println("Users table created successfully");
        } catch (SQLException e) {
            System.err.println("Failed to create users table: " + e.getMessage());
        }
    }
}
