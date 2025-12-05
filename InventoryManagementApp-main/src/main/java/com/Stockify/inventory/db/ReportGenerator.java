package com.Stockify.inventory.db;

import com.Stockify.inventory.model.Products;
import com.Stockify.inventory.model.Supplier;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class ReportGenerator {
    private Connection conn;
    public ProductDAO productDAO;
    private SupplierDAO supplierDAO;

    public ReportGenerator() {
        conn = DBConnection.getConnection();
        productDAO = new ProductDAO();
        supplierDAO = new SupplierDAO();
    }

    public class InventoryReport {
        private int totalItems;
        private int totalQuantity;
        private double totalValue;
        private int lowStockItems;
        private Map<String, Integer> categoryCount;
        private Map<String, Integer> brandCount;
        private List<Products> lowStockProducts;

        public InventoryReport() {
            categoryCount = new HashMap<>();
            brandCount = new HashMap<>();
            lowStockProducts = new ArrayList<>();
        }

        // Getters and setters
        public int getTotalItems() { return totalItems; }
        public void setTotalItems(int totalItems) { this.totalItems = totalItems; }
        
        public int getTotalQuantity() { return totalQuantity; }
        public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }
        
        public double getTotalValue() { return totalValue; }
        public void setTotalValue(double totalValue) { this.totalValue = totalValue; }
        
        public int getLowStockItems() { return lowStockItems; }
        public void setLowStockItems(int lowStockItems) { this.lowStockItems = lowStockItems; }
        
        public Map<String, Integer> getCategoryCount() { return categoryCount; }
        public void setCategoryCount(Map<String, Integer> categoryCount) { this.categoryCount = categoryCount; }
        
        public Map<String, Integer> getBrandCount() { return brandCount; }
        public void setBrandCount(Map<String, Integer> brandCount) { this.brandCount = brandCount; }
        
        public List<Products> getLowStockProducts() { return lowStockProducts; }
        public void setLowStockProducts(List<Products> lowStockProducts) { this.lowStockProducts = lowStockProducts; }
    }

    public InventoryReport generateInventoryReport() {
        InventoryReport report = new InventoryReport();
        
        try {
            // Get total items and quantities
            String sql = "SELECT COUNT(*) as total_items, SUM(quantity) as total_quantity, SUM(price * quantity) as total_value FROM items";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    report.setTotalItems(rs.getInt("total_items"));
                    report.setTotalQuantity(rs.getInt("total_quantity"));
                    report.setTotalValue(rs.getDouble("total_value"));
                }
            }

            // Get low stock items (quantity <= 5)
            List<Products> lowStock = productDAO.getLowStockProducts(5);
            report.setLowStockProducts(lowStock);
            report.setLowStockItems(lowStock.size());

            // Get category distribution
            sql = "SELECT category, COUNT(*) as count FROM items WHERE category IS NOT NULL GROUP BY category ORDER BY count DESC";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    report.getCategoryCount().put(rs.getString("category"), rs.getInt("count"));
                }
            }

            // Get brand distribution
            sql = "SELECT brand, COUNT(*) as count FROM items WHERE brand IS NOT NULL GROUP BY brand ORDER BY count DESC";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    report.getBrandCount().put(rs.getString("brand"), rs.getInt("count"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Failed to generate inventory report: " + e.getMessage());
        }
        
        return report;
    }

    public List<Products> generateLowStockReport(int threshold) {
        return productDAO.getLowStockProducts(threshold);
    }

    public List<Products> generateCategoryReport(String category) {
        return productDAO.searchByCategory(category);
    }

    public List<Products> generateBrandReport(String brand) {
        return productDAO.searchByBrand(brand);
    }

    public List<Products> generateSupplierReport(int supplierId) {
        List<Products> list = new ArrayList<>();
        String sql = "SELECT item_id, item_name, price, category, quantity, brand, supplier_id FROM items WHERE supplier_id = ? ORDER BY item_name";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, supplierId);
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
            System.err.println("Failed to generate supplier report: " + e.getMessage());
        }
        return list;
    }

    public Map<String, Double> generateValueByCategoryReport() {
        Map<String, Double> valueByCategory = new HashMap<>();
        String sql = "SELECT category, SUM(price * quantity) as total_value FROM items WHERE category IS NOT NULL GROUP BY category ORDER BY total_value DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                valueByCategory.put(rs.getString("category"), rs.getDouble("total_value"));
            }
        } catch (SQLException e) {
            System.err.println("Failed to generate value by category report: " + e.getMessage());
        }
        return valueByCategory;
    }

    public Map<String, Double> generateValueByBrandReport() {
        Map<String, Double> valueByBrand = new HashMap<>();
        String sql = "SELECT brand, SUM(price * quantity) as total_value FROM items WHERE brand IS NOT NULL GROUP BY brand ORDER BY total_value DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                valueByBrand.put(rs.getString("brand"), rs.getDouble("total_value"));
            }
        } catch (SQLException e) {
            System.err.println("Failed to generate value by brand report: " + e.getMessage());
        }
        return valueByBrand;
    }
}
