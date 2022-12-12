-- replace planogram table

DROP TABLE planogram CASCADE;

CREATE TABLE IF NOT EXISTS planogram
(
  id SERIAL PRIMARY KEY, -- primary key column
  store_id INTEGER NOT NULL,
  reference_id VARCHAR(255) NOT NULL,
  timestamp TIMESTAMP WITH TIME ZONE,
  data_format VARCHAR(255) NOT NULL,
  blob_url VARCHAR(255),

  CONSTRAINT fk_planogram_store
    FOREIGN KEY (store_id)
      REFERENCES store(id)
);

CREATE INDEX idx_planogram_id ON planogram(id);