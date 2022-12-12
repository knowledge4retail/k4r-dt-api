CREATE TABLE IF NOT EXISTS device_status
(
  device_id VARCHAR(255) NOT NULL,
  timestamp BIGINT NOT NULL,
  reference_id VARCHAR(255),
  position_x DOUBLE PRECISION NOT NULL,
  position_y DOUBLE PRECISION NOT NULL,
  position_z DOUBLE PRECISION NOT NULL,
  orientation_x DOUBLE PRECISION NOT NULL,
  orientation_y DOUBLE PRECISION NOT NULL,
  orientation_z DOUBLE PRECISION NOT NULL,
  orientation_w DOUBLE PRECISION NOT NULL,

  CONSTRAINT pk_device_status
    PRIMARY KEY (device_id, timestamp) -- primary key columns
);

CREATE INDEX idx_device_status_device_id ON device_status(device_id);
CREATE INDEX idx_device_status_timestamp ON device_status(timestamp);

