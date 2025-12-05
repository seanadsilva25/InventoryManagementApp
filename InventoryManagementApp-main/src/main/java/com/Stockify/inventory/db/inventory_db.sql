-- CREATE DATABASE Inventory;

USE Inventory;
SHOW TABLES;

-- CREATE TABLE supplier(
-- supplier_id INT AUTO_INCREMENT PRIMARY KEY,
-- supplier_name VARCHAR(50),
-- category VARCHAR(50),
-- company_name VARCHAR(50),
-- contact_number VARCHAR(15),
-- email VARCHAR(100),
-- address VARCHAR(255)
-- );


-- CREATE TABLE items(
-- item_id INT AUTO_INCREMENT PRIMARY KEY,
-- item_name VARCHAR(100) NOT NULL,
-- price DECIMAL(10,2),
-- category VARCHAR(50),
-- quantity INT DEFAULT 0,
-- brand VARCHAR(50),
-- supplier_id INT,
-- FOREIGN KEY (supplier_id) REFERENCES supplier(supplier_id) 
-- ON DELETE RESTRICT
-- ON UPDATE CASCADE
-- );

-- NSERTING VALUES IN SUPPLIER TABLE
-- INSERT INTO supplier (supplier_name, category, company_name, contact_number, email, address)
-- VALUES 
-- ('Ravi Traders', 'Electronics', 'Ravi Distributors', '9876543210', 'ravi@example.com', 'Mumbai'),
-- ('Mehta Papers', 'Stationery', 'Mehta Supplies', '9822113456', 'mehta@example.com', 'Pune'),
-- ('Fashion Forever', 'Clothes', 'Fashion Forever Pvt Ltd', '9811223344', 'fashion2forever@example.com', 'Nagpur'),
-- ('Ajay Sales', 'Electronics', 'Ajay Sales Pvt Ltd', '9898722334', 'fresh@example.com', 'Bangalore'),
-- ('Tech Solutions', 'Electronics', 'TechSol Pvt Ltd', '9087730957', 'techSol_123@gmail.com', 'Pune'),
-- ('Office Essentials', 'Stationery', 'OffiSo', '9866613456', 'office_essentials@example.com', 'Pune'),
-- ('FingerTips', 'Stationery', 'FingerTips Pvt Ltd', '7752113411', 'fingertip_pen_567@example.com', 'Kolkata'),
-- ('TrendSetters', 'Clothes', 'TrendSetters Pvt Ltd', '7741290844', 'TrendSetters_world@example.com', 'Ahemdabad'),
-- ('Casanova Bros', 'Clothes', 'Casanova Bros Pvt Ltd', '1234567895', 'Casanova_Bros@example.com', 'Ahemdabad');

-- INSERT INTO items (item_name, price, category, quantity, brand, supplier_id)
-- VALUES
-- ('Laptop', 65000.00, 'Electronics', 10, 'Dell', 1),
-- ('Mouse', 750.00, 'Electronics', 50, 'Logitech', 1),
-- ('Keyboard', 1200.00, 'Electronics', 30, 'HP', 4),
-- ('Smartphone', 25000.00, 'Electronics', 20, 'Samsung', 5),
-- ('Notebook', 25.00, 'Stationery', 200, 'Classmate', 2),
-- ('Pen', 15.00, 'Stationery', 500, 'Reynolds', 2),
-- ('Marker', 35.00, 'Stationery', 100, 'FingerTips', 7),
-- ('T-Shirt', 499.00, 'Clothes', 150, 'TrendSetters', 8),
-- ('Jeans', 1200.00, 'Clothes', 80, 'Casanova Bros', 9),
-- ('Shirt', 799.00, 'Clothes', 100, 'Fashion Forever', 3),
-- ('Stapler', 150.00, 'Stationery', 75, 'Office Essentials', 6),
-- ('Headphones', 1800.00, 'Electronics', 40, 'Sony', 4);


CREATE TABLE IF NOT EXISTS categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    category_code VARCHAR(20) UNIQUE,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

INSERT INTO categories (category_code, name, description) VALUES
('ELEC', 'Electronics', 'Electronic devices and components'),
('STAT', 'Stationery', 'Office and school supplies'),
('CLOTH', 'Clothing', 'Apparel and fashion items'),
('BOOK', 'Books', 'Educational and recreational books'),
('TOOL', 'Tools', 'Hardware and construction tools');

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

CREATE OR REPLACE VIEW inventory_summary AS
SELECT 
    COUNT(*) as total_items,
    SUM(quantity) as total_quantity,
    SUM(price * quantity) as total_value,
    COUNT(CASE WHEN quantity <= 5 THEN 1 END) as low_stock_count
FROM items;

SELECT * FROM items;
-- DESCRIBE items;
