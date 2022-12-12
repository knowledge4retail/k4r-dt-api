-- change trolley entities

ALTER TABLE delivered_item RENAME TO delivered_unit;
ALTER TABLE delivered_unit ADD COLUMN width DOUBLE PRECISION NOT NULL;
ALTER TABLE delivered_unit ADD COLUMN height DOUBLE PRECISION NOT NULL;
ALTER TABLE delivered_unit ADD COLUMN depth DOUBLE PRECISION NOT NULL;
ALTER TABLE delivered_unit ADD COLUMN amount_items INTEGER NOT NULL;
ALTER TABLE delivered_unit RENAME COLUMN amount TO amount_unit;
ALTER TABLE delivered_unit ALTER COLUMN pallet_id SET NOT NULL;
ALTER TABLE delivered_unit ALTER COLUMN amount_unit SET NOT NULL;
ALTER TABLE delivered_unit ALTER COLUMN product_gtin_id SET NOT NULL;
ALTER TABLE delivered_unit ALTER COLUMN product_unit_id SET NOT NULL;

ALTER TABLE trolley ADD COLUMN layers INTEGER NOT NULL;
ALTER TABLE trolley ADD COLUMN width DOUBLE PRECISION NOT NULL;
ALTER TABLE trolley ADD COLUMN height DOUBLE PRECISION NOT NULL;
ALTER TABLE trolley ADD COLUMN depth DOUBLE PRECISION NOT NULL;
ALTER TABLE trolley ALTER COLUMN store_id SET NOT NULL;
ALTER TABLE trolley DROP COLUMN position_x;
ALTER TABLE trolley DROP COLUMN position_y;
ALTER TABLE trolley DROP COLUMN position_z;
ALTER TABLE trolley DROP COLUMN orientation_x;
ALTER TABLE trolley DROP COLUMN orientation_y;
ALTER TABLE trolley DROP COLUMN orientation_z;
ALTER TABLE trolley DROP COLUMN orientation_w;
ALTER TABLE trolley DROP COLUMN type;


CREATE TABLE IF NOT EXISTS trolley_route
(
  id SERIAL PRIMARY KEY, -- primary key column
  trolley_id INTEGER NOT NULL,
  sorting_date TIMESTAMP WITH TIME ZONE NOT NULL,
  route_order INTEGER NOT NULL,
  shelf_id INTEGER NOT NULL,

  CONSTRAINT fk_trolley_route_trolley
    FOREIGN KEY (trolley_id)
      REFERENCES trolley(id),
  CONSTRAINT fk_trolley_route_shelf
    FOREIGN KEY (shelf_id)
      REFERENCES shelf(id)
);

CREATE INDEX idx_trolley_route_id ON trolley_route(id);
