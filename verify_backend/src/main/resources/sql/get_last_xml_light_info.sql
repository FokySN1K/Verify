select xml.id          as id
     , xml.name        as name
     , xml.status      as status
     , xml.reason      as reason
     , xml.version     as version
     , xsd.id          as xsd_id
     , xsd.name        as xsd_name
     , xsd.link        as xsd_link
     , xsd.stage       as xsd_stage
     , xsd.status      as xsd_status
     , con.id          as contractor_id
     , con.client_id   as contractor_client_id
     , con.role        as contractor_role
     , con.email       as contractor_email
     , cust.id         as customer_id
     , cust.client_id  as customer_client_id
     , cust.role       as customer_role
     , cust.email      as customer_email
     , c.id            as contract_id
     , c.name          as contract_name
     , c.description   as contract_description
     , c.status        as contract_status
from xml xml
  join xsd xsd on xsd.id = xml.xsd_id
  join contract c on c.id = xml.contract_id
  left join client con on c.contractor_id = con.id
  left join client cust on c.customer_id = cust.id
 where xml.contract_id = :contract_id
   and xml.xsd_id = :xsd_id
   and xml.name = :name
 order by xml."version" desc
 limit 1
