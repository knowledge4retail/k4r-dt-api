-- add running_number and block_id to shelf and shelf_layer

ALTER TABLE shelf ADD COLUMN block_id INTEGER;
ALTER TABLE shelf ADD COLUMN running_number INTEGER;

ALTER TABLE shelf_layer ADD COLUMN running_number INTEGER;

