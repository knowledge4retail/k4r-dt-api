-- remove logistical_unit
ALTER TABLE item_group DROP CONSTRAINT fk_item_group_logistical_unit;
ALTER TABLE planogram DROP CONSTRAINT fk_planogram_logistical_unit;
ALTER TABLE delivery DROP CONSTRAINT fk_delivery_logistical_unit;
DROP TABLE logistical_unit;

-- create product_unit
CREATE TABLE IF NOT EXISTS product_unit
(
  id SERIAL PRIMARY KEY, -- primary key column
  product_id VARCHAR(255),
  unit_code VARCHAR(255),
  numerator_base_unit INTEGER,
  denominator_base_unit INTEGER,
  length DOUBLE PRECISION NOT NULL,
  width DOUBLE PRECISION NOT NULL,
  height DOUBLE PRECISION NOT NULL,
  dimension_unit VARCHAR(255),
  volume DOUBLE PRECISION,
  volume_unit VARCHAR(255),
  net_weight DOUBLE PRECISION,
  gross_weight DOUBLE PRECISION,
  weight_unit VARCHAR(255),
  max_stack_size INTEGER,

  CONSTRAINT fk_product_unit_product
    FOREIGN KEY (product_id)
      REFERENCES product(id)
);

-- create all foreign keys to product unit
ALTER TABLE item_group RENAME COLUMN logistical_unit_id TO product_unit_id;
ALTER TABLE planogram RENAME COLUMN logistical_unit_id TO product_unit_id;

ALTER TABLE item_group ADD
  CONSTRAINT fk_item_group_product_unit
    FOREIGN KEY (product_unit_id)
      REFERENCES product_unit(id);
ALTER TABLE planogram ADD
  CONSTRAINT fk_planogram_group_product_unit
    FOREIGN KEY (product_unit_id)
      REFERENCES product_unit(id);
ALTER TABLE product_gtin ADD
  CONSTRAINT fk_product_gtin_group_product_unit
    FOREIGN KEY (product_unit_id)
      REFERENCES product_unit(id);
