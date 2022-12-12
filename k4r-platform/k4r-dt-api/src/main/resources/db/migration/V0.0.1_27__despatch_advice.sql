ALTER TABLE delivery DROP CONSTRAINT fk_delivery_store;

CREATE TABLE IF NOT EXISTS despatch_advice
(
  id SERIAL PRIMARY KEY, -- primary key column
  store_id INTEGER NOT NULL,
  ship_time TIMESTAMP WITH TIME ZONE,
  creation_time TIMESTAMP WITH TIME ZONE,
  sender_qualifier VARCHAR(255),
  sender_id INTEGER,

  CONSTRAINT fk_despatch_advice_store
      FOREIGN KEY (store_id)
        REFERENCES store(id)
);

CREATE INDEX idx_despatch_advice_id ON despatch_advice(id);
CREATE INDEX idx_despatch_advice_store_id ON despatch_advice(store_id);