CREATE TABLE IF NOT EXISTS planogram
(
  id SERIAL PRIMARY KEY, -- primary key column
  shelf_layer_id INTEGER NOT NULL,
  logistical_unit_id INTEGER NOT NULL,
  version_timestamp BIGINT NOT NULL,
  orientation_yaw DOUBLE PRECISION,
  position_x INTEGER,
  number_of_facings INTEGER,

  CONSTRAINT fk_planogram_shelf_layer
    FOREIGN KEY (shelf_layer_id)
      REFERENCES shelf_layer(id),
  CONSTRAINT fk_planogram_logistical_unit
      FOREIGN KEY (logistical_unit_id)
        REFERENCES logistical_unit(id)
);

CREATE INDEX idx_planogram_id ON planogram(id);
CREATE INDEX idx_planogram_shelf_layer_id ON planogram(shelf_layer_id);