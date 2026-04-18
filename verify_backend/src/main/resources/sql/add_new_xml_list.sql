insert into xml
       (name, contract_id, xsd_id, status, version, reason)
select (:name, :contract_id, :xsd_id, 'NEW', 0, 'Инициализациия xml документа')
