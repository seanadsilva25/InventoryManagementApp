package com.Stockify.inventory.gui;

import javax.swing.*;

public class DemoForm {
    public DemoForm()
    {
        JFrame frame = new JFrame();
    JLabel label = new JLabel("Hello World!! Welcome to inventory management app");

    frame.add(label);
    frame.setSize(400, 300);
    frame.setVisible(true);
    }
}
