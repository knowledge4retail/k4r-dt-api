CREATE TABLE IF NOT EXISTS despatch_logistical_unit
(
  id SERIAL PRIMARY KEY, -- primary key column
  parent_id INTEGER,
  despatch_advice_id INTEGER,
  product_id VARCHAR(255),
  package_type_code VARCHAR(255),
  logistical_unit_id INTEGER,
  estimated_delivery TIMESTAMP WITH TIME ZONE,

  CONSTRAINT fk_despatch_logistical_unit_despatch_logistical_unit
    FOREIGN KEY (parent_id)
      REFERENCES despatch_logistical_unit(id),
  CONSTRAINT fk_despatch_logistical_unit_despatch_advice
    FOREIGN KEY (despatch_advice_id)
      REFERENCES despatch_advice(id),
  CONSTRAINT fk_despatch_logistical_unit_product
      FOREIGN KEY (product_id)
        REFERENCES product(id)
);

CREATE INDEX idx_despatch_logistical_unit_id ON despatch_logistical_unit(id);