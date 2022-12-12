CREATE TABLE IF NOT EXISTS product
(
  id VARCHAR(128) PRIMARY KEY, -- primary key column
  name VARCHAR(128) NOT NULL,
  description VARCHAR(255),
  material_group_id INTEGER,
  product_type VARCHAR(255),
  product_unit VARCHAR(255),

  CONSTRAINT fk_product_material_group
    FOREIGN KEY (material_group_id)
      REFERENCES material_group(id)
);

CREATE INDEX idx_product_id ON product(id);
CREATE INDEX idx_material_group_id ON product(material_group_id);

