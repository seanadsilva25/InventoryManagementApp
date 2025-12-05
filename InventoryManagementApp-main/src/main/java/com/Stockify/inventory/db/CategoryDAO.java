package com.Stockify.inventory.db;

import com.Stockify.inventory.model.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private Connection conn;

    public CategoryDAO() {
        conn = DBConnection.getConnection();
    }

    public List<Category> findAll() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT category_id, category_code, name, description FROM categories ORDER BY category_id";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Category category = new Category(
                    rs.getInt("category_id"),
                    rs.getString("category_code"),
                    rs.getString("name"),
                    rs.getString("description")
                );
                list.add(category);
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch categories: " + e.getMessage());
        }
        return list;
    }

    public Category insert(Category category) throws SQLException {
        String sql = "INSERT INTO categories (category_code, name, description) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, category.getCategory_code());
            ps.setString(2, category.getName());
            ps.setString(3, category.getDescription());
            ps.executeUpdate();
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    category.setCategory_id(generatedKeys.getInt(1));
                }
            }
        }
        return category;
    }

    public void update(Category category) throws SQLException {
        String sql = "UPDATE categories SET category_code=?, name=?, description=? WHERE category_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category.getCategory_code());
            ps.setString(2, category.getName());
            ps.setString(3, category.getDescription());
            ps.setInt(4, category.getCategory_id());
            ps.executeUpdate();
        }
    }

    public void deleteById(int categoryId) throws SQLException {
        String sql = "DELETE FROM categories WHERE category_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ps.executeUpdate();
        }
    }

    public Category findById(int categoryId) {
        String sql = "SELECT category_id, category_code, name, description FROM categories WHERE category_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Category(
                        rs.getInt("category_id"),
                        rs.getString("category_code"),
                        rs.getString("name"),
                        rs.getString("description")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to find category: " + e.getMessage());
        }
        return null;
    }

    public List<Category> searchByName(String searchTerm) {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT category_id, category_code, name, description FROM categories WHERE name LIKE ? ORDER BY name";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + searchTerm + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Category category = new Category(
                        rs.getInt("category_id"),
                        rs.getString("category_code"),
                        rs.getString("name"),
                        rs.getString("description")
                    );
                    list.add(category);
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to search categories: " + e.getMessage());
        }
        return list;
    }
}
