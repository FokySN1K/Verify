create type xml_status as enum
    (
    'NEW',
    'OLD',
    'PROCESSING',
    'CHECKING',
    'DONE'
);