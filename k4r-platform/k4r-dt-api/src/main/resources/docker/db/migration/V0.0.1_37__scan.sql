CREATE TABLE IF NOT EXISTS scan
(
  timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
  entity_type VARCHAR(255),
  dt_id INT,
  origin VARCHAR(255),
  position_x DOUBLE PRECISION,
  position_y DOUBLE PRECISION,
  position_z DOUBLE PRECISION,
  orientation_x DOUBLE PRECISION,
  orientation_y DOUBLE PRECISION,
  orientation_z DOUBLE PRECISION,
  orientation_w DOUBLE PRECISION,
  width DOUBLE PRECISION,
  height DOUBLE PRECISION,
  depth DOUBLE PRECISION,
  length_unit_id INTEGER,

  CONSTRAINT pk_scan
    PRIMARY KEY (timestamp, entity_type, dt_id)
);

CREATE INDEX idx_scan_timestamp ON scan(timestamp);
CREATE INDEX idx_scan_entity_type ON scan(entity_type);
CREATE INDEX idx_scan_dt_id ON scan(dt_id);


SET search_path TO dt_v1, public;
SELECT create_hypertable('scan', 'timestamp', if_not_exists => TRUE, create_default_indexes => FALSE);
