package com.tshirt.designApp.ui;

import com.tshirt.designApp.Layer;
import com.tshirt.designApp.Design;
import com.tshirt.designApp.DesignAdapter;
import javax.swing.event.ChangeListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.imageio.ImageIO;
import com.tshirt.designApp.Export.DesignCanvasExport;

public class DesignCanvasPage extends JFrame {
    private final TshirtCanvasPanel canvasPanel;

    public DesignCanvasPage(String username, String style) {
        setTitle("T-Shirt Designer - " + username);
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        canvasPanel = new TshirtCanvasPanel(style);
        JScrollPane scrollPane = new JScrollPane(canvasPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(new ToolbarPanel(canvasPanel), BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DesignCanvasPage("TestUser", "Regular").setVisible(true));
    }
}

class ToolbarPanel extends JPanel {
    private final TshirtCanvasPanel canvas;

    public ToolbarPanel(TshirtCanvasPanel canvas) {
        this.canvas = canvas;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(280, 800));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(new Color(240, 245, 250));

        add(createSection("T-Shirt Options", createTshirtControls()));
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(createSection("Zoom", createZoomControls()));
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(createSection("Text Tools", createTextControls()));
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(createSection("Image Tools", createImageControls()));
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(createSection("Shape Tools", createShapeControls()));
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(createSection("Element Controls", createElementControls()));
        add(Box.createVerticalGlue());
        add(createSection("Export Options", createExportControls()));
    }

    private JPanel createSection(String title, JPanel content) {
        content.setBorder(new TitledBorder(title));
        return content;
    }

    private JPanel createTshirtControls() {
    JPanel panel = createStyledPanel();
    
    String[] styles = {"Regular", "Cropped", "Oversized"};
    // Declare as final or use array to work around lambda scope
    final JComboBox<String> styleCombo = createComboBox(styles, null);
    styleCombo.addActionListener(e -> 
        canvas.setTshirtStyle((String) styleCombo.getSelectedItem()));

    JToggleButton frontBtn = new JToggleButton("Front", true);
    JToggleButton backBtn = new JToggleButton("Back");
    ButtonGroup viewGroup = new ButtonGroup();
    viewGroup.add(frontBtn);
    viewGroup.add(backBtn);
    frontBtn.addActionListener(e -> canvas.setView(true));
    backBtn.addActionListener(e -> canvas.setView(false));

    JPanel viewPanel = createButtonPanel(frontBtn, backBtn);
    
    JButton colorBtn = createButton("Change T-Shirt Color", e -> {
        Color newColor = JColorChooser.showDialog(this, "Choose Color", canvas.getTshirtColor());
        if (newColor != null) canvas.setTshirtColor(newColor);
    });

    panel.add(createLabel("Style:"));
    panel.add(styleCombo);
    panel.add(Box.createRigidArea(new Dimension(0, 10)));
    panel.add(createLabel("View:"));
    panel.add(viewPanel);
    panel.add(Box.createRigidArea(new Dimension(0, 10)));
    panel.add(colorBtn);
    
    return panel;
}

    private JPanel createZoomControls() {
        JPanel panel = createStyledPanel();
        
        JLabel zoomLabel = new JLabel("Zoom: 100%");
        zoomLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton zoomOut = createButton("-", e -> updateZoom(-0.1, zoomLabel));
        JButton reset = createButton("Reset", e -> resetZoom(zoomLabel));
        JButton zoomIn = createButton("+", e -> updateZoom(0.1, zoomLabel));

        JPanel btnPanel = createButtonPanel(zoomOut, reset, zoomIn);
        
        panel.add(zoomLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnPanel);
        
        return panel;
    }

