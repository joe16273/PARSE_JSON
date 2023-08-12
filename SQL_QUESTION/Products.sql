CREATE DATABASE product_db;
USE product_db;

-- Table for storing product information
CREATE TABLE Product (
  product_id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  category VARCHAR(50) NOT NULL,
  added_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  added_by VARCHAR(50) NOT NULL
);

-- Table for storing product price information
CREATE TABLE Product_price (
  product_price_id INT PRIMARY KEY AUTO_INCREMENT,
  product_id INT NOT NULL,
  price DECIMAL(10, 2) NOT NULL,
  discount_percentage DECIMAL(5, 4) DEFAULT 0.0000,
  last_update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  updated_by VARCHAR(50) NOT NULL,
  FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

-- Table for storing product price change logs
CREATE TABLE Price_change_log (
  log_id INT PRIMARY KEY AUTO_INCREMENT,
  product_price_id INT NOT NULL,
  old_price DECIMAL(10, 2) NOT NULL,
  new_price DECIMAL(10, 2) NOT NULL,
  change_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  changed_by VARCHAR(50) NOT NULL,
  FOREIGN KEY (product_price_id) REFERENCES Product_price(product_price_id)
);

-- Query to join product and product price tables
SELECT p.name, p.category, pp.price, pp.last_update_time, pp.updated_by
FROM Product p
JOIN Product_price pp ON p.product_id = pp.product_id;
