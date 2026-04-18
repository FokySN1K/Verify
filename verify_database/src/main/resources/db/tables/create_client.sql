create table client
(
    id int8 generated always as identity,
    client_id varchar(50),
    name varchar(40),
    surname varchar(40),
    email varchar(50),
    role client_role,
    --
    constraint client_id_pk primary key (id),
    constraint client_email_unique unique (email)
);

create unique index client_client_id_unique_idx on client(client_id);
