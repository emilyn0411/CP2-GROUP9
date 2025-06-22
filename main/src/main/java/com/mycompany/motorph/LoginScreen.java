package com.mycompany.motorph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JFrame {
    private JTextField employeeIDField;
    private JPasswordField passwordField;

    public LoginScreen() {
        setTitle("MotorPH Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1));

        Color backgroundColor = new Color(240, 248, 255); // Light blue background
        Color buttonColor = new Color(70, 130, 180); // Steel blue
        Color textColor = Color.WHITE;

        // Set background color for main content pane
        getContentPane().setBackground(backgroundColor);

        // Top label
        JLabel titleLabel = new JLabel("MotorPH Employee App", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel);

        // Input panel
        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.setBackground(backgroundColor);
        panel.add(new JLabel("Employee ID:"));
        employeeIDField = new JTextField();
        panel.add(employeeIDField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);
        add(panel);

        // Button panel
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(buttonColor);
        loginButton.setForeground(textColor);
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Tahoma", Font.BOLD, 12));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(backgroundColor);
        buttonPanel.add(loginButton);
        add(buttonPanel);

        // Login logic
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String employeeID = employeeIDField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();

                String role = Authenticator.authenticate(employeeID, password);
                if (role != null) {
                    dispose(); // close login window
                    new Dashboard(employeeID, role); // open dashboard with ID and role
                } else {
                    JOptionPane.showMessageDialog(LoginScreen.this,
                            "Invalid credentials", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginScreen();
    }
}
