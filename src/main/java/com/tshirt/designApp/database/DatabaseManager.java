package com.tshirt.designApp.database;

import java.io.*;
import java.nio.file.*;
import java.sql.*;

/**
 * Database Manager for Custom T-Shirt Designer
 * Handles database connection and initialization
 */
public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:C:/TshirtAppData/CustomTshirt.db";
    private static final String SCHEMA_FILE = "db/schema.sql";
    
    /**
     * Get database connection
     * @return Connection to SQLite database
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
    
    /**
     * Initialize database with tables and sample data
     * Call this method when your application starts
     */
    public static void initializeDatabase() {
        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            
            // Create database connection (creates file if doesn't exist)
            try (Connection conn = getConnection()) {
                System.out.println("Database connected successfully!");
                
                // Read and execute schema.sql
                String schema = loadSchemaFromFile();
                if (schema != null) {
                    executeSchema(conn, schema);
                    System.out.println("Database schema created successfully!");
                } else {
                    System.err.println("Could not load schema file!");
                }
                
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            }
            
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found!");
            System.err.println("Make sure to add sqlite-jdbc dependency to your project");
            e.printStackTrace();
        }
    }
    
    /**
     * Load schema.sql file from resources
     */
    private static String loadSchemaFromFile() {
        return """
        CREATE TABLE IF NOT EXISTS users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            username VARCHAR(255) NOT NULL UNIQUE,
            email VARCHAR(255) NOT NULL UNIQUE,
            password VARCHAR(255) NOT NULL,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        );
        
        CREATE TABLE IF NOT EXISTS tshirt_styles (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name VARCHAR(255) NOT NULL
        );
        
        CREATE TABLE IF NOT EXISTS tshirt_colors (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name VARCHAR(255) NOT NULL,
            hex_code VARCHAR(7) NOT NULL
        );
        
        CREATE TABLE IF NOT EXISTS symbols_library (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name VARCHAR(255) NOT NULL,
            image_path VARCHAR(500) NOT NULL,
            category VARCHAR(255)
        );
        
        CREATE TABLE IF NOT EXISTS designs (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            user_id INTEGER NOT NULL,
            tshirt_style_id INTEGER NOT NULL,
            tshirt_color_id INTEGER NOT NULL,
            custom_color_hex VARCHAR(7),
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            exported BOOLEAN DEFAULT 0,
            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
            FOREIGN KEY (tshirt_style_id) REFERENCES tshirt_styles(id),
            FOREIGN KEY (tshirt_color_id) REFERENCES tshirt_colors(id)
        );
        
        CREATE TABLE IF NOT EXISTS text_elements (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            design_id INTEGER NOT NULL,
            content VARCHAR(1000) NOT NULL,
            font VARCHAR(255),
            size INTEGER,
            color VARCHAR(7),
            position_x INTEGER,
            position_y INTEGER,
            rotation REAL DEFAULT 0,
            FOREIGN KEY (design_id) REFERENCES designs(id) ON DELETE CASCADE
        );
        
        CREATE TABLE IF NOT EXISTS image_elements (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            design_id INTEGER NOT NULL,
            image_path VARCHAR(500) NOT NULL,
            width INTEGER,
            height INTEGER,
            position_x INTEGER,
            position_y INTEGER,
            rotation REAL DEFAULT 0,
            symbol_id INTEGER,
            FOREIGN KEY (design_id) REFERENCES designs(id) ON DELETE CASCADE,
            FOREIGN KEY (symbol_id) REFERENCES symbols_library(id)
        );
        
        INSERT OR IGNORE INTO tshirt_styles (id, name) VALUES 
        (1, 'Regular T-Shirt'),
        (2, 'V-Neck'),
        (3, 'Tank Top'),
        (4, 'Long Sleeve'),
        (5, 'Hoodie');
        
        INSERT OR IGNORE INTO tshirt_colors (id, name, hex_code) VALUES 
        (1, 'White', '#FFFFFF'),
        (2, 'Black', '#000000'),
        (3, 'Red', '#FF0000'),
        (4, 'Blue', '#0000FF'),
        (5, 'Green', '#008000');
        
        INSERT OR IGNORE INTO symbols_library (id, name, image_path, category) VALUES 
        (1, 'Heart', '/symbols/heart.png', 'Love'),
        (2, 'Star', '/symbols/star.png', 'Shapes'),
        (3, 'Smiley Face', '/symbols/smiley.png', 'Emotions');
        """;
    }
    
    /**
     * Execute SQL schema (multiple statements)
     */
    private static void executeSchema(Connection conn, String schema) throws SQLException {
        // Split schema into individual statements
        String[] statements = schema.split(";");
        
        for (String statement : statements) {
            statement = statement.trim();
            if (!statement.isEmpty()) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.executeUpdate(statement);
                }
            }
        }
    }
    
    /**
     * Test database connection and show some sample data
     */
    public static void testDatabase() {
        try (Connection conn = getConnection()) {
            // Test query - show available t-shirt styles
            String sql = "SELECT * FROM tshirt_styles";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                System.out.println("\nAvailable T-Shirt Styles:");
                System.out.println("ID | Name");
                System.out.println("---|-----");
                while (rs.next()) {
                    System.out.println(rs.getInt("id") + "  | " + rs.getString("name"));
                }
            }
            
            // Test query - show available colors
            sql = "SELECT * FROM tshirt_colors LIMIT 5";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                System.out.println("\nAvailable Colors (first 5):");
                System.out.println("ID | Name    | Hex");
                System.out.println("---|---------|--------");
                while (rs.next()) {
                    System.out.printf("%-2d | %-7s | %s%n", 
                        rs.getInt("id"), 
                        rs.getString("name"), 
                        rs.getString("hex_code"));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Database test error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}