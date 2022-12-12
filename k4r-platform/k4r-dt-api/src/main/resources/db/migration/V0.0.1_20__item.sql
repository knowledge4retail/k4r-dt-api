CREATE TABLE IF NOT EXISTS item
(
  id SERIAL PRIMARY KEY, -- primary key column
  position_in_facing_x INTEGER,
  position_in_facing_y INTEGER,
  position_in_facing_z INTEGER,
  item_group_id INTEGER,

  CONSTRAINT fk_item_item_group
    FOREIGN KEY (item_group_id)
      REFERENCES item_group(id)
);

CREATE INDEX idx_item_id ON item(id);