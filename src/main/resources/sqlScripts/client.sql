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

create sequence client_seq increment by 1 start with 1;

insert into client (id, name, manager_id)
    (select nextval('client_seq'), u.name || ' - менеджер', u.id
     from user_details u
     where u.id in (select o.manager_id from paint_order o));

update paint_order o
set client_id = (select c.id
                 from client c
                 where c.manager_id = o.manager_id);

alter table client alter column manager_id set not null;
alter table client add column deleted boolean not null default false;

update client
set deleted = true;

alter table paint_order drop column manager_id;