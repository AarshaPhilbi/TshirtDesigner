package com.tshirt.designApp.ui;

import com.tshirt.designApp.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class DesignCanvasExport extends JFrame {

    private Design currentDesign;
    private CanvasPanel canvasPanel;
    private JButton exportPngButton;
    private JButton exportJpegButton;

    public DesignCanvasExport(Design design) {
        this.currentDesign = design;

        setTitle("T-Shirt Design Canvas");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Canvas panel
        canvasPanel = new CanvasPanel();
        canvasPanel.setPreferredSize(new Dimension(600, 400));
        canvasPanel.setBackground(Color.WHITE);

        // Export buttons
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

    private class CanvasPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (currentDesign == null) return;

            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.decode(currentDesign.getBackgroundColor()));
            g2d.fillRect(0, 0, getWidth(), getHeight());

            for (Layer layer : currentDesign.getLayers()) {
                if (!layer.isVisible()) continue;
                GraphicElement content = layer.getContent();

                if (content instanceof Shape) {
                    Shape s = (Shape) content;
                    g2d.setColor(s.getFillColor());
                    switch (s.getShapeType().toLowerCase()) {
                        case "rectangle":
                            g2d.fillRect((int) s.getPosition().getX(), (int) s.getPosition().getY(),
                                    (int) s.getBounds().getWidth(), (int) s.getBounds().getHeight());
                            break;
                        case "circle":
                            g2d.fillOval((int) s.getPosition().getX(), (int) s.getPosition().getY(),
                                    (int) s.getBounds().getWidth(), (int) s.getBounds().getHeight());
                            break;
                    }
                } else if (content instanceof TextElement) {
                    TextElement t = (TextElement) content;
                    g2d.setColor(t.getFillColor());
                    g2d.setFont(new Font(t.getFontFamily(), Font.PLAIN, t.getFontSize()));
                    g2d.drawString(t.getText(), (int) t.getPosition().getX(),
                            (int) t.getPosition().getY() + t.getFontSize());
                } else if (content instanceof ImageElement) {
                    ImageElement img = (ImageElement) content;
                    try {
                        Image image = new ImageIcon(img.getImagePath()).getImage();
                        g2d.drawImage(image, (int) img.getPosition().getX(), (int) img.getPosition().getY(),
                                (int) img.getBounds().getWidth(), (int) img.getBounds().getHeight(), null);
                    } catch (Exception ex) {
                        System.err.println("Error loading image: " + img.getImagePath());
                    }
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

            String homeDir = System.getProperty("user.home");
            String fileName = "TshirtDesign_" + System.currentTimeMillis() + "." + format;
            File outputFile = new File(homeDir, fileName);

            boolean result = ImageIO.write(image, format, outputFile);
            if (!result) throw new Exception("Unsupported image format: " + format);

            JOptionPane.showMessageDialog(this,
                    "Design exported successfully!\nSaved at: " + outputFile.getAbsolutePath(),
                    "Export Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to export design.\nError: " + ex.getMessage(),
                    "Export Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Design emptyDesign = new Design(600, 400);
        SwingUtilities.invokeLater(() -> new DesignCanvasExport(emptyDesign));
    }
}
