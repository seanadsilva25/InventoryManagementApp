package com.Stockify.inventory.db;

import com.Stockify.inventory.model.Products;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private Connection conn;

    public ProductDAO() {
        conn = DBConnection.getConnection();
    }

    public List<Products> findAll() {
        List<Products> list = new ArrayList<>();
        String sql = "SELECT item_id, item_name, price, category, quantity, brand, supplier_id FROM items ORDER BY item_id";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Products product = new Products(
                    rs.getInt("item_id"),
                    rs.getString("item_name"),
                    rs.getDouble("price"),
                    rs.getString("category"),
                    rs.getInt("quantity"),
                    rs.getString("brand"),
                    rs.getInt("supplier_id")
                );
                list.add(product);
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch products: " + e.getMessage());
        }
        return list;
    }

    public Products insert(Products product) throws SQLException {
        String sql = "INSERT INTO items (item_name, price, category, quantity, brand, supplier_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, product.getItem_name());
            ps.setDouble(2, product.getPrice());
            ps.setString(3, product.getCategory());
            ps.setInt(4, product.getQuantity());
            ps.setString(5, product.getBrand());
            ps.setInt(6, product.getSupplier_id());
            ps.executeUpdate();
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setItem_id(generatedKeys.getInt(1));
                }
            }
        }
        return product;
    }

    public void update(Products product) throws SQLException {
        String sql = "UPDATE items SET item_name=?, price=?, category=?, quantity=?, brand=?, supplier_id=? WHERE item_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getItem_name());
            ps.setDouble(2, product.getPrice());
            ps.setString(3, product.getCategory());
            ps.setInt(4, product.getQuantity());
            ps.setString(5, product.getBrand());
            ps.setInt(6, product.getSupplier_id());
            ps.setInt(7, product.getItem_id());
            ps.executeUpdate();
        }
    }

    public void deleteById(int itemId) throws SQLException {
        String sql = "DELETE FROM items WHERE item_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            ps.executeUpdate();
        }
    }

    public Products findById(int itemId) {
        String sql = "SELECT item_id, item_name, price, category, quantity, brand, supplier_id FROM items WHERE item_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Products(
                        rs.getInt("item_id"),
                        rs.getString("item_name"),
                        rs.getDouble("price"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getString("brand"),
                        rs.getInt("supplier_id")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to find product: " + e.getMessage());
        }
        return null;
    }

    public List<Products> searchByName(String searchTerm) {
        List<Products> list = new ArrayList<>();
        String sql = "SELECT item_id, item_name, price, category, quantity, brand, supplier_id FROM items WHERE item_name LIKE ? ORDER BY item_name";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + searchTerm + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Products product = new Products(
                        rs.getInt("item_id"),
                        rs.getString("item_name"),
                        rs.getDouble("price"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getString("brand"),
                        rs.getInt("supplier_id")
                    );
                    list.add(product);
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to search products: " + e.getMessage());
        }
        return list;
    }

    public List<Products> searchByCategory(String category) {
        List<Products> list = new ArrayList<>();
        String sql = "SELECT item_id, item_name, price, category, quantity, brand, supplier_id FROM items WHERE category LIKE ? ORDER BY item_name";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + category + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Products product = new Products(
                        rs.getInt("item_id"),
                        rs.getString("item_name"),
                        rs.getDouble("price"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getString("brand"),
                        rs.getInt("supplier_id")
                    );
                    list.add(product);
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to search products by category: " + e.getMessage());
        }
        return list;
    }

    public List<Products> searchByBrand(String brand) {
        List<Products> list = new ArrayList<>();
        String sql = "SELECT item_id, item_name, price, category, quantity, brand, supplier_id FROM items WHERE brand LIKE ? ORDER BY item_name";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + brand + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Products product = new Products(
                        rs.getInt("item_id"),
                        rs.getString("item_name"),
                        rs.getDouble("price"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getString("brand"),
                        rs.getInt("supplier_id")
                    );
                    list.add(product);
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to search products by brand: " + e.getMessage());
        }
        return list;
    }

    public List<Products> getLowStockProducts(int threshold) {
        List<Products> list = new ArrayList<>();
        String sql = "SELECT item_id, item_name, price, category, quantity, brand, supplier_id FROM items WHERE quantity <= ? ORDER BY quantity";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, threshold);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Products product = new Products(
                        rs.getInt("item_id"),
                        rs.getString("item_name"),
                        rs.getDouble("price"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getString("brand"),
                        rs.getInt("supplier_id")
                    );
                    list.add(product);
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to get low stock products: " + e.getMessage());
        }
        return list;
    }

    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT category FROM items WHERE category IS NOT NULL ORDER BY category";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            System.err.println("Failed to get categories: " + e.getMessage());
        }
        return categories;
    }

    public List<String> getAllBrands() {
        List<String> brands = new ArrayList<>();
        String sql = "SELECT DISTINCT brand FROM items WHERE brand IS NOT NULL ORDER BY brand";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                brands.add(rs.getString("brand"));
            }
        } catch (SQLException e) {
            System.err.println("Failed to get brands: " + e.getMessage());
        }
        return brands;
    }
}
