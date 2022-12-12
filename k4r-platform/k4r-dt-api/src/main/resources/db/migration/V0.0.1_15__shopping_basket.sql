CREATE TABLE IF NOT EXISTS shopping_basket_position
(
  id SERIAL PRIMARY KEY, -- primary key column
  product_id VARCHAR(128) NOT NULL,
  store_id INTEGER NOT NULL,
  customer_id INTEGER NOT NULL,
  selling_price NUMERIC,
  quantity INTEGER NOT NULL,
  currency VARCHAR(128),

  CONSTRAINT fk_shopping_basket_position_product
    FOREIGN KEY (product_id)
      REFERENCES product(id),
  CONSTRAINT fk_shopping_basket_position_store
    FOREIGN KEY (store_id)
      REFERENCES store(id),
  CONSTRAINT fk_shopping_basket_position_customer
    FOREIGN KEY (customer_id)
      REFERENCES customer(id)
);

CREATE INDEX idx_shopping_basket_store_id_customer_id ON shopping_basket_position(store_id, customer_id);
CREATE INDEX idx_shopping_basket_id ON shopping_basket_position(id);