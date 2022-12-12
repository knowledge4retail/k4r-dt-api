CREATE TABLE IF NOT EXISTS logistical_unit
(
  id SERIAL PRIMARY KEY, -- primary key column
  predecessor_id INTEGER,
  product_id VARCHAR(255),
  quantity_unit VARCHAR(255),
  quantity_unit_ISO VARCHAR(255),
  quantity_of_predecessors DOUBLE PRECISION,
  basic_unit BOOLEAN,
  order_unit BOOLEAN,
  delivery_expanse_unit BOOLEAN,
  retail_unit BOOLEAN,
  length DOUBLE PRECISION NOT NULL,
  width DOUBLE PRECISION NOT NULL,
  height DOUBLE PRECISION NOT NULL,
  dimension_unit VARCHAR(255),
  net_weight DOUBLE PRECISION,
  gross_weight DOUBLE PRECISION,
  weight_unit VARCHAR(255),
  max_stack_size INTEGER,

  CONSTRAINT fk_logistical_unit_logistical_unit
    FOREIGN KEY (predecessor_id)
      REFERENCES logistical_unit(id),
  CONSTRAINT fk_logistical_unit_product
    FOREIGN KEY (product_id)
      REFERENCES product(id)
);