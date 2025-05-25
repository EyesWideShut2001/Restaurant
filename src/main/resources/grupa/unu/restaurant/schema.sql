-- Drop existing tables if they exist
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS staff;
DROP TABLE IF EXISTS manager;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS payments;

-- Create tables
CREATE TABLE customer (
    customer_id   INT AUTO_INCREMENT PRIMARY KEY,
    first_name    VARCHAR(50) NOT NULL,
    last_name     VARCHAR(50) NOT NULL,
    email         VARCHAR(100) UNIQUE NOT NULL,
    phone         VARCHAR(20),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE payments (
    order_id BIGINT,
    amount DOUBLE,
    payment_method VARCHAR(50),
    card_number VARCHAR(50),
    receipt_number VARCHAR(50),
    payment_date TIMESTAMP
);

CREATE TABLE manager (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL
);

CREATE TABLE staff (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    role VARCHAR(100) NOT NULL
);

CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    total_price DOUBLE,
    status VARCHAR(50) DEFAULT 'PENDING',
    order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    approved_by VARCHAR(100),
    approval_time TIMESTAMP,
    estimated_time INTEGER DEFAULT 0,
    notes VARCHAR(500)
);

CREATE TABLE order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT,
    product_name VARCHAR(255),
    price DOUBLE,
    quantity INTEGER,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

CREATE TABLE menu (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nume VARCHAR(255) NOT NULL UNIQUE,
    ingrediente VARCHAR(500),
    vegetarian BOOLEAN,
    picant BOOLEAN,
    pret DOUBLE,
    categorie VARCHAR(100),
    alcoolica BOOLEAN,
    available BOOLEAN DEFAULT TRUE
);


-- Insert default manager account
INSERT INTO manager (username, password) 
VALUES ('admin@restaurant.null', 'XVuH0cKCqBP/GWJ5pX5dXQ==$R8L/iGeF/laZiGoBeUs3O909f5dT5309np/ypmqrTNk=');

-- Insert test staff accounts
INSERT INTO staff (username, password, role) 
VALUES 
('waiter1', 'XVuH0cKCqBP/GWJ5pX5dXQ==$R8L/iGeF/laZiGoBeUs3O909f5dT5309np/ypmqrTNk=', 'STAFF'),
('chef1', 'XVuH0cKCqBP/GWJ5pX5dXQ==$R8L/iGeF/laZiGoBeUs3O909f5dT5309np/ypmqrTNk=', 'STAFF'),
('bartender1', 'XVuH0cKCqBP/GWJ5pX5dXQ==$R8L/iGeF/laZiGoBeUs3O909f5dT5309np/ypmqrTNk=', 'STAFF');

-- Insert default menu items
INSERT INTO menu (nume, ingrediente, vegetarian, picant, pret, categorie, alcoolica)
VALUES
-- Aperitive
('Bruschete cu roșii', 'Pâine, roșii, usturoi, busuioc, ulei de măsline', true, false, 18.00, 'Aperitive', false),
('Bruschete cu pesto', 'Pâine, busuioc, usturoi, parmezan, pin, ulei de măsline', true, false, 20.00, 'Aperitive', false),
('Bruschete cu somon', 'Pâine, somon afumat, cremă de brânză, mărar', false, false, 25.00, 'Aperitive', false),

-- Băuturi nespirtoase
('Apă plată', 'Apă minerală naturală', true, false, 8.00, 'Băuturi nespirtoase', false),
('Apă minerală', 'Apă minerală carbogazoasă', true, false, 8.00, 'Băuturi nespirtoase', false),
('Limonadă', 'Lămâie, apă minerală, zahăr, mentă', true, false, 15.00, 'Băuturi nespirtoase', false),

-- Băuturi spirtoase
('Whisky', 'Whisky', false, false, 22.00, 'Băuturi spirtoase', true),
('Vodcă', 'Vodcă', false, false, 18.00, 'Băuturi spirtoase', true),
('Rom', 'Rom brun', false, false, 20.00, 'Băuturi spirtoase', true),

-- Supe
('Supă cremă de legume', 'Morcovi, țelină, cartofi, smântână, condimente', true, false, 16.00, 'Fel Principal', false),
('Ciorbă de burtă', 'Burtă de vită, smântână, usturoi, oțet', false, false, 20.00, 'Fel Principal', false),

-- Feluri de carne
('Piept de pui la grătar', 'Piept de pui, condimente, ulei de măsline', false, false, 28.00, 'Fel Principal', false),
('Ceafă de porc la grătar', 'Ceafă de porc, usturoi, condimente', false, false, 30.00, 'Fel Principal', false),

-- Produse vegetariene
('Tocăniță de legume', 'Vinete, dovlecei, roșii, ceapă, ardei', true, false, 22.00, 'Fel Principal', false),
('Paste cu sos de roșii și busuioc', 'Paste, roșii, usturoi, busuioc, ulei de măsline', true, false, 24.00, 'Fel Principal', false),

-- Garnituri
('Cartofi prăjiți', 'Cartofi, ulei, sare', true, false, 10.00, 'Fel Principal', false),
('Orez cu legume', 'Orez, morcovi, mazăre, porumb', true, false, 12.00, 'Fel Principal', false);