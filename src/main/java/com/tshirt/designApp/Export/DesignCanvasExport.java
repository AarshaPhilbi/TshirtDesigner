package com.tshirt.designApp.Export;

import com.tshirt.designApp.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class DesignCanvasExport extends JFrame {

    private Design currentDesign;
    private CanvasPanel canvasPanel;
    private JButton exportPngButton;
    private Color tshirtColor;
    private String tshirtStyle;
    private boolean isFrontView;
    private final Map<String, BufferedImage> tshirtImages = new HashMap<>();

    public DesignCanvasExport(Design design, Color tshirtColor, String style, boolean isFrontView) {
        this.currentDesign = design;
        this.tshirtColor = tshirtColor;
        this.tshirtStyle = style;
        this.isFrontView = isFrontView;

        loadTshirtImages();
        setupUI();
    }

    private void loadTshirtImages() {
        try {
            tshirtImages.put("Cropped_Front", ImageIO.read(getClass().getResource("/images/cropFront.png")));
            tshirtImages.put("Regular_Front", ImageIO.read(getClass().getResource("/images/regularFront.png")));
            tshirtImages.put("Oversized_Front", ImageIO.read(getClass().getResource("/images/oversized_front.png")));
            tshirtImages.put("Cropped_Back", ImageIO.read(getClass().getResource("/images/cropBack.png")));
            tshirtImages.put("Regular_Back", ImageIO.read(getClass().getResource("/images/regularBack.png")));
            tshirtImages.put("Oversized_Back", ImageIO.read(getClass().getResource("/images/oversized_back.png")));
        } catch (Exception e) {
            System.err.println("T-shirt images not found: " + e.getMessage());
        }
    }

    private void setupUI() {
        setTitle("T-Shirt Design Export");
        setSize(900, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        canvasPanel = new CanvasPanel();
        canvasPanel.setPreferredSize(new Dimension(800, 700));
        canvasPanel.setBackground(Color.WHITE);

        exportPngButton = new JButton("Export as PNG");
        exportPngButton.addActionListener(e -> exportCanvas("png"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(exportPngButton);

        add(canvasPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    // **UPDATED: Now preserves outline**
    private void drawTshirt(Graphics2D g) {
        String key = tshirtStyle + (isFrontView ? "_Front" : "_Back");
        BufferedImage img = tshirtImages.get(key);

        int w = 400, h = 500, x = 200, y = 100;

        if (img != null) {
            if (tshirtColor.equals(Color.WHITE)) {
                g.drawImage(img, x, y, w, h, null);
            } else {
                // Create colored version
                BufferedImage temp = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = temp.createGraphics();
                g2.drawImage(img, 0, 0, w, h, null);
                g2.setComposite(AlphaComposite.SrcIn);
                g2.setColor(tshirtColor);
                g2.fillRect(0, 0, w, h);
                g2.dispose();

                // Draw colored t-shirt
                g.drawImage(temp, x, y, w, h, null);

                // **NEW: Draw outline on top**
                BufferedImage outline = extractOutline(img, w, h);
                if (outline != null) {
                    g.drawImage(outline, x, y, w, h, null);
                }
            }
        } else {
            g.setColor(tshirtColor);
            g.fillRect(250, 150, 300, 400);
        }
    }

    // **NEW METHOD: Extract and draw the outline**
    private BufferedImage extractOutline(BufferedImage original, int width, int height) {
        BufferedImage outline = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = outline.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw original image scaled
        g2.drawImage(original, 0, 0, width, height, null);

        // Apply a filter to keep only dark pixels (the outline)
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = outline.getRGB(x, y);
                int alpha = (rgb >> 24) & 0xFF;
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;

                // Calculate brightness
                int brightness = (red + green + blue) / 3;

                // Keep only dark pixels (outline) - threshold of 100
                if (brightness > 100 || alpha < 50) {
                    outline.setRGB(x, y, 0x00000000); // Make transparent
                }
            }
        }

        g2.dispose();
        return outline;
    }

    private class CanvasPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (currentDesign == null) return;

            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            drawTshirt(g2d);

            for (Layer layer : currentDesign.getLayers()) {
                if (layer.isVisible()) {
                    layer.draw(g2d);
                }
            }
        }
    }

    // --- NEW: Exports folder inside app home ---
    private File getExportsFolder() {
        String appHome = System.getProperty("user.dir"); // app home folder
        File exportsFolder = new File(appHome, "exports");
        if (!exportsFolder.exists()) {
            exportsFolder.mkdir();
        }
        return exportsFolder;
    }

    private void exportCanvas(String format) {
        try {
            int exportWidth = 800;
            int exportHeight = 700;

            BufferedImage image = new BufferedImage(exportWidth, exportHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();

            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, exportWidth, exportHeight);

            canvasPanel.paint(g2d);
            g2d.dispose();

            // Save file in app's exports folder
            File exportsFolder = getExportsFolder();
            File outputFile = new File(exportsFolder, "TshirtDesign_" + System.currentTimeMillis() + "." + format);

            if (!ImageIO.write(image, format, outputFile)) {
                throw new Exception("Unsupported image format: " + format);
            }

            JOptionPane.showMessageDialog(this,
                    "Design exported successfully!\nSaved at: " + outputFile.getAbsolutePath(),
                    "Export Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to export design.\nError: " + ex.getMessage(),
                    "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}