with updated_old as (
     update xml
        set status = 'OLD'::xml_status
      where name = :name
        and contract_id = :contract_id
        and xsd_id = :xsd_id
        and version = :version
  returning id, name, contract_id, xsd_id, version
)
insert into xml
       (name, contract_id, xsd_id, status, version, data, reason)
select uo.name, uo.contract_id, uo.xsd_id, 'PROCESSING'::xml_status, uo.version + 1, cast(:xml_new_data as xml), :reason
  from updated_old uo;