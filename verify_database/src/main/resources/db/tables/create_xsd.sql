create table xsd
(
    id int8 generated always as identity,
    stage varchar(100),
    name varchar(50),
    begin_date date,
    end_date date,
    data xml,
    status xsd_status,
    link varchar(100),
    version int8,
    --
    constraint xsd_id_pk primary key (id)
);
