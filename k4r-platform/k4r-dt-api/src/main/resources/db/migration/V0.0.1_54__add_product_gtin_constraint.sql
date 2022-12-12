ALTER TABLE product_gtin ADD UNIQUE (gtin);
ALTER TABLE product_gtin ADD UNIQUE (external_reference_id);
