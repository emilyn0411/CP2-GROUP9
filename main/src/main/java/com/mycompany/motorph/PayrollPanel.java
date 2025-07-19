package com.mycompany.motorph;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class PayrollPanel extends JPanel {
    private DefaultTableModel model;
    private JTable table;
    private JTextField filterIdField;
    private JComboBox<String> periodComboBox;
    private String employeeID;
    private String role;

    public PayrollPanel(String employeeID, String role) {
        this.employeeID = employeeID;
        this.role = role;
        setLayout(new BorderLayout());

        String[] columns = {"Employee ID", "Employee Name", "Period", "Gross Pay", "Deductions", "Net Pay"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);

        BufferedImage bgImage = null;
        try {
            bgImage = ImageIO.read(getClass().getResource("/images/MotorPH Login Design.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Admin-only filter and calculation tools
        if (role.equals("admin")) {
            JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            filterPanel.add(new JLabel("Filter by Employee ID:"));
            filterIdField = new JTextField(10);
            filterPanel.add(filterIdField);

            filterPanel.add(new JLabel("Cutoff Period:"));
            periodComboBox = new JComboBox<>(new String[]{"All", "May 15", "May 30"});
            filterPanel.add(periodComboBox);

            JButton applyFilterBtn = new JButton("Apply Filters");
            filterPanel.add(applyFilterBtn);
            applyFilterBtn.addActionListener(e -> applyFilters());

            add(filterPanel, BorderLayout.NORTH);

            // Calculation panel
            JPanel calcPanel = new JPanel(new GridLayout(0, 2, 5, 5));
            calcPanel.setBorder(BorderFactory.createTitledBorder("Calculate Payroll"));

            JTextField empIdField = new JTextField();
            JTextField nameField = new JTextField();
            JTextField hoursField = new JTextField();
            JTextField rateField = new JTextField();
            JComboBox<String> periodInput = new JComboBox<>(new String[]{"May 15", "May 30"});

            calcPanel.add(new JLabel("Employee ID:"));
            calcPanel.add(empIdField);
            calcPanel.add(new JLabel("Employee Name:"));
            calcPanel.add(nameField);
            calcPanel.add(new JLabel("Hours Worked:"));
            calcPanel.add(hoursField);
            calcPanel.add(new JLabel("Rate per Hour:"));
            calcPanel.add(rateField);
            calcPanel.add(new JLabel("Cutoff Period:"));
            calcPanel.add(periodInput);

            JButton calculateBtn = new JButton("Calculate & Save");
            calcPanel.add(new JLabel()); // empty cell
            calcPanel.add(calculateBtn);

            calculateBtn.addActionListener(e -> {
                try {
                    String id = empIdField.getText().trim();
                    String name = nameField.getText().trim();
                    String period = (String) periodInput.getSelectedItem();
                    double hours = Double.parseDouble(hoursField.getText().trim());
                    double rate = Double.parseDouble(rateField.getText().trim());

                    calculateAndSavePayroll(id, name, period, hours, rate);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Please enter valid numeric values.");
                }
            });

            add(calcPanel, BorderLayout.SOUTH);
        }

        add(new JScrollPane(table), BorderLayout.CENTER);
        loadPayrollData(); // initial load
    }

    private void loadPayrollData() {
        model.setRowCount(0); // Clear existing data
        try (BufferedReader br = new BufferedReader(new FileReader("payroll_data.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split("\t");
                if (row.length == 6) {
                    boolean isAdmin = role.equals("admin");
                    boolean isOwner = row[0].equals(employeeID);

                    if (isAdmin || isOwner) {
                        model.addRow(row);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void applyFilters() {
        if (!role.equals("admin")) return;

        String empIdFilter = filterIdField.getText().trim();
        String selectedPeriod = (String) periodComboBox.getSelectedItem();

        model.setRowCount(0); // Clear rows
        try (BufferedReader br = new BufferedReader(new FileReader("payroll_data.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split("\t");
                if (row.length == 6) {
                    boolean matchesId = empIdFilter.isEmpty() || row[0].equals(empIdFilter);
                    boolean matchesPeriod = selectedPeriod.equals("All") || row[2].equals(selectedPeriod);
                    if (matchesId && matchesPeriod) {
                        model.addRow(row);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void calculateAndSavePayroll(String empId, String empName, String period, double hoursWorked, double ratePerHour) {
        double grossPay = hoursWorked * ratePerHour;
        double deductions = grossPay * 0.20; // 20% deductions
        double netPay = grossPay - deductions;

        String rowData = String.format("%s\t%s\t%s\t%.2f\t%.2f\t%.2f", empId, empName, period, grossPay, deductions, netPay);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("payroll_data.txt", true))) {
            writer.write(rowData);
            writer.newLine();
            JOptionPane.showMessageDialog(this, "Payroll calculated and saved!");
            loadPayrollData(); // refresh table
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving payroll.");
        }
    }
}