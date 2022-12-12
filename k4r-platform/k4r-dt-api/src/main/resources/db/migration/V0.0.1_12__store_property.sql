CREATE TABLE IF NOT EXISTS store_property
(
  id SERIAL PRIMARY KEY, -- primary key column
  characteristic_id INTEGER NOT NULL,
  store_id INTEGER,
  value_ VARCHAR(255) NOT NULL,

  CONSTRAINT fk_store_property_store_characteristic
    FOREIGN KEY (characteristic_id)
      REFERENCES store_characteristic(id),
  CONSTRAINT fk_store_property_store
    FOREIGN KEY (store_id)
      REFERENCES store(id),
  CONSTRAINT uni_store_property_store_id_characteristic_id
    UNIQUE (store_id, characteristic_id)
);

CREATE INDEX idx_store_property_id ON store_property(id);
CREATE INDEX idx_store_property_store_id_characteristic_id ON store_property(store_id, characteristic_id);




