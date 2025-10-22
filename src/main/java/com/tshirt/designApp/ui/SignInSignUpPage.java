package com.tshirt.designApp.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SignInSignUpPage extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;

    public SignInSignUpPage() {
        setTitle("T-Shirt Designer - Sign In / Sign Up");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Create and add both panels
        cardPanel.add(createSignInPanel(), "SIGNIN");
        cardPanel.add(createSignUpPanel(), "SIGNUP");

        add(cardPanel);
        cardLayout.show(cardPanel, "SIGNIN");
    }

    private JPanel createSignInPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Sign In", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Username
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(userLabel, gbc);

        JTextField userField = new JTextField(20);
        userField.setFont(new Font("Arial", Font.PLAIN, 16));
        userField.setPreferredSize(new Dimension(250, 35));
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(userField, gbc);

        // Password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(passLabel, gbc);

        JPasswordField passField = new JPasswordField(20);
        passField.setFont(new Font("Arial", Font.PLAIN, 16));
        passField.setPreferredSize(new Dimension(250, 35));
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(passField, gbc);

        // Sign In Button
        JButton signInBtn = new JButton("Sign In");
        signInBtn.setBackground(new Color(70, 130, 180));
        signInBtn.setForeground(Color.WHITE);
        signInBtn.setFont(new Font("Arial", Font.BOLD, 18));
        signInBtn.setPreferredSize(new Dimension(200, 45));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(signInBtn, gbc);

        // Switch to Sign Up
        JLabel switchLabel = new JLabel("Don't have an account? Sign Up");
        switchLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        switchLabel.setForeground(Color.BLUE);
        switchLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        switchLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardPanel, "SIGNUP");
            }
        });
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(switchLabel, gbc);

        // Sign In Action
        signInBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // TODO: Add actual authentication logic here
            boolean loginSuccess = authenticateUser(username, password);

            if (loginSuccess) {
                // SUCCESS: Close login and open homepage
                this.dispose();
                SwingUtilities.invokeLater(() -> {
                    new HomePage(username).setVisible(true);
                });
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel createSignUpPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Sign Up", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Username - CHANGED: Increased font from default to 18
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        panel.add(userLabel, gbc);

        // CHANGED: Increased text field from 15 to 20 columns and added font size
        JTextField userField = new JTextField(20);
        userField.setFont(new Font("Arial", Font.PLAIN, 16));
        userField.setPreferredSize(new Dimension(250, 35));
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(userField, gbc);

        // Email - CHANGED: Increased font from default to 18
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(emailLabel, gbc);

        // CHANGED: Increased text field from 15 to 20 columns and added font size
        JTextField emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 16));
        emailField.setPreferredSize(new Dimension(250, 35));
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(emailField, gbc);

        // Password - CHANGED: Increased font from default to 18
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(passLabel, gbc);

        // CHANGED: Increased password field from 15 to 20 columns and added font size
        JPasswordField passField = new JPasswordField(20);
        passField.setFont(new Font("Arial", Font.PLAIN, 16));
        passField.setPreferredSize(new Dimension(250, 35));
        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(passField, gbc);

        // Confirm Password - CHANGED: Increased font from default to 18
        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(confirmLabel, gbc);

        // CHANGED: Increased password field from 15 to 20 columns and added font size
        JPasswordField confirmField = new JPasswordField(20);
        confirmField.setFont(new Font("Arial", Font.PLAIN, 16));
        confirmField.setPreferredSize(new Dimension(250, 35));
        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(confirmField, gbc);

        // Sign Up Button - CHANGED: Increased font from 14 to 18 and added preferred size
        JButton signUpBtn = new JButton("Sign Up");
        signUpBtn.setBackground(new Color(40, 167, 69));
        signUpBtn.setForeground(Color.WHITE);
        signUpBtn.setFont(new Font("Arial", Font.BOLD, 18));
        signUpBtn.setPreferredSize(new Dimension(200, 45));
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        panel.add(signUpBtn, gbc);

        // Switch to Sign In - CHANGED: Increased font from default to 16
        JLabel switchLabel = new JLabel("Already have an account? Sign In");
        switchLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        switchLabel.setForeground(Color.BLUE);
        switchLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        switchLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(cardPanel, "SIGNIN");
            }
        });
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        panel.add(switchLabel, gbc);

        // Sign Up Action
        signUpBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passField.getPassword());
            String confirmPassword = new String(confirmField.getPassword());

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // TODO: Add actual registration logic here
            boolean registrationSuccess = registerUser(username, email, password);

            if (registrationSuccess) {
                JOptionPane.showMessageDialog(this, "Registration successful! Please sign in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                cardLayout.show(cardPanel, "SIGNIN");
                // Clear fields
                userField.setText("");
                emailField.setText("");
                passField.setText("");
                confirmField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Username may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    // TODO: Implement actual authentication
    private boolean authenticateUser(String username, String password) {
        // For now, accept any non-empty credentials
        // Later: Connect to database
        return !username.isEmpty() && !password.isEmpty();
    }

    // TODO: Implement actual registration
    private boolean registerUser(String username, String email, String password) {
        // For now, accept any registration
        // Later: Connect to database and check for duplicates
        return !username.isEmpty() && !email.isEmpty() && !password.isEmpty();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SignInSignUpPage().setVisible(true);
        });
    }
}