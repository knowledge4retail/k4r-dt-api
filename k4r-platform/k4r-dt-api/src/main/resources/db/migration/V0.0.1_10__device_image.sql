CREATE TABLE IF NOT EXISTS device_image
(
  id SERIAL PRIMARY KEY, -- primary key column
  timestamp BIGINT,
  device_id VARCHAR(255),
  reference_id VARCHAR(255),
  device_position_x DOUBLE PRECISION NOT NULL,
  device_position_y DOUBLE PRECISION NOT NULL,
  device_position_z DOUBLE PRECISION NOT NULL,
  device_orientation_x DOUBLE PRECISION NOT NULL,
  device_orientation_y DOUBLE PRECISION NOT NULL,
  device_orientation_z DOUBLE PRECISION NOT NULL,
  device_orientation_w DOUBLE PRECISION NOT NULL,
  camera_position_x DOUBLE PRECISION NOT NULL,
  camera_position_y DOUBLE PRECISION NOT NULL,
  camera_position_z DOUBLE PRECISION NOT NULL,
  camera_orientation_x DOUBLE PRECISION NOT NULL,
  camera_orientation_y DOUBLE PRECISION NOT NULL,
  camera_orientation_z DOUBLE PRECISION NOT NULL,
  camera_orientation_w DOUBLE PRECISION NOT NULL,
  label_id VARCHAR(255) NOT NULL,
  label_name VARCHAR(255) NOT NULL,
  blob_url VARCHAR(511)
);

CREATE INDEX idx_device_image_id ON device_image(id);
CREATE INDEX idx_device_image_device_id ON device_image(device_id);
CREATE INDEX idx_device_image_timestamp ON device_image(timestamp);
CREATE INDEX idx_device_image_label_id ON device_image(label_id);
CREATE INDEX idx_device_image_label_name ON device_image(label_name);

