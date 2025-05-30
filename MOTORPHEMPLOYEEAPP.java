/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.motorphemployeeapp;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
/**
 *
 * @author DAYANG GWAPA
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MOTORPHEMPLOYEEAPP {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MOTORPHEMPLOYEEAPP().createAndShowGUI());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("MotorPH Employee App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Dashboard Tab
        JPanel dashboardPanel = new JPanel();
        dashboardPanel.add(new JLabel("Welcome to MotorPH Employee System!"));
        tabbedPane.addTab("Dashboard", dashboardPanel);

        // Profile Tab
        JPanel profilePanel = new JPanel(new GridLayout(5, 2, 10, 10));
        profilePanel.add(new JLabel("Employee No:"));
        profilePanel.add(new JTextField());
        profilePanel.add(new JLabel("Name:"));
        profilePanel.add(new JTextField());
        profilePanel.add(new JLabel("Position:"));
        profilePanel.add(new JTextField());
        profilePanel.add(new JLabel("Department:"));
        profilePanel.add(new JTextField());
        profilePanel.add(new JLabel("Email:"));
        profilePanel.add(new JTextField());
        tabbedPane.addTab("Profile", profilePanel);

        // Time Tracker Tab
        JPanel timePanel = new JPanel(new FlowLayout());
        JLabel timeLabel = new JLabel("Current Time: " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        JButton timeInBtn = new JButton("Time In");
        JButton timeOutBtn = new JButton("Time Out");
        
        timeInBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Time In recorded at: " + LocalTime.now()));
        timeOutBtn.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Time Out recorded at: " + LocalTime.now()));

        timePanel.add(timeLabel);
        timePanel.add(timeInBtn);
        timePanel.add(timeOutBtn);
        tabbedPane.addTab("Time Tracker", timePanel);

        // Payroll Tab
        String[] columns = {"Period", "Gross Pay", "Deductions", "Net Pay"};
        Object[][] data = {
            {"May 2025", "15000.00", "2450.00", "12550.00"}
        };
        JTable payrollTable = new JTable(data, columns);
        tabbedPane.addTab("Payroll", new JScrollPane(payrollTable));

        // Logout Tab
        JPanel logoutPanel = new JPanel();
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to logout?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        logoutPanel.add(logoutButton);
        tabbedPane.addTab("Logout", logoutPanel);

        frame.add(tabbedPane);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}