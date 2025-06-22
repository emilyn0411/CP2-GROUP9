package com.mycompany.motorph;

public class Authenticator {
    public static String authenticate(String employeeID, String password) {
        //  employeeID, password, role
        String[][] users = {
            {"admin", "pass123", "admin"},
            {"10001", "user456", "user"},
            {"10002", "demo789", "user"}
        };

        for (String[] user : users) {
            String storedID = user[0];
            String storedPassword = user[1];
            String role = user[2];

            if (employeeID.equals(storedID) && password.equals(storedPassword)) {
                return role;
            }
        }

        return null; // invalid credentials
    }
}