CREATE TABLE IF NOT EXISTS store_characteristic
(
  id SERIAL PRIMARY KEY, -- primary key column
  name VARCHAR(255) NOT NULL
);

CREATE INDEX idx_store_characteristic_id ON store_characteristic(id);
