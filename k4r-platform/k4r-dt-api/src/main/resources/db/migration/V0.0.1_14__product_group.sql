CREATE TABLE IF NOT EXISTS product_group
(
  id SERIAL PRIMARY KEY, -- primary key column
  name VARCHAR(255) NOT NULL,
  store_id INTEGER,

  CONSTRAINT fk_product_group_store
     FOREIGN KEY (store_id)
        REFERENCES store(id),
  CONSTRAINT uni_name_store_id
    UNIQUE (name, store_id)
);

CREATE INDEX idx_product_group_id ON product_group(id);
CREATE INDEX idx_product_group_store_id ON product_group(store_id);

CREATE TABLE IF NOT EXISTS product_in_group
(
  product_id VARCHAR(255),
  product_group_id INTEGER,

  CONSTRAINT pk_product_in_group
    PRIMARY KEY (product_id, product_group_id), -- primary key columns
  CONSTRAINT fk_product_in_group_product
    FOREIGN KEY (product_id)
      REFERENCES product(id),
  CONSTRAINT fk_product_in_group_product_group
    FOREIGN KEY (product_group_id)
      REFERENCES product_group(id)
);

CREATE INDEX idx_product_in_group_product_id_product_group_id ON product_in_group(product_id, product_group_id);