package com.Stockify.inventory.gui;

import com.Stockify.inventory.model.Category;
import com.Stockify.inventory.db.CategoryDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CategoryManagementGUI extends JFrame {
    private CategoryDAO categoryDAO;
    private JFrame parentFrame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtCode, txtName, txtDescription;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh;

    public CategoryManagementGUI(CategoryDAO categoryDAO, JFrame parentFrame) {
        this.categoryDAO = categoryDAO;
        this.parentFrame = parentFrame;

        setTitle("Category Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(parentFrame);

        initComponents();
        loadTableData();
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

        JLabel title = new JLabel("Category Management");
        title.setFont(new Font("Inter", Font.BOLD, 18));
        title.setForeground(new Color(107, 114, 179));
        header.add(title, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);

        // Main content
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(bg);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Form panel
        JPanel formPanel = createFormPanel(card);
        mainPanel.add(formPanel, BorderLayout.WEST);

        // Table panel
        JPanel tablePanel = createTablePanel(card);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
        
        // Set up event listeners
        setupEventListeners();
    }

    private JPanel createFormPanel(Color card) {
        JPanel formPanel = new JPanel();
        formPanel.setBackground(card);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 235)),
                new EmptyBorder(15, 15, 15, 15)));
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setPreferredSize(new Dimension(250, 0));

        JLabel formTitle = new JLabel("Category Details");
        formTitle.setFont(new Font("Inter", Font.BOLD, 14));
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(formTitle);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Input fields
        txtCode = createTextField("Category Code", formPanel);
        txtName = createTextField("Category Name", formPanel);
        txtDescription = createTextField("Description", formPanel);

        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        buttonPanel.setOpaque(false);

        btnAdd = createButton("Add");
        btnUpdate = createButton("Update");
        btnDelete = createButton("Delete");
        btnRefresh = createButton("Refresh");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);

        formPanel.add(buttonPanel);

        return formPanel;
    }

    private JPanel createTablePanel(Color card) {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(card);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 235)),
                new EmptyBorder(15, 15, 15, 15)));

        JLabel tableTitle = new JLabel("Categories");
        tableTitle.setFont(new Font("Inter", Font.BOLD, 14));
        tablePanel.add(tableTitle, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Code", "Name", "Description"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JTextField createTextField(String label, JPanel parent) {
        JPanel row = new JPanel(new BorderLayout(5, 5));
        row.setOpaque(false);
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Inter", Font.PLAIN, 11));
        row.add(lbl, BorderLayout.NORTH);

        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(200, 25));
        tf.setFont(new Font("Inter", Font.PLAIN, 12));
        row.add(tf, BorderLayout.CENTER);
        
        parent.add(row);
        parent.add(Box.createRigidArea(new Dimension(0, 5)));
        return tf;
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        btn.setFont(new Font("Inter", Font.BOLD, 11));
        btn.setBackground(new Color(107, 114, 179));
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void setupEventListeners() {
        btnAdd.addActionListener(e -> onAdd());
        btnUpdate.addActionListener(e -> onUpdate());
        btnDelete.addActionListener(e -> onDelete());
        btnRefresh.addActionListener(e -> loadTableData());

        // Table row click -> populate fields
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    int modelRow = table.convertRowIndexToModel(row);
                    txtCode.setText(String.valueOf(tableModel.getValueAt(modelRow, 1)));
                    txtName.setText(String.valueOf(tableModel.getValueAt(modelRow, 2)));
                    txtDescription.setText(String.valueOf(tableModel.getValueAt(modelRow, 3)));
                }
            }
        });
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        try {
            List<Category> categories = categoryDAO.findAll();
            for (Category category : categories) {
                Object[] row = {
                    category.getCategory_id(),
                    category.getCategory_code(),
                    category.getName(),
                    category.getDescription()
                };
                tableModel.addRow(row);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading categories: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        txtCode.setText("");
        txtName.setText("");
        txtDescription.setText("");
    }

    private boolean validateInputs() {
        if (txtCode.getText().trim().isEmpty() ||
            txtName.getText().trim().isEmpty() ||
            txtDescription.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void onAdd() {
        if (!validateInputs()) return;
        
        try {
            Category category = new Category();
            category.setCategory_code(txtCode.getText().trim());
            category.setName(txtName.getText().trim());
            category.setDescription(txtDescription.getText().trim());
            
            categoryDAO.insert(category);
            JOptionPane.showMessageDialog(this, "Category added successfully!");
            clearFields();
            loadTableData();
            
            // Refresh parent frame if it's the enhanced products GUI
            if (parentFrame instanceof EnhancedManageProductsGUI) {
                ((EnhancedManageProductsGUI) parentFrame).loadCategories();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error adding category: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onUpdate() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a category to update.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!validateInputs()) return;
        
        try {
            int modelRow = table.convertRowIndexToModel(selectedRow);
            int categoryId = (Integer) tableModel.getValueAt(modelRow, 0);
            
            Category category = new Category();
            category.setCategory_id(categoryId);
            category.setCategory_code(txtCode.getText().trim());
            category.setName(txtName.getText().trim());
            category.setDescription(txtDescription.getText().trim());
            
            categoryDAO.update(category);
            JOptionPane.showMessageDialog(this, "Category updated successfully!");
            clearFields();
            loadTableData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error updating category: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onDelete() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a category to delete.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this category?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        
        if (confirm != JOptionPane.YES_OPTION) return;
        
        try {
            int modelRow = table.convertRowIndexToModel(selectedRow);
            int categoryId = (Integer) tableModel.getValueAt(modelRow, 0);
            
            categoryDAO.deleteById(categoryId);
            JOptionPane.showMessageDialog(this, "Category deleted successfully!");
            clearFields();
            loadTableData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error deleting category: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
