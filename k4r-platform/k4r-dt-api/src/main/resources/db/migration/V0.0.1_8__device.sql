CREATE TABLE IF NOT EXISTS device
(
  id VARCHAR(255) PRIMARY KEY,
  store_id INTEGER,
  device_type VARCHAR(255),
  description VARCHAR(255),

  CONSTRAINT fk_device_store
    FOREIGN KEY (store_id)
      REFERENCES store(id)
);