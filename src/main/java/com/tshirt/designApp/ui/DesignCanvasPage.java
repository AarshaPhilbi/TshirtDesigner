package com.tshirt.designApp.ui;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DesignCanvasPage extends JFrame {
    private final TshirtCanvasPanel canvasPanel;

    public DesignCanvasPage(String username, String style) {
        setTitle("T-Shirt Designer - " + username);
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        canvasPanel = new TshirtCanvasPanel(style);
        JScrollPane scrollPane = new JScrollPane(canvasPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        ToolbarPanel toolbarPanel = new ToolbarPanel(canvasPanel);

        add(toolbarPanel, BorderLayout.WEST);
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

        add(createTshirtControls());
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(createZoomControls());
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(createTextControls());
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(createImageControls());
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(createElementControls());
        add(Box.createVerticalGlue());
        add(createExportControls());
    }

    private JPanel createTshirtControls() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("T-Shirt Options"));
        panel.setBackground(Color.WHITE);

        JLabel styleLabel = new JLabel("Style:");
        String[] styles = {"Regular", "Cropped", "Oversized"};
        JComboBox<String> styleComboBox = new JComboBox<>(styles);
        styleComboBox.setMaximumSize(new Dimension(250, 30));
        styleComboBox.addActionListener(e -> canvas.setTshirtStyle((String) styleComboBox.getSelectedItem()));

        JToggleButton frontButton = new JToggleButton("Front", true);
        JToggleButton backButton = new JToggleButton("Back");
        ButtonGroup viewGroup = new ButtonGroup();
        viewGroup.add(frontButton);
        viewGroup.add(backButton);

        frontButton.addActionListener(e -> canvas.setView(true));
        backButton.addActionListener(e -> canvas.setView(false));

        JPanel viewPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        viewPanel.setBackground(Color.WHITE);
        viewPanel.add(frontButton);
        viewPanel.add(backButton);
        viewPanel.setMaximumSize(new Dimension(250, 40));

        JButton colorButton = new JButton("Change T-Shirt Color");
        colorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(this, "Choose Color", canvas.getTshirtColor());
            if (newColor != null) canvas.setTshirtColor(newColor);
        });

        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(styleLabel);
        panel.add(styleComboBox);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(new JLabel("View:"));
        panel.add(viewPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(colorButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        return panel;
    }

    private JPanel createZoomControls() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("Zoom"));
        panel.setBackground(Color.WHITE);

        JLabel zoomLabel = new JLabel("Zoom: 100%");
        zoomLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setMaximumSize(new Dimension(250, 35));

        JButton zoomOutBtn = new JButton("-");
        JButton resetBtn = new JButton("Reset");
        JButton zoomInBtn = new JButton("+");

        zoomInBtn.addActionListener(e -> {
            canvas.adjustZoom(0.1);
            zoomLabel.setText(String.format("Zoom: %.0f%%", canvas.getZoom() * 100));
        });

        zoomOutBtn.addActionListener(e -> {
            canvas.adjustZoom(-0.1);
            zoomLabel.setText(String.format("Zoom: %.0f%%", canvas.getZoom() * 100));
        });

        resetBtn.addActionListener(e -> {
            canvas.resetZoom();
            zoomLabel.setText("Zoom: 100%");
        });

        buttonPanel.add(zoomOutBtn);
        buttonPanel.add(resetBtn);
        buttonPanel.add(zoomInBtn);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(zoomLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(buttonPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        return panel;
    }

    private JPanel createTextControls() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("Text Tools"));
        panel.setBackground(Color.WHITE);

        JButton addTextButton = new JButton("Add Text");
        addTextButton.addActionListener(e -> {
            String text = JOptionPane.showInputDialog(this, "Enter text:");
            if (text != null && !text.trim().isEmpty()) canvas.addTextElement(text);
        });

        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        JComboBox<String> fontComboBox = new JComboBox<>(fonts);
        fontComboBox.setMaximumSize(new Dimension(250, 30));
        fontComboBox.addActionListener(e -> canvas.setSelectedTextFont((String) fontComboBox.getSelectedItem()));

        JSpinner fontSizeSpinner = new JSpinner(new SpinnerNumberModel(24, 8, 120, 1));
        fontSizeSpinner.setMaximumSize(new Dimension(100, 30));
        fontSizeSpinner.addChangeListener(e -> canvas.setSelectedTextSize((Integer) fontSizeSpinner.getValue()));

        JButton fontColorButton = new JButton("Change Text Color");
        fontColorButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(this, "Choose Font Color", Color.BLACK);
            if (newColor != null) canvas.setSelectedTextColor(newColor);
        });

        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(addTextButton);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(new JLabel("Font:"));
        panel.add(fontComboBox);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(new JLabel("Size:"));
        panel.add(fontSizeSpinner);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(fontColorButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        return panel;
    }

    private JPanel createImageControls() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("Image Tools"));
        panel.setBackground(Color.WHITE);

        JButton addImageButton = new JButton("Add Image");
        addImageButton.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    BufferedImage img = ImageIO.read(fc.getSelectedFile());
                    if (img != null) canvas.addImageElement(img);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error loading image", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(addImageButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        return panel;
    }

    private JPanel createElementControls() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("Element Controls"));
        panel.setBackground(Color.WHITE);

        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(e -> canvas.deleteSelected());

        JButton bringForwardBtn = new JButton("Bring Forward");
        bringForwardBtn.addActionListener(e -> canvas.bringForward());

        JButton sendBackwardBtn = new JButton("Send Backward");
        sendBackwardBtn.addActionListener(e -> canvas.sendBackward());

        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(bringForwardBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(sendBackwardBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(deleteButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        return panel;
    }

    private JPanel createExportControls() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("Export Options"));
        panel.setBackground(Color.WHITE);

        String[] formats = {"PNG (Transparent)", "JPEG (White Background)", "PDF (Document)"};
        JComboBox<String> formatComboBox = new JComboBox<>(formats);
        formatComboBox.setMaximumSize(new Dimension(250, 30));

        JButton exportButton = new JButton("Export Design");
        exportButton.setBackground(new Color(40, 167, 69));
        exportButton.setForeground(Color.WHITE);
        exportButton.setFont(new Font("Arial", Font.BOLD, 14));

        exportButton.addActionListener(e -> {
            String selectedFormat = (String) formatComboBox.getSelectedItem();
            String format, description;

            if (selectedFormat.startsWith("PNG")) {
                format = "png";
                description = "PNG Images";
            } else if (selectedFormat.startsWith("JPEG")) {
                format = "jpg";
                description = "JPEG Images";
            } else {
                format = "pdf";
                description = "PDF Documents";
            }

            JFileChooser fc = new JFileChooser();
            fc.setAcceptAllFileFilterUsed(false);
            fc.addChoosableFileFilter(new FileNameExtensionFilter(description, format));
            fc.setSelectedFile(new File("MyTshirtDesign." + format));

            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith("." + format)) {
                    file = new File(file.getAbsolutePath() + "." + format);
                }
                canvas.exportDesign(file, format);
                JOptionPane.showMessageDialog(this, "Design saved successfully!");
            }
        });

        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(new JLabel("File Format:"));
        panel.add(formatComboBox);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(exportButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        return panel;
    }
}

