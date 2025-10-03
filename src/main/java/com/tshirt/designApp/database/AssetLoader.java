package com.tshirt.designApp.database;

import java.sql.*;
import java.util.*;

/**
 * AssetLoader - Retrieves fonts, symbols, and colors from database
 * Used by GUI components to load design assets
 */
public class AssetLoader {

    /**
     * Font data structure
     */
    public static class Font {
        public int id;
        public String fontName;
        public String fontFamily;
        public boolean boldAvailable;
        public boolean italicAvailable;

        public Font(int id, String fontName, String fontFamily, boolean bold, boolean italic) {
            this.id = id;
            this.fontName = fontName;
            this.fontFamily = fontFamily;
            this.boldAvailable = bold;
            this.italicAvailable = italic;
        }

        @Override
        public String toString() {
            return fontName + " (" + fontFamily + ")";
        }
    }

    /**
     * Symbol data structure
     */
    public static class Symbol {
        public int id;
        public String name;
        public String imagePath;
        public String category;

        public Symbol(int id, String name, String imagePath, String category) {
            this.id = id;
            this.name = name;
            this.imagePath = imagePath;
            this.category = category;
        }

        @Override
        public String toString() {
            return name + " [" + category + "]";
        }
    }

    /**
     * Color data structure
     */
    public static class TshirtColor {
        public int id;
        public String name;
        public String hexCode;

        public TshirtColor(int id, String name, String hexCode) {
            this.id = id;
            this.name = name;
            this.hexCode = hexCode;
        }

        @Override
        public String toString() {
            return name + " (" + hexCode + ")";
        }
    }

    /**
     * Load all available fonts
     */
    public static List<Font> loadAllFonts() {
        List<Font> fonts = new ArrayList<>();
        String sql = "SELECT id, font_name, font_family, is_bold_available, is_italic_available FROM fonts ORDER BY font_name";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                fonts.add(new Font(
                        rs.getInt("id"),
                        rs.getString("font_name"),
                        rs.getString("font_family"),
                        rs.getBoolean("is_bold_available"),
                        rs.getBoolean("is_italic_available")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error loading fonts: " + e.getMessage());
        }

        return fonts;
    }

    /**
     * Load all symbols (or filter by category)
     */
    public static List<Symbol> loadSymbols(String category) {
        List<Symbol> symbols = new ArrayList<>();
        String sql = category == null ?
                "SELECT id, name, image_path, category FROM symbols_library ORDER BY category, name" :
                "SELECT id, name, image_path, category FROM symbols_library WHERE category = ? ORDER BY name";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (category != null) {
                pstmt.setString(1, category);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    symbols.add(new Symbol(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("image_path"),
                            rs.getString("category")
                    ));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error loading symbols: " + e.getMessage());
        }

        return symbols;
    }

    /**
     * Load all available symbol categories
     */
    public static List<String> loadSymbolCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM symbols_library ORDER BY category";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categories.add(rs.getString("category"));
            }

        } catch (SQLException e) {
            System.err.println("Error loading categories: " + e.getMessage());
        }

        return categories;
    }

    /**
     * Load all t-shirt colors
     */
    public static List<TshirtColor> loadAllColors() {
        List<TshirtColor> colors = new ArrayList<>();
        String sql = "SELECT id, name, hex_code FROM tshirt_colors ORDER BY name";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                colors.add(new TshirtColor(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("hex_code")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error loading colors: " + e.getMessage());
        }

        return colors;
    }

    /**
     * Load all t-shirt styles
     */
    public static Map<Integer, String> loadTshirtStyles() {
        Map<Integer, String> styles = new HashMap<>();
        String sql = "SELECT id, name FROM tshirt_styles ORDER BY name";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                styles.put(rs.getInt("id"), rs.getString("name"));
            }

        } catch (SQLException e) {
            System.err.println("Error loading t-shirt styles: " + e.getMessage());
        }

        return styles;
    }
}