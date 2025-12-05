package com.Stockify.inventory.test;

import com.Stockify.inventory.db.ProductDAO;
import com.Stockify.inventory.db.CategoryDAO;
import com.Stockify.inventory.db.SupplierDAO;
import com.Stockify.inventory.db.ReportGenerator;
import com.Stockify.inventory.model.Products;
import com.Stockify.inventory.model.Category;
import com.Stockify.inventory.model.Supplier;

import java.util.List;

/**
 * Integration test class to verify the enhanced features work correctly
 * Run this class to test database connectivity and basic functionality
 */
public class IntegrationTest {
    
    public static void main(String[] args) {
        System.out.println("=== Stockify Enhanced Features Integration Test ===\n");
        
        try {
            // Test database connectivity
            testDatabaseConnectivity();
            
            // Test product operations
            testProductOperations();
            
            // Test category operations
            testCategoryOperations();
            
            // Test supplier operations
            testSupplierOperations();
            
            // Test search functionality
            testSearchFunctionality();
            
            // Test report generation
            testReportGeneration();
            
            System.out.println("\n=== All tests completed successfully! ===");
            
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testDatabaseConnectivity() {
        System.out.println("1. Testing database connectivity...");
        
        ProductDAO productDAO = new ProductDAO();
        List<Products> products = productDAO.findAll();
        
        System.out.println("   ✓ Database connection successful");
        System.out.println("   ✓ Found " + products.size() + " products in database");
    }
    
    private static void testProductOperations() {
        System.out.println("\n2. Testing product operations...");
        
        ProductDAO productDAO = new ProductDAO();
        
        // Test finding all products
        List<Products> allProducts = productDAO.findAll();
        System.out.println("   ✓ Retrieved " + allProducts.size() + " products");
        
        // Test getting categories
        List<String> categories = productDAO.getAllCategories();
        System.out.println("   ✓ Found " + categories.size() + " categories: " + categories);
        
        // Test getting brands
        List<String> brands = productDAO.getAllBrands();
        System.out.println("   ✓ Found " + brands.size() + " brands: " + brands);
    }
    
    private static void testCategoryOperations() {
        System.out.println("\n3. Testing category operations...");
        
        CategoryDAO categoryDAO = new CategoryDAO();
        
        // Test finding all categories
        List<Category> categories = categoryDAO.findAll();
        System.out.println("   ✓ Retrieved " + categories.size() + " categories");
        
        // Test searching categories
        List<Category> searchResults = categoryDAO.searchByName("Electronics");
        System.out.println("   ✓ Category search returned " + searchResults.size() + " results");
    }
    
    private static void testSupplierOperations() {
        System.out.println("\n4. Testing supplier operations...");
        
        SupplierDAO supplierDAO = new SupplierDAO();
        
        // Test finding all suppliers
        List<Supplier> suppliers = supplierDAO.findAll();
        System.out.println("   ✓ Retrieved " + suppliers.size() + " suppliers");
        
        // Test searching suppliers
        List<Supplier> searchResults = supplierDAO.searchByName("Tech");
        System.out.println("   ✓ Supplier search returned " + searchResults.size() + " results");
    }
    
    private static void testSearchFunctionality() {
        System.out.println("\n5. Testing search functionality...");
        
        ProductDAO productDAO = new ProductDAO();
        
        // Test name search
        List<Products> nameResults = productDAO.searchByName("Laptop");
        System.out.println("   ✓ Name search returned " + nameResults.size() + " results");
        
        // Test category search
        List<Products> categoryResults = productDAO.searchByCategory("Electronics");
        System.out.println("   ✓ Category search returned " + categoryResults.size() + " results");
        
        // Test brand search
        List<Products> brandResults = productDAO.searchByBrand("Dell");
        System.out.println("   ✓ Brand search returned " + brandResults.size() + " results");
        
        // Test low stock search
        List<Products> lowStockResults = productDAO.getLowStockProducts(10);
        System.out.println("   ✓ Low stock search returned " + lowStockResults.size() + " results");
    }
    
    private static void testReportGeneration() {
        System.out.println("\n6. Testing report generation...");
        
        ReportGenerator reportGenerator = new ReportGenerator();
        
        // Test inventory report
        ReportGenerator.InventoryReport report = reportGenerator.generateInventoryReport();
        System.out.println("   ✓ Generated inventory report:");
        System.out.println("     - Total items: " + report.getTotalItems());
        System.out.println("     - Total quantity: " + report.getTotalQuantity());
        System.out.println("     - Total value: $" + String.format("%.2f", report.getTotalValue()));
        System.out.println("     - Low stock items: " + report.getLowStockItems());
        
        // Test low stock report
        List<Products> lowStockReport = reportGenerator.generateLowStockReport(5);
        System.out.println("   ✓ Generated low stock report with " + lowStockReport.size() + " items");
        
        // Test value analysis
        var valueByCategory = reportGenerator.generateValueByCategoryReport();
        System.out.println("   ✓ Generated value by category report with " + valueByCategory.size() + " categories");
        
        var valueByBrand = reportGenerator.generateValueByBrandReport();
        System.out.println("   ✓ Generated value by brand report with " + valueByBrand.size() + " brands");
    }
}
