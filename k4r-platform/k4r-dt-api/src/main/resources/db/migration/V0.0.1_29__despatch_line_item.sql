CREATE TABLE IF NOT EXISTS despatch_line_item
(
  id SERIAL PRIMARY KEY, -- primary key column
  despatch_logistical_unit_id INTEGER,
  product_id VARCHAR(255),
  requested_product_id VARCHAR(255),
  line_item_number INTEGER,
  despatch_quantity INTEGER,

  CONSTRAINT fk_despatch_line_item_despatch_logistical_unit
    FOREIGN KEY (despatch_logistical_unit_id)
      REFERENCES despatch_logistical_unit(id),
  CONSTRAINT fk_despatch_line_item_product
    FOREIGN KEY (product_id)
      REFERENCES product(id),
  CONSTRAINT fk_despatch_line_item_product_2
    FOREIGN KEY (requested_product_id)
      REFERENCES product(id)
);

CREATE INDEX idx_despatch_line_item_id ON despatch_line_item(id);