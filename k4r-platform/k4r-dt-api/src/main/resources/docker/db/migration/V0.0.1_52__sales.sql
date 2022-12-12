CREATE TABLE IF NOT EXISTS sales
(
  timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
  store_id INT,
  gtin VARCHAR(255),
  sold_units DOUBLE PRECISION,
  turnover DOUBLE PRECISION,
  time_resolution VARCHAR(255),
  type VARCHAR(255),

  CONSTRAINT pk_sales
    PRIMARY KEY (timestamp, store_id, gtin)
);

CREATE INDEX idx_sales_timestamp ON sales(timestamp);
CREATE INDEX idx_sales_store_id ON sales(store_id);
CREATE INDEX idx_store_gtin ON sales(gtin);


SET search_path TO dt_v1, public;
SELECT create_hypertable('sales', 'timestamp', if_not_exists => TRUE, create_default_indexes => FALSE);
