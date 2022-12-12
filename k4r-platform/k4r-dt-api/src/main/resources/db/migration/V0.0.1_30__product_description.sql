ALTER TABLE product DROP COLUMN name;
ALTER TABLE product DROP COLUMN description;

CREATE TABLE IF NOT EXISTS product_description
(
  id SERIAL PRIMARY KEY, -- primary key column
  product_id VARCHAR(255),
  description VARCHAR(255),
  iso_language_code VARCHAR(255),

  CONSTRAINT fk_product_description_product
      FOREIGN KEY (product_id)
        REFERENCES product(id)
);

CREATE INDEX idx_product_description_id ON product_description(id);
CREATE INDEX idx_product_description_product_id ON product_description(product_id);