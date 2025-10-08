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
    private JButton exportJpegButton;
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
            tshirtImages.put("Regular_Back", ImageIO.read(getClass().getResource("/images/regularBack.png")));
            tshirtImages.put("Cropped_Back", ImageIO.read(getClass().getResource("/images/cropBack.png")));
            tshirtImages.put("Oversized_Back", ImageIO.read(getClass().getResource("/images/oversized_back.png")));
        } catch (Exception e) {
            System.err.println("Export: T-shirt images not found");
        }
    }

    private void setupUI() {
        setTitle("T-Shirt Design Export");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        canvasPanel = new CanvasPanel();
        canvasPanel.setPreferredSize(new Dimension(600, 400));
        canvasPanel.setBackground(Color.WHITE);

        exportPngButton = new JButton("Export as PNG");
        exportPngButton.addActionListener(e -> exportCanvas("png"));

        exportJpegButton = new JButton("Export as JPEG");
        exportJpegButton.addActionListener(e -> exportCanvas("jpeg"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(exportPngButton);
        buttonPanel.add(exportJpegButton);

        add(canvasPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void drawTshirt(Graphics2D g) {
        String key = tshirtStyle + (isFrontView ? "_Front" : "_Back");
        BufferedImage img = tshirtImages.get(key);
        
        if (img != null) {
            int w = 400, h = 500, x = 200, y = 150;
            if (tshirtColor.equals(Color.WHITE)) {
                g.drawImage(img, x, y, w, h, null);
            } else {
                BufferedImage temp = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = temp.createGraphics();
                g2.drawImage(img, 0, 0, w, h, null);
                g2.setComposite(AlphaComposite.SrcIn);
                g2.setColor(tshirtColor);
                g2.fillRect(0, 0, w, h);
                g2.dispose();
                g.drawImage(temp, x, y, w, h, null);
            }
        } else {
            g.setColor(tshirtColor);
            g.fillRect(250, 150, 300, 400);
        }
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

    private void exportCanvas(String format) {
        try {
            BufferedImage image = new BufferedImage(canvasPanel.getWidth(),
                    canvasPanel.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();
            canvasPanel.paint(g2d);
            g2d.dispose();

            File outputFile = new File(System.getProperty("user.home"), 
                "TshirtDesign_" + System.currentTimeMillis() + "." + format);

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