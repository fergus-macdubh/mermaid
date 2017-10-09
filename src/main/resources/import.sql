INSERT INTO item (id, name, producer, quantity, supplier, unit) VALUES (9999991, 'Pulver 7024 mat', 'Pulver', 20, null, 'KILOGRAM');
INSERT INTO item (id, name, producer, quantity, supplier, unit) VALUES (9999992, 'Pulver 2012 mat', 'Pulver', 150, null, 'KILOGRAM');

INSERT INTO storage_action (id, price, quantity, type, item_id, manager) VALUES (9999991, 540, 20, 'PURCHASE', 9999991, 'Петренко');
INSERT INTO storage_action (id, price, quantity, type, item_id, manager) VALUES (9999992, 3560, 150, 'PURCHASE', 9999992, null);