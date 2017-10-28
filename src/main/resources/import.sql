INSERT INTO product (id, name, producer, quantity, supplier, price, unit) VALUES (nextval('product_seq'), 'Pulver 7024 mat', 'Pulver', 20, null, 26, 'KILOGRAM');
INSERT INTO product (id, name, producer, quantity, supplier, price, unit) VALUES (nextval('product_seq'), 'Pulver 2012 mat', 'Pulver', 150, null, 30, 'KILOGRAM');
INSERT INTO product (id, name, producer, quantity, supplier, price, unit) VALUES (nextval('product_seq'), 'Pulver 1002 mat', 'Pulver', 0, null, 0, 'KILOGRAM');
INSERT INTO product (id, name, producer, quantity, supplier, price, unit) VALUES (nextval('product_seq'), 'Shmulver 5145 mat', 'Shmulver', 0, null, 0, 'KILOGRAM');
INSERT INTO product (id, name, producer, quantity, supplier, price, unit) VALUES (nextval('product_seq'), 'Shmulver 1012 mat', 'Shmulver', 0, null, 0, 'KILOGRAM');

INSERT INTO user_details (id, email, family_name, gender, given_name, locale, name, picture, authority) VALUES (nextval('user_seq'), 'fergusmacdubh@gmail.com', 'MacDubh', 'male', 'Fergus', 'en', 'Fergus MacDubh', 'https://lh5.googleusercontent.com/-JedDN0NvHv4/AAAAAAAAAAI/AAAAAAAADUs/Rw5rhJTzyZ8/photo.jpg', 'ROLE_ADMIN');
INSERT INTO user_details (id, email, family_name, gender, given_name, locale, name, picture, authority) VALUES (nextval('user_seq'), 'oleksandr.cherniaiev@gmail.com', 'Петренко', 'male', 'Вася', 'en', 'Вася Петренко', 'https://cdn3.iconfinder.com/data/icons/rcons-user-action/32/boy-512.png', 'ROLE_SALES');
INSERT INTO user_details (id, email, family_name, gender, given_name, locale, name, picture, authority) VALUES (nextval('user_seq'), 'vasiliev@vfasad.com.ua', 'Васильев', 'male', 'Петя', 'en', 'Петя Васильев', 'https://img00.deviantart.net/daf4/i/2008/255/a/f/avatar_icon_by_pharaun_mizzrym.jpg', 'ROLE_SALES');
INSERT INTO user_details (id, email, family_name, gender, given_name, locale, name, picture, authority) VALUES (nextval('user_seq'), 'khomiakov@vfasad.com.ua', 'Хомяков', 'male', 'Коля', 'en', 'Коля Хомяков', 'https://maxcdn.icons8.com/Share/icon/Cinema//avatar1600.png', 'ROLE_SALES');
INSERT INTO user_details (id, email, family_name, gender, given_name, locale, name, picture, authority) VALUES (nextval('user_seq'), 'dmitry.drepin@vfasad.com.ua', 'Дрепин', 'male', 'Дмитрий', 'en', 'Дмитрий Дрепин', 'https://lh3.googleusercontent.com/-cnjy0eG_Q24/AAAAAAAAAAI/AAAAAAAACXA/kUI5QHOz4Ys/s75-p-rw-no/photo.jpg', 'ROLE_ADMIN');

INSERT INTO product_action (id, price, quantity, type, product_id, actor_id, created) VALUES (nextval('product_action_seq'), 540, 20, 'PURCHASE', 1, 2, current_timestamp);
INSERT INTO product_action (id, price, quantity, type, product_id, actor_id, created) VALUES (nextval('product_action_seq'), 260, 10, 'PURCHASE', 1, 3, current_timestamp);
INSERT INTO product_action (id, price, quantity, type, product_id, actor_id, created) VALUES (nextval('product_action_seq'), 4500, 150, 'PURCHASE', 2, 4, current_timestamp);

INSERT INTO paint_order (id, area, document_column, created, manager_id, price, status, planned, completed) VALUES (nextval('paint_order_seq'), 50, 'Петя клиент', CURRENT_TIMESTAMP, 2, 100500, 'CREATED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO paint_order (id, area, document_column, created, manager_id, price, status, planned, completed) VALUES (nextval('paint_order_seq'), 20, 'Вася клиент', CURRENT_TIMESTAMP, 3, 100500, 'IN_PROGRESS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO paint_order (id, area, document_column, created, manager_id, price, status, planned, completed) VALUES (nextval('paint_order_seq'), 11, 'Коля клиент', CURRENT_TIMESTAMP, 3, 100500, 'IN_PROGRESS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO paint_order (id, area, document_column, created, manager_id, price, status, planned, completed) VALUES (nextval('paint_order_seq'), 24, 'Петя клиент', CURRENT_TIMESTAMP, 4, 100500, 'CREATED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO paint_order (id, area, document_column, created, manager_id, price, status, planned, completed) VALUES (nextval('paint_order_seq'), 3, 'Вася клиент', CURRENT_TIMESTAMP, 2, 100500, 'SHIPPING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO paint_order_consume (id, actual_used_quantity, calculated_quantity, product_id, order_fk) VALUES (nextval('paint_order_consume_seq'), 0, 12, 1, 1);
INSERT INTO paint_order_consume (id, actual_used_quantity, calculated_quantity, product_id, order_fk) VALUES (nextval('paint_order_consume_seq'), 0, 2, 2, 1);
INSERT INTO paint_order_consume (id, actual_used_quantity, calculated_quantity, product_id, order_fk) VALUES (nextval('paint_order_consume_seq'), 0, 122, 1, 2);
INSERT INTO paint_order_consume (id, actual_used_quantity, calculated_quantity, product_id, order_fk) VALUES (nextval('paint_order_consume_seq'), 0, 2, 1, 3);
INSERT INTO paint_order_consume (id, actual_used_quantity, calculated_quantity, product_id, order_fk) VALUES (nextval('paint_order_consume_seq'), 0, 32, 1, 4);
INSERT INTO paint_order_consume (id, actual_used_quantity, calculated_quantity, product_id, order_fk) VALUES (nextval('paint_order_consume_seq'), 0, 5, 1, 5);
INSERT INTO paint_order_consume (id, actual_used_quantity, calculated_quantity, product_id, order_fk) VALUES (nextval('paint_order_consume_seq'), 0, 15, 2, 5);