package com.Stockify.inventory.gui;

import com.Stockify.inventory.model.Products;
import com.Stockify.inventory.model.Category;
import com.Stockify.inventory.model.Supplier;
import com.Stockify.inventory.db.ProductDAO;
import com.Stockify.inventory.db.CategoryDAO;
import com.Stockify.inventory.db.SupplierDAO;
import com.Stockify.inventory.db.ReportGenerator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnhancedManageProductsGUI extends JFrame {
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;
    private SupplierDAO supplierDAO;
    private ReportGenerator reportGenerator;

    private JPanel contentPane;
    private JTextField txtId, txtName, txtPrice, txtQuantity, txtBrand, txtSupplierId;
    private JComboBox<String> cmbCategory;
    private JButton btnAdd, btnUpdate, btnDelete, btnView, btnSearch, btnGenerateReport, btnManageCategories;
    private JTextField txtSearch;
    private JComboBox<String> cmbSearchType;
    private JTable table;
    private DefaultTableModel tableModel;

    public EnhancedManageProductsGUI() {
        productDAO = new ProductDAO();
        categoryDAO = new CategoryDAO();
        supplierDAO = new SupplierDAO();
        reportGenerator = new ReportGenerator();

        setTitle("Stockify — Enhanced Product Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 600));

        initComponents();
        loadTableData();
        loadCategories();
    }

    private void initComponents() {
        // Modern pastel theme colors
        Color bg = new Color(250, 250, 252);
        Color panel = new Color(245, 246, 249);
        Color accent = new Color(107, 114, 179);
        Color card = new Color(255, 255, 255);

        contentPane = new JPanel(new BorderLayout(14, 14));
        contentPane.setBackground(bg);
        contentPane.setBorder(new EmptyBorder(14, 14, 14, 14));
        setContentPane(contentPane);

        // Top header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(bg);
        JLabel title = new JLabel("Stockify — Enhanced Products");
        title.setFont(new Font("Inter", Font.BOLD, 22));
        JLabel subtitle = new JLabel("Add, update, delete, search and generate reports for inventory items");
        subtitle.setFont(new Font("Inter", Font.PLAIN, 12));
        subtitle.setForeground(Color.DARK_GRAY);

        JPanel titleBox = new JPanel(new GridLayout(2, 1));
        titleBox.setOpaque(false);
        titleBox.add(title);
        titleBox.add(subtitle);
        header.add(titleBox, BorderLayout.WEST);

        contentPane.add(header, BorderLayout.NORTH);

        // Main center area
        JPanel main = new JPanel(new BorderLayout(12, 12));
        main.setOpaque(false);
        contentPane.add(main, BorderLayout.CENTER);

        // Left form card
        JPanel formCard = createFormCard(card);
        main.add(formCard, BorderLayout.WEST);

        // Center: Search and table
        JPanel centerPanel = new JPanel(new BorderLayout(12, 12));
        centerPanel.setOpaque(false);

        // Search panel
        JPanel searchCard = createSearchCard(card);
        centerPanel.add(searchCard, BorderLayout.NORTH);

        // Table card
        JPanel tableCard = createTableCard(card);
        centerPanel.add(tableCard, BorderLayout.CENTER);

        main.add(centerPanel, BorderLayout.CENTER);

        // Button actions
        setupEventListeners();
    }

    private JPanel createFormCard(Color card) {
        JPanel formCard = new JPanel();
        formCard.setBackground(card);
        formCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 235)),
                new EmptyBorder(16, 16, 16, 16)));
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setPreferredSize(new Dimension(380, 0));

        JLabel formTitle = new JLabel("Product Details");
        formTitle.setFont(new Font("Inter", Font.BOLD, 16));
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(formTitle);
        formCard.add(Box.createRigidArea(new Dimension(0, 10)));

        // Input fields
        txtId = makeTextField("Product ID (for update/delete)", formCard, true);
        txtName = makeTextField("Name", formCard, false);
        txtPrice = makeTextField("Price", formCard, false);
        
        // Category dropdown
        JPanel categoryRow = new JPanel(new BorderLayout(6, 6));
        categoryRow.setOpaque(false);
        JLabel categoryLbl = new JLabel("Category");
        categoryLbl.setFont(new Font("Inter", Font.PLAIN, 12));
        categoryRow.add(categoryLbl, BorderLayout.NORTH);
        cmbCategory = new JComboBox<>();
        cmbCategory.setPreferredSize(new Dimension(200, 30));
        cmbCategory.setFont(new Font("Inter", Font.PLAIN, 13));
        categoryRow.add(cmbCategory, BorderLayout.CENTER);
        formCard.add(categoryRow);
        formCard.add(Box.createRigidArea(new Dimension(0, 6)));

        txtQuantity = makeTextField("Quantity", formCard, false);
        txtBrand = makeTextField("Brand", formCard, false);
        txtSupplierId = makeTextField("Supplier ID", formCard, false);

        formCard.add(Box.createRigidArea(new Dimension(0, 8)));

        // Buttons row 1
        JPanel btnRow1 = new JPanel(new GridLayout(2, 2, 8, 8));
        btnRow1.setOpaque(false);

        btnAdd = styledButton("Add Product");
        btnUpdate = styledButton("Update Product");
        btnDelete = styledButton("Delete Product");
        btnView = styledButton("View All");

        btnRow1.add(btnAdd);
        btnRow1.add(btnUpdate);
        btnRow1.add(btnDelete);
        btnRow1.add(btnView);

        formCard.add(btnRow1);
        formCard.add(Box.createRigidArea(new Dimension(0, 8)));

        // Buttons row 2
        JPanel btnRow2 = new JPanel(new GridLayout(1, 2, 8, 0));
        btnRow2.setOpaque(false);

        btnGenerateReport = styledButton("Generate Report");
        btnManageCategories = styledButton("Manage Categories");

        btnRow2.add(btnGenerateReport);
        btnRow2.add(btnManageCategories);

        formCard.add(btnRow2);

        return formCard;
    }

    private JPanel createSearchCard(Color card) {
        JPanel searchCard = new JPanel(new BorderLayout());
        searchCard.setBackground(card);
        searchCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 235)),
                new EmptyBorder(12, 12, 12, 12)));

        JLabel searchTitle = new JLabel("Search Products");
        searchTitle.setFont(new Font("Inter", Font.BOLD, 14));

        JPanel searchControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        searchControls.setOpaque(false);

        txtSearch = new JTextField(15);
        txtSearch.setFont(new Font("Inter", Font.PLAIN, 13));

        cmbSearchType = new JComboBox<>(new String[]{"Name", "Category", "Brand", "All"});
        cmbSearchType.setFont(new Font("Inter", Font.PLAIN, 13));

        btnSearch = styledButton("Search");
        btnSearch.setPreferredSize(new Dimension(80, 30));

        searchControls.add(new JLabel("Search:"));
        searchControls.add(txtSearch);
        searchControls.add(new JLabel("Type:"));
        searchControls.add(cmbSearchType);
        searchControls.add(btnSearch);

        searchCard.add(searchTitle, BorderLayout.NORTH);
        searchCard.add(searchControls, BorderLayout.CENTER);

        return searchCard;
    }

    private JPanel createTableCard(Color card) {
        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(card);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 235)),
                new EmptyBorder(12, 12, 12, 12)));

        JLabel tableTitle = new JLabel("Inventory Items");
        tableTitle.setFont(new Font("Inter", Font.BOLD, 14));
        tableCard.add(tableTitle, BorderLayout.NORTH);

        // Table
        String[] cols = {"ID", "Name", "Price", "Category", "Quantity", "Brand", "Supplier ID"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(28);
        table.setAutoCreateRowSorter(true);

        JScrollPane scroll = new JScrollPane(table);
        tableCard.add(scroll, BorderLayout.CENTER);

        return tableCard;
    }

    private JTextField makeTextField(String placeholder, JPanel parent, boolean disabled) {
        JPanel row = new JPanel(new BorderLayout(6, 6));
        row.setOpaque(false);
        JLabel lbl = new JLabel(placeholder);
        lbl.setFont(new Font("Inter", Font.PLAIN, 12));
        row.add(lbl, BorderLayout.NORTH);

        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(200, 30));
        tf.setFont(new Font("Inter", Font.PLAIN, 13));
        if (disabled) {
            tf.setEditable(true);
        }
        row.add(tf, BorderLayout.CENTER);
        parent.add(row);
        parent.add(Box.createRigidArea(new Dimension(0, 6)));
        return tf;
    }

    private JButton styledButton(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 6, 8, 6));
        b.setFont(new Font("Inter", Font.BOLD, 12));
        b.setBackground(new Color(107, 114, 179));
        b.setForeground(Color.WHITE);
        b.setOpaque(true);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void setupEventListeners() {
        btnAdd.addActionListener(e -> onAdd());
        btnUpdate.addActionListener(e -> onUpdate());
        btnDelete.addActionListener(e -> onDelete());
        btnView.addActionListener(e -> loadTableData());
        btnSearch.addActionListener(e -> onSearch());
        btnGenerateReport.addActionListener(e -> onGenerateReport());
        btnManageCategories.addActionListener(e -> onManageCategories());

        // Table row click -> populate fields
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = table.getSelectedRow();
                if (r >= 0) {
                    int modelRow = table.convertRowIndexToModel(r);
                    txtId.setText(String.valueOf(tableModel.getValueAt(modelRow, 0)));
                    txtName.setText(String.valueOf(tableModel.getValueAt(modelRow, 1)));
                    txtPrice.setText(String.valueOf(tableModel.getValueAt(modelRow, 2)));
                    cmbCategory.setSelectedItem(String.valueOf(tableModel.getValueAt(modelRow, 3)));
                    txtQuantity.setText(String.valueOf(tableModel.getValueAt(modelRow, 4)));
                    txtBrand.setText(String.valueOf(tableModel.getValueAt(modelRow, 5)));
                    txtSupplierId.setText(String.valueOf(tableModel.getValueAt(modelRow, 6)));
                }
            }
        });

        // Enter key on search field
        txtSearch.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    onSearch();
                }
            }
        });
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        try {
            List<Products> list = productDAO.findAll();
            for (Products p : list) {
                Object[] row = {
                    p.getItem_id(),
                    p.getItem_name(),
                    p.getPrice(),
                    p.getCategory(),
                    p.getQuantity(),
                    p.getBrand(),
                    p.getSupplier_id()
                };
                tableModel.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading data. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadCategories() {
        cmbCategory.removeAllItems();
        try {
            List<String> categories = productDAO.getAllCategories();
            for (String category : categories) {
                cmbCategory.addItem(category);
            }
        } catch (Exception ex) {
            System.err.println("Error loading categories: " + ex.getMessage());
        }
    }

    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtPrice.setText("");
        cmbCategory.setSelectedIndex(-1);
        txtQuantity.setText("");
        txtBrand.setText("");
        txtSupplierId.setText("");
    }

    private boolean validateInputs(boolean requireIdForUpdateOrDelete) {
        if (requireIdForUpdateOrDelete) {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Product ID to update or delete.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        if (txtName.getText().trim().isEmpty()
                || txtPrice.getText().trim().isEmpty()
                || cmbCategory.getSelectedItem() == null
                || txtQuantity.getText().trim().isEmpty()
                || txtBrand.getText().trim().isEmpty()
                || txtSupplierId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Numeric checks
        try {
            double price = Double.parseDouble(txtPrice.getText().trim());
            int qty = Integer.parseInt(txtQuantity.getText().trim());
            int suppId = Integer.parseInt(txtSupplierId.getText().trim());

            if (price <= 0) {
                JOptionPane.showMessageDialog(this, "Price must be greater than 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (qty < 0) {
                JOptionPane.showMessageDialog(this, "Quantity cannot be negative.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Price, Quantity and Supplier ID must be numeric.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private Products productFromFields(boolean includeId) {
        int id = 0;
        if (includeId) {
            try {
                id = Integer.parseInt(txtId.getText().trim());
            } catch (NumberFormatException e) {
                // handle upstream with validation
            }
        }
        String name = txtName.getText().trim();
        double price = Double.parseDouble(txtPrice.getText().trim());
        String category = (String) cmbCategory.getSelectedItem();
        int qty = Integer.parseInt(txtQuantity.getText().trim());
        String brand = txtBrand.getText().trim();
        int supp = Integer.parseInt(txtSupplierId.getText().trim());

        return new Products(id, name, price, category, qty, brand, supp);
    }

    private void onAdd() {
        if (!validateInputs(false)) return;
        try {
            Products p = productFromFields(false);
            productDAO.insert(p);
            JOptionPane.showMessageDialog(this, "Product added successfully!");
            clearFields();
            loadTableData();
            loadCategories();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding product: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onUpdate() {
        if (!validateInputs(true)) return;
        try {
            Products p = productFromFields(true);
            productDAO.update(p);
            JOptionPane.showMessageDialog(this, "Product updated successfully!");
            clearFields();
            loadTableData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating product: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onDelete() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Product ID to delete.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            int id = Integer.parseInt(txtId.getText().trim());
            productDAO.deleteById(id);
            JOptionPane.showMessageDialog(this, "Product deleted successfully!");
            clearFields();
            loadTableData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting product: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onSearch() {
        String searchTerm = txtSearch.getText().trim();
        if (searchTerm.isEmpty()) {
            loadTableData();
            return;
        }

        tableModel.setRowCount(0);
        try {
            List<Products> results = new ArrayList<>();
            String searchType = (String) cmbSearchType.getSelectedItem();

            switch (searchType) {
                case "Name":
                    results = productDAO.searchByName(searchTerm);
                    break;
                case "Category":
                    results = productDAO.searchByCategory(searchTerm);
                    break;
                case "Brand":
                    results = productDAO.searchByBrand(searchTerm);
                    break;
                case "All":
                    results = productDAO.searchByName(searchTerm);
                    results.addAll(productDAO.searchByCategory(searchTerm));
                    results.addAll(productDAO.searchByBrand(searchTerm));
                    break;
            }

            for (Products p : results) {
                Object[] row = {
                    p.getItem_id(),
                    p.getItem_name(),
                    p.getPrice(),
                    p.getCategory(),
                    p.getQuantity(),
                    p.getBrand(),
                    p.getSupplier_id()
                };
                tableModel.addRow(row);
            }

            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No products found matching your search criteria.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error searching products: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onGenerateReport() {
        new ReportGUI(reportGenerator).setVisible(true);
    }

    private void onManageCategories() {
        new CategoryManagementGUI(categoryDAO, this).setVisible(true);
    }
}
