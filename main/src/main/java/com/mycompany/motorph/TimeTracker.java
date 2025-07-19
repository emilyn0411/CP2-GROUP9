package com.mycompany.motorph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class TimeTracker extends JPanel {
    private JLabel statusLabel;
    private JCheckBox timeFormatToggle;
    private LocalTime timeIn, timeOut;
    private String employeeID;
    private String employeeName;
    private BufferedImage backgroundImage;

    public TimeTracker(String employeeID) {
        this.employeeID = employeeID;
        this.employeeName = fetchEmployeeName(employeeID);

        // Load background image
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/images/MotorPH Timetracker Design.jpg")); // Path to your image
        } catch (IOException e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Time Tracker - " + employeeName, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE); // Make text visible on image
        add(titleLabel, BorderLayout.NORTH);

        // Center content panel
        JPanel centerPanel = new JPanel(new FlowLayout());
        centerPanel.setOpaque(false); // Transparent to see background

        JButton timeInButton = new JButton("Time In");
        JButton timeOutButton = new JButton("Time Out");
        timeFormatToggle = new JCheckBox("Use 12-hour format");
        statusLabel = new JLabel("Status: Not logged in");
        statusLabel.setForeground(Color.WHITE);

        timeInButton.addActionListener(e -> handleTimeIn());
        timeOutButton.addActionListener(e -> handleTimeOut());

        centerPanel.add(timeInButton);
        centerPanel.add(timeOutButton);
        centerPanel.add(timeFormatToggle);
        centerPanel.add(statusLabel);

        add(centerPanel, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void handleTimeIn() {
        timeIn = LocalTime.now().withSecond(0).withNano(0);
        String timeStr = formatTime(timeIn);
        boolean late = isLate(timeIn);

        if (late) {
            JOptionPane.showMessageDialog(this, "You are late! Time In: " + timeStr, "Late Notice", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "On time. Time In: " + timeStr);
        }

        statusLabel.setText("Time In at " + timeStr);
    }

    private void handleTimeOut() {
        if (timeIn == null) {
            JOptionPane.showMessageDialog(this, "Please Time In first.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        timeOut = LocalTime.now().withSecond(0).withNano(0);
        String timeStr = formatTime(timeOut);

        JOptionPane.showMessageDialog(this, "Time Out at " + timeStr);
        statusLabel.setText(statusLabel.getText() + " | Time Out at " + timeStr);

        saveTimeLog();
    }

    private void saveTimeLog() {
        LocalDate today = LocalDate.now();
        String period = getCutoffPeriod(today);

        Duration duration = Duration.between(timeIn, timeOut);
        long hoursWorked = duration.toHours();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("payroll_data.txt", true))) {
            writer.write(String.format("%s\t%s\t%s\t%s\t%s\t%d%n",
                    employeeID, employeeName, period, timeIn, timeOut, hoursWorked));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isLate(LocalTime time) {
        return time.isAfter(LocalTime.of(8, 0));
    }

    private String formatTime(LocalTime time) {
        return timeFormatToggle.isSelected()
                ? time.format(DateTimeFormatter.ofPattern("hh:mm a"))
                : time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private String getCutoffPeriod(LocalDate date) {
        int day = date.getDayOfMonth();
        String monthYear = date.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        return day <= 15 ? monthYear + "-15" : monthYear + "-30";
    }

    private String fetchEmployeeName(String empId) {
        try (BufferedReader reader = new BufferedReader(new FileReader("employee_data.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\t");
                if (fields[0].equals(empId)) {
                    return fields[2] + " " + fields[1]; // Firstname Lastname
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return " Employee";
    }
}