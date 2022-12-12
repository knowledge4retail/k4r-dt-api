CREATE TABLE IF NOT EXISTS store_object
(
  id SERIAL PRIMARY KEY, -- primary key column
  type VARCHAR(255) NOT NULL,
  description TEXT,
  location_timestamp TIMESTAMP WITH TIME ZONE,
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

  CONSTRAINT fk_store_object_store
    FOREIGN KEY (store_id)
      REFERENCES store(id)
);

CREATE INDEX idx_store_object_id ON store_object(id);
CREATE INDEX idx_store_object_store_id_type ON store_object(store_id, type);