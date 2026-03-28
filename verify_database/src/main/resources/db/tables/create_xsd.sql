create table xsd
(
    id int8 generated always as identity,
    stage text,
    name text,
    begin_date date,
    end_date date,
    data xml,
    status xsd_status,
    link text,
    version int8,
    --
    constraint xsd_id_pk primary key (id),
    constraint xsd_name_version_unique unique (name, version)
);
