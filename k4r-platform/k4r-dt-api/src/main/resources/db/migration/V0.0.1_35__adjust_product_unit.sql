-- adjust product_unit to using unit entity

ALTER TABLE product_unit DROP COLUMN dimension_unit;
ALTER TABLE product_unit ADD COLUMN dimension_unit INTEGER;
ALTER TABLE product_unit DROP COLUMN volume_unit;
ALTER TABLE product_unit ADD COLUMN volume_unit INTEGER;
ALTER TABLE product_unit DROP COLUMN weight_unit;
ALTER TABLE product_unit ADD COLUMN weight_unit INTEGER;

ALTER TABLE product_unit ADD
  CONSTRAINT fk_product_unit_dimension_unit
    FOREIGN KEY (dimension_unit)
      REFERENCES unit(id);
ALTER TABLE product_unit ADD
  CONSTRAINT fk_product_unit_volume_unit
    FOREIGN KEY (volume_unit)
      REFERENCES unit(id);
ALTER TABLE product_unit ADD
  CONSTRAINT fk_product_unit_weight_unit
    FOREIGN KEY (weight_unit)
      REFERENCES unit(id);