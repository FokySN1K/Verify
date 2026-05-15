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

CREATE UNIQUE INDEX client_client_id_unique_idx ON client(client_id);

COMMENT ON TABLE client IS 'Справочник клиентов (заказчики, подрядчики)';
COMMENT ON COLUMN client.id IS 'Уникальный идентификатор клиента';
COMMENT ON COLUMN client.client_id IS 'Внешний/системный идентификатор клиента';
COMMENT ON COLUMN client.name IS 'Имя клиента';
COMMENT ON COLUMN client.surname IS 'Фамилия клиента';
COMMENT ON COLUMN client.email IS 'Электронная почта (уникальная)';