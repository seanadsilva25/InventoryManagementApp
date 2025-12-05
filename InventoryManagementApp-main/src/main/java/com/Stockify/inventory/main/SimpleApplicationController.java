package com.Stockify.inventory.main;

import com.Stockify.inventory.backend.LoginPage;
import com.Stockify.inventory.gui.EnhancedManageProductsGUI;

import javax.swing.*;
import java.awt.*;

/**
 * Simple Application Controller that uses the existing LoginPage
 * and transitions to the Enhanced Product Manager
 */
public class SimpleApplicationController {
    
    public static void startApplication() {
        SwingUtilities.invokeLater(() -> {
            showLoginPage();
        });
    }
    
    private static void showLoginPage() {
        // Create and show the existing LoginPage
        LoginPage loginPage = new LoginPage();
        
        // Override the performLogin method to handle successful login
        // We'll use reflection to access the private method, or create a custom version
        // For now, let's create a custom login page that extends the existing one
        
        CustomLoginPage customLogin = new CustomLoginPage();
        customLogin.setVisible(true);
    }
    
    // Custom login page that extends the existing LoginPage functionality
    static class CustomLoginPage extends JFrame {
        private JTextField usernameField;
        private JPasswordField passwordField;
        private JButton loginButton;
        private static final Color PURPLE = new Color(102, 51, 153);

        public CustomLoginPage() {
            super("Stockify User Login");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(420, 300);
            setLocationRelativeTo(null);
            initUI();
        }

        private void initUI() {
            JPanel content = new JPanel();
            content.setBackground(Color.WHITE);
            content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            content.setLayout(new BorderLayout());
            setContentPane(content);

            // Title
            JLabel titleLbl = new JLabel("Stockify User Login", SwingConstants.CENTER);
            titleLbl.setForeground(PURPLE);
            titleLbl.setFont(new Font("SansSerif", Font.BOLD, 28));
            content.add(titleLbl, BorderLayout.NORTH);

            // Form panel
            JPanel form = new JPanel();
            form.setOpaque(false);
            form.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);

            // Username label
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.EAST;
            JLabel uLbl = new JLabel("Username:");
            uLbl.setForeground(PURPLE);
            form.add(uLbl, gbc);

            // Username field
            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.WEST;
            usernameField = new JTextField(16);
            form.add(usernameField, gbc);

            // Password label
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.EAST;
            JLabel pLbl = new JLabel("Password:");
            pLbl.setForeground(PURPLE);
            form.add(pLbl, gbc);

            // Password field
            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.WEST;
            passwordField = new JPasswordField(16);
            form.add(passwordField, gbc);

            content.add(form, BorderLayout.CENTER);

            // Button panel & login button
            JPanel btnPanel = new JPanel();
            btnPanel.setOpaque(false);
            loginButton = new JButton("Login");
            loginButton.setBackground(PURPLE);
            loginButton.setForeground(Color.WHITE);
            loginButton.setFocusPainted(false);
            loginButton.setFont(loginButton.getFont().deriveFont(Font.BOLD, 14f));
            btnPanel.add(loginButton);
            content.add(btnPanel, BorderLayout.SOUTH);

            // Action listeners
            loginButton.addActionListener(e -> performLogin());
            passwordField.addActionListener(e -> performLogin());
        }

        private void performLogin() {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if ("admin".equals(username) && "admin".equals(password)) {
                JOptionPane.showMessageDialog(this,
                        "Login successful! Welcome to Stockify.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                
                // Close login window and open main application
                dispose();
                openMainApplication();
                
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid username or password. Try again.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        
        private void openMainApplication() {
            // Open the Enhanced Product Manager
            SwingUtilities.invokeLater(() -> {
                EnhancedManageProductsGUI mainApp = new EnhancedManageProductsGUI();
                mainApp.setVisible(true);
            });
        }
    }
}
