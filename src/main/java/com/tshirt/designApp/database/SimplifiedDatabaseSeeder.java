package com.tshirt.designApp.database;

import java.sql.*;

public class SimplifiedDatabaseSeeder {

    public static void main(String[] args) {
        System.out.println("=== Populating T-Shirt Design Database (CustomInk Style) ===");

        DatabaseManager.initializeDatabase();
        seedFonts();
        seedSymbols();
        seedExtendedColors();
        displayAvailableAssets();
        fixImagePaths();
        cleanupDuplicates();

        System.out.println("=== Database ready for t-shirt design! ===");
    }

    public static void seedFonts() {
        String createFontsTable = "CREATE TABLE IF NOT EXISTS fonts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "font_name VARCHAR(255) NOT NULL, " +
                "font_family VARCHAR(100) NOT NULL, " +
                "is_bold_available BOOLEAN DEFAULT 0, " +
                "is_italic_available BOOLEAN DEFAULT 0" +
                ")";

        String insertFont = "INSERT OR IGNORE INTO fonts (font_name, font_family, is_bold_available, is_italic_available) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection()) {
            conn.createStatement().executeUpdate(createFontsTable);

            try (PreparedStatement pstmt = conn.prepareStatement(insertFont)) {
                addFont(pstmt, "Arial Bold", "Sans-serif", true, false);
                addFont(pstmt, "Helvetica", "Sans-serif", true, true);
                addFont(pstmt, "Impact", "Sans-serif", false, false);
                addFont(pstmt, "Times New Roman", "Serif", true, true);
                addFont(pstmt, "Georgia", "Serif", true, true);
                addFont(pstmt, "Comic Sans MS", "Casual", true, false);
                addFont(pstmt, "Courier New", "Monospace", true, true);
                addFont(pstmt, "Trebuchet MS", "Sans-serif", true, true);
                addFont(pstmt, "Verdana", "Sans-serif", true, true);
                addFont(pstmt, "Palatino", "Serif", true, true);

                System.out.println("✓ Added 10 fonts for text customization");
            }
        } catch (SQLException e) {
            System.err.println("Error adding fonts: " + e.getMessage());
        }
    }

    public static void seedSymbols() {
        String insertSymbol = "INSERT OR IGNORE INTO symbols_library (name, image_path, category) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSymbol)) {

            // Animals
            addSymbol(pstmt, "Eagle", "assets/symbols/animals/eagle.png", "Animals");
            addSymbol(pstmt, "Lion", "assets/symbols/animals/lion.png", "Animals");
            addSymbol(pstmt, "Dog", "assets/symbols/animals/dog.png", "Animals");
            addSymbol(pstmt, "Cat", "assets/symbols/animals/cat.png", "Animals");

            // Sports
            addSymbol(pstmt, "Soccer Ball", "assets/symbols/sports/soccer.png", "Sports");
            addSymbol(pstmt, "Basketball", "assets/symbols/sports/basketball.png", "Sports");
            addSymbol(pstmt, "Baseball", "assets/symbols/sports/baseball.png", "Sports");
            addSymbol(pstmt, "Football", "assets/symbols/sports/football.png", "Sports");
            addSymbol(pstmt, "Tennis", "assets/symbols/sports/tennis.png", "Sports");

            // Shapes & Symbols
            addSymbol(pstmt, "Star", "assets/symbols/shapes/star.png", "Shapes");
            addSymbol(pstmt, "Heart", "assets/symbols/shapes/heart.png", "Shapes");
            addSymbol(pstmt, "Circle", "assets/symbols/shapes/circle.png", "Shapes");
            addSymbol(pstmt, "Arrow", "assets/symbols/shapes/arrow.png", "Shapes");
            addSymbol(pstmt, "Lightning", "assets/symbols/shapes/lightning.png", "Shapes");

            // Business & Professional
            addSymbol(pstmt, "Building", "assets/symbols/business/building.png", "Business");
            addSymbol(pstmt, "Handshake", "assets/symbols/business/handshake.png", "Business");
            addSymbol(pstmt, "Graph", "assets/symbols/business/graph.png", "Business");

            // Music
            addSymbol(pstmt, "Music Note", "assets/symbols/music/note.png", "Music");
            addSymbol(pstmt, "Guitar", "assets/symbols/music/guitar.png", "Music");
            addSymbol(pstmt, "Microphone", "assets/symbols/music/microphone.png", "Music");

            // Transportation
            addSymbol(pstmt, "Car", "assets/symbols/transport/car.png", "Transportation");
            addSymbol(pstmt, "Motorcycle", "assets/symbols/transport/motorcycle.png", "Transportation");
            addSymbol(pstmt, "Plane", "assets/symbols/transport/plane.png", "Transportation");

            System.out.println("✓ Added symbols across 6 categories");

        } catch (SQLException e) {
            System.err.println("Error adding symbols: " + e.getMessage());
        }
    }

    public static void seedExtendedColors() {
        String insertColor = "INSERT OR IGNORE INTO tshirt_colors (name, hex_code) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertColor)) {

            addColor(pstmt, "Navy Blue", "#000080");
            addColor(pstmt, "Royal Blue", "#4169E1");
            addColor(pstmt, "Sky Blue", "#87CEEB");
            addColor(pstmt, "Forest Green", "#228B22");
            addColor(pstmt, "Lime Green", "#32CD32");
            addColor(pstmt, "Orange", "#FFA500");
            addColor(pstmt, "Purple", "#800080");
            addColor(pstmt, "Pink", "#FFC0CB");
            addColor(pstmt, "Maroon", "#800000");
            addColor(pstmt, "Brown", "#A52A2A");
            addColor(pstmt, "Gold", "#FFD700");
            addColor(pstmt, "Silver", "#C0C0C0");
            addColor(pstmt, "Charcoal", "#36454F");
            addColor(pstmt, "Cream", "#F5F5DC");
            addColor(pstmt, "Burgundy", "#800020");

            System.out.println("✓ Extended color palette to 20+ colors");

        } catch (SQLException e) {
            System.err.println("Error adding colors: " + e.getMessage());
        }
    }

    public static void displayAvailableAssets() {
        try (Connection conn = DatabaseManager.getConnection()) {

            System.out.println("\n=== Available Fonts ===");
            ResultSet fontRs = conn.createStatement().executeQuery("SELECT font_name FROM fonts LIMIT 5");
            while (fontRs.next()) {
                System.out.println("• " + fontRs.getString("font_name"));
            }
            System.out.println("... and more");

            System.out.println("\n=== Symbol Categories ===");
            ResultSet categoryRs = conn.createStatement().executeQuery("SELECT DISTINCT category, COUNT(*) as count FROM symbols_library GROUP BY category");
            while (categoryRs.next()) {
                System.out.println("• " + categoryRs.getString("category") + " (" + categoryRs.getInt("count") + " symbols)");
            }

            ResultSet colorRs = conn.createStatement().executeQuery("SELECT COUNT(*) as count FROM tshirt_colors");
            if (colorRs.next()) {
                System.out.println("\n=== Colors ===");
                System.out.println("• " + colorRs.getInt("count") + " colors available");
            }

        } catch (SQLException e) {
            System.err.println("Error displaying assets: " + e.getMessage());
        }
    }

    private static void addFont(PreparedStatement pstmt, String fontName, String family, boolean bold, boolean italic) throws SQLException {
        pstmt.setString(1, fontName);
        pstmt.setString(2, family);
        pstmt.setBoolean(3, bold);
        pstmt.setBoolean(4, italic);
        pstmt.executeUpdate();
    }

    private static void addSymbol(PreparedStatement pstmt, String name, String path, String category) throws SQLException {
        pstmt.setString(1, name);
        pstmt.setString(2, path);
        pstmt.setString(3, category);
        pstmt.executeUpdate();
    }

    private static void addColor(PreparedStatement pstmt, String name, String hexCode) throws SQLException {
        pstmt.setString(1, name);
        pstmt.setString(2, hexCode);
        pstmt.executeUpdate();
    }
    public static void fixImagePaths() {
        // First, reset all paths to remove TshirtDesigner prefix
        String resetPath = "UPDATE symbols_library SET image_path = REPLACE(image_path, 'TshirtDesigner/', '')";

        // Then add it back once
        String updatePath = "UPDATE symbols_library SET image_path = 'TshirtDesigner/' || image_path WHERE image_path NOT LIKE 'TshirtDesigner/%'";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(resetPath);
            stmt.executeUpdate(updatePath);
            System.out.println("Fixed image paths - removed duplicates");

        } catch (SQLException e) {
            System.err.println("Error fixing paths: " + e.getMessage());
        }
    }
    public static void cleanupDuplicates() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            // Delete duplicate fonts, keeping only the first occurrence
            stmt.executeUpdate("DELETE FROM fonts WHERE id NOT IN (SELECT MIN(id) FROM fonts GROUP BY font_name)");

            // Delete duplicate symbols, keeping only the first occurrence
            stmt.executeUpdate("DELETE FROM symbols_library WHERE id NOT IN (SELECT MIN(id) FROM symbols_library GROUP BY name, category)");

            // Delete duplicate colors, keeping only the first occurrence
            stmt.executeUpdate("DELETE FROM tshirt_colors WHERE id NOT IN (SELECT MIN(id) FROM tshirt_colors GROUP BY name, hex_code)");

            System.out.println("Cleaned up duplicate entries");

        } catch (SQLException e) {
            System.err.println("Error cleaning duplicates: " + e.getMessage());
        }
    }
}