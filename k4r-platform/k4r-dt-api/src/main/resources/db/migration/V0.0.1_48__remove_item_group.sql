-- remove item_group and add attributes to facing

ALTER TABLE item DROP COLUMN item_group_id;
ALTER TABLE item ADD COLUMN facing_id INTEGER;
DROP TABLE item_group;

ALTER TABLE item ADD
  CONSTRAINT fk_item_facing
    FOREIGN KEY (facing_id)
      REFERENCES facing(id);

ALTER TABLE facing ADD COLUMN no_of_items_height INTEGER;
ALTER TABLE facing ADD COLUMN min_stock INTEGER;
ALTER TABLE facing ADD COLUMN stock INTEGER;
ALTER TABLE facing ADD COLUMN product_unit_id INTEGER;

ALTER TABLE facing ADD
  CONSTRAINT fk_facing_product_unit
    FOREIGN KEY (product_unit_id)
      REFERENCES product_unit(id);