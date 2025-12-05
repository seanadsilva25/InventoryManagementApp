-- Enhanced Inventory Management Database Schema
-- This schema extends the existing schema to support categories, suppliers, and enhanced reporting

USE inventory;

-- Create categories table if it doesn't exist
CREATE TABLE IF NOT EXISTS categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_code VARCHAR(20) UNIQUE,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

-- Create supplier table if it doesn't exist (already exists in your schema)
CREATE TABLE IF NOT EXISTS supplier (
    supplier_id INT AUTO_INCREMENT PRIMARY KEY,
    supplier_name VARCHAR(50),
    company_name VARCHAR(50),
    contact_number VARCHAR(15),
    email VARCHAR(100),
    address VARCHAR(255)
);

-- Create items table if it doesn't exist (already exists in your schema)
CREATE TABLE IF NOT EXISTS items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(100) NOT NULL,
    price DECIMAL(10,2),
    category VARCHAR(50),
    quantity INT DEFAULT 0,
    brand VARCHAR(50),
    supplier_id INT,
    FOREIGN KEY (supplier_id) REFERENCES supplier(supplier_id) 
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);

-- Insert sample categories
INSERT IGNORE INTO categories (category_code, name, description) VALUES
('ELEC', 'Electronics', 'Electronic devices and components'),
('STAT', 'Stationery', 'Office and school supplies'),
('CLOTH', 'Clothing', 'Apparel and fashion items'),
('BOOK', 'Books', 'Educational and recreational books'),
('TOOL', 'Tools', 'Hardware and construction tools');

-- Insert sample suppliers (if not already present)
INSERT IGNORE INTO supplier (supplier_name, company_name, contact_number, email, address) VALUES
('Ravi Traders', 'Ravi Distributors', '9876543210', 'ravi@example.com', 'Mumbai'),
('Mehta Papers', 'Mehta Supplies', '9822113456', 'mehta@example.com', 'Pune'),
('Fashion Forever', 'Fashion Forever Pvt Ltd', '9811223344', 'fashion2forever@example.com', 'Nagpur'),
('Ajay Sales', 'Ajay Sales Pvt Ltd', '9898722334', 'fresh@example.com', 'Bangalore'),
('Tech Solutions', 'TechSol Pvt Ltd', '9087730957', 'techSol_123@gmail.com', 'Pune'),
('Office Essentials', 'OffiSo', '9866613456', 'office_essentials@example.com', 'Pune'),
('FingerTips', 'FingerTips Pvt Ltd', '7752113411', 'fingertip_pen_567@example.com', 'Kolkata'),
('TrendSetters', 'TrendSetters Pvt Ltd', '7741290844', 'TrendSetters_world@example.com', 'Ahemdabad'),
('Casanova Bros', 'Casanova Bros Pvt Ltd', '1234567895', 'Casanova_Bros@example.com', 'Ahemdabad');

-- Insert sample items (if not already present)
INSERT IGNORE INTO items (item_name, price, category, quantity, brand, supplier_id) VALUES
('Laptop', 65000.00, 'Electronics', 10, 'Dell', 1),
('Mouse', 750.00, 'Electronics', 50, 'Logitech', 1),
('Keyboard', 1200.00, 'Electronics', 30, 'HP', 4),
('Smartphone', 25000.00, 'Electronics', 20, 'Samsung', 5),
('Notebook', 25.00, 'Stationery', 200, 'Classmate', 2),
('Pen', 15.00, 'Stationery', 500, 'Reynolds', 2),
('Marker', 35.00, 'Stationery', 100, 'FingerTips', 7),
('T-Shirt', 499.00, 'Clothing', 150, 'TrendSetters', 8),
('Jeans', 1200.00, 'Clothing', 80, 'Casanova Bros', 9),
('Shirt', 799.00, 'Clothing', 100, 'Fashion Forever', 3),
('Stapler', 150.00, 'Stationery', 75, 'Office Essentials', 6),
('Headphones', 1800.00, 'Electronics', 40, 'Sony', 4);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_items_category ON items(category);
CREATE INDEX IF NOT EXISTS idx_items_brand ON items(brand);
CREATE INDEX IF NOT EXISTS idx_items_supplier ON items(supplier_id);
CREATE INDEX IF NOT EXISTS idx_items_quantity ON items(quantity);
CREATE INDEX IF NOT EXISTS idx_categories_name ON categories(name);
CREATE INDEX IF NOT EXISTS idx_supplier_company ON supplier(company_name);

-- Create a view for low stock items
CREATE OR REPLACE VIEW low_stock_items AS
SELECT 
    i.item_id,
    i.item_name,
    i.price,
    i.category,
    i.quantity,
    i.brand,
    i.supplier_id,
    s.company_name as supplier_name
FROM items i
LEFT JOIN supplier s ON i.supplier_id = s.supplier_id
WHERE i.quantity <= 5;

-- Create a view for inventory summary
CREATE OR REPLACE VIEW inventory_summary AS
SELECT 
    COUNT(*) as total_items,
    SUM(quantity) as total_quantity,
    SUM(price * quantity) as total_value,
    COUNT(CASE WHEN quantity <= 5 THEN 1 END) as low_stock_count
FROM items;

-- Create a view for category statistics
CREATE OR REPLACE VIEW category_stats AS
SELECT 
    category,
    COUNT(*) as item_count,
    SUM(quantity) as total_quantity,
    SUM(price * quantity) as total_value,
    AVG(price) as avg_price
FROM items 
WHERE category IS NOT NULL
GROUP BY category
ORDER BY total_value DESC;
