with old_xml as (
    select name
         , contract_id
         , xsd_id
         , reason
         , version
      from xml
     where name = :name
       and contract_id = :contract_id
       and xsd_id = :xsd_id
       and version = :version
),
updated_old AS (
    update xml
       set status = 'OLD'::xml_status
      from old_xml
      returning id
),
new_version AS (
    insert into xml (
                name
              , contract_id
              , xsd_id
              , status
              , version
              , data
              , reason)
         select ox.name
              , ox.contract_id
              , ox.xsd_id
              , 'PROCESSING'::xml_status
              , ox.version + 1
              , :data
              , :reason
           from old_xml ox
      returning id
)
select id
  from updated_old
 union all
select id
  from new_version
