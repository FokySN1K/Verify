create table client
(
    id int8 generated always as identity,
    client_id int8,
    name varchar(40),
    surname varchar(40),
    email varchar(50),
    --
    constraint client_id_pk primary key (id)
);