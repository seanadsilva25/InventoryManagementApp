package com.Stockify.inventory.gui;

import com.Stockify.inventory.backend.UserAuthentication.User;
import com.Stockify.inventory.db.ProductDAO;
import com.Stockify.inventory.db.CategoryDAO;
import com.Stockify.inventory.db.SupplierDAO;
import com.Stockify.inventory.db.ReportGenerator;
import com.Stockify.inventory.main.SimpleApplicationController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainDashboard extends JFrame {
    public User currentUser;
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;
    private SupplierDAO supplierDAO;
    private ReportGenerator reportGenerator;
    
    // UI Components
    private JPanel contentPane;
    private JLabel welcomeLabel;
    private JLabel userInfoLabel;
    private JButton btnManageProducts;
    private JButton btnGenerateReports;
    private JButton btnManageCategories;
    private JButton btnLowStockAlert;
    private JButton btnLogout;
    private JTable summaryTable;
    private DefaultTableModel summaryTableModel;
    
    // Modern color scheme matching the application
    private static final Color BG_COLOR = new Color(250, 250, 252);
    private static final Color CARD_COLOR = new Color(255, 255, 255);
    private static final Color ACCENT_COLOR = new Color(107, 114, 179);
    private static final Color TEXT_COLOR = new Color(60, 60, 60);
    private static final Color BORDER_COLOR = new Color(230, 230, 235);
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private static final Color WARNING_COLOR = new Color(255, 152, 0);
    
    public MainDashboard(User user) {
        this.currentUser = user;
        this.productDAO = new ProductDAO();
        this.categoryDAO = new CategoryDAO();
        this.supplierDAO = new SupplierDAO();
        this.reportGenerator = new ReportGenerator();
        
        initComponents();
        loadDashboardData();
    }
    
    private void initComponents() {
        setTitle("Stockify — Main Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));
        
        contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(BG_COLOR);
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(contentPane);
        
        // Create header
        createHeader();
        
        // Create main content
        createMainContent();
        
        // Create footer
        createFooter();
    }
    
    private void createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_COLOR);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Title and welcome
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(BG_COLOR);
        
        JLabel titleLabel = new JLabel("Stockify Dashboard");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 28));
        titleLabel.setForeground(ACCENT_COLOR);
        
        welcomeLabel = new JLabel("Welcome back, " + currentUser.getFullName());
        welcomeLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        welcomeLabel.setForeground(TEXT_COLOR);
        
        titlePanel.add(titleLabel);
        titlePanel.add(welcomeLabel);
        
        // User info and logout
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBackground(BG_COLOR);
        
        userInfoLabel = new JLabel("Role: " + currentUser.getRole().toUpperCase());
        userInfoLabel.setFont(new Font("Inter", Font.PLAIN, 12));
        userInfoLabel.setForeground(TEXT_COLOR);
        userInfoLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Inter", Font.PLAIN, 12));
        btnLogout.setPreferredSize(new Dimension(80, 30));
        btnLogout.setBackground(Color.WHITE);
        btnLogout.setForeground(TEXT_COLOR);
        btnLogout.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        userPanel.add(userInfoLabel, BorderLayout.NORTH);
        userPanel.add(btnLogout, BorderLayout.SOUTH);
        
        header.add(titlePanel, BorderLayout.WEST);
        header.add(userPanel, BorderLayout.EAST);
        
        contentPane.add(header, BorderLayout.NORTH);
    }
    
    private void createMainContent() {
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BG_COLOR);
        
        // Quick actions panel
        JPanel actionsPanel = createQuickActionsPanel();
        mainPanel.add(actionsPanel, BorderLayout.WEST);
        
        // Summary panel
        JPanel summaryPanel = createSummaryPanel();
        mainPanel.add(summaryPanel, BorderLayout.CENTER);
        
        contentPane.add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createQuickActionsPanel() {
        JPanel actionsPanel = new JPanel();
        actionsPanel.setBackground(CARD_COLOR);
        actionsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(20, 20, 20, 20)));
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
        actionsPanel.setPreferredSize(new Dimension(250, 0));
        
        JLabel actionsTitle = new JLabel("Quick Actions");
        actionsTitle.setFont(new Font("Inter", Font.BOLD, 16));
        actionsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        actionsTitle.setForeground(TEXT_COLOR);
        actionsPanel.add(actionsTitle);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Manage Products button
        btnManageProducts = createActionButton("Manage Products", "Add, edit, and view inventory items");
        actionsPanel.add(btnManageProducts);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Generate Reports button
        btnGenerateReports = createActionButton("Generate Reports", "Create comprehensive inventory reports");
        actionsPanel.add(btnGenerateReports);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Manage Categories button
        btnManageCategories = createActionButton("Manage Categories", "Organize product categories");
        actionsPanel.add(btnManageCategories);
        actionsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Low Stock Alert button
        btnLowStockAlert = createActionButton("Low Stock Alert", "Check items running low on stock");
        actionsPanel.add(btnLowStockAlert);
        
        return actionsPanel;
    }
    
    private JButton createActionButton(String title, String description) {
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(CARD_COLOR);
        buttonPanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        buttonPanel.setPreferredSize(new Dimension(200, 70));
        
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setBackground(CARD_COLOR);
        button.setBorder(null);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 12));
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setBorder(new EmptyBorder(10, 15, 5, 15));
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Inter", Font.PLAIN, 10));
        descLabel.setForeground(TEXT_COLOR);
        descLabel.setBorder(new EmptyBorder(0, 15, 10, 15));
        
        button.add(titleLabel, BorderLayout.NORTH);
        button.add(descLabel, BorderLayout.SOUTH);
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(245, 246, 249));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(CARD_COLOR);
            }
        });
        
        return button;
    }
    
    private JPanel createSummaryPanel() {
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBackground(CARD_COLOR);
        summaryPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(20, 20, 20, 20)));
        
        JLabel summaryTitle = new JLabel("Inventory Summary");
        summaryTitle.setFont(new Font("Inter", Font.BOLD, 16));
        summaryTitle.setForeground(TEXT_COLOR);
        summaryPanel.add(summaryTitle, BorderLayout.NORTH);
        
        // Summary table
        String[] columns = {"Metric", "Value", "Status"};
        summaryTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        summaryTable = new JTable(summaryTableModel);
        summaryTable.setFillsViewportHeight(true);
        summaryTable.setRowHeight(30);
        summaryTable.setFont(new Font("Inter", Font.PLAIN, 12));
        summaryTable.setShowGrid(false);
        summaryTable.setIntercellSpacing(new Dimension(0, 0));
        
        // Custom renderer for status column
        summaryTable.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value != null) {
                    String status = value.toString();
                    if ("Good".equals(status)) {
                        c.setForeground(SUCCESS_COLOR);
                    } else if ("Warning".equals(status)) {
                        c.setForeground(WARNING_COLOR);
                    } else {
                        c.setForeground(TEXT_COLOR);
                    }
                    setFont(getFont().deriveFont(Font.BOLD));
                }
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(summaryTable);
        scrollPane.setBorder(null);
        summaryPanel.add(scrollPane, BorderLayout.CENTER);
        
        return summaryPanel;
    }
    
    private void createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(BG_COLOR);
        footer.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        JLabel footerLabel = new JLabel("© 2024 Stockify Inventory Management System");
        footerLabel.setFont(new Font("Inter", Font.PLAIN, 10));
        footerLabel.setForeground(Color.GRAY);
        
        footer.add(footerLabel);
        contentPane.add(footer, BorderLayout.SOUTH);
    }
    
    private void loadDashboardData() {
        try {
            // Load summary data
            ReportGenerator.InventoryReport report = reportGenerator.generateInventoryReport();
            
            // Clear existing data
            summaryTableModel.setRowCount(0);
            
            // Add summary rows
            summaryTableModel.addRow(new Object[]{"Total Products", report.getTotalItems(), "Good"});
            summaryTableModel.addRow(new Object[]{"Total Quantity", report.getTotalQuantity(), "Good"});
            summaryTableModel.addRow(new Object[]{"Total Value", "$" + String.format("%.2f", report.getTotalValue()), "Good"});
            
            String lowStockStatus = report.getLowStockItems() > 0 ? "Warning" : "Good";
            summaryTableModel.addRow(new Object[]{"Low Stock Items", report.getLowStockItems(), lowStockStatus});
            
            // Category distribution
            summaryTableModel.addRow(new Object[]{"Categories", report.getCategoryCount().size(), "Good"});
            
            // Brand distribution
            summaryTableModel.addRow(new Object[]{"Brands", report.getBrandCount().size(), "Good"});
            
        } catch (Exception e) {
            System.err.println("Error loading dashboard data: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error loading dashboard data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void setupEventListeners() {
        btnManageProducts.addActionListener(e -> {
            new EnhancedManageProductsGUI().setVisible(true);
        });
        
        btnGenerateReports.addActionListener(e -> {
            new ReportGUI(reportGenerator).setVisible(true);
        });
        
        btnManageCategories.addActionListener(e -> {
            new CategoryManagementGUI(categoryDAO, this).setVisible(true);
        });
        
        btnLowStockAlert.addActionListener(e -> {
            new LowStockAlertGUI().setVisible(true);
        });
        
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to logout?", 
                "Confirm Logout", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                // Show login page again
                SwingUtilities.invokeLater(() -> {
                    SimpleApplicationController.startApplication();
                });
            }
        });
    }
    
    // Initialize event listeners
    {
        setupEventListeners();
    }
    
    // Refresh dashboard data
    public void refreshDashboard() {
        loadDashboardData();
    }
}
