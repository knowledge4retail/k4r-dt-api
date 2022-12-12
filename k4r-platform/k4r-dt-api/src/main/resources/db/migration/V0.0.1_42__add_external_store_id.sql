-- add one attribute to store

ALTER TABLE store ADD COLUMN external_reference_id VARCHAR(255);
ALTER TABLE scan RENAME COLUMN dt_id to id;
ALTER TABLE scan ADD COLUMN link_to_external_reference_id VARCHAR(255);
ALTER TABLE scan ADD COLUMN additional_info VARCHAR(255);

