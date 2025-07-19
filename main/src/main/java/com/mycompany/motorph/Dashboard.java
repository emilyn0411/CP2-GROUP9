package com.mycompany.motorph;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class Dashboard extends JFrame {

    public Dashboard(String employeeID, String role) {
        setTitle("MotorPH Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Add user-level tabs
        tabbedPane.addTab("My Info", new EmployeeInfoPanel(employeeID));

        // Only show Time Tracker if user is not an admin
        if (!role.equalsIgnoreCase("admin")) {
            tabbedPane.addTab("Time Tracker", new TimeTracker(employeeID));
        }

        tabbedPane.addTab("Payroll", new PayrollPanel(employeeID, role));

        // If admin, show All Employees panel
        if (role.equalsIgnoreCase("admin")) {
            tabbedPane.addTab("All Employees", new AllEmployeesPanel());
        }

        // Add Logout tab
        JLabel logoutLabel = new JLabel("Logout");
        logoutLabel.setForeground(Color.RED);
        JPanel logoutPanel = new JPanel();
        logoutPanel.add(new JLabel("You have been logged out."));
        tabbedPane.addTab("Logout", logoutPanel);
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, logoutLabel);

        // Handle logout logic when "Logout" tab is selected
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedComponent() == logoutPanel) {
                int confirmed = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Logout", JOptionPane.YES_NO_OPTION);
                if (confirmed == JOptionPane.YES_OPTION) {
                    dispose();
                    new LoginScreen(); // Redirect to login screen
                } else {
                    tabbedPane.setSelectedIndex(0); // Go back to first tab
                }
            }
        });

        add(tabbedPane);
        setVisible(true);
    }
}