package com.tshirt.designApp.database;

import com.tshirt.designApp.Design;
import com.tshirt.designApp.Layer;
import com.tshirt.designApp.TextElement;
import com.tshirt.designApp.ImageElement;

import java.sql.*;
import java.util.List;
import java.awt.Rectangle;
import java.awt.Color;
// NOTE: java.awt imports are necessary because GraphicElement and Layer use them.


public class DesignDAO {

    // =========================================================================
    // 1. MAIN METHOD: SAVE DESIGN
    // =========================================================================

    public static boolean saveDesign(int userId, Design design, String style, String colorHex) {
        Connection conn = null;
        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // --- Look up IDs (IMPLEMENTATION REQUIRED) ---
            int styleId = getStyleId(style);
            int colorId = getColorId(colorHex);

            // 1. INSERT the main design record (into 'designs' table)
            String insertDesignSql =
                    "INSERT INTO designs (user_id, tshirt_style_id, tshirt_color_id, custom_color_hex) VALUES (?, ?, ?, ?)";

            int designId = -1;

            try (PreparedStatement ps = conn.prepareStatement(insertDesignSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, userId);
                ps.setInt(2, styleId);
                ps.setInt(3, colorId);
                ps.setString(4, colorHex);
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) {
                        conn.rollback();
                        System.err.println("Failed to retrieve generated design ID.");
                        return false;
                    }
                    designId = rs.getInt(1);
                }
            }

            // 2. Loop through all layers and save them individually
            for (Layer layer : design.getLayers()) {
                if (layer.getContent() instanceof TextElement) {
                    saveTextElement(conn, designId, (TextElement) layer.getContent(), layer);
                } else if (layer.getContent() instanceof ImageElement) {
                    saveImageElement(conn, designId, (ImageElement) layer.getContent(), layer);
                }
                // TODO: Add logic for ShapeElement here
            }

            conn.commit(); // Commit all inserts
            return true;

        } catch (SQLException e) {
            System.err.println("Database Error saving design: " + e.getMessage());
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }

    // =========================================================================
    // 2. HELPER METHODS: SAVE LAYERS
    // =========================================================================

    // Inside com.tshirt.designApp.database.DesignDAO.java

    private static void saveTextElement(Connection conn, int designId, TextElement textElement, Layer layer) throws SQLException {
        // CORRECT ACCESS: Use the content to get the bounding rectangle
        // (This is correct because GraphicElement has getBoundsAsRectangle())
        Rectangle bounds = layer.getContent().getBoundsAsRectangle();

        String sql = "INSERT INTO text_elements (design_id, content, font, size, color, position_x, position_y, rotation) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, designId);

            // ➡️ FIX 1: Use getText() for content
            ps.setString(2, textElement.getText());

            // ➡️ FIX 2: Use getFontFamily(), getFontSize(), getTextColor()
            ps.setString(3, textElement.getFontFamily());
            ps.setInt(4, textElement.getFontSize());
            ps.setString(5, String.format("#%06x", textElement.getTextColor().getRGB() & 0xFFFFFF)); // Convert Color to Hex

            ps.setInt(6, bounds.x);
            ps.setInt(7, bounds.y);
            ps.setDouble(8, 0.0); // Assuming no rotation tracking for now

            ps.executeUpdate();
        }
    }

    // Inside com.tshirt.designApp.database.DesignDAO.java

    private static void saveImageElement(Connection conn, int designId, ImageElement imageElement, Layer layer) throws SQLException {
        // CORRECT ACCESS: Use the content to get the bounding rectangle
        Rectangle bounds = layer.getContent().getBoundsAsRectangle();

        String sql = "INSERT INTO image_elements (design_id, image_path, width, height, position_x, position_y, rotation) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, designId);

            // This uses the confirmed getImagePath() method
            ps.setString(2, imageElement.getImagePath());

            ps.setInt(3, bounds.width);
            ps.setInt(4, bounds.height);
            ps.setInt(5, bounds.x);
            ps.setInt(6, bounds.y);
            ps.setDouble(7, 0.0); // Rotation (set to 0.0 as placeholder)

            ps.executeUpdate();
        }
    }

    // =========================================================================
    // 3. LOOKUP STUBS (MUST BE IMPLEMENTED)
    // =========================================================================

    private static int getStyleId(String styleName) throws SQLException {
        // TODO: Implement actual lookup from tshirt_styles table
        return 1;
    }

    private static int getColorId(String colorHex) throws SQLException {
        // TODO: Implement actual lookup from tshirt_colors table
        return 1;
    }

    // =========================================================================
    // 4. LOAD METHODS (TODO)
    // =========================================================================

    // TODO: Implement loadDesignsByUserId(int userId) for HomePage
    // TODO: Implement loadDesignById(int designId) for editing/reconstruction
}