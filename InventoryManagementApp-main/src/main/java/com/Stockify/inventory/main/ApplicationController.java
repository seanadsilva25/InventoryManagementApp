package com.Stockify.inventory.main;

import com.Stockify.inventory.backend.UserAuthentication;
import com.Stockify.inventory.backend.UserAuthentication.User;
import com.Stockify.inventory.gui.EnhancedLoginPage;
import com.Stockify.inventory.gui.MainDashboard;

import javax.swing.*;

/**
 * Application Controller manages the flow between login and main application
 * This class handles the authentication flow and window management
 */
public class ApplicationController {
    private EnhancedLoginPage loginPage;
    private MainDashboard dashboard;
    
    public ApplicationController() {
        // Initialize the application controller
        System.out.println("Application Controller initialized");
    }
    
    /**
     * Start the application by showing the login page
     */
    public void startApplication() {
        SwingUtilities.invokeLater(() -> {
            showLogin();
        });
    }
    
    /**
     * Show the login page
     */
    public void showLogin() {
        if (loginPage != null) {
            loginPage.dispose();
        }
        
        loginPage = new EnhancedLoginPage(new EnhancedLoginPage.LoginCallback() {
            @Override
            public void onLoginSuccess(User user) {
                System.out.println("Login successful for user: " + user.getUsername());
                showMainDashboard(user);
            }
            
            @Override
            public void onLoginCancel() {
                System.out.println("Login cancelled by user");
                System.exit(0);
            }
        });
        
        loginPage.setVisible(true);
    }
    
    /**
     * Show the main dashboard after successful login
     */
    public void showMainDashboard(User user) {
        if (dashboard != null) {
            dashboard.dispose();
        }
        
        dashboard = new MainDashboard(user);
        dashboard.setVisible(true);
        
        // Show welcome message
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(dashboard,
                "Welcome to Stockify, " + user.getFullName() + "!\n\n" +
                "You are logged in as: " + user.getRole().toUpperCase() + "\n\n" +
                "Use the Quick Actions panel to navigate to different features.",
                "Welcome to Stockify",
                JOptionPane.INFORMATION_MESSAGE);
        });
    }
    
    /**
     * Main entry point for the application
     */
    public static void main(String[] args) {
        // Set system look and feel for better appearance
        try {
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }
        
        // Set application properties
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        
        // Create and start the application controller
        ApplicationController app = new ApplicationController();
        app.startApplication();
    }
    
    /**
     * Get current logged-in user (if any)
     */
    public User getCurrentUser() {
        return dashboard != null ? dashboard.currentUser : null;
    }
    
    /**
     * Check if user is logged in
     */
    public boolean isUserLoggedIn() {
        return getCurrentUser() != null;
    }
    
    /**
     * Logout current user and return to login
     */
    public void logout() {
        if (dashboard != null) {
            dashboard.dispose();
            dashboard = null;
        }
        showLogin();
    }
}
