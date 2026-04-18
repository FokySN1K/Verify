update xml
   set status = :status
 where name = :name
   and contract_id = :contract_id
   and xsd_id = :xsd_id
   and version = :version
   and status in ('PROCESSING', 'CHECKING')
