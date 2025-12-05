package com.Stockify.inventory.gui;

import com.Stockify.inventory.db.ReportGenerator;
import com.Stockify.inventory.model.Products;
import com.Stockify.inventory.model.Supplier;
import com.Stockify.inventory.db.SupplierDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class ReportGUI extends JFrame {
    private ReportGenerator reportGenerator;
    private SupplierDAO supplierDAO;
    private JTabbedPane tabbedPane;
    private JTextArea reportTextArea;

    public ReportGUI(ReportGenerator reportGenerator) {
        this.reportGenerator = reportGenerator;
        this.supplierDAO = new SupplierDAO();

        setTitle("Stockify — Reports");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        initComponents();
        generateAllReports();
    }

    private void initComponents() {
        Color bg = new Color(250, 250, 252);
        Color card = new Color(255, 255, 255);

        setLayout(new BorderLayout());
        getContentPane().setBackground(bg);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(bg);
        header.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Inventory Reports");
        title.setFont(new Font("Inter", Font.BOLD, 20));
        title.setForeground(new Color(107, 114, 179));
        header.add(title, BorderLayout.WEST);

        JButton refreshBtn = new JButton("Refresh Reports");
        refreshBtn.setBackground(new Color(107, 114, 179));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.addActionListener(e -> generateAllReports());
        header.add(refreshBtn, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // Tabbed pane for different reports
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(bg);
        add(tabbedPane, BorderLayout.CENTER);

        // Create tabs
        createInventorySummaryTab();
        createLowStockTab();
        createCategoryReportTab();
        createBrandReportTab();
        createSupplierReportTab();
        createValueAnalysisTab();
    }

    private void createInventorySummaryTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);
        reportTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        reportTextArea.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(reportTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        tabbedPane.addTab("Inventory Summary", panel);
    }

    private void createLowStockTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Threshold input
        JPanel thresholdPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        thresholdPanel.setBackground(Color.WHITE);
        JLabel thresholdLabel = new JLabel("Low Stock Threshold:");
        JSpinner thresholdSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 100, 1));
        JButton generateBtn = new JButton("Generate");
        generateBtn.setBackground(new Color(107, 114, 179));
        generateBtn.setForeground(Color.WHITE);
        generateBtn.setFocusPainted(false);

        thresholdPanel.add(thresholdLabel);
        thresholdPanel.add(thresholdSpinner);
        thresholdPanel.add(generateBtn);

        // Table for low stock items
        String[] columns = {"ID", "Name", "Category", "Quantity", "Brand", "Supplier ID"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        generateBtn.addActionListener(e -> {
            int threshold = (Integer) thresholdSpinner.getValue();
            List<Products> lowStockItems = reportGenerator.generateLowStockReport(threshold);
            
            model.setRowCount(0);
            for (Products product : lowStockItems) {
                Object[] row = {
                    product.getItem_id(),
                    product.getItem_name(),
                    product.getCategory(),
                    product.getQuantity(),
                    product.getBrand(),
                    product.getSupplier_id()
                };
                model.addRow(row);
            }
        });

        panel.add(thresholdPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        tabbedPane.addTab("Low Stock Report", panel);
    }

    private void createCategoryReportTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Category selection
        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        categoryPanel.setBackground(Color.WHITE);
        JLabel categoryLabel = new JLabel("Select Category:");
        JComboBox<String> categoryCombo = new JComboBox<>();
        JButton generateBtn = new JButton("Generate");
        generateBtn.setBackground(new Color(107, 114, 179));
        generateBtn.setForeground(Color.WHITE);
        generateBtn.setFocusPainted(false);

        // Load categories
        try {
            List<String> categories = reportGenerator.productDAO.getAllCategories();
            for (String category : categories) {
                categoryCombo.addItem(category);
            }
        } catch (Exception ex) {
            System.err.println("Error loading categories: " + ex.getMessage());
        }

        categoryPanel.add(categoryLabel);
        categoryPanel.add(categoryCombo);
        categoryPanel.add(generateBtn);

        // Table for category items
        String[] columns = {"ID", "Name", "Price", "Quantity", "Brand", "Supplier ID"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        generateBtn.addActionListener(e -> {
            String selectedCategory = (String) categoryCombo.getSelectedItem();
            if (selectedCategory != null) {
                List<Products> categoryItems = reportGenerator.generateCategoryReport(selectedCategory);
                
                model.setRowCount(0);
                for (Products product : categoryItems) {
                    Object[] row = {
                        product.getItem_id(),
                        product.getItem_name(),
                        product.getPrice(),
                        product.getQuantity(),
                        product.getBrand(),
                        product.getSupplier_id()
                    };
                    model.addRow(row);
                }
            }
        });

        panel.add(categoryPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        tabbedPane.addTab("Category Report", panel);
    }

    private void createBrandReportTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Brand selection
        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        brandPanel.setBackground(Color.WHITE);
        JLabel brandLabel = new JLabel("Select Brand:");
        JComboBox<String> brandCombo = new JComboBox<>();
        JButton generateBtn = new JButton("Generate");
        generateBtn.setBackground(new Color(107, 114, 179));
        generateBtn.setForeground(Color.WHITE);
        generateBtn.setFocusPainted(false);

        // Load brands
        try {
            List<String> brands = reportGenerator.productDAO.getAllBrands();
            for (String brand : brands) {
                brandCombo.addItem(brand);
            }
        } catch (Exception ex) {
            System.err.println("Error loading brands: " + ex.getMessage());
        }

        brandPanel.add(brandLabel);
        brandPanel.add(brandCombo);
        brandPanel.add(generateBtn);

        // Table for brand items
        String[] columns = {"ID", "Name", "Price", "Category", "Quantity", "Supplier ID"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        generateBtn.addActionListener(e -> {
            String selectedBrand = (String) brandCombo.getSelectedItem();
            if (selectedBrand != null) {
                List<Products> brandItems = reportGenerator.generateBrandReport(selectedBrand);
                
                model.setRowCount(0);
                for (Products product : brandItems) {
                    Object[] row = {
                        product.getItem_id(),
                        product.getItem_name(),
                        product.getPrice(),
                        product.getCategory(),
                        product.getQuantity(),
                        product.getSupplier_id()
                    };
                    model.addRow(row);
                }
            }
        });

        panel.add(brandPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        tabbedPane.addTab("Brand Report", panel);
    }

    private void createSupplierReportTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Supplier selection
        JPanel supplierPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        supplierPanel.setBackground(Color.WHITE);
        JLabel supplierLabel = new JLabel("Select Supplier:");
        JComboBox<Supplier> supplierCombo = new JComboBox<>();
        JButton generateBtn = new JButton("Generate");
        generateBtn.setBackground(new Color(107, 114, 179));
        generateBtn.setForeground(Color.WHITE);
        generateBtn.setFocusPainted(false);

        // Load suppliers
        try {
            List<Supplier> suppliers = supplierDAO.findAll();
            for (Supplier supplier : suppliers) {
                supplierCombo.addItem(supplier);
            }
        } catch (Exception ex) {
            System.err.println("Error loading suppliers: " + ex.getMessage());
        }

        supplierPanel.add(supplierLabel);
        supplierPanel.add(supplierCombo);
        supplierPanel.add(generateBtn);

        // Table for supplier items
        String[] columns = {"ID", "Name", "Price", "Category", "Quantity", "Brand"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        generateBtn.addActionListener(e -> {
            Supplier selectedSupplier = (Supplier) supplierCombo.getSelectedItem();
            if (selectedSupplier != null) {
                List<Products> supplierItems = reportGenerator.generateSupplierReport(selectedSupplier.getSupplier_id());
                
                model.setRowCount(0);
                for (Products product : supplierItems) {
                    Object[] row = {
                        product.getItem_id(),
                        product.getItem_name(),
                        product.getPrice(),
                        product.getCategory(),
                        product.getQuantity(),
                        product.getBrand()
                    };
                    model.addRow(row);
                }
            }
        });

        panel.add(supplierPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        tabbedPane.addTab("Supplier Report", panel);
    }

    private void createValueAnalysisTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextArea valueTextArea = new JTextArea();
        valueTextArea.setEditable(false);
        valueTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        valueTextArea.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(valueTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton generateBtn = new JButton("Generate Value Analysis");
        generateBtn.setBackground(new Color(107, 114, 179));
        generateBtn.setForeground(Color.WHITE);
        generateBtn.setFocusPainted(false);
        generateBtn.addActionListener(e -> {
            StringBuilder report = new StringBuilder();
            report.append("VALUE ANALYSIS REPORT\n");
            report.append("====================\n\n");

            // Value by Category
            report.append("VALUE BY CATEGORY:\n");
            report.append("------------------\n");
            Map<String, Double> valueByCategory = reportGenerator.generateValueByCategoryReport();
            for (Map.Entry<String, Double> entry : valueByCategory.entrySet()) {
                report.append(String.format("%-20s: $%.2f\n", entry.getKey(), entry.getValue()));
            }

            report.append("\nVALUE BY BRAND:\n");
            report.append("---------------\n");
            Map<String, Double> valueByBrand = reportGenerator.generateValueByBrandReport();
            for (Map.Entry<String, Double> entry : valueByBrand.entrySet()) {
                report.append(String.format("%-20s: $%.2f\n", entry.getKey(), entry.getValue()));
            }

            valueTextArea.setText(report.toString());
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(generateBtn);
        panel.add(buttonPanel, BorderLayout.NORTH);

        tabbedPane.addTab("Value Analysis", panel);
    }

    private void generateAllReports() {
        ReportGenerator.InventoryReport report = reportGenerator.generateInventoryReport();
        
        StringBuilder reportText = new StringBuilder();
        reportText.append("INVENTORY SUMMARY REPORT\n");
        reportText.append("=======================\n\n");
        
        reportText.append("OVERVIEW:\n");
        reportText.append("---------\n");
        reportText.append(String.format("Total Items: %d\n", report.getTotalItems()));
        reportText.append(String.format("Total Quantity: %d\n", report.getTotalQuantity()));
        reportText.append(String.format("Total Value: $%.2f\n", report.getTotalValue()));
        reportText.append(String.format("Low Stock Items: %d\n\n", report.getLowStockItems()));
        
        reportText.append("CATEGORY DISTRIBUTION:\n");
        reportText.append("---------------------\n");
        for (Map.Entry<String, Integer> entry : report.getCategoryCount().entrySet()) {
            reportText.append(String.format("%-20s: %d items\n", entry.getKey(), entry.getValue()));
        }
        
        reportText.append("\nBRAND DISTRIBUTION:\n");
        reportText.append("------------------\n");
        for (Map.Entry<String, Integer> entry : report.getBrandCount().entrySet()) {
            reportText.append(String.format("%-20s: %d items\n", entry.getKey(), entry.getValue()));
        }
        
        reportText.append("\nLOW STOCK ITEMS:\n");
        reportText.append("---------------\n");
        for (Products product : report.getLowStockProducts()) {
            reportText.append(String.format("%-20s: %d units\n", product.getItem_name(), product.getQuantity()));
        }
        
        reportTextArea.setText(reportText.toString());
    }
}
