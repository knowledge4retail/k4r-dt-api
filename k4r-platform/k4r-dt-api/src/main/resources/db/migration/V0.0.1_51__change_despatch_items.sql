-- change despatch items

ALTER TABLE despatch_advice ALTER COLUMN sender_id TYPE VARCHAR(255);
ALTER TABLE despatch_advice ADD COLUMN document_number VARCHAR(255);

ALTER TABLE despatch_logistic_unit DROP CONSTRAINT fk_despatch_logistical_unit_product;
ALTER TABLE despatch_logistic_unit DROP COLUMN product_id;
ALTER TABLE despatch_logistic_unit ALTER COLUMN logistic_unit_id TYPE VARCHAR(255);

ALTER TABLE despatch_line_item ALTER COLUMN line_item_number TYPE VARCHAR(255);

