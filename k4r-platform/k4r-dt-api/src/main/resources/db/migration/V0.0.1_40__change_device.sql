-- change attributes in device domain

ALTER TABLE device ALTER COLUMN store_id SET NOT NULL;

ALTER TABLE device_status RENAME COLUMN reference_id TO frame_id;
ALTER TABLE device_status ALTER COLUMN frame_id SET NOT NULL;
ALTER TABLE device_image RENAME COLUMN reference_id TO frame_id;

ALTER TABLE device_image ALTER COLUMN frame_id SET NOT NULL;
ALTER TABLE device_image ALTER COLUMN device_id SET NOT NULL;
ALTER TABLE device_image ALTER COLUMN timestamp SET NOT NULL;

ALTER TABLE device_image ADD
  CONSTRAINT fk_device_image_device
    FOREIGN KEY (device_id)
      REFERENCES device(id);

ALTER TABLE map2d ADD
  CONSTRAINT fk_map2d_device
    FOREIGN KEY (device_id)
      REFERENCES device(id);
