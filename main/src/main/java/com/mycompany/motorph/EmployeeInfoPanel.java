package com.mycompany.motorph;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class EmployeeInfoPanel extends JPanel {
    private BufferedImage backgroundImage;

    public EmployeeInfoPanel(String employeeID) {
        try {
            // Load image from resources (adjust path as needed)
            backgroundImage = ImageIO.read(getClass().getResource("/images/MotorPH Timetracker Design.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout());

        JLabel title = new JLabel("My Info", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        title.setForeground(Color.BLACK);  // ensure visible over background
        add(title, BorderLayout.NORTH);

        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setBackground(new Color(255, 255, 255, 200)); // slightly transparent white
        card.setOpaque(true);

        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 15, 10));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        try (BufferedReader br = new BufferedReader(new FileReader("employee_data.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\t");

                if (data[0].equals(employeeID)) {
                    String[] labels = {
                        "Employee ID", "Last Name", "First Name", "Birthday", "Address", "Phone Number",
                        "SSS #", "Philhealth #", "TIN #", "Pag-ibig #", "Status", "Position",
                        "Immediate Supervisor", "Basic Salary", "Rice Subsidy", "Phone Allowance",
                        "Clothing Allowance", "Gross Semi-monthly Rate", "Hourly Rate"
                    };

                    for (int i = 0; i < labels.length && i < data.length; i++) {
                        JLabel label = new JLabel(labels[i] + ": ");
                        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
                        label.setForeground(Color.BLACK);
                        infoPanel.add(label);

                        JLabel value = new JLabel(data[i]);
                        value.setFont(new Font("SansSerif", Font.BOLD, 14));
                        value.setForeground(Color.DARK_GRAY);
                        infoPanel.add(value);
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            infoPanel.add(new JLabel("Error loading employee data."));
        }

        card.add(infoPanel, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);
    }

    // Draw the background image
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}