create table client
(
  id          bigint not null
    constraint client_pkey
    primary key,
  name        varchar(255),
  phone       varchar(255),
  contact     varchar(255),
  email       varchar(255) unique,
  manager_id  bigint constraint client_manager_fkey references user_details);

alter table paint_order add column client_id bigint;
alter table paint_order add constraint paint_order_client_fkey foreign key (client_id) references client(id);