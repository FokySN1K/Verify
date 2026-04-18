update contract
   set contractor_id = :contractor_id
     , status = 'PROCESSING'
 where id = :contract_id
   and contractor_id is null
   and status = 'NEW'
