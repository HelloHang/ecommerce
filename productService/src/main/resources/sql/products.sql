CREATE TABLE products(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(60) NOT NULL,
    description VARCHAR(255),
    product_type VARCHAR(50)
  );

CREATE TABLE orders(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    total_price DOUBLE NOT NULL,
    UNIQUE(order_id)
  );

CREATE TABLE order_entries(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    entry_id BIGINT NOT NULL,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity BIGINT NOT NULL,
    UNIQUE(entry_id)
);
