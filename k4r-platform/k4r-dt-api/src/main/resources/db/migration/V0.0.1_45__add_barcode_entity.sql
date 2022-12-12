-- add barcode as entity

CREATE TABLE IF NOT EXISTS barcode
(
  id SERIAL PRIMARY KEY, -- primary key column
  product_gtin_id INTEGER,
  shelf_layer_id INTEGER,
  position_x DOUBLE PRECISION,
  position_y DOUBLE PRECISION,
  position_z DOUBLE PRECISION,

  CONSTRAINT fk_barcode_product_gtin
    FOREIGN KEY (product_gtin_id)
      REFERENCES product_gtin(id),
  CONSTRAINT fk_barcode_shelf_layer
    FOREIGN KEY (shelf_layer_id)
      REFERENCES shelf_layer(id)
);

CREATE INDEX idx_barcode_id ON barcode(id);

ALTER TABLE product_gtin ADD COLUMN external_reference_id VARCHAR(255);

