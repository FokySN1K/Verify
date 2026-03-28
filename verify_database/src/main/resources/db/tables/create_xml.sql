create table xml
(
    id int8 generated always as identity,
    name text,
    contract_id int8,
    xsd_id int8,
    status xml_status,
    version int8,
    data xml,
    reason text,
    created_ts timestamp default current_timestamp,
    --
    constraint xml_id_pk primary key (id),
    constraint xml_contract_id_fk foreign key (contract_id) references contract(id),
    constraint xml_xsd_id_fk foreign key (xsd_id) references xsd(id),
    constraint xml_contract_id_xsd_id_version_unique unique (contract_id, xsd_id, version)
);

create index xml_contract_id_idx on xml(contract_id);
