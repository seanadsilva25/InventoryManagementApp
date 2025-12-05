package com.Stockify.inventory.model;

public class Products{
    private int item_id;
    private String item_name;
    private double price;
    private String category;
    private int quantity;
    private String brand;
    private int supplier_id;

    public Products(int item_id, String item_name, double price, String category, int quantity, String brand, int supplier_id) {
        this.item_id = item_id;
        this.item_name = item_name;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
        this.brand = brand;
        this.supplier_id = supplier_id;
    }

    // Getters and setters to modify the values later on if needed
    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }
}
