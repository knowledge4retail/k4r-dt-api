CREATE TABLE IF NOT EXISTS product_gtin
(
  id VARCHAR(255) PRIMARY KEY, -- primary key column
  logistical_unit_id INTEGER,
  product_id VARCHAR(255),
  gtin_typ VARCHAR(255),
  main_gtin BOOLEAN,

  CONSTRAINT fk_product_gtin_product
    FOREIGN KEY (product_id)
      REFERENCES product(id),
  CONSTRAINT fk_product_gtin_logistical_unit
    FOREIGN KEY (logistical_unit_id)
      REFERENCES logistical_unit(id),
  CONSTRAINT uni_logistical_unit_id_product_id
    UNIQUE(logistical_unit_id, product_id)
);

CREATE INDEX idx_product_gtin_id ON product_gtin(id);
