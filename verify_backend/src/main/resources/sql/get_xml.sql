select x.id,
       x.name,
       x.status,
       x.version,
       x.reason,
       x.contract_id,
       x.xsd_id,
       x.created_ts,
       x.data,
       c.name         as contract_name,
       c.description  as contract_description,
       c.status       as contract_status,
       c.reason       as contract_reason,
       xsd.stage      as xsd_stage,
       xsd.status     as xsd_staatus,
       xsd.version    as xsd_version,
       xsd.name       as xsd_name,
       xsd.begin_date as xsd_begin_date,
       xsd.end_date   as xsd_end_date,
       xsd.link       as xsd_link
from xml x
         left join contract c on c.id = x.contract_id
         left join xsd on xsd.id = x.xsd_id
         left join client con on con.id = c.customer_id
where x.contract_id = :contractId
  and con.client_id = :clientId
  and x.id = :id