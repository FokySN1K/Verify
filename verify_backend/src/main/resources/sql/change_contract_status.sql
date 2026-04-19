update contract
   set status = :status::contract_status
 where id = :contract_id
   and status in ('NEW', 'PROCESSING')
