package com.Stockify.inventory.db;
import com.Stockify.inventory.model.Products;
import com.Stockify.inventory.db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManageProducts {

    //Code to connect to Database
    private Connection conn;
    public ManageProducts()     // must call connect() before using any method like addProduct()
    {
        conn = DBConnection.getConnection();

        if (conn != null) {
            System.out.println("Connection object is not null. Test passed!");
        }
    }

    //Code to add products into database
    public void addProduct(Products p)
    {
        String add_product_query = "INSERT INTO items (item_name, price, category, quantity, brand, supplier_id) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement pst = conn.prepareStatement(add_product_query);
            pst.setString(1, p.getItem_name()); //question mark no, data is in p
            pst.setDouble(2, p.getPrice());
            pst.setString(3, p.getCategory());
            pst.setInt(4, p.getQuantity());
            pst.setString(5, p.getBrand());
            pst.setInt(6, p.getSupplier_id());

            pst.executeUpdate();
        } catch (SQLException e) { //can use (Exception e) too
            System.err.println("Failed to add product: " + e.getMessage());
        }
    }
    
    public void updateProduct(Products p) {
        String update_product_query = "UPDATE items SET item_name=?, price=?, category=?, quantity=?, brand=?, supplier_id=? WHERE item_id=?";
        try {
            PreparedStatement pst = conn.prepareStatement(update_product_query);
            pst.setString(1, p.getItem_name());
            pst.setDouble(2, p.getPrice());
            pst.setString(3, p.getCategory());
            pst.setInt(4, p.getQuantity());
            pst.setString(5, p.getBrand());
            pst.setInt(6, p.getSupplier_id());
            pst.setInt(7, p.getItem_id());
            pst.executeUpdate();
        } catch (SQLException e) { // can use (Exception e) too
            System.err.println("Failed to update product: " + e.getMessage());
        }
    }

    public void deleteProduct(Products p)
    {
        String delete_product_query = "DELETE FROM items WHERE item_id=?";
        try {
            PreparedStatement pst = conn.prepareStatement(delete_product_query);
            pst.setInt(1, p.getItem_id());

            pst.executeUpdate();
        } catch (SQLException e) { // can use (Exception e) too
            System.err.println("Failed to delete product: " + e.getMessage());
        }
    }


    public ArrayList<Products> getViewOfProducts() 
    {
        ArrayList<Products> list = new ArrayList<>();
        String view_items_query = "SELECT * FROM items";
        try{
            PreparedStatement pst = conn.prepareStatement(view_items_query);
            ResultSet rs = pst.executeQuery();

            while(rs.next()){
                Products p = new Products
                (
                rs.getInt("item_id"),
                rs.getString("item_name"),
                rs.getDouble("price"),
                rs.getString("category"),
                rs.getInt("quantity"),
                rs.getString("brand"),
                rs.getInt("supplier_id")
                );
                list.add(p);
            } 
        } catch (SQLException e) {
            System.err.println("Failed to show product: " + e.getMessage());
        }
        return list;
    }


}
