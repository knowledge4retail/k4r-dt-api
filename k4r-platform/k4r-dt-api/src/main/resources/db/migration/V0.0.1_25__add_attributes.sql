-- add many attributes to different entities

ALTER TABLE material_group ADD COLUMN description VARCHAR(255);
ALTER TABLE material_group ADD COLUMN hierarchy_level INTEGER;

ALTER TABLE store_property DROP COLUMN value_;
ALTER TABLE store_property ADD COLUMN value_low VARCHAR(255) NOT NULL;
ALTER TABLE store_property ADD COLUMN value_high VARCHAR(255);

ALTER TABLE product_property DROP COLUMN value_;
ALTER TABLE product_property ADD COLUMN value_low VARCHAR(255) NOT NULL;
ALTER TABLE product_property ADD COLUMN value_high VARCHAR(255);

ALTER TABLE product_characteristic ADD COLUMN code VARCHAR(255);

ALTER TABLE product_gtin DROP COLUMN id;
ALTER TABLE product_gtin DROP COLUMN gtin_typ;
ALTER TABLE product_gtin ADD COLUMN id SERIAL PRIMARY KEY;
ALTER TABLE product_gtin ADD COLUMN gtin VARCHAR(255);
ALTER TABLE product_gtin ADD COLUMN gtin_type VARCHAR(255);
ALTER TABLE product_gtin RENAME COLUMN logistical_unit_id TO product_unit_id;
ALTER TABLE product_gtin DROP CONSTRAINT fk_product_gtin_product;
ALTER TABLE product_gtin DROP CONSTRAINT fk_product_gtin_logistical_unit;
ALTER TABLE product_gtin DROP CONSTRAINT uni_logistical_unit_id_product_id;
