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
    constraint xml_contract_id_xsd_id_version_unique unique (name, contract_id, xsd_id, version)
);

create index xml_contract_id_idx on xml(contract_id);

COMMENT ON TABLE xml IS 'Хранилище XML-документов и результатов валидации';
COMMENT ON COLUMN xml.id IS 'Уникальный идентификатор XML-документа';
COMMENT ON COLUMN xml.name IS 'Имя/название документа';
COMMENT ON COLUMN xml.contract_id IS 'Привязка к контракту';
COMMENT ON COLUMN xml.xsd_id IS 'Привязка к схеме валидации (XSD)';
COMMENT ON COLUMN xml.status IS 'Статус валидации (enum: xml_status)';
COMMENT ON COLUMN xml.version IS 'Версия документа';
COMMENT ON COLUMN xml.data IS 'Содержимое XML-документа';
COMMENT ON COLUMN xml.reason IS 'Причина ошибки или комментарий к статусу';
COMMENT ON COLUMN xml.created_ts IS 'Дата и время создания записи';