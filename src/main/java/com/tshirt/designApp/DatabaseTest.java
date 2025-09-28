package com.tshirt.designApp;

import com.tshirt.designApp.database.DatabaseManager;

/**
 * Simple test class to initialize and test the database
 * Run this to set up your database for the first time
 */
public class DatabaseTest {
    
    public static void main(String[] args) {
        System.out.println("=== Custom T-Shirt Designer Database Setup ===");
        
        // Initialize database (creates tables and sample data)
        DatabaseManager.initializeDatabase();
        
        // Test the database by showing some data
        DatabaseManager.testDatabase();
        
        System.out.println("\n=== Database setup  complete! ===");
        System.out.println("Your CustomTshirt.db file has been created.");
        System.out.println("You can now use the database in your application.");
    }
}