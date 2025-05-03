CREATE TABLE customer (
                          customer_id   INT AUTO_INCREMENT PRIMARY KEY,
                          first_name    VARCHAR(50) NOT NULL,
                          last_name     VARCHAR(50) NOT NULL,
                          email         VARCHAR(100) UNIQUE NOT NULL,
                          phone         VARCHAR(20),
                          created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
