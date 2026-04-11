select x.id,
       x.name,
       x.status,
       x.version,
       x.reason,
       x.contract_id,
       x.xsd_id,
       x.created_ts,
       null          as data    ,
       c.name        as contract_name,
       c.description as contract_description,
       c.status      as contract_status,
       c.reason      as contract_reason
from xml x
         left join contract c on c.id = x.contract_id
         left join client con on con.id = c.customer_id
where x.contract_id = :contractId
  and con.client_id = :clientId