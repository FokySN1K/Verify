create table contract
(
    id int8 generated always as identity,
    customer_id int8,
    contractor_id int8,
    name  varchar(100),
    description varchar(500),
    status contract_status,
    reason varchar(500),
    --
    constraint contract_id_pk primary key (id),
    constraint contract_customer_id_fk foreign key (customer_id) references client(id),
    constraint contract_contractor_id_fk foreign key (contractor_id) references client(id),
    constraint contract_customer_id_name_unique unique (customer_id, name)
);

create index contract_customer_id_idx on contract(customer_id);
create index contract_contractor_id_idx on contract(contractor_id);
