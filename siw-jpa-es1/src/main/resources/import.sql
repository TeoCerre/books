CREATE TABLE IF NOT EXISTS product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price FLOAT NOT NULL,
    description TEXT,
    code VARCHAR(100) UNIQUE NOT NULL
);

INSERT INTO product (name, price, description, code) 
VALUES 
('Laptop HP', 799.99, 'Laptop HP con processore Intel i7 e 16GB di RAM', 'HP12345');

INSERT INTO product (name, price, description, code) 
VALUES 
('Smartphone Samsung', 599.50, 'Samsung Galaxy con schermo AMOLED e 128GB di memoria', 'SAM56789');

INSERT INTO product (name, price, description, code) 
VALUES 
('Monitor LG', 249.99, 'Monitor LG 27 pollici Full HD con tecnologia IPS', 'LG98765');
