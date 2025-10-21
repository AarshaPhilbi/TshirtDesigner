package com.tshirt.designApp.database;

import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserAuthentication {

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public static boolean registerUser(String username, String email, String password) {
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";

        // Declaring conn outside the try-with-resources for the rollback logic
        Connection conn = null;

        try {
            conn = DatabaseManager.getConnection();

            // Ensure auto-commit is off for explicit transaction control (typical default, but safer to check)
            // If not explicitly set, SQLite defaults to manual transaction management.
            // We ensure we handle the commit manually.

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, username);
                pstmt.setString(2, email);
                pstmt.setString(3, hashPassword(password));

                int rowsAffected = pstmt.executeUpdate(); // Execute the statement

                // CRITICAL FIX: Explicitly commit the transaction if the insert worked
                if (rowsAffected > 0) {
                    conn.commit();
                    System.out.println("User registered successfully: " + username);
                    return true;
                } else {
                    return false; // Insert failed for unknown reason
                }
            }

        } catch (SQLException e) {
            // Handle Rollback on failure
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    System.err.println("Rollback failed: " + rollbackEx.getMessage());
                }
            }

            // Print detailed error information for debugging
            if (e.getMessage().contains("UNIQUE") || e.getMessage().contains("constraint")) {
                System.err.println("Registration failed: Username or email already exists (UNIQUE constraint violation).");
            } else {
                System.err.println("Error registering user: " + e.getMessage());
                e.printStackTrace();
            }
            return false;
        } finally {
            // Ensure connection is closed
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException closeEx) {
                    System.err.println("Error closing connection: " + closeEx.getMessage());
                }
            }
        }
    }

    public static int loginUser(String username, String password) {
        String sql = "SELECT id, password FROM users WHERE username = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password");
                String inputHash = hashPassword(password);

                if (storedHash.equals(inputHash)) {
                    int userId = rs.getInt("id");
                    System.out.println("Login successful for user: " + username);
                    return userId;
                } else {
                    System.out.println("Incorrect password");
                    return -1;
                }
            } else {
                System.out.println("User not found");
                return -1;
            }

        } catch (SQLException e) {
            System.err.println("Error during login: " + e.getMessage());
            return -1;
        }
    }

    public static User getUserById(int userId) {
        String sql = "SELECT id, username, email, created_at FROM users WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getTimestamp("created_at")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error getting user: " + e.getMessage());
        }
        return null;
    }

    public static class User {
        public int id;
        public String username;
        public String email;
        public Timestamp createdAt;

        public User(int id, String username, String email, Timestamp createdAt) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.createdAt = createdAt;
        }
    }
}