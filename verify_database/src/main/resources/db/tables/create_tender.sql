create table tender
(
    id int8 generated always as identity,
    contract_id int8,
    contractor_id int8,
    --
    constraint tender_id_pk primary key (id),
    constraint tender_contract_id_fk foreign key (contract_id) references contract(id),
    constraint tender_contractor_id_fk foreign key (contractor_id) references client(id)
);


-- create index
create unique index tender_contract_id_contractor_id_idx on tender(contract_id, contractor_id);


-- comment
comment on table tender is 'Таблица с информацией по тендеру';

comment on column tender.contract_id is 'Идентификатор заказа, переданного на тендер';
comment on column tender.contractor_id is 'Идентификатор подрядчика, согласившегося участвовать в тенлере';
