update contract
set contractor_id = c.id
  , status = 'PROCESSING'
    from client c
where contract.id = :contract_id
  and contract.contractor_id is null
  and contract.status = 'NEW'
  and c.client_id = :client_id
  and c.role = 'CONTRACTOR'