    private JPanel createTextControls() {
    JPanel panel = createStyledPanel();
    
    JButton addTextBtn = createButton("Add Text", e -> {
        String text = JOptionPane.showInputDialog(this, "Enter text:");
        if (text != null && !text.trim().isEmpty()) canvas.addTextElement(text);
    });

    String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    // Declare as final for lambda
    final JComboBox<String> fontCombo = createComboBox(fonts, null);
    fontCombo.addActionListener(e -> 
        canvas.setSelectedTextFont((String) fontCombo.getSelectedItem()));

    // Declare spinner as final for lambda
    final JSpinner fontSize = createSpinner(24, 8, 120, null);
    fontSize.addChangeListener(e -> 
        canvas.setSelectedTextSize((Integer) fontSize.getValue()));

    JButton colorBtn = createButton("Change Text Color", e -> {
        Color newColor = JColorChooser.showDialog(this, "Choose Font Color", Color.BLACK);
        if (newColor != null) canvas.setSelectedTextColor(newColor);
    });

    panel.add(addTextBtn);
    panel.add(Box.createRigidArea(new Dimension(0, 10)));
    panel.add(createLabel("Font:"));
    panel.add(fontCombo);
    panel.add(Box.createRigidArea(new Dimension(0, 5)));
    panel.add(createLabel("Size:"));
    panel.add(fontSize);
    panel.add(Box.createRigidArea(new Dimension(0, 10)));
    panel.add(colorBtn);
    
    return panel;
}

    private JPanel createImageControls() {
        JPanel panel = createStyledPanel();
        panel.add(createButton("Add Image", e -> addImage()));
        return panel;
    }

    private JPanel createShapeControls() {
        JPanel panel = createStyledPanel();
        
        String[] shapes = {"Rectangle", "Circle", "Triangle"};
        JComboBox<String> shapeCombo = createComboBox(shapes, null);
        
        JButton addShapeBtn = createButton("Add Shape", e -> {
            String shapeType = (String) shapeCombo.getSelectedItem();
            Color color = JColorChooser.showDialog(this, "Choose Shape Color", Color.RED);
            if (color != null) canvas.addShapeElement(shapeType.toLowerCase(), color);
        });

        panel.add(createLabel("Shape Type:"));
        panel.add(shapeCombo);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(addShapeBtn);
        
        return panel;
    }

    private JPanel createElementControls() {
        JPanel panel = createStyledPanel();
        
        panel.add(createButton("Bring Forward", e -> canvas.bringForward()));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(createButton("Send Backward", e -> canvas.sendBackward()));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createDangerButton("Delete Selected", e -> canvas.deleteSelected()));
        
        return panel;
    }

    
    private JPanel createExportControls() {
    JPanel panel = createStyledPanel();
    
    JButton exportBtn = createButton("Export Design", e -> {
        Design coreDesign = canvas.getDesignAdapter().getCoreDesign();
        Color tshirtColor = canvas.getTshirtColor();
        String tshirtStyle = canvas.getTshirtStyle();
        boolean isFrontView = true; // Use true for front view
        
        // UPDATED CONSTRUCTOR CALL:
        DesignCanvasExport exportWindow = new DesignCanvasExport(
            coreDesign, 
            tshirtColor, 
            tshirtStyle, 
            isFrontView
        );
        exportWindow.setVisible(true);
    });
    
    exportBtn.setBackground(new Color(40, 167, 69));
    exportBtn.setForeground(Color.WHITE);
    exportBtn.setFont(new Font("Arial", Font.BOLD, 14));

    panel.add(exportBtn);
    panel.add(Box.createRigidArea(new Dimension(0, 5)));
    panel.add(new JLabel("<html><center>Opens separate window<br>for PNG/JPEG export</center></html>"));
    
    return panel;
}

