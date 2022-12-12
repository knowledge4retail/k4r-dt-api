--
-- Base data used ccross the integration test suite.
-- These items should never be deleted but fields which are not primary- or foreign keys may be modified.
-- Further items required for foreign-key-combinations etc. should also be added here.
--
--
-- base store for all integration tests. Do not delete during testing
INSERT INTO store (id, store_name, address_city)
        VALUES (1000, 'STORE_ID', 'Herne');
-- base customer for all integration tests. Do not delete during testing
INSERT INTO customer (id, anonymised_name)
        VALUES (1000, 'CUSTOMER_ID');
-- base store characteristic for all integration tests. Do not delete during testing
INSERT INTO store_characteristic (id, name)
        VALUES (1000, 'PRODUCT_CHARACTERISTIC_ID');
INSERT INTO store_characteristic (id, name)
        VALUES (1002, 'PRODUCT_CHARACTERISTIC_ID_PROPERTY_NEW');
-- base product characteristic for all integration tests. Do not delete during testing
INSERT INTO product_characteristic (id, name)
        VALUES (1000, 'PRODUCT_CHARACTERISTIC_ID');
INSERT INTO product_characteristic (id, name)
        VALUES (1002, 'PRODUCT_CHARACTERISTIC_ID_PROPERTY_NEW');
-- base material group for all integration tests. Do not delete during testing
INSERT INTO material_group (id, name, parent_id)
        VALUES (1000, 'MATERIAL_GROUP_ID', 1000);
-- base product for all integration tests. Do not delete during testing
INSERT INTO product (id, material_group_id)
        VALUES ('TST-1000', 1000);
INSERT INTO product (id, material_group_id)
        VALUES ('TST-1002', 1000);
INSERT INTO product (id, material_group_id)
        VALUES ('TST-1003', 1000);
-- base product group for all integration tests. Do not delete during testing
INSERT INTO product_group (id, store_id, name)
        VALUES (1000, 1000, 'PRODUCT_GROUP_ID');
INSERT INTO product_in_group (product_group_id, product_id)
        VALUES (1000, 'TST-1000');
-- base product_unit for all integration tests. Do not delete during testing
INSERT INTO product_unit (id, product_id, length, width, height)
        VALUES (1000, 'TST-1000', 10.0, 10.0, 10.0);
-- base device for all integration tests. Do not delete during testing
INSERT INTO device (id, store_id)
        VALUES ('ROB-1000', 1000);
-- base store property for all integration tests. Do not delete during testing
INSERT INTO store_property (id, characteristic_id, store_id, value_low)
        VALUES (1000, 1000, 1000, 'PROPERTY_ID');
-- base product property for all integration tests. Do not delete during testing
INSERT INTO product_property (id, product_id, characteristic_id, store_id, value_low)
        VALUES (1000, 'TST-1000', 1000, 1000, 'PROPERTY_ID');
-- base product property for all integration tests. Do not delete during testing
INSERT INTO product_property (id, product_id, characteristic_id, value_low)
        VALUES (1002, 'TST-1000', 1000, 'PROPERTY_ID');
-- used in unit for all integration tests. Do not delete during testing
INSERT INTO unit (id, name, type, symbol)
        VALUES (1000, 'millimeter', 'length', 'mm');
-- base shelf for all integration tests. Do not delete during testing
INSERT INTO shelf (id, store_id, external_reference_id, width, height, depth, length_unit_id, product_group_id)
        VALUES (1000, 1000, 'SHELF_ID', 1, 1, 1, 1000, 1000);
-- base shelf layer for all integration tests. Do not delete during testing
INSERT INTO shelf_layer (id, shelf_id, external_reference_id, width, height, depth, length_unit_id)
        VALUES (1000, 1000, 'SHELF_LAYER_ID', 1, 1, 1, 1000);
-- base facing for all integration tests. Do not delete during testing
INSERT INTO facing (id, product_unit_id, shelf_layer_id, layer_relative_position, no_of_items_width, no_of_items_depth)
        VALUES (1000, 1000, 1000, 1, 1000, 1001);
-- base item for all integration tests. Do not delete during testing
INSERT INTO item (id, facing_id)
        VALUES (1000, 1000);
-- base product_gtin for all integration tests. Do not delete during testing
INSERT INTO product_gtin (id, product_unit_id, gtin, external_reference_id)
        VALUES (1000, 1000, 'GTIN_1000', 'DAN_1000');
