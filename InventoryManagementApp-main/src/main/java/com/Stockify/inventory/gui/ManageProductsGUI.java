package com.Stockify.inventory.gui; // change to your package or remove if using default

import com.Stockify.inventory.model.Products;
import com.Stockify.inventory.db.ManageProducts;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
// import java.sql.SQLException;
import java.util.ArrayList;

public class ManageProductsGUI extends JFrame {
    private ManageProducts manageProducts;

    private JPanel contentPane;
    private JTextField txtId, txtName, txtPrice, txtCategory, txtQuantity, txtBrand, txtSupplierId;
    private JButton btnAdd, btnUpdate, btnDelete, btnView;
    private JTable table;
    private DefaultTableModel tableModel;

    public ManageProductsGUI() {
        // Initialize backend manager (assumes it sets up DB connection internally)
        manageProducts = new ManageProducts();

        setTitle("Stockify — Product Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 640);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));

        initComponents();
        loadTableData();
    }

    private void initComponents() {
        // Modern pastel theme colors
        Color bg = new Color(250, 250, 252);
        Color panel = new Color(245, 246, 249);
        Color accent = new Color(107, 114, 179); // soft purple
        Color card = new Color(255, 255, 255);

        contentPane = new JPanel(new BorderLayout(14, 14));
        contentPane.setBackground(bg);
        contentPane.setBorder(new EmptyBorder(14, 14, 14, 14));
        setContentPane(contentPane);

        // Top header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(bg);
        JLabel title = new JLabel("Stockify — Products");
        title.setFont(new Font("Inter", Font.BOLD, 22));
        JLabel subtitle = new JLabel("Add, update, delete and view inventory items");
        subtitle.setFont(new Font("Inter", Font.PLAIN, 12));
        subtitle.setForeground(Color.DARK_GRAY);

        JPanel titleBox = new JPanel(new GridLayout(2, 1));
        titleBox.setOpaque(false);
        titleBox.add(title);
        titleBox.add(subtitle);
        header.add(titleBox, BorderLayout.WEST);

        contentPane.add(header, BorderLayout.NORTH);

        // Main center area with form (left) and table (right)
        JPanel main = new JPanel(new BorderLayout(12, 12));
        main.setOpaque(false);
        contentPane.add(main, BorderLayout.CENTER);

        // Left form card
        JPanel formCard = new JPanel();
        formCard.setBackground(card);
        formCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 235)),
                new EmptyBorder(16, 16, 16, 16)));
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setPreferredSize(new Dimension(360, 0));

        JLabel formTitle = new JLabel("Product Details");
        formTitle.setFont(new Font("Inter", Font.BOLD, 16));
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(formTitle);
        formCard.add(Box.createRigidArea(new Dimension(0, 10)));

        // input fields helper
        txtId = makeTextField("Product ID (for update/delete)", formCard, true);
        txtName = makeTextField("Name", formCard, false);
        txtPrice = makeTextField("Price", formCard, false);
        txtCategory = makeTextField("Category", formCard, false);
        txtQuantity = makeTextField("Quantity", formCard, false);
        txtBrand = makeTextField("Brand", formCard, false);
        txtSupplierId = makeTextField("Supplier ID", formCard, false);

        formCard.add(Box.createRigidArea(new Dimension(0, 8)));

        // Buttons row
        JPanel btnRow = new JPanel(new GridLayout(1, 4, 8, 0));
        btnRow.setOpaque(false);

        btnAdd = styledButton("Add Product");
        btnUpdate = styledButton("Update Product");
        btnDelete = styledButton("Delete Product");
        btnView = styledButton("View All");

        btnRow.add(btnAdd);
        btnRow.add(btnUpdate);
        btnRow.add(btnDelete);
        btnRow.add(btnView);

        formCard.add(btnRow);
        formCard.add(Box.createRigidArea(new Dimension(0, 8)));

        // small note
        JLabel note = new JLabel("<html><i>Tip: Fill Product ID for update/delete.</i></html>");
        note.setFont(new Font("Inter", Font.PLAIN, 11));
        note.setForeground(Color.GRAY);
        formCard.add(note);

        main.add(formCard, BorderLayout.WEST);

        // Right: Table card
        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(card);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 235)),
                new EmptyBorder(12, 12, 12, 12)));

        // Search & controls top of table
        JPanel tableTop = new JPanel(new BorderLayout(8, 8));
        tableTop.setOpaque(false);

        JLabel tableTitle = new JLabel("Inventory Items");
        tableTitle.setFont(new Font("Inter", Font.BOLD, 14));

        tableTop.add(tableTitle, BorderLayout.WEST);

        tableCard.add(tableTop, BorderLayout.NORTH);

        // Table
        String[] cols = { "ID", "Name", "Price", "Category", "Quantity", "Brand", "Supplier ID" };
        tableModel = new DefaultTableModel(cols, 0) {
            // prevent editing directly in the table
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

        main.add(tableCard, BorderLayout.CENTER);

        // Button actions
        btnAdd.addActionListener(e -> onAdd());
        btnUpdate.addActionListener(e -> onUpdate());
        btnDelete.addActionListener(e -> onDelete());
        btnView.addActionListener(e -> loadTableData());

        // Table row click -> populate fields
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = table.getSelectedRow();
                if (r >= 0) {
                    // convert view index to model index if sorted
                    int modelRow = table.convertRowIndexToModel(r);
                    txtId.setText(String.valueOf(tableModel.getValueAt(modelRow, 0)));
                    txtName.setText(String.valueOf(tableModel.getValueAt(modelRow, 1)));
                    txtPrice.setText(String.valueOf(tableModel.getValueAt(modelRow, 2)));
                    txtCategory.setText(String.valueOf(tableModel.getValueAt(modelRow, 3)));
                    txtQuantity.setText(String.valueOf(tableModel.getValueAt(modelRow, 4)));
                    txtBrand.setText(String.valueOf(tableModel.getValueAt(modelRow, 5)));
                    txtSupplierId.setText(String.valueOf(tableModel.getValueAt(modelRow, 6)));
                }
            }
        });

        // Allow Enter key on fields to trigger Add
        txtSupplierId.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    onAdd();
                }
            }
        });
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
            tf.setEditable(true); // keep editable because user may input ID
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

    // Load table rows from backend
    private void loadTableData() {
        tableModel.setRowCount(0);
        try {
            ArrayList<Products> list = manageProducts.getViewOfProducts();
            if (list != null) {
                for (Products p : list) {
                    Object[] row = new Object[] {
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
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading data. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Clear all text fields
    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtPrice.setText("");
        txtCategory.setText("");
        txtQuantity.setText("");
        txtBrand.setText("");
        txtSupplierId.setText("");
    }

    // Validate inputs before Add/Update
    private boolean validateInputs(boolean requireIdForUpdateOrDelete) {
        if (requireIdForUpdateOrDelete) {
            if (txtId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Product ID to update or delete.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        // For Add and Update (except ID-only check) ensure fields are not empty
        if (txtName.getText().trim().isEmpty()
                || txtPrice.getText().trim().isEmpty()
                || txtCategory.getText().trim().isEmpty()
                || txtQuantity.getText().trim().isEmpty()
                || txtBrand.getText().trim().isEmpty()
                || txtSupplierId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Numeric checks
        double price;
        int qty, suppId;
        try {
            price = Double.parseDouble(txtPrice.getText().trim());
            qty = Integer.parseInt(txtQuantity.getText().trim());
            suppId = Integer.parseInt(txtSupplierId.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Price, Quantity and Supplier ID must be numeric.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (price <= 0) {
            JOptionPane.showMessageDialog(this, "Price must be greater than 0.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (qty <= 0) {
            JOptionPane.showMessageDialog(this, "Quantity cannot be negative.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    // Build Products model from fields (for add)
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
        String category = txtCategory.getText().trim();
        int qty = Integer.parseInt(txtQuantity.getText().trim());
        String brand = txtBrand.getText().trim();
        int supp = Integer.parseInt(txtSupplierId.getText().trim());

        if (includeId) {
            // Assumes Products has constructor Products(int id, String name, double price, String category, int quantity, String brand, int supplierId)
            return new Products(id, name, price, category, qty, brand, supp);
        } else {
            // Assumes Products has constructor without id or one where id is auto-managed
            // If there's no such constructor, still pass 0 for id (backend insert should ignore)
            return new Products(0, name, price, category, qty, brand, supp);
        }
    }

    // Button handlers
    private void onAdd() {
        if (!validateInputs(false))
            return;
        Products p = productFromFields(false);
        manageProducts.addProduct(p);
        JOptionPane.showMessageDialog(this, "Product added successfully (or check console if failed).");
        clearFields();
        loadTableData();
    }

    private void onUpdate() {
        if (!validateInputs(true))
            return;
        Products p = productFromFields(true);
        manageProducts.updateProduct(p);
        JOptionPane.showMessageDialog(this, "Product updated successfully (or check console if failed).");
        clearFields();
        loadTableData();
    }

    private void onDelete() {
        if (txtId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Product ID to delete.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION)
            return;

        int id = Integer.parseInt(txtId.getText().trim());
        Products p = new Products(id, "", 0, "", 0, "", 0);
        manageProducts.deleteProduct(p);
        JOptionPane.showMessageDialog(this, "Product deleted successfully (or check console if failed).");
        clearFields();
        loadTableData();
    }
}