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
                          payment_method VARCHAR,
                          card_number VARCHAR,
                          receipt_number VARCHAR,
                          payment_date TIMESTAMP
);
CREATE TABLE IF NOT EXISTS manager (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS staff (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(100) NOT NULL
);

INSERT INTO manager (username, password)
SELECT 'admin@restaurant.null', 'adminRestaurantMagic12'
WHERE NOT EXISTS (
    SELECT 1 FROM manager WHERE username = 'admin@restaurant.null'
);
