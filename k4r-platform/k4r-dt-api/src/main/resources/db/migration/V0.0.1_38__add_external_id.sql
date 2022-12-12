-- adjust product_unit to using unit entity

ALTER TABLE product RENAME COLUMN product_unit TO product_base_unit;
ALTER TABLE facing ADD COLUMN external_reference_id VARCHAR(255);
ALTER TABLE item ADD COLUMN external_reference_id VARCHAR(255);
