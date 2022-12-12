CREATE TABLE IF NOT EXISTS delivery
(
  id SERIAL PRIMARY KEY, -- primary key column
  source VARCHAR(255),
  logistical_unit_id INTEGER,
  order_unit VARCHAR(255),
  store_id INTEGER,
  planned_delivery BIGINT,
  handling_unit VARCHAR(255),
  amount INTEGER,

  CONSTRAINT fk_delivery_logistical_unit
    FOREIGN KEY (logistical_unit_id)
      REFERENCES logistical_unit(id),
  CONSTRAINT fk_delivery_store
      FOREIGN KEY (store_id)
        REFERENCES store(id)
);

CREATE INDEX idx_delivery_id ON delivery(id);