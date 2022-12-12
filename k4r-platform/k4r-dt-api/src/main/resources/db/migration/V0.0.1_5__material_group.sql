CREATE TABLE IF NOT EXISTS material_group
(
  id SERIAL PRIMARY KEY, -- primary key column
  name VARCHAR(255) NOT NULL,
  parent_id INTEGER,

  CONSTRAINT fk_material_group_material_group
    FOREIGN KEY (parent_id)
      REFERENCES material_group(id)
);
