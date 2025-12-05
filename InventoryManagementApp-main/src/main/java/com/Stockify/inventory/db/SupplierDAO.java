package com.Stockify.inventory.db;

import com.Stockify.inventory.model.Supplier;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {
    private Connection conn;

    public SupplierDAO() {
        conn = DBConnection.getConnection();
    }

    public List<Supplier> findAll() {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT supplier_id, supplier_name, company_name, contact_number, email, address FROM supplier ORDER BY supplier_id";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Supplier supplier = new Supplier(
                    rs.getInt("supplier_id"),
                    rs.getString("supplier_name"),
                    rs.getString("company_name"),
                    rs.getString("contact_number"),
                    rs.getString("email"),
                    rs.getString("address")
                );
                list.add(supplier);
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch suppliers: " + e.getMessage());
        }
        return list;
    }

    public Supplier insert(Supplier supplier) throws SQLException {
        String sql = "INSERT INTO supplier (supplier_name, company_name, contact_number, email, address) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, supplier.getSupplier_name());
            ps.setString(2, supplier.getCompany_name());
            ps.setString(3, supplier.getContact_number());
            ps.setString(4, supplier.getEmail());
            ps.setString(5, supplier.getAddress());
            ps.executeUpdate();
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    supplier.setSupplier_id(generatedKeys.getInt(1));
                }
            }
        }
        return supplier;
    }

    public void update(Supplier supplier) throws SQLException {
        String sql = "UPDATE supplier SET supplier_name=?, company_name=?, contact_number=?, email=?, address=? WHERE supplier_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, supplier.getSupplier_name());
            ps.setString(2, supplier.getCompany_name());
            ps.setString(3, supplier.getContact_number());
            ps.setString(4, supplier.getEmail());
            ps.setString(5, supplier.getAddress());
            ps.setInt(6, supplier.getSupplier_id());
            ps.executeUpdate();
        }
    }

    public void deleteById(int supplierId) throws SQLException {
        String sql = "DELETE FROM supplier WHERE supplier_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, supplierId);
            ps.executeUpdate();
        }
    }

    public Supplier findById(int supplierId) {
        String sql = "SELECT supplier_id, supplier_name, company_name, contact_number, email, address FROM supplier WHERE supplier_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, supplierId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Supplier(
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getString("company_name"),
                        rs.getString("contact_number"),
                        rs.getString("email"),
                        rs.getString("address")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to find supplier: " + e.getMessage());
        }
        return null;
    }

    public List<Supplier> searchByName(String searchTerm) {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT supplier_id, supplier_name, company_name, contact_number, email, address FROM supplier WHERE supplier_name LIKE ? OR company_name LIKE ? ORDER BY company_name";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + searchTerm + "%");
            ps.setString(2, "%" + searchTerm + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Supplier supplier = new Supplier(
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getString("company_name"),
                        rs.getString("contact_number"),
                        rs.getString("email"),
                        rs.getString("address")
                    );
                    list.add(supplier);
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to search suppliers: " + e.getMessage());
        }
        return list;
    }
}
