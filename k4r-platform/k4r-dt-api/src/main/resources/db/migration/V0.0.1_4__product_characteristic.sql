CREATE TABLE IF NOT EXISTS product_characteristic
(
  id SERIAL PRIMARY KEY, -- primary key column
  name VARCHAR(255) NOT NULL
);

CREATE INDEX idx_product_characteristic_id ON product_characteristic(id);
