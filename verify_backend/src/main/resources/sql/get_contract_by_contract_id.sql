select c.id          as id
     , c.name        as name
     , c.description as description
     , c.status      as status
     , c.reason      as reason
     , con.id        as contractor_id
     , con.name      as contractor_name
     , con.client_id as contractor_client_id
     , con.surname   as contractor_surname
     , con.email     as contractor_email
     , cust.id       as customer_id
     , cust.name     as customer_name
     , cust.client_id as customer_client_id
     , cust.surname  as customer_surname
     , cust.email    as customer_email
  from contract c
 left join client con on c.contractor_id = con.id
 left join client cust on c.customer_id = cust.id
where c.id = :contract_id
