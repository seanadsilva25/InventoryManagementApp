package com.Stockify.inventory.db;

import com.Stockify.inventory.db.DBConnection;
import com.Stockify.inventory.model.Products;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LowStockAlert {

    private static final int THRESHOLD = 5; // low-stock limit
    private Connection conn;

    // Constructor to initialize connection
    public LowStockAlert() {
        conn = DBConnection.getConnection();
        if (conn != null) {
            System.out.println("LowStockAlert: DB Connection established.");
        }
    }

    public ArrayList<Products> getLowStockProducts() {
        ArrayList<Products> lowStockList = new ArrayList<>();
        String query = "SELECT * FROM items WHERE quantity <= ?";

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, THRESHOLD);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Products p = new Products(
                        rs.getInt("item_id"),
                        rs.getString("item_name"),
                        rs.getDouble("price"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getString("brand"),
                        rs.getInt("supplier_id"));
                lowStockList.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lowStockList;
    }

    //method to close connection when done
    public void closeConnection() {
        DBConnection.closeConnection(conn);
    }
}
