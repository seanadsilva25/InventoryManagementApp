package com.Stockify.inventory.gui;
import javax.swing.*;

import com.Stockify.inventory.db.LowStockAlert;
import com.Stockify.inventory.model.Products;

import java.awt.*;
import java.util.ArrayList;

public class LowStockAlertGUI extends JFrame {
    private JButton checkBtn;
private JTextArea resultArea;
private LowStockAlert backend;

public LowStockAlertGUI() {
    backend = new LowStockAlert(); // connect to DB

    setTitle("Low Stock Alert");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(400, 300);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout(10, 10));

    // Top panel with button only
    JPanel topPanel = new JPanel();
    checkBtn = new JButton("Check Low Stock");
    topPanel.add(checkBtn);
    add(topPanel, BorderLayout.NORTH);

    // Result area
    resultArea = new JTextArea();
    resultArea.setEditable(false);
    add(new JScrollPane(resultArea), BorderLayout.CENTER);

    // Button action
    checkBtn.addActionListener(e -> showLowStock());

    setVisible(true);
}

private void showLowStock() {
    resultArea.setText("");
    ArrayList<Products> list = backend.getLowStockProducts();
    if (list.isEmpty()) {
        resultArea.setText("✅ All products are above threshold 5!");
    } else {
        for (Products p : list) {
            resultArea.append(p.getItem_name() + " → Quantity: " + p.getQuantity() + "\n");
        }
    }
}

}
