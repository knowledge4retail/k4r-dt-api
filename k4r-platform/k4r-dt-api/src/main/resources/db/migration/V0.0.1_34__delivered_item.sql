CREATE TABLE IF NOT EXISTS delivered_item
(
  id SERIAL PRIMARY KEY, -- primary key column
  trolley_id INTEGER,
  product_unit_id INTEGER,
  product_gtin_id INTEGER,
  trolley_layer INTEGER,
  pallet_id VARCHAR(255),
  sorting_state VARCHAR(255),
  sorting_date TIMESTAMP WITH TIME ZONE,
  amount INTEGER,

  CONSTRAINT fk_delivered_item_trolley
    FOREIGN KEY (trolley_id)
      REFERENCES trolley(id),
  CONSTRAINT fk_delivered_item_product_unit
    FOREIGN KEY (product_unit_id)
      REFERENCES product_unit(id),
  CONSTRAINT fk_delivered_item_product_gtin
    FOREIGN KEY (product_gtin_id)
      REFERENCES product_gtin(id)
);

CREATE INDEX idx_delivered_item_id ON delivered_item(id);
