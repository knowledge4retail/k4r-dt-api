CREATE TABLE IF NOT EXISTS unit
(
  id SERIAL PRIMARY KEY, -- primary key column
  name VARCHAR(255),
  type VARCHAR(255),
  Symbol VARCHAR(255)
);

CREATE INDEX idx_unit_id ON unit(id);

-- product already have a product_unit
-- ALTER TABLE despatch_line_item ADD COLUMN despatch_unit VARCHAR(255);