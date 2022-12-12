CREATE TABLE IF NOT EXISTS item_group
(
  id SERIAL PRIMARY KEY, -- primary key column
  logistical_unit_id INTEGER,
  facing_id INTEGER,
  stock INTEGER,

  CONSTRAINT fk_item_group_logistical_unit
    FOREIGN KEY (logistical_unit_id)
      REFERENCES logistical_unit(id),
  CONSTRAINT fk_item_group_facing
    FOREIGN KEY (facing_id)
      REFERENCES facing(id)
);

CREATE INDEX idx_item_group_id ON item_group(id);