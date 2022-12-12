CREATE TABLE IF NOT EXISTS shelf_layer
(
  id SERIAL PRIMARY KEY, -- primary key column
  shelf_id INTEGER,
  level INTEGER,
  type VARCHAR(255),
  position_z DOUBLE PRECISION,
  width INTEGER NOT NULL
    DEFAULT 0,
  height INTEGER NOT NULL
    DEFAULT 0,
  depth INTEGER NOT NULL
    DEFAULT 0,
  external_reference_id VARCHAR(255),

  CONSTRAINT fk_shelf_layers_shelf
    FOREIGN KEY (shelf_id)
      REFERENCES shelf(id)
);

CREATE INDEX idx_shelf_layer_id ON shelf_layer(id);