private void openExportWindow() {
    Design coreDesign = canvas.getDesignAdapter().getCoreDesign();
    Color tshirtColor = canvas.getTshirtColor();
    String tshirtStyle = canvas.getTshirtStyle();
    boolean isFrontView = true; // or get from canvas if available
    
    SwingUtilities.invokeLater(() -> {
        com.tshirt.designApp.Export.DesignCanvasExport exportWindow = 
            new com.tshirt.designApp.Export.DesignCanvasExport(
                coreDesign, 
                tshirtColor, 
                tshirtStyle, 
                isFrontView
            );
        exportWindow.setVisible(true);
    });
}

    // Helper methods
    private JPanel createStyledPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        return panel;
    }

    private JButton createButton(String text, ActionListener action) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(250, 30));
        if (action != null) btn.addActionListener(action);
        return btn;
    }

    private JButton createDangerButton(String text, ActionListener action) {
        JButton btn = createButton(text, action);
        btn.setBackground(new Color(220, 53, 69));
        btn.setForeground(Color.WHITE);
        return btn;
    }

    private JComboBox<String> createComboBox(String[] items, ActionListener action) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setMaximumSize(new Dimension(250, 30));
        if (action != null) combo.addActionListener(action);
        return combo;
    }

    private JSpinner createSpinner(int value, int min, int max, ChangeListener listener) {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(value, min, max, 1));
        spinner.setMaximumSize(new Dimension(100, 30));
        if (listener != null) spinner.addChangeListener(listener);
        return spinner;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel createButtonPanel(JComponent... components) {
        JPanel panel = new JPanel(new GridLayout(1, components.length, 5, 5));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(250, 35));
        for (JComponent comp : components) panel.add(comp);
        return panel;
    }

    private void updateZoom(double delta, JLabel label) {
        canvas.adjustZoom(delta);
        label.setText(String.format("Zoom: %.0f%%", canvas.getZoom() * 100));
    }

    private void resetZoom(JLabel label) {
        canvas.resetZoom();
        label.setText("Zoom: 100%");
    }

    private void addImage() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedImage img = ImageIO.read(fc.getSelectedFile());
                if (img != null) canvas.addImageElement(img);
            } catch (IOException ex) {
                showError("Error loading image");
            }
        }
    }

    // private void exportDesign(JComboBox<String> formatCombo) {
    //     String selected = (String) formatCombo.getSelectedItem();
    //     String format = selected.startsWith("PNG") ? "png" : 
    //                    selected.startsWith("JPEG") ? "jpg" : "pdf";
    //     String description = selected.split(" ")[0] + " Files";

    //     JFileChooser fc = new JFileChooser();
    //     fc.setFileFilter(new FileNameExtensionFilter(description, format));
    //     fc.setSelectedFile(new File("MyTshirtDesign." + format));

    //     if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
    //         File file = fc.getSelectedFile();
    //         if (!file.getName().toLowerCase().endsWith("." + format)) {
    //             file = new File(file.getAbsolutePath() + "." + format);
    //         }
    //         canvas.exportDesign(file, format);
    //         JOptionPane.showMessageDialog(this, "Design saved successfully!");
    //     }
    // }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

class TshirtCanvasPanel extends JPanel {
    private String tShirtStyle = "Regular";
    private Color tshirtColor = Color.WHITE; // Changed from WHITE to be visible
    private boolean isFrontView = true;
    private double zoomLevel = 1.0;
    
    private final DesignAdapter designAdapter = new DesignAdapter();
    
    private final Map<String, BufferedImage> tshirtImages = new HashMap<>();
    private Layer selectedLayer = null;
    private Point dragStartPoint;
    private int activeHandle = -1;

    public TshirtCanvasPanel(String style) {
        this.tShirtStyle = style;
        loadTshirtImages();
        setBackground(new Color(220, 220, 225));
        setupMouseListeners();
        updatePreferredSize();
    }

    private void setupMouseListeners() {
        MouseAdapter adapter = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                Point p = scale(e.getPoint());
                selectedLayer = designAdapter.selectElementAt(p);
                if (selectedLayer != null) {
                    activeHandle = getHandle(p, selectedLayer);
                    dragStartPoint = (activeHandle >= 0) ? p : p;
                }
                repaint();
            }

            public void mouseDragged(MouseEvent e) {
                Point p = scale(e.getPoint());
                if (selectedLayer != null && dragStartPoint != null) {
                    if (activeHandle >= 0) {
                        resizeElement(selectedLayer, activeHandle, p);
                    } else {
                        int dx = p.x - dragStartPoint.x, dy = p.y - dragStartPoint.y;
                        selectedLayer.moveContent(dx, dy);
                    }
                    dragStartPoint = p;
                    repaint();
                }
            }

