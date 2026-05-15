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

COMMENT ON TABLE xsd IS 'Реестр XSD-схем для валидации';
COMMENT ON COLUMN xsd.id IS 'Уникальный идентификатор схемы';
COMMENT ON COLUMN xsd.stage IS 'Этап/стадия использования схемы';
COMMENT ON COLUMN xsd.name IS 'Название схемы';
COMMENT ON COLUMN xsd.begin_date IS 'Дата начала действия схемы';
COMMENT ON COLUMN xsd.end_date IS 'Дата окончания действия схемы';
COMMENT ON COLUMN xsd.data IS 'Тело XSD-схемы';
COMMENT ON COLUMN xsd.status IS 'Статус схемы (enum: xsd_status)';
COMMENT ON COLUMN xsd.link IS 'Ссылка на источник схемы';
COMMENT ON COLUMN xsd.version IS 'Номер версии схемы';