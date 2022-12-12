-- add attributes to facing, item and barcode

ALTER TABLE facing ADD COLUMN misplaced_stock INTEGER;

ALTER TABLE item ADD COLUMN product_unit_id INTEGER;
ALTER TABLE item ADD
  CONSTRAINT fk_item_product_unit
    FOREIGN KEY (product_unit_id)
      REFERENCES product_unit(id);

ALTER TABLE barcode ADD COLUMN orientation_x DOUBLE PRECISION;
ALTER TABLE barcode ADD COLUMN orientation_y DOUBLE PRECISION;
ALTER TABLE barcode ADD COLUMN orientation_z DOUBLE PRECISION;
ALTER TABLE barcode ADD COLUMN orientation_w DOUBLE PRECISION;
ALTER TABLE barcode ADD COLUMN length_unit_id INTEGER;
ALTER TABLE barcode ADD
  CONSTRAINT fk_barcode_unit
    FOREIGN KEY (length_unit_id)
      REFERENCES unit(id);
