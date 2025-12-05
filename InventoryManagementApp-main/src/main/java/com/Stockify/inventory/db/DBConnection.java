package com.Stockify.inventory.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;                                                                                                                                            

public class DBConnection {

    // Change these according to your MySQL setup
    private static final String URL = "jdbc:mysql://localhost:3306/inventory";
    private static final String USER = "root";        // your MySQL username
    private static final String PASSWORD = "mj8@2004"; // your MySQL password

    // Method to get connection
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            System.err.println("Cannot connect to the database!");
            e.printStackTrace();
        }
        return conn;
    }

    //Closing connection safely
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
