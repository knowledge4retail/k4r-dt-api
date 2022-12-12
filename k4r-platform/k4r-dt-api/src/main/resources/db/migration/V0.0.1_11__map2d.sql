CREATE TABLE IF NOT EXISTS map2d
(
  id SERIAL PRIMARY KEY, -- primary key column
  timestamp BIGINT NOT NULL,
  frame_id VARCHAR(255) NOT NULL,
  device_id VARCHAR(255),
  resolution DOUBLE PRECISION NOT NULL,
  width INTEGER NOT NULL,
  height INTEGER NOT NULL,
  position_x DOUBLE PRECISION NOT NULL,
  position_y DOUBLE PRECISION NOT NULL,
  position_z DOUBLE PRECISION NOT NULL,
  orientation_x DOUBLE PRECISION NOT NULL,
  orientation_y DOUBLE PRECISION NOT NULL,
  orientation_z DOUBLE PRECISION NOT NULL,
  orientation_w DOUBLE PRECISION NOT NULL,
  data TEXT NOT NULL,
  store_id INTEGER NOT NULL,

  CONSTRAINT fk_map2d_store
    FOREIGN KEY (store_id)
      REFERENCES store(id)
);

CREATE INDEX idx_map2d_id ON map2d(id);
CREATE INDEX idx_map2d_store_id ON map2d(store_id);