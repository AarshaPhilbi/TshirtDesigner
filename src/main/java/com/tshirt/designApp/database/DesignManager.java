package com.tshirt.designApp.database;

import java.sql.*;
import java.util.*;

public class DesignManager {
    
    // Design data structure
    public static class Design {
        public int id;
        public int userId;
        public int tshirtStyleId;
        public int tshirtColorId;
        public String customColorHex;
        public Timestamp createdAt;
        public boolean exported;
        
        public Design(int id, int userId, int styleId, int colorId, String customHex, Timestamp created, boolean exported) {
            this.id = id;
            this.userId = userId;
            this.tshirtStyleId = styleId;
            this.tshirtColorId = colorId;
            this.customColorHex = customHex;
            this.createdAt = created;
            this.exported = exported;
        }
    }
    
    // Text element structure
    public static class TextElement {
        public int id;
        public int designId;
        public String content;
        public String font;
        public int size;
        public String color;
        public int positionX;
        public int positionY;
        public float rotation;
    }
    
    // Image element structure
    public static class ImageElement {
        public int id;
        public int designId;
        public String imagePath;
        public int width;
        public int height;
        public int positionX;
        public int positionY;
        public float rotation;
        public Integer symbolId;
    }
    
    /**
     * CREATE - Save a new design
     */
    public static int saveNewDesign(int userId, int tshirtStyleId, int tshirtColorId, String customColorHex) {
        String sql = "INSERT INTO designs (user_id, tshirt_style_id, tshirt_color_id, custom_color_hex) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, userId);
            pstmt.setInt(2, tshirtStyleId);
            pstmt.setInt(3, tshirtColorId);
            pstmt.setString(4, customColorHex);
            
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int designId = rs.getInt(1);
                System.out.println("Design saved with ID: " + designId);
                return designId;
            }
            
        } catch (SQLException e) {
            System.err.println("Error saving design: " + e.getMessage());
        }
        return -1;
    }
    
    /**
     * READ - Get all designs for a user
     */
    public static List<Design> getUserDesigns(int userId) {
        List<Design> designs = new ArrayList<>();
        String sql = "SELECT * FROM designs WHERE user_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                designs.add(new Design(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("tshirt_style_id"),
                    rs.getInt("tshirt_color_id"),
                    rs.getString("custom_color_hex"),
                    rs.getTimestamp("created_at"),
                    rs.getBoolean("exported")
                ));
            }
            
        } catch (SQLException e) {
            System.err.println("Error loading designs: " + e.getMessage());
        }
        return designs;
    }
    
    /**
     * READ - Get a specific design
     */
    public static Design getDesign(int designId) {
        String sql = "SELECT * FROM designs WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, designId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Design(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("tshirt_style_id"),
                    rs.getInt("tshirt_color_id"),
                    rs.getString("custom_color_hex"),
                    rs.getTimestamp("created_at"),
                    rs.getBoolean("exported")
                );
            }
            
        } catch (SQLException e) {
            System.err.println("Error loading design: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * UPDATE - Update existing design
     */
    public static boolean updateDesign(int designId, int tshirtStyleId, int tshirtColorId, String customColorHex) {
        String sql = "UPDATE designs SET tshirt_style_id = ?, tshirt_color_id = ?, custom_color_hex = ? WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, tshirtStyleId);
            pstmt.setInt(2, tshirtColorId);
            pstmt.setString(3, customColorHex);
            pstmt.setInt(4, designId);
            
            int updated = pstmt.executeUpdate();
            return updated > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating design: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * DELETE - Remove a design
     */
    public static boolean deleteDesign(int designId) {
        String sql = "DELETE FROM designs WHERE id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, designId);
            int deleted = pstmt.executeUpdate();
            
            if (deleted > 0) {
                System.out.println("Design deleted: " + designId);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error deleting design: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Add text element to design
     */
    public static int addTextElement(int designId, String content, String font, int size, String color, int x, int y, float rotation) {
        String sql = "INSERT INTO text_elements (design_id, content, font, size, color, position_x, position_y, rotation) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, designId);
            pstmt.setString(2, content);
            pstmt.setString(3, font);
            pstmt.setInt(4, size);
            pstmt.setString(5, color);
            pstmt.setInt(6, x);
            pstmt.setInt(7, y);
            pstmt.setFloat(8, rotation);
            
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error adding text element: " + e.getMessage());
        }
        return -1;
    }
    
    /**
     * Add image element to design
     */
    public static int addImageElement(int designId, String imagePath, int width, int height, int x, int y, float rotation, Integer symbolId) {
        String sql = "INSERT INTO image_elements (design_id, image_path, width, height, position_x, position_y, rotation, symbol_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, designId);
            pstmt.setString(2, imagePath);
            pstmt.setInt(3, width);
            pstmt.setInt(4, height);
            pstmt.setInt(5, x);
            pstmt.setInt(6, y);
            pstmt.setFloat(7, rotation);
            if (symbolId != null) {
                pstmt.setInt(8, symbolId);
            } else {
                pstmt.setNull(8, Types.INTEGER);
            }
            
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error adding image element: " + e.getMessage());
        }
        return -1;
    }
    
    /**
     * Get text elements for a design
     */
    public static List<TextElement> getTextElements(int designId) {
        List<TextElement> elements = new ArrayList<>();
        String sql = "SELECT * FROM text_elements WHERE design_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, designId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                TextElement te = new TextElement();
                te.id = rs.getInt("id");
                te.designId = rs.getInt("design_id");
                te.content = rs.getString("content");
                te.font = rs.getString("font");
                te.size = rs.getInt("size");
                te.color = rs.getString("color");
                te.positionX = rs.getInt("position_x");
                te.positionY = rs.getInt("position_y");
                te.rotation = rs.getFloat("rotation");
                elements.add(te);
            }
            
        } catch (SQLException e) {
            System.err.println("Error loading text elements: " + e.getMessage());
        }
        return elements;
    }
    
    /**
     * Get image elements for a design
     */
    public static List<ImageElement> getImageElements(int designId) {
        List<ImageElement> elements = new ArrayList<>();
        String sql = "SELECT * FROM image_elements WHERE design_id = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, designId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                ImageElement ie = new ImageElement();
                ie.id = rs.getInt("id");
                ie.designId = rs.getInt("design_id");
                ie.imagePath = rs.getString("image_path");
                ie.width = rs.getInt("width");
                ie.height = rs.getInt("height");
                ie.positionX = rs.getInt("position_x");
                ie.positionY = rs.getInt("position_y");
                ie.rotation = rs.getFloat("rotation");
                ie.symbolId = (Integer) rs.getObject("symbol_id");
                elements.add(ie);
            }
            
        } catch (SQLException e) {
            System.err.println("Error loading image elements: " + e.getMessage());
        }
        return elements;
    }
}