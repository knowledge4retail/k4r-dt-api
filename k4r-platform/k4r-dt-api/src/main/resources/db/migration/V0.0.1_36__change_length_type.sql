-- change length type from int to double and add columns to use unit entity

ALTER TABLE shelf ALTER COLUMN width TYPE DOUBLE PRECISION;
ALTER TABLE shelf ALTER COLUMN height TYPE DOUBLE PRECISION;
ALTER TABLE shelf ALTER COLUMN depth TYPE DOUBLE PRECISION;
ALTER TABLE shelf ADD COLUMN length_unit_id INTEGER;
ALTER TABLE shelf_layer ALTER COLUMN width TYPE DOUBLE PRECISION;
ALTER TABLE shelf_layer ALTER COLUMN height TYPE DOUBLE PRECISION;
ALTER TABLE shelf_layer ALTER COLUMN depth TYPE DOUBLE PRECISION;
ALTER TABLE shelf_layer ADD COLUMN length_unit_id INTEGER;
ALTER TABLE store_object ALTER COLUMN width TYPE DOUBLE PRECISION;
ALTER TABLE store_object ALTER COLUMN height TYPE DOUBLE PRECISION;
ALTER TABLE store_object ALTER COLUMN depth TYPE DOUBLE PRECISION;
ALTER TABLE store_object ADD COLUMN length_unit_id INTEGER;
ALTER TABLE map2d ALTER COLUMN width TYPE DOUBLE PRECISION;
ALTER TABLE map2d ALTER COLUMN height TYPE DOUBLE PRECISION;
ALTER TABLE map2d ADD COLUMN length_unit_id INTEGER;


ALTER TABLE shelf ADD
  CONSTRAINT fk_shelf_length_unit_id
    FOREIGN KEY (length_unit_id)
      REFERENCES unit(id);
ALTER TABLE shelf_layer ADD
  CONSTRAINT fk_shelf_layer_length_unit_id
    FOREIGN KEY (length_unit_id)
      REFERENCES unit(id);
ALTER TABLE store_object ADD
  CONSTRAINT fk_store_object_length_unit_id
    FOREIGN KEY (length_unit_id)
      REFERENCES unit(id);
ALTER TABLE map2d ADD
  CONSTRAINT fk_map2d_length_unit_id
    FOREIGN KEY (length_unit_id)
      REFERENCES unit(id);
