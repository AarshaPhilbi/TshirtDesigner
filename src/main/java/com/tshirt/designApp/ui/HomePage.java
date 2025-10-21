package com.tshirt.designApp.ui;
import com.tshirt.designApp.ui.DesignCanvasPage;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class HomePage extends JFrame {

    private String currentUsername;
    private JPanel mainContentPanel;

    public HomePage(String username) {
        this.currentUsername = username;

        setTitle("Custom T-Shirt Designer - Home");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main layout
        setLayout(new BorderLayout());

        // Top Navigation Bar
        add(createNavigationBar(), BorderLayout.NORTH);

        // Main Content Area with Scroll
        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.setBackground(new Color(245, 245, 250));
        mainContentPanel.setAlignmentX(Component.CENTER_ALIGNMENT); //added by Anne

        // Add sections
        mainContentPanel.add(createNewDesignSection());
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContentPanel.add(createSavedDesignsSection());
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainContentPanel.add(createTemplatesSection());
        mainContentPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JScrollPane scrollPane = new JScrollPane(mainContentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createNavigationBar() {
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(new Color(99, 94, 59));
        navBar.setPreferredSize(new Dimension(1200, 70));
        navBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Left side - Logo/Title
        JLabel titleLabel = new JLabel("Custom T-Shirt Designer");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        // Right side - User info and logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);

        JLabel welcomeLabel = new JLabel("Welcome, " + currentUsername);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        welcomeLabel.setForeground(Color.WHITE);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> handleLogout());

        rightPanel.add(welcomeLabel);
        rightPanel.add(logoutButton);

        navBar.add(titleLabel, BorderLayout.WEST);
        navBar.add(rightPanel, BorderLayout.EAST);

        return navBar;
    }

    private JPanel createNewDesignSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(new Color(245, 245, 250));
        section.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Section Title
        JLabel titleLabel = new JLabel("Start New Design");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); //changed from left alignment

        JLabel subtitleLabel = new JLabel("Choose a T-shirt style to begin designing");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); //changed from left

        section.add(titleLabel);
        section.add(Box.createRigidArea(new Dimension(0, 5)));
        section.add(subtitleLabel);
        section.add(Box.createRigidArea(new Dimension(0, 25)));

        // T-Shirt Style Options Panel
        JPanel stylesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0)); // changed from left
        stylesPanel.setBackground(new Color(245, 245, 250));
        stylesPanel.setAlignmentX(Component.CENTER_ALIGNMENT); //changed from left

        stylesPanel.add(createStyleCard("Cropped", "Trendy cropped fit", new Color(255, 182, 193)));
        stylesPanel.add(createStyleCard("Regular", "Classic regular fit", new Color(135, 206, 250)));
        stylesPanel.add(createStyleCard("Oversized", "Relaxed Oversized fit", new Color(221, 160, 221)));

        section.add(stylesPanel);

        return section;
    }

    private JPanel createStyleCard(String styleName, String description, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(280, 320));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // T-Shirt Image Panel (MODIFIED)
        JPanel tshirtPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                try {
                    // Load image based on style name
                    String imagePath = "/images/" + styleName.toLowerCase() + ".jpeg";
                    java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(
                            getClass().getResource(imagePath)
                    );

                    // Draw the image centered
                    int imgWidth = 160;
                    int imgHeight = 160;
                    int x = (getWidth() - imgWidth) / 2;
                    int y = (getHeight() - imgHeight) / 2;
                    g2d.drawImage(img, x, y, imgWidth, imgHeight, null);

                } catch (Exception e) {
                    // Fallback to original drawing if image not found
                    g2d.setColor(accentColor);
                    int width = getWidth();
                    int height = getHeight();

                    // Body
                    g2d.fillRoundRect(width/4, height/3, width/2, height/2, 20, 20);

                    // Sleeves
                    g2d.fillOval(width/8, height/3, width/4, height/5);
                    g2d.fillOval(width*5/8, height/3, width/4, height/5);

                    // Neck
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(width/2 - 15, height/3 - 5, 30, 30);
                }
            }
        };
        tshirtPanel.setPreferredSize(new Dimension(250, 180));
        tshirtPanel.setBackground(Color.WHITE);

        // Rest of the code remains the same...
        // Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel nameLabel = new JLabel(styleName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descLabel.setForeground(new Color(120, 120, 120));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton selectButton = new JButton("Design Now");
        selectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectButton.setBackground(new Color(99, 94, 59));
        selectButton.setForeground(Color.WHITE);
        selectButton.setFont(new Font("Arial", Font.BOLD, 14));
        selectButton.setFocusPainted(false);
        selectButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        selectButton.addActionListener(e -> startNewDesign(styleName));

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(descLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        infoPanel.add(selectButton);

        card.add(tshirtPanel, BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.SOUTH);

        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(accentColor, 2),
                        BorderFactory.createEmptyBorder(14, 14, 14, 14)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
            }
        });

        return card;
    }

    private JPanel createSavedDesignsSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(new Color(245, 245, 250));
        section.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Section Title
        JLabel titleLabel = new JLabel("Your Saved Designs");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Continue editing your previous designs");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        section.add(titleLabel);
        section.add(Box.createRigidArea(new Dimension(0, 5)));
        section.add(subtitleLabel);
        section.add(Box.createRigidArea(new Dimension(0, 25)));

        // Saved Designs Grid
        JPanel designsGrid = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        designsGrid.setBackground(new Color(245, 245, 250));
        designsGrid.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Sample saved designs (TODO: Load from database)
        designsGrid.add(createSavedDesignCard("My Design 1", "Regular", "2 days ago"));
        designsGrid.add(createSavedDesignCard("Summer Vibes", "Cropped", "1 week ago"));
        designsGrid.add(createSavedDesignCard("Cool Quote", "Oversized", "2 weeks ago"));

        // Empty state if no designs
        if (designsGrid.getComponentCount() == 0) {
            JLabel emptyLabel = new JLabel("No saved designs yet. Start creating!");
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            emptyLabel.setForeground(new Color(150, 150, 150));
            designsGrid.add(emptyLabel);
        }

        section.add(designsGrid);

        return section;
    }

    private JPanel createSavedDesignCard(String designName, String style, String lastModified) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(250, 280));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Thumbnail (placeholder for actual design preview)
        JPanel thumbnail = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(240, 240, 245));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2d.setColor(new Color(99, 94, 59));
                g2d.setFont(new Font("Arial", Font.BOLD, 16));
                String text = "Design Preview";
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = getHeight() / 2;
                g2d.drawString(text, x, y);
            }
        };
        thumbnail.setPreferredSize(new Dimension(230, 150));

        // Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));

        JLabel nameLabel = new JLabel(designName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel styleLabel = new JLabel("Style: " + style);
        styleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        styleLabel.setForeground(new Color(100, 100, 100));
        styleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel dateLabel = new JLabel("Modified: " + lastModified);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(120, 120, 120));
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton editButton = new JButton("Edit");
        editButton.setBackground(new Color(70, 130, 180));
        editButton.setForeground(Color.WHITE);
        editButton.setFont(new Font("Arial", Font.BOLD, 12));
        editButton.setFocusPainted(false);
        editButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editButton.addActionListener(e -> editDesign(designName));

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 12));
        deleteButton.setFocusPainted(false);
        deleteButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteButton.addActionListener(e -> deleteDesign(designName));

        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(styleLabel);
        infoPanel.add(dateLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        infoPanel.add(buttonsPanel);

        card.add(thumbnail, BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createTemplatesSection() {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(new Color(245, 245, 250));
        section.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Section Title
        JLabel titleLabel = new JLabel("Ready-Made Templates");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Start with a template and customize it to your style");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        section.add(titleLabel);
        section.add(Box.createRigidArea(new Dimension(0, 5)));
        section.add(subtitleLabel);
        section.add(Box.createRigidArea(new Dimension(0, 25)));

        // Templates Grid
        JPanel templatesGrid = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        templatesGrid.setBackground(new Color(245, 245, 250));
        templatesGrid.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Sample templates
        templatesGrid.add(createTemplateCard("Vintage Rock", "Retro music theme", new Color(139, 69, 19)));
        templatesGrid.add(createTemplateCard("Minimal Quote", "Simple typography", new Color(50, 50, 50)));
        templatesGrid.add(createTemplateCard("Nature Vibes", "Green & earthy", new Color(85, 107, 47)));
        templatesGrid.add(createTemplateCard("Neon City", "Urban & vibrant", new Color(255, 20, 147)));

        section.add(templatesGrid);

        return section;
    }

    private JPanel createTemplateCard(String templateName, String description, Color themeColor) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(220, 260));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Template Preview
        JPanel preview = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background
                g2d.setColor(new Color(250, 250, 250));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                // Simple template representation
                g2d.setColor(themeColor);
                g2d.fillRoundRect(30, 40, getWidth()-60, 80, 8, 8);

                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 14));
                g2d.drawString("Template", 50, 85);
            }
        };
        preview.setPreferredSize(new Dimension(200, 140));

        // Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));

        JLabel nameLabel = new JLabel(templateName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        descLabel.setForeground(new Color(100, 100, 100));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton useButton = new JButton("Use Template");
        useButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        useButton.setBackground(new Color(40, 167, 69));
        useButton.setForeground(Color.WHITE);
        useButton.setFont(new Font("Arial", Font.BOLD, 12));
        useButton.setFocusPainted(false);
        useButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        useButton.addActionListener(e -> useTemplate(templateName));

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(descLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(useButton);

        card.add(preview, BorderLayout.CENTER);
        card.add(infoPanel, BorderLayout.SOUTH);

        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(themeColor, 2),
                        BorderFactory.createEmptyBorder(9, 9, 9, 9)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
            }
        });

        return card;
    }

    // Action Handlers
    // Action Handlers
    private void startNewDesign(String style) {
        // Close homepage and open design canvas with selected style
        this.dispose();

        SwingUtilities.invokeLater(() -> {
            new DesignCanvasPage(currentUsername, style).setVisible(true);
        });
    }

    private void editDesign(String designName) {
        this.dispose();
        SwingUtilities.invokeLater(() -> {
            new DesignCanvasPage(currentUsername, "Regular").setVisible(true);
            // TODO: Load the saved design
        });
    }

    private void deleteDesign(String designName) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete '" + designName + "'?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            // TODO: Delete from database
            JOptionPane.showMessageDialog(this,
                    "Design deleted successfully.",
                    "Deleted",
                    JOptionPane.INFORMATION_MESSAGE);

            // Refresh the page
            // reloadSavedDesigns();
        }
    }

    private void useTemplate(String templateName) {
        this.dispose();
        SwingUtilities.invokeLater(() -> {
            new DesignCanvasPage(currentUsername, "Regular").setVisible(true);
            // TODO: Pre-load template elements
        });
    }


    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            new com.tshirt.designApp.ui.SignInSignUpPage().setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HomePage("TestUser").setVisible(true);
        });
    }
}