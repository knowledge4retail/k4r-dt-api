-- adjust despatch entities

ALTER TABLE despatch_logistical_unit RENAME TO despatch_logistic_unit;
ALTER TABLE despatch_logistic_unit RENAME COLUMN logistical_unit_id TO logistic_unit_id;
ALTER TABLE despatch_line_item RENAME COLUMN despatch_logistical_unit_id TO despatch_logistic_unit_id;
ALTER TABLE product_gtin RENAME COLUMN main_gtin TO main_gtin_flag;


