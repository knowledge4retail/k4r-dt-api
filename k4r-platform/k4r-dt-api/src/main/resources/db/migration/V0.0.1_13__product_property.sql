CREATE TABLE IF NOT EXISTS product_property
(
  id SERIAL PRIMARY KEY, -- primary key column
  product_id VARCHAR(128) NOT NULL,
  characteristic_id INTEGER NOT NULL,
  store_id INTEGER,
  value_ VARCHAR(255) NOT NULL,

  CONSTRAINT fk_product_property_product
    FOREIGN KEY (product_id)
      REFERENCES product(id),
  CONSTRAINT fk_product_property_product_characteristic
    FOREIGN KEY (characteristic_id)
      REFERENCES product_characteristic(id),
  CONSTRAINT fk_product_property_store
    FOREIGN KEY (store_id)
      REFERENCES store(id),
  CONSTRAINT uni_product_property_product_id_characteristic_id_store_id
    UNIQUE (product_id, characteristic_id, store_id)
);

CREATE INDEX idx_product_property_id ON product_property(id);
CREATE INDEX idx_product_property_product_id_characteristic_id_store_id ON product_property(product_id, characteristic_id, store_id);




