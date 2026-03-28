insert into contract
    (name, description, status, customer_id)
    select :name
         , :description
         , 'NEW'::contract_status
         , c.id
      from client c
     where c.client_id = :client_id
