package com.mycompany.motorph;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class LoginScreen extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public LoginScreen() {
        setTitle("MotorPH Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(null); // Needed to place background manually

        // Load and scale background image
        BufferedImage bgImage = null;
        try {
            bgImage = ImageIO.read(getClass().getResource("/images/MotorPH Login Design.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Image scaledImage = bgImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        JLabel backgroundLabel = new JLabel(new ImageIcon(scaledImage));
        backgroundLabel.setBounds(0, 0, getWidth(), getHeight());
        backgroundLabel.setLayout(null); // Important for absolute layout

        // Content panel layered over background
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(null);
        contentPanel.setBounds(0, 0, getWidth(), getHeight()); // Covers entire frame

        // Username Label and Field
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.BLACK);
        userLabel.setBounds(300, 200, 100, 25);
        contentPanel.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(400, 200, 150, 25);
        contentPanel.add(usernameField);

        // Password Label and Field
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.BLACK);
        passLabel.setBounds(300, 240, 100, 25);
        contentPanel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(400, 240, 150, 25);
        contentPanel.add(passwordField);

        // Login Button
        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(400, 280, 150, 30);
        loginBtn.setBackground(new Color(70, 130, 180));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Tahoma", Font.BOLD, 12));
        loginBtn.setFocusPainted(false);
        contentPanel.add(loginBtn);

        // Button action
        loginBtn.addActionListener(e -> authenticate());

        // Add content on top of background
        backgroundLabel.add(contentPanel);
        setContentPane(backgroundLabel);

        setVisible(true);
    }

    private void authenticate() {
        String employeeID = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        String role = Authenticator.authenticate(employeeID, password);
        if (role != null) {
            dispose();
            new Dashboard(employeeID, role);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid credentials", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new LoginScreen();
    }
}