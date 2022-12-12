CREATE TABLE IF NOT EXISTS trolley
(
  id SERIAL PRIMARY KEY, -- primary key column
  store_id INTEGER,
  type VARCHAR(255),
  position_x DOUBLE PRECISION,
  position_y DOUBLE PRECISION,
  position_z DOUBLE PRECISION,
  orientation_x DOUBLE PRECISION,
  orientation_y DOUBLE PRECISION,
  orientation_z DOUBLE PRECISION,
  orientation_w DOUBLE PRECISION,

  CONSTRAINT fk_trolley_store
    FOREIGN KEY (store_id)
      REFERENCES store(id)
);

CREATE INDEX idx_trolley_id ON trolley(id);
