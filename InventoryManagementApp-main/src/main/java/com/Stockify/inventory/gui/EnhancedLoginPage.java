package com.Stockify.inventory.gui;

import com.Stockify.inventory.backend.UserAuthentication;
import com.Stockify.inventory.backend.UserAuthentication.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class EnhancedLoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    private JLabel statusLabel;
    
    // Modern color scheme matching the main application
    private static final Color BG_COLOR = new Color(250, 250, 252);
    private static final Color CARD_COLOR = new Color(255, 255, 255);
    private static final Color ACCENT_COLOR = new Color(107, 114, 179);
    private static final Color TEXT_COLOR = new Color(60, 60, 60);
    private static final Color BORDER_COLOR = new Color(230, 230, 235);
    
    private LoginCallback callback;
    
    public interface LoginCallback {
        void onLoginSuccess(User user);
        void onLoginCancel();
    }
    
    public EnhancedLoginPage(LoginCallback callback) {
        this.callback = callback;
        initComponents();
    }
    
    private void initComponents() {
        setTitle("Stockify — Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);
        
        // Header
        JPanel headerPanel = createHeader();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Login form
        JPanel formPanel = createLoginForm();
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Footer with buttons
        JPanel footerPanel = createFooter();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        // Set up keyboard shortcuts
        setupKeyboardShortcuts();
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_COLOR);
        header.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Logo/Title
        JLabel titleLabel = new JLabel("Stockify", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 32));
        titleLabel.setForeground(ACCENT_COLOR);
        
        JLabel subtitleLabel = new JLabel("Inventory Management System", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Inter", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_COLOR);
        
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(BG_COLOR);
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        
        header.add(titlePanel, BorderLayout.CENTER);
        return header;
    }
    
    private JPanel createLoginForm() {
        JPanel formPanel = new JPanel();
        formPanel.setBackground(CARD_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(30, 30, 30, 30)));
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        
        // Form title
        JLabel formTitle = new JLabel("Sign In to Your Account");
        formTitle.setFont(new Font("Inter", Font.BOLD, 18));
        formTitle.setForeground(TEXT_COLOR);
        formTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(formTitle);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Username field
        JPanel usernamePanel = createInputField("Username", "Enter your username");
        usernameField = (JTextField) usernamePanel.getComponent(1);
        formPanel.add(usernamePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Password field
        JPanel passwordPanel = createPasswordField("Password", "Enter your password");
        passwordField = (JPasswordField) passwordPanel.getComponent(1);
        formPanel.add(passwordPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Status label
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Inter", Font.PLAIN, 12));
        statusLabel.setForeground(Color.RED);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(statusLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Demo credentials info
        JLabel demoLabel = new JLabel("<html><center>Demo Credentials:<br>Username: admin, Password: admin<br>Username: demo, Password: demo123</center></html>");
        demoLabel.setFont(new Font("Inter", Font.PLAIN, 10));
        demoLabel.setForeground(Color.GRAY);
        demoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(demoLabel);
        
        return formPanel;
    }
    
    private JPanel createInputField(String label, String placeholder) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(CARD_COLOR);
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Inter", Font.PLAIN, 12));
        labelComponent.setForeground(TEXT_COLOR);
        panel.add(labelComponent, BorderLayout.NORTH);
        
        JTextField textField = new JTextField();
        textField.setFont(new Font("Inter", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(8, 12, 8, 12)));
        textField.setPreferredSize(new Dimension(0, 40));
        
        // Placeholder effect
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (textField.getText().trim().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                }
            }
        });
        
        panel.add(textField, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createPasswordField(String label, String placeholder) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(CARD_COLOR);
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Inter", Font.PLAIN, 12));
        labelComponent.setForeground(TEXT_COLOR);
        panel.add(labelComponent, BorderLayout.NORTH);
        
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Inter", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(8, 12, 8, 12)));
        passwordField.setPreferredSize(new Dimension(0, 40));
        passwordField.setEchoChar('*');
        
        panel.add(passwordField, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        footer.setBackground(BG_COLOR);
        footer.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        // Cancel button
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Inter", Font.PLAIN, 12));
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.setBackground(Color.WHITE);
        cancelButton.setForeground(TEXT_COLOR);
        cancelButton.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Login button
        loginButton = new JButton("Sign In");
        loginButton.setFont(new Font("Inter", Font.BOLD, 12));
        loginButton.setPreferredSize(new Dimension(100, 35));
        loginButton.setBackground(ACCENT_COLOR);
        loginButton.setForeground(Color.WHITE);
        loginButton.setBorder(null);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        footer.add(cancelButton);
        footer.add(loginButton);
        
        return footer;
    }
    
    private void setupKeyboardShortcuts() {
        // Enter key on password field triggers login
        passwordField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        
        // Enter key on username field moves to password
        usernameField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passwordField.requestFocus();
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        
        // Escape key cancels login
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");
        getRootPane().getActionMap().put("cancel", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performCancel();
            }
        });
    }
    
    private void performLogin() {
        String username = getUsername();
        String password = getPassword();
        
        if (username.isEmpty() || password.isEmpty()) {
            showStatus("Please enter both username and password", true);
            return;
        }
        
        // Authenticate user
        User user = UserAuthentication.authenticate(username, password);
        
        if (user != null) {
            showStatus("Login successful! Welcome, " + user.getFullName(), false);
            
            // Small delay to show success message
            Timer timer = new Timer(1000, e -> {
                dispose();
                if (callback != null) {
                    callback.onLoginSuccess(user);
                }
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            showStatus("Invalid username or password. Please try again.", true);
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }
    
    private void performCancel() {
        dispose();
        if (callback != null) {
            callback.onLoginCancel();
        }
    }
    
    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setForeground(isError ? Color.RED : Color.GREEN);
        statusLabel.repaint();
    }
    
    private String getUsername() {
        String text = usernameField.getText();
        return "Enter your username".equals(text) ? "" : text;
    }
    
    private String getPassword() {
        return new String(passwordField.getPassword());
    }
    
    // Event listeners
    {
        loginButton.addActionListener(e -> performLogin());
        cancelButton.addActionListener(e -> performCancel());
    }
}
