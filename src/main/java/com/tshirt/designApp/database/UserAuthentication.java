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
        Connection conn = null; // Defined outside try block

        System.out.println("DEBUG 1: Starting registration process."); // <-- DEBUG POINT 1

        try {
            conn = DatabaseManager.getConnection();
            System.out.println("DEBUG 2: Connection secured. AutoCommit forced ON."); // <-- DEBUG POINT 2
            conn.setAutoCommit(true); // Keep this for stability

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                System.out.println("DEBUG 3: PreparedStatement created."); // <-- DEBUG POINT 3

                pstmt.setString(1, username);
                pstmt.setString(2, email);
                pstmt.setString(3, hashPassword(password));

                int rowsAffected = pstmt.executeUpdate();
                System.out.println("DEBUG 4: ExecuteUpdate completed. Rows affected: " + rowsAffected); // <-- DEBUG POINT 4

                if (rowsAffected > 0) {
                    // If it reaches here, it works!
                    System.out.println("User registered successfully: " + username);
                    return true;
                } else {
                    System.err.println("ERROR: No rows affected by INSERT."); // <-- DEBUG POINT 5
                    return false;
                }
            }

        } catch (SQLException e) {
            System.err.println("DEBUG CATCH: Caught SQL Exception!");
            e.printStackTrace(); // Must ensure this prints
            return false;
        } catch (Exception e) {
            System.err.println("DEBUG CATCH: Caught GENERIC Exception!");
            e.printStackTrace(); // Catch everything else!
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException closeEx) {
                    System.err.println("Error closing connection.");
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