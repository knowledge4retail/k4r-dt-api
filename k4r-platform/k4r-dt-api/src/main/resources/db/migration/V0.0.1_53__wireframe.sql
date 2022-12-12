CREATE TABLE IF NOT EXISTS wireframe
(
  id SERIAL PRIMARY KEY, -- primary key column
  gTIN VARCHAR(255) NOT NULL,
  timestamp TIMESTAMP WITH TIME ZONE,
  data_format VARCHAR(255) NOT NULL,
  blob_url VARCHAR(255)
);

CREATE INDEX idx_wireframe_id ON wireframe(id);
CREATE INDEX idx_wireframe_gtin ON wireframe(gTIN);
CREATE UNIQUE INDEX idx_wireframe_gtin_data_format ON wireframe(gTIN, data_format);