-- base despatch_advice for all integration tests. Do not delete during testing
INSERT INTO despatch_advice (id, store_id)
        VALUES (1000, 1000);
-- base store object for all integration tests. Do not delete during testing
INSERT INTO store_object (id, type, width, height, depth, length_unit_id, store_id)
        VALUES (1000, 'cashzone', 1, 1, 1, 1000, 1000);
INSERT INTO store_object (id, type, width, height, depth, length_unit_id, store_id)
        VALUES (1002, 'warehousentrance', 1, 1, 1, 1000, 1000);
-- base despatch_logistic_unit for all integration tests. Do not delete during testing
INSERT INTO despatch_logistic_unit (id, despatch_advice_id)
        VALUES (1000, 1000);
-- base despatch_line_item for all integration tests. Do not delete during testing
INSERT INTO despatch_line_item (id, despatch_logistic_unit_id, product_id)
        VALUES (1000, 1000, 'TST-1000');
-- used in product_description for all integration tests. Do not delete during testing
INSERT INTO product_description (id, product_id, description)
        VALUES (1000, 'TST-1000', 'PRODUCT_1');
-- used in shopping_basket_position for all integration tests. Do not delete during testing
INSERT INTO shopping_basket_position (id, product_id, store_id, customer_id, quantity)
        VALUES (1000, 'TST-1000', 1000, 1000, 1000);
-- used in trolley for all integration tests. Do not delete during testing
INSERT INTO trolley (id, store_id, layers, width, height, depth)
        VALUES (1000, 1000, 3, 1.0, 1.0, 1.0);
-- used in delivered_unit for all integration tests. Do not delete during testing
INSERT INTO delivered_unit (id, trolley_id, product_unit_id, product_gtin_id, trolley_layer, pallet_id, amount_unit, amount_items, width, height, depth)
        VALUES (1000, 1000, 1000, 1000, 1, '1', 1, 1, 1.0, 1.0, 1.0);
-- used in scan for all integration tests. Do not delete during testing
INSERT INTO scan (timestamp, entity_type, id)
        VALUES ('2019-01-15 07:00:00+00', 'store', 1000);
-- used in barcode for all integration tests. Do not delete during testing
INSERT INTO barcode (id, product_gtin_id, shelf_layer_id, position_x, position_y, position_z)
        VALUES (1000, 1000, 1000, 1.0, 1.0, 1.0);
-- used in trolley_route for all integration tests. Do not delete during testing
INSERT INTO trolley_route (id, trolley_id, sorting_date, route_order, shelf_id)
        VALUES (1000, 1000, '2019-01-15 07:00:00+00', 1, 1000);


--
-- Data used for deletion.
-- If you are writing a new test case that should successfully delete, add the item here.
--
--
-- used in store integration test
INSERT INTO store (id, store_name, address_city)
        VALUES (1001, 'STORE_ID_DELETE', 'Herne2');
-- used in customer integration test
INSERT INTO customer (id, anonymised_name)
        VALUES (1001, 'CUSTOMER_ID_DELETE');
-- used in store characteristic integration test
INSERT INTO store_characteristic (id, name)
        VALUES (1001, 'PRODUCT_CHARACTERISTIC_ID_DELETE');
-- used in store property integration test
INSERT INTO store_characteristic (id, name)
        VALUES (1003, 'PRODUCT_CHARACTERISTIC_ID_PROPERTY_DELETE');
-- used in product characteristic integration test
INSERT INTO product_characteristic (id, name)
        VALUES (1001, 'PRODUCT_CHARACTERISTIC_ID_DELETE');
-- used in product property integration test
INSERT INTO product_characteristic (id, name)
        VALUES (1003, 'PRODUCT_CHARACTERISTIC_ID_PROPERTY_DELETE');
-- used in material_group integration test
INSERT INTO material_group (id, name, parent_id)
        VALUES (1001, 'MATERIAL_GROUP_ID_DELETE', 1000);
-- used in product integration test
INSERT INTO product (id, material_group_id)
        VALUES ('TST-1001', 1000);
-- used in product group integration test
INSERT INTO product_group (id, store_id, name)
        VALUES (1001, 1000, 'PRODUCT_GROUP_ID_DELETE');
INSERT INTO product_in_group (product_group_id, product_id)
        VALUES (1001, 'TST-1000');