            public void mouseReleased(MouseEvent e) {
                activeHandle = -1;
            }
        };
        addMouseListener(adapter);
        addMouseMotionListener(adapter);
    }

    // Core functionality methods
    public void addTextElement(String text) { designAdapter.addTextElement(text, 400, 400); repaint(); }
    public void addImageElement(BufferedImage image) { designAdapter.addImageElement(image, 350, 350); repaint(); }
    public void addShapeElement(String type, Color color) { designAdapter.addShapeElement(type, 300, 300, 100, 100, color); repaint(); }
    public void deleteSelected() { if (selectedLayer != null) { designAdapter.deleteLayer(selectedLayer); selectedLayer = null; repaint(); } }
    public void bringForward() { if (selectedLayer != null) { designAdapter.bringForward(selectedLayer); repaint(); } }
    public void sendBackward() { if (selectedLayer != null) { designAdapter.sendBackward(selectedLayer); repaint(); } }

    // Getters and setters
    public DesignAdapter getDesignAdapter() {
    return designAdapter;
}
    public String getTshirtStyle() { return tShirtStyle; }
    public void setTshirtStyle(String style) { tShirtStyle = style; repaint(); }
    public void setView(boolean front) { isFrontView = front; repaint(); }
    public Color getTshirtColor() { return tshirtColor; }
    public void setTshirtColor(Color color) { tshirtColor = color; repaint(); }
    public double getZoom() { return zoomLevel; }

    public void adjustZoom(double delta) {
        zoomLevel = Math.max(0.5, Math.min(3.0, zoomLevel + delta));
        updatePreferredSize();
        repaint();
    }

    public void resetZoom() {
        zoomLevel = 1.0;
        updatePreferredSize();
        repaint();
    }

    public void setSelectedTextFont(String font) {
        if (selectedLayer != null && selectedLayer.getContent() instanceof com.tshirt.designApp.TextElement) {
            ((com.tshirt.designApp.TextElement) selectedLayer.getContent()).setFontFamily(font);
            repaint();
        }
    }

    public void setSelectedTextSize(int size) {
        if (selectedLayer != null && selectedLayer.getContent() instanceof com.tshirt.designApp.TextElement) {
            ((com.tshirt.designApp.TextElement) selectedLayer.getContent()).setFontSize(size);
            repaint();
        }
    }

    public void setSelectedTextColor(Color color) {
        if (selectedLayer != null && selectedLayer.getContent() instanceof com.tshirt.designApp.TextElement) {
            ((com.tshirt.designApp.TextElement) selectedLayer.getContent()).setTextColor(color);
            repaint();
        }
    }

    // Drawing and rendering
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.scale(zoomLevel, zoomLevel);

        drawTshirt(g2d);
        
        if (isFrontView) {
            designAdapter.getLayersForDrawing().forEach(layer -> {
                layer.draw(g2d);
                if (layer == selectedLayer) drawSelection(g2d, layer);
            });
        }
    }

    private void drawTshirt(Graphics2D g) {
        String key = tShirtStyle + (isFrontView ? "_Front" : "_Back");
        BufferedImage img = tshirtImages.get(key);

        if (img != null) {
            int w = 400, h = 500, x = 200, y = 150;

            if (tshirtColor.equals(Color.WHITE)) {
                // Just draw the original image
                g.drawImage(img, x, y, w, h, null);
            } else {
                // Create a temporary image for the colored t-shirt
                BufferedImage temp = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = temp.createGraphics();

                // Draw the original image
                g2.drawImage(img, 0, 0, w, h, null);

                // Apply color using SrcIn composite (colors only non-transparent pixels)
                g2.setComposite(AlphaComposite.SrcIn);
                g2.setColor(tshirtColor);
                g2.fillRect(0, 0, w, h);
                g2.dispose();

                // Draw the colored t-shirt
                g.drawImage(temp, x, y, w, h, null);

                // **NEW: Draw the outline on top**
                // Create outline image by extracting edges
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

    private void drawSelection(Graphics2D g, Layer layer) {
        Rectangle bounds = layer.getContent().getBoundsAsRectangle();
        g.setColor(new Color(0, 120, 215));
        g.setStroke(new BasicStroke(2));
        g.draw(bounds);
        drawHandles(g, bounds);
    }

    private void drawHandles(Graphics2D g, Rectangle b) {
        g.setColor(new Color(0, 120, 215));
        int s = 8;
        int[][] handlePositions = {{b.x, b.y}, {b.x + b.width, b.y}, {b.x, b.y + b.height}, {b.x + b.width, b.y + b.height}};
        for (int[] pos : handlePositions) {
            g.fillRect(pos[0] - s/2, pos[1] - s/2, s, s);
        }
    }
    //commented because anagha's export function is doing the work

    // Export functionality
//     private void exportDesign(JComboBox<String> formatCombo) {
//     // Get the core design from Anne's canvas
//     Design coreDesign = canvas.getDesignAdapter().getCoreDesign();
    
//     // Open Anagha's export window with the current design
//     SwingUtilities.invokeLater(() -> {
//         com.tshirt.designApp.Export.DesignCanvasExport exportWindow = 
//             new com.tshirt.designApp.Export.DesignCanvasExport(coreDesign);
//         exportWindow.setVisible(true);
//     });
    
//     JOptionPane.showMessageDialog(this, 
//         "Opening Export Window...\nUse the buttons in the new window to export as PNG or JPEG.",
//         "Export", 
//         JOptionPane.INFORMATION_MESSAGE);
// }

//     private void exportAsImage(File file, String format) {
//     try {
//         // Use DesignAdapter to create the image (uses your core classes)
//         BufferedImage image = designAdapter.exportToImage(800, 800);
        
//         // Add t-shirt on top
//         Graphics2D g = image.createGraphics();
//         drawTshirt(g);
//         g.dispose();
        
//         ImageIO.write(image, format, file);
//     } catch (IOException e) {
//         showError("Error saving image: " + e.getMessage());
//     }
// }

//     private void exportAsPdf(File file) {
//     try {
//         // Use DesignAdapter to create the image (uses your core classes)
//         BufferedImage designImage = designAdapter.exportToImage(800, 800);
        
//         // Add t-shirt on top
//         Graphics2D g = designImage.createGraphics();
//         drawTshirt(g);
//         g.dispose();

//         // Create PDF
//         try (FileOutputStream fos = new FileOutputStream(file)) {
//             PdfDocument pdf = new PdfDocument(new PdfWriter(fos));
//             Document document = new Document(pdf, PageSize.A4);

//             ByteArrayOutputStream baos = new ByteArrayOutputStream();
//             ImageIO.write(designImage, "png", baos);
//             Image pdfImage = new Image(ImageDataFactory.create(baos.toByteArray()));
//             pdfImage.setAutoScale(true);
            
//             document.add(pdfImage);
//             document.close();
//         }
//     } catch (Exception e) {
//         showError("Error exporting to PDF: " + e.getMessage());
//     }
// }

    // Utility methods
     private void loadTshirtImages() {
        try {
            tshirtImages.put("Cropped_Front", ImageIO.read(getClass().getResource("/images/cropFront.png")));
            tshirtImages.put("Regular_Front", ImageIO.read(getClass().getResource("/images/regularFront.png")));
            tshirtImages.put("Oversized_Front", ImageIO.read(getClass().getResource("/images/oversized_front.png")));
            tshirtImages.put("Regular_Back", ImageIO.read(getClass().getResource("/images/regularBack.png")));
            tshirtImages.put("Cropped_Back", ImageIO.read(getClass().getResource("/images/cropBack.png")));
            tshirtImages.put("Oversized_Back", ImageIO.read(getClass().getResource("/images/oversized_back.png")));
        } catch (Exception e) {
            System.err.println("Images not found, using fallback");
        }
    }

    private void updatePreferredSize() {
        int size = (int) (800 * zoomLevel);
        setPreferredSize(new Dimension(size, size));
        revalidate();
    }

    private Point scale(Point p) {
        return new Point((int)(p.x / zoomLevel), (int)(p.y / zoomLevel));
    }

    private int getHandle(Point p, Layer layer) {
        Rectangle b = layer.getContent().getBoundsAsRectangle();
        int s = 8;
        Rectangle[] handles = {
            new Rectangle(b.x - s/2, b.y - s/2, s, s), new Rectangle(b.x + b.width - s/2, b.y - s/2, s, s),
            new Rectangle(b.x - s/2, b.y + b.height - s/2, s, s), new Rectangle(b.x + b.width - s/2, b.y + b.height - s/2, s, s)
        };
        for (int i = 0; i < 4; i++) if (handles[i].contains(p)) return i;
        return -1;
    }

    private void resizeElement(Layer layer, int handle, Point p) {
        Rectangle b = layer.getContent().getBoundsAsRectangle();
        int dx = p.x - dragStartPoint.x, dy = p.y - dragStartPoint.y;
        int[][] adjustments = {{dx, dy, -dx, -dy}, {0, dy, dx, -dy}, {dx, 0, -dx, dy}, {0, 0, dx, dy}};
        int[] adj = adjustments[handle];
        layer.resizeContent(b.x + adj[0], b.y + adj[1], b.width + adj[2], b.height + adj[3]);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}