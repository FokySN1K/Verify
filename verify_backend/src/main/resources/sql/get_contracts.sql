select c.name        as name
     , c.description as description
     , c.status      as status
     , c.reason      as reason
     , con.name      as contractor_name
     , con.surname   as contractor_surname
     , con.email     as contractor_email
     , cust.name     as customer_name
     , cust.surname  as customer_surname
     , cust.email    as customer_email
  from contract c
  left join client con on c.contractor_id = con.id
  left join client cust on c.customer_id = cust.id
 where con.client_id = :client_id
    or cust.client_id = :client_id
