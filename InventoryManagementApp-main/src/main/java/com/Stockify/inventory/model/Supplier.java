package com.Stockify.inventory.model;

public class Supplier {
    private int supplier_id;
    private String supplier_name;
    private String company_name;
    private String contact_number;
    private String email;
    private String address;

    public Supplier() {}

    public Supplier(int supplier_id, String supplier_name, String company_name, String contact_number, String email, String address) {
        this.supplier_id = supplier_id;
        this.supplier_name = supplier_name;
        this.company_name = company_name;
        this.contact_number = contact_number;
        this.email = email;
        this.address = address;
    }

    // Getters and setters
    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return company_name + " (" + supplier_name + ")";
    }
}
