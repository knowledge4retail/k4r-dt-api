CREATE TABLE IF NOT EXISTS store
(
  id SERIAL PRIMARY KEY, -- primary key column
  store_name VARCHAR(255) NOT NULL,
  store_number VARCHAR(255),
  address_country VARCHAR(255),
  address_state VARCHAR(255),
  address_city VARCHAR(255),
  address_postcode VARCHAR(255),
  address_street VARCHAR(255),
  address_street_number VARCHAR(255),
  address_additional VARCHAR(255),
  latitude NUMERIC(8,6),
  longitude NUMERIC(9,6),
  cad_plan_id VARCHAR(255)
);

CREATE INDEX idx_store_id ON store(id);


