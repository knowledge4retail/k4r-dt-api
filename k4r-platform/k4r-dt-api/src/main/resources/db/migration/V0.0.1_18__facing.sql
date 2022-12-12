CREATE TABLE IF NOT EXISTS facing
(
  id SERIAL PRIMARY KEY, -- primary key column
  shelf_layer_id INTEGER,
  layer_relative_position INTEGER,
  no_of_items_width INTEGER,
  no_of_items_depth INTEGER,

  CONSTRAINT fk_facing_shelf_layer
    FOREIGN KEY (shelf_layer_id)
      REFERENCES shelf_layer(id)
);

CREATE INDEX idx_facing_id ON facing(id);
CREATE INDEX idx_facing_shelf_layer_id ON facing(shelf_layer_id);