class TshirtCanvasPanel extends JPanel {
    private String tShirtStyle;
    private Color tshirtColor = Color.WHITE;
    private boolean isFrontView = true;
    private final List<DesignElement> elements = new ArrayList<>();
    private DesignElement selectedElement = null;
    private Point dragStartPoint;
    private int activeHandle = -1;
    private double zoomLevel = 1.0;
    private final Map<String, BufferedImage> tshirtImages = new HashMap<>();

    public TshirtCanvasPanel(String style) {
        this.tShirtStyle = style;
        loadTshirtImages();
        setBackground(new Color(220, 220, 225));
        updatePreferredSize();

        MouseAdapter adapter = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                Point p = scale(e.getPoint());
                if (selectedElement != null) {
                    activeHandle = getHandle(p, selectedElement);
                    if (activeHandle >= 0) {
                        dragStartPoint = p;
                        return;
                    }
                }
                selectedElement = null;
                for (int i = elements.size() - 1; i >= 0; i--) {
                    if (elements.get(i).contains(p)) {
                        selectedElement = elements.get(i);
                        dragStartPoint = p;
                        elements.remove(i);
                        elements.add(selectedElement);
                        break;
                    }
                }
                repaint();
            }

            public void mouseDragged(MouseEvent e) {
                Point p = scale(e.getPoint());
                if (selectedElement != null && dragStartPoint != null) {
                    if (activeHandle >= 0) {
                        resizeElement(selectedElement, activeHandle, p);
                    } else {
                        selectedElement.move(p.x - dragStartPoint.x, p.y - dragStartPoint.y);
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

    private Point scale(Point p) {
        return new Point((int) (p.x / zoomLevel), (int) (p.y / zoomLevel));
    }

    private int getHandle(Point p, DesignElement el) {
        Rectangle b = el.getBounds();
        int s = 8;
        Rectangle[] handles = {
                new Rectangle(b.x - s / 2, b.y - s / 2, s, s),
                new Rectangle(b.x + b.width - s / 2, b.y - s / 2, s, s),
                new Rectangle(b.x - s / 2, b.y + b.height - s / 2, s, s),
                new Rectangle(b.x + b.width - s / 2, b.y + b.height - s / 2, s, s)
        };
        for (int i = 0; i < 4; i++) if (handles[i].contains(p)) return i;
        return -1;
    }

    private void resizeElement(DesignElement el, int handle, Point p) {
        Rectangle b = el.getBounds();
        int dx = p.x - dragStartPoint.x;
        int dy = p.y - dragStartPoint.y;
        switch (handle) {
            case 0:
                el.resize(b.x + dx, b.y + dy, b.width - dx, b.height - dy);
                break;
            case 1:
                el.resize(b.x, b.y + dy, b.width + dx, b.height - dy);
                break;
            case 2:
                el.resize(b.x + dx, b.y, b.width - dx, b.height + dy);
                break;
            case 3:
                el.resize(b.x, b.y, b.width + dx, b.height + dy);
                break;
        }
    }

    private void updatePreferredSize() {
        int size = (int) (800 * zoomLevel);
        setPreferredSize(new Dimension(size, size));
        revalidate();
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
            System.err.println("Images not found, using fallback");
        }
    }

    public String getTshirtStyle() {
        return tShirtStyle;
    }

    public void setTshirtStyle(String s) {
        tShirtStyle = s;
        repaint();
    }

    public void setView(boolean f) {
        isFrontView = f;
        repaint();
    }

    public Color getTshirtColor() {
        return tshirtColor;
    }

    public void setTshirtColor(Color c) {
        tshirtColor = c;
        repaint();
    }

    public double getZoom() {
        return zoomLevel;
    }

    public void adjustZoom(double d) {
        zoomLevel = Math.max(0.5, Math.min(3.0, zoomLevel + d));
        updatePreferredSize();
        repaint();
    }

    public void resetZoom() {
        zoomLevel = 1.0;
        updatePreferredSize();
        repaint();
    }

    public void addTextElement(String t) {
        elements.add(new TextElement(t, 400, 400));
        repaint();
    }

    public void addImageElement(BufferedImage i) {
        elements.add(new ImageElement(i, 350, 350));
        repaint();
    }

    public void deleteSelected() {
        if (selectedElement != null) {
            elements.remove(selectedElement);
            selectedElement = null;
            repaint();
        }
    }

    public void bringForward() {
        if (selectedElement != null) {
            int i = elements.indexOf(selectedElement);
            if (i < elements.size() - 1) {
                elements.remove(i);
                elements.add(i + 1, selectedElement);
                repaint();
            }
        }
    }

    public void sendBackward() {
        if (selectedElement != null) {
            int i = elements.indexOf(selectedElement);
            if (i > 0) {
                elements.remove(i);
                elements.add(i - 1, selectedElement);
                repaint();
            }
        }
    }

    public void setSelectedTextFont(String f) {
        if (selectedElement instanceof TextElement) {
            ((TextElement) selectedElement).setFontName(f);
            repaint();
        }
    }

    public void setSelectedTextSize(int s) {
        if (selectedElement instanceof TextElement) {
            ((TextElement) selectedElement).setFontSize(s);
            repaint();
        }
    }

    public void setSelectedTextColor(Color c) {
        if (selectedElement instanceof TextElement) {
            ((TextElement) selectedElement).setColor(c);
            repaint();
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.scale(zoomLevel, zoomLevel);

        drawTshirt(g2d);
        if (isFrontView) {
            for (DesignElement el : elements) {
                el.draw(g2d);
                if (el == selectedElement) {
                    g2d.setColor(new Color(0, 120, 215));
                    g2d.setStroke(new BasicStroke(2));
                    g2d.draw(el.getBounds());
                    drawHandles(g2d, el.getBounds());
                }
            }
        }
    }

    private void drawHandles(Graphics2D g, Rectangle b) {
        g.setColor(new Color(0, 120, 215));
        int s = 8;
        g.fillRect(b.x - s / 2, b.y - s / 2, s, s);
        g.fillRect(b.x + b.width - s / 2, b.y - s / 2, s, s);
        g.fillRect(b.x - s / 2, b.y + b.height - s / 2, s, s);
        g.fillRect(b.x + b.width - s / 2, b.y + b.height - s / 2, s, s);
    }

    private void drawTshirt(Graphics2D g) {
        String key = tShirtStyle + (isFrontView ? "_Front" : "_Back");
        BufferedImage img = tshirtImages.get(key);
        if (img != null) {
            int w = 400, h = 500;
            int x = 200, y = 150;

            if (tshirtColor.equals(Color.WHITE)) {
                g.drawImage(img, x, y, w, h, null);
                return;
            }

            BufferedImage tempImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = tempImage.createGraphics();

            g2.drawImage(img, 0, 0, w, h, null);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, 1.0f));
            g2.setColor(tshirtColor);
            g2.fillRect(0, 0, w, h);
            g2.dispose();

            g.drawImage(tempImage, x, y, w, h, null);

        } else {
            g.setColor(tshirtColor);
            g.fillRect(250, 150, 300, 400); // Fallback rectangle
        }
    }

    public void exportDesign(File file, String format) {
        if (format.equalsIgnoreCase("pdf")) {
            exportAsPdf(file);
            return;
        }

        int imageType = format.equalsIgnoreCase("png") ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        BufferedImage img = new BufferedImage(800, 800, imageType);
        Graphics2D g = img.createGraphics();

        if (format.equalsIgnoreCase("jpg")) {
            g.setColor(getBackground());
            g.fillRect(0, 0, 800, 800);
        }

        DesignElement temp = selectedElement;
        selectedElement = null;

        drawTshirt(g);
        for (DesignElement el : elements) el.draw(g);

        selectedElement = temp;
        g.dispose();

        try {
            ImageIO.write(img, format, file);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportAsPdf(File file) {
        // Create an image of the current design first
        BufferedImage designImage = new BufferedImage(800, 800, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = designImage.createGraphics();
        DesignElement temp = selectedElement;
        selectedElement = null;
        drawTshirt(g);
        for (DesignElement el : elements) el.draw(g);
        selectedElement = temp;
        g.dispose();

        // Now, embed this image into a PDF document
        try (FileOutputStream fos = new FileOutputStream(file)) {
            PdfWriter writer = new PdfWriter(fos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(designImage, "png", baos);
            byte[] imageBytes = baos.toByteArray();

            Image pdfImage = new Image(ImageDataFactory.create(imageBytes));

            // Scale image to fit on the page while maintaining aspect ratio
            pdfImage.setAutoScale(true);

            document.add(pdfImage);
            document.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error exporting to PDF: " + e.getMessage(), "PDF Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

abstract class DesignElement {
    protected int x, y, width, height;

    public DesignElement(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.width = Math.max(10, w);
        this.height = Math.max(10, h);
    }

    public abstract void draw(Graphics2D g);

    public boolean contains(Point p) {
        return getBounds().contains(p);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public void resize(int nx, int ny, int nw, int nh) {
        if (nw > 10 && nh > 10) {
            x = nx;
            y = ny;
            width = nw;
            height = nh;
        }
    }
}

class TextElement extends DesignElement {
    private String text;
    private Font font;
    private Color color = Color.BLACK;

    public TextElement(String t, int x, int y) {
        super(x, y, 100, 30);
        this.text = t;
        this.font = new Font("Arial", Font.BOLD, 24);
    }

    public void setFontName(String n) {
        font = new Font(n, font.getStyle(), font.getSize());
    }

    public void setFontSize(int s) {
        font = font.deriveFont((float) s);
    }

    public void setColor(Color c) {
        color = c;
    }

    public void draw(Graphics2D g) {
        g.setFont(font);
        g.setColor(color);
        FontMetrics fm = g.getFontMetrics();
        width = fm.stringWidth(text);
        height = fm.getHeight();
        g.drawString(text, x, y + fm.getAscent());
    }
}

class ImageElement extends DesignElement {
    private BufferedImage image;

    public ImageElement(BufferedImage img, int x, int y) {
        super(x, y, 200, (int) (200.0 / img.getWidth() * img.getHeight()));
        this.image = img;
    }

    public void draw(Graphics2D g) {
        g.drawImage(image, x, y, width, height, null);
    }
}

