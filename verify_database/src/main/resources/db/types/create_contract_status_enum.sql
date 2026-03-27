create type contract_status as enum
    (
    'NEW',
    'TENDER',
    'PROCESSING',
    'FAILED',
    'DONE'
);