package com.Stockify.inventory.model;

public class Category {
    private int category_id;
    private String category_code;
    private String name;
    private String description;

    public Category() {}

    public Category(int category_id, String category_code, String name, String description) {
        this.category_id = category_id;
        this.category_code = category_code;
        this.name = name;
        this.description = description;
    }

    // Getters and setters
    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_code() {
        return category_code;
    }

    public void setCategory_code(String category_code) {
        this.category_code = category_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }
}
