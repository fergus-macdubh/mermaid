INSERT INTO product (id, name, producer, quantity, supplier, unit) VALUES (nextval('product_seq'), 'Pulver 7024 mat', 'Pulver', 20, null, 'KILOGRAM');
INSERT INTO product (id, name, producer, quantity, supplier, unit) VALUES (nextval('product_seq'), 'Pulver 2012 mat', 'Pulver', 150, null, 'KILOGRAM');

INSERT INTO product_action (id, price, quantity, type, product_id, manager) VALUES (nextval('product_action_seq'), 540, 20, 'PURCHASE', 1, 'Петренко');
INSERT INTO product_action (id, price, quantity, type, product_id, manager) VALUES (nextval('product_action_seq'), 3560, 150, 'PURCHASE', 2, null);

INSERT INTO paint_order (id, area, client, created, manager, price, status) VALUES (nextval('paint_order_seq'), 50, 'Петя клиент', CURRENT_TIMESTAMP, 'Петренко', 100500, 'CREATED');
INSERT INTO paint_order (id, area, client, created, manager, price, status) VALUES (nextval('paint_order_seq'), 50, 'Вася клиент', CURRENT_TIMESTAMP, 'Васильев', 100500, 'IN_PROGRESS');
INSERT INTO paint_order (id, area, client, created, manager, price, status) VALUES (nextval('paint_order_seq'), 50, 'Коля клиент', CURRENT_TIMESTAMP, 'Васильев', 100500, 'IN_PROGRESS');
INSERT INTO paint_order (id, area, client, created, manager, price, status) VALUES (nextval('paint_order_seq'), 50, 'Петя клиент', CURRENT_TIMESTAMP, 'Хомяков', 100500, 'CREATED');
INSERT INTO paint_order (id, area, client, created, manager, price, status) VALUES (nextval('paint_order_seq'), 50, 'Вася клиент', CURRENT_TIMESTAMP, 'Петренко', 100500, 'COMPLETED');