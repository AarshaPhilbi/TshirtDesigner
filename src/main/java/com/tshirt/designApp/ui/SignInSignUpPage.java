package com.tshirt.designApp.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SignInSignUpPage extends JFrame {

    private JPanel mainPanel;
    private CardLayout cardLayout;

    // Sign In Components
    private JTextField signInUsernameField;
    private JPasswordField signInPasswordField;
    private JButton signInButton;
    private JButton goToSignUpButton;

    // Sign Up Components
    private JTextField signUpUsernameField;
    private JTextField signUpEmailField;
    private JPasswordField signUpPasswordField;
    private JPasswordField signUpConfirmPasswordField;
    private JButton signUpButton;
    private JButton goToSignInButton;

    public SignInSignUpPage() {
        setTitle("Custom T-Shirt Designer - Login");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        // CardLayout to switch between Sign In and Sign Up
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Create both panels
        JPanel signInPanel = createSignInPanel();
        JPanel signUpPanel = createSignUpPanel();

        mainPanel.add(signInPanel, "SignIn");
        mainPanel.add(signUpPanel, "SignUp");

        add(mainPanel);

        // Show Sign In by default
        cardLayout.show(mainPanel, "SignIn");
    }

    private JPanel createSignInPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(240, 240, 245));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setPreferredSize(new Dimension(450, 100));
        JLabel headerLabel = new JLabel("Sign In");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 32));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(new Color(240, 240, 245));
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(usernameLabel, gbc);

        gbc.gridy = 1;
        signInUsernameField = new JTextField(20);
        signInUsernameField.setPreferredSize(new Dimension(300, 35));
        signInUsernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(signInUsernameField, gbc);

        // Password
        gbc.gridy = 2;
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(passwordLabel, gbc);

        gbc.gridy = 3;
        signInPasswordField = new JPasswordField(20);
        signInPasswordField.setPreferredSize(new Dimension(300, 35));
        signInPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(signInPasswordField, gbc);

        // Sign In Button
        gbc.gridy = 4;
        gbc.insets = new Insets(30, 10, 10, 10);
        signInButton = new JButton("Sign In");
        signInButton.setPreferredSize(new Dimension(300, 40));
        signInButton.setBackground(new Color(70, 130, 180));
        signInButton.setForeground(Color.WHITE);
        signInButton.setFont(new Font("Arial", Font.BOLD, 16));
        signInButton.setFocusPainted(false);
        signInButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signInButton.addActionListener(e -> handleSignIn());
        formPanel.add(signInButton, gbc);

        // Switch to Sign Up
        gbc.gridy = 5;
        gbc.insets = new Insets(20, 10, 10, 10);
        JPanel switchPanel = new JPanel();
        switchPanel.setBackground(new Color(240, 240, 245));
        JLabel switchLabel = new JLabel("Don't have an account? ");
        switchLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        goToSignUpButton = new JButton("Sign Up");
        goToSignUpButton.setFont(new Font("Arial", Font.BOLD, 12));
        goToSignUpButton.setForeground(new Color(70, 130, 180));
        goToSignUpButton.setBorderPainted(false);
        goToSignUpButton.setContentAreaFilled(false);
        goToSignUpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        goToSignUpButton.addActionListener(e -> cardLayout.show(mainPanel, "SignUp"));
        switchPanel.add(switchLabel);
        switchPanel.add(goToSignUpButton);
        formPanel.add(switchPanel, gbc);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSignUpPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(240, 240, 245));

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(34, 139, 34));
        headerPanel.setPreferredSize(new Dimension(450, 100));
        JLabel headerLabel = new JLabel("Sign Up");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 32));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(new Color(240, 240, 245));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(usernameLabel, gbc);

        gbc.gridy = 1;
        signUpUsernameField = new JTextField(20);
        signUpUsernameField.setPreferredSize(new Dimension(300, 35));
        signUpUsernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(signUpUsernameField, gbc);

        // Email
        gbc.gridy = 2;
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(emailLabel, gbc);

        gbc.gridy = 3;
        signUpEmailField = new JTextField(20);
        signUpEmailField.setPreferredSize(new Dimension(300, 35));
        signUpEmailField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(signUpEmailField, gbc);

        // Password
        gbc.gridy = 4;
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(passwordLabel, gbc);

        gbc.gridy = 5;
        signUpPasswordField = new JPasswordField(20);
        signUpPasswordField.setPreferredSize(new Dimension(300, 35));
        signUpPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(signUpPasswordField, gbc);

        // Confirm Password
        gbc.gridy = 6;
        JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        confirmPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(confirmPasswordLabel, gbc);

        gbc.gridy = 7;
        signUpConfirmPasswordField = new JPasswordField(20);
        signUpConfirmPasswordField.setPreferredSize(new Dimension(300, 35));
        signUpConfirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(signUpConfirmPasswordField, gbc);

        // Sign Up Button
        gbc.gridy = 8;
        gbc.insets = new Insets(20, 10, 10, 10);
        signUpButton = new JButton("Sign Up");
        signUpButton.setPreferredSize(new Dimension(300, 40));
        signUpButton.setBackground(new Color(34, 139, 34));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setFont(new Font("Arial", Font.BOLD, 16));
        signUpButton.setFocusPainted(false);
        signUpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpButton.addActionListener(e -> handleSignUp());
        formPanel.add(signUpButton, gbc);

        // Switch to Sign In
        gbc.gridy = 9;
        gbc.insets = new Insets(15, 10, 10, 10);
        JPanel switchPanel = new JPanel();
        switchPanel.setBackground(new Color(240, 240, 245));
        JLabel switchLabel = new JLabel("Already have an account? ");
        switchLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        goToSignInButton = new JButton("Sign In");
        goToSignInButton.setFont(new Font("Arial", Font.BOLD, 12));
        goToSignInButton.setForeground(new Color(34, 139, 34));
        goToSignInButton.setBorderPainted(false);
        goToSignInButton.setContentAreaFilled(false);
        goToSignInButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        goToSignInButton.addActionListener(e -> cardLayout.show(mainPanel, "SignIn"));
        switchPanel.add(switchLabel);
        switchPanel.add(goToSignInButton);
        formPanel.add(switchPanel, gbc);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);

        return panel;
    }

    private void handleSignIn() {
        String username = signInUsernameField.getText().trim();
        String password = new String(signInPasswordField.getPassword());

        // Validation
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // TODO: Add database authentication logic here
        // For now, just show a success message
        JOptionPane.showMessageDialog(this,
                "Sign In functionality will be connected to database.\nUsername: " + username,
                "Sign In",
                JOptionPane.INFORMATION_MESSAGE);

        // After successful login, you would navigate to HomePage
        // this.dispose();
        // new HomePage(username).setVisible(true);
    }

    private void handleSignUp() {
        String username = signUpUsernameField.getText().trim();
        String email = signUpEmailField.getText().trim();
        String password = new String(signUpPasswordField.getPassword());
        String confirmPassword = new String(signUpConfirmPasswordField.getPassword());

        // Validation
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Passwords do not match.",
                    "Password Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this,
                    "Password must be at least 6 characters long.",
                    "Password Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Basic email validation
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid email address.",
                    "Email Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // TODO: Add database registration logic here
        // For now, just show a success message
        JOptionPane.showMessageDialog(this,
                "Sign Up functionality will be connected to database.\nUsername: " + username + "\nEmail: " + email,
                "Sign Up Success",
                JOptionPane.INFORMATION_MESSAGE);

        // Clear fields and switch to sign in
        signUpUsernameField.setText("");
        signUpEmailField.setText("");
        signUpPasswordField.setText("");
        signUpConfirmPasswordField.setText("");
        cardLayout.show(mainPanel, "SignIn");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SignInSignUpPage().setVisible(true);
        });
    }
}