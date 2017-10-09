INSERT INTO item (id, name, price, producer, quantity, supplier, unit) VALUES (1, 'Pulver 7024 mat', 0, 'Pulver', 20, null, 'KILOGRAM');
INSERT INTO item (id, name, price, producer, quantity, supplier, unit) VALUES (2, 'Pulver 2012 mat', 0, 'Pulver', 150, null, 'KILOGRAM');

INSERT INTO storage_action (id, price, quantity, type, item_id, manager) VALUES (1, 540, 20, 'PURCHASE', 1, 'Петренко');
INSERT INTO storage_action (id, price, quantity, type, item_id, manager) VALUES (2, 3560, 150, 'PURCHASE', 2, null);