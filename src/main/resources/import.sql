INSERT INTO product (id, name, producer, quantity, supplier, price, unit) VALUES (nextval('product_seq'), 'Pulver 7024 mat', 'Pulver', 20, null, 26, 'KILOGRAM');
INSERT INTO product (id, name, producer, quantity, supplier, price, unit) VALUES (nextval('product_seq'), 'Pulver 2012 mat', 'Pulver', 150, null, 30, 'KILOGRAM');
INSERT INTO product (id, name, producer, quantity, supplier, price, unit) VALUES (nextval('product_seq'), 'Pulver 1002 mat', 'Pulver', 0, null, 0, 'KILOGRAM');
INSERT INTO product (id, name, producer, quantity, supplier, price, unit) VALUES (nextval('product_seq'), 'Shmulver 5145 mat', 'Shmulver', 0, null, 0, 'KILOGRAM');
INSERT INTO product (id, name, producer, quantity, supplier, price, unit) VALUES (nextval('product_seq'), 'Shmulver 1012 mat', 'Shmulver', 0, null, 0, 'KILOGRAM');

INSERT INTO product_action (id, price, quantity, type, product_id, manager, created) VALUES (nextval('product_action_seq'), 540, 20, 'PURCHASE', 1, 'Петренко', current_timestamp);
INSERT INTO product_action (id, price, quantity, type, product_id, manager, created) VALUES (nextval('product_action_seq'), 260, 10, 'PURCHASE', 1, 'Петренко',current_timestamp);
INSERT INTO product_action (id, price, quantity, type, product_id, manager, created) VALUES (nextval('product_action_seq'), 4500, 150, 'PURCHASE', 2, null, current_timestamp);

INSERT INTO paint_order (id, area, client, created, manager, price, status) VALUES (nextval('paint_order_seq'), 50, 'Петя клиент', CURRENT_TIMESTAMP, 'Петренко', 100500, 'CREATED');
INSERT INTO paint_order (id, area, client, created, manager, price, status) VALUES (nextval('paint_order_seq'), 20, 'Вася клиент', CURRENT_TIMESTAMP, 'Васильев', 100500, 'IN_PROGRESS');
INSERT INTO paint_order (id, area, client, created, manager, price, status) VALUES (nextval('paint_order_seq'), 11, 'Коля клиент', CURRENT_TIMESTAMP, 'Васильев', 100500, 'IN_PROGRESS');
INSERT INTO paint_order (id, area, client, created, manager, price, status) VALUES (nextval('paint_order_seq'), 24, 'Петя клиент', CURRENT_TIMESTAMP, 'Хомяков', 100500, 'CREATED');
INSERT INTO paint_order (id, area, client, created, manager, price, status) VALUES (nextval('paint_order_seq'), 3, 'Вася клиент', CURRENT_TIMESTAMP, 'Петренко', 100500, 'SHIPPING');

INSERT INTO paint_order_consume (id, actual_used_quantity, calculated_quantity, product_id, order_fk) VALUES (nextval('paint_order_consume_seq'), 0, 12, 1, 1);
INSERT INTO paint_order_consume (id, actual_used_quantity, calculated_quantity, product_id, order_fk) VALUES (nextval('paint_order_consume_seq'), 0, 2, 2, 1);
INSERT INTO paint_order_consume (id, actual_used_quantity, calculated_quantity, product_id, order_fk) VALUES (nextval('paint_order_consume_seq'), 0, 122, 1, 2);
INSERT INTO paint_order_consume (id, actual_used_quantity, calculated_quantity, product_id, order_fk) VALUES (nextval('paint_order_consume_seq'), 0, 2, 1, 3);
INSERT INTO paint_order_consume (id, actual_used_quantity, calculated_quantity, product_id, order_fk) VALUES (nextval('paint_order_consume_seq'), 0, 32, 1, 4);
INSERT INTO paint_order_consume (id, actual_used_quantity, calculated_quantity, product_id, order_fk) VALUES (nextval('paint_order_consume_seq'), 0, 5, 1, 5);
INSERT INTO paint_order_consume (id, actual_used_quantity, calculated_quantity, product_id, order_fk) VALUES (nextval('paint_order_consume_seq'), 0, 15, 2, 5);

INSERT INTO user_details (id, email, family_name, gender, given_name, locale, name, picture) VALUES (nextval('user_seq'), 'fergusmacdubh@gmail.com', 'MacDubh', 'male', 'Fergus', 'en', 'Fergus MacDubh', 'https://lh5.googleusercontent.com/-JedDN0NvHv4/AAAAAAAAAAI/AAAAAAAADUs/Rw5rhJTzyZ8/photo.jpg');
INSERT INTO user_authorities (user_id, authorities) VALUES (1, 'ROLE_ADMIN');