-- used in product_unit integration test
INSERT INTO product_unit (id, product_id, length, width, height)
        VALUES (1001, 'TST-1000', 10.0, 10.0, 10.0);
-- used in device integration test
INSERT INTO device (id, store_id)
        VALUES ('ROB-1001', 1000);
-- used in store property integration test
INSERT INTO store_property (id, characteristic_id, store_id, value_low)
        VALUES (1001, 1003, 1000, 'PRODUCT_PROPERTY_TO_DELETE');
-- used in product property integration test
INSERT INTO product_property (id, product_id, characteristic_id, store_id, value_low)
        VALUES (1001, 'TST-1000', 1003, 1000, 'PRODUCT_PROPERTY_TO_DELETE');
-- used in product property integration test
INSERT INTO product_property (id, product_id, characteristic_id, value_low)
        VALUES (1003, 'TST-1000', 1003, 'PRODUCT_PROPERTY_TO_DELETE');
-- used in shelf integration test
INSERT INTO shelf (id, store_id, external_reference_id, width, height, depth, length_unit_id)
        VALUES (1001, 1000, 'SHELF_ID_DELETE', 1, 1, 1, 1000);
-- used in shelf integration test
INSERT INTO shelf (id, store_id, external_reference_id, width, height, depth, length_unit_id)
        VALUES (1002, 1000, 'SHELF_ID_DELETE_2', 1, 1, 1, 1000);
-- used in shelf integration test
INSERT INTO shelf_layer (id, shelf_id, external_reference_id, width, height, depth, length_unit_id)
        VALUES (1001, 1001, 'SHELF_LAYER_ID_DELETE', 1, 1, 1, 1000);
-- used in shelf layer integration test
INSERT INTO shelf_layer (id, shelf_id, external_reference_id, width, height, depth, length_unit_id)
        VALUES (1002, 1000, 'SHELF_LAYER_ID_DELETE_2', 1, 1, 1, 1000);
-- used in facing integration test
INSERT INTO facing (id, shelf_layer_id, layer_relative_position, no_of_items_width, no_of_items_depth)
        VALUES (1001, 1000, 1, 1000, 1001);
-- used in item integration test
INSERT INTO item (id, facing_id)
        VALUES (1001, 1000);
-- used in product_gtin integration test
INSERT INTO product_gtin (id, product_unit_id, gtin,external_reference_id)
        VALUES (1001, 1000, 'GTIN_1001', 'DAN_1001');
-- used in despatch_advice integration test
INSERT INTO despatch_advice (id, store_id)
        VALUES (1001, 1000);
-- used in store object integration test
INSERT INTO store_object (id, type, width, height, depth, length_unit_id, store_id)
        VALUES (1001, 'STORE_OBJECT_ID_DELETE', 1, 1, 1, 1000, 1000);
-- used in despatch_logistic_unit integration test
INSERT INTO despatch_logistic_unit (id, despatch_advice_id)
        VALUES (1001, 1000);
-- used in despatch_line_item integration test
INSERT INTO despatch_line_item (id, despatch_logistic_unit_id, product_id)
        VALUES (1001, 1000, 'TST-1000');
-- used in product_description integration test
INSERT INTO product_description (id, product_id, description)
        VALUES (1001, 'TST-1000', 'PRODUCT_2');
-- used in unit integration test
INSERT INTO unit (id, name, type, symbol)
        VALUES (1001, 'millimeter', 'length', 'mm');
-- used in trolley integration test
INSERT INTO trolley (id, store_id, layers, width, height, depth)
        VALUES (1001, 1000, 3, 1.0, 1.0, 1.0);
-- used in delivered_unit integration test
INSERT INTO delivered_unit (id, trolley_id, product_unit_id, product_gtin_id, trolley_layer, pallet_id, amount_unit, amount_items, width, height, depth)
        VALUES (1001, 1000, 1000, 1000, 1, '1', 1, 1, 1.0, 1.0, 1.0);
-- used in barcode integration tests
INSERT INTO barcode (id, product_gtin_id, shelf_layer_id, position_x, position_y, position_z)
        VALUES (1001, 1000, 1000, 1.0, 1.0, 1.0);
-- used in trolley_route for all integration tests
INSERT INTO trolley_route (id, trolley_id, sorting_date, route_order, shelf_id)
        VALUES (1001, 1000, '2019-01-15 07:00:00+00', 1, 1000);
