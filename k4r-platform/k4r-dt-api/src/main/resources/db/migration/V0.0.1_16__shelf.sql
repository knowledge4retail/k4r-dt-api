CREATE TABLE IF NOT EXISTS shelf
(
  id SERIAL PRIMARY KEY, -- primary key column
  position_x DOUBLE PRECISION,
  position_y DOUBLE PRECISION,
  position_z DOUBLE PRECISION,
  orientation_x DOUBLE PRECISION,
  orientation_y DOUBLE PRECISION,
  orientation_z DOUBLE PRECISION,
  orientation_w DOUBLE PRECISION,
  width INTEGER NOT NULL,
  height INTEGER NOT NULL,
  depth INTEGER NOT NULL,
  store_id INTEGER,
  product_group_id INTEGER,
  cad_plan_id VARCHAR(255),
  external_reference_id VARCHAR(255),

  CONSTRAINT fk_shelf_store
    FOREIGN KEY (store_id)
      REFERENCES store(id),
  CONSTRAINT fk_shelf_product_group
    FOREIGN KEY (product_group_id)
      REFERENCES product_group(id)
);

CREATE INDEX idx_shelf_id ON shelf(id);
