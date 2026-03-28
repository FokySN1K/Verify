create type xml_status as enum
(
    'NEW',
    'OLD',
    'REFUSED',
    'PROCESSING',
    'CHECKING',
    'DONE'
);