update contract
   set status = :status
 where id = :contract_id
   and status = 'PROCESSING'
