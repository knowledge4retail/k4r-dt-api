CREATE TABLE IF NOT EXISTS customer
(
  id SERIAL PRIMARY KEY, -- primary key column
  anonymised_name VARCHAR(255) NOT NULL UNIQUE
);

CREATE INDEX idx_customer_id ON customer(id);
