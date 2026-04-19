select xsd.id
     , xsd.name
     , xsd.stage
     , xsd.begin_date
     , xsd.end_date
     , xsd.status
     , xsd.link
     , xsd.version
  from xsd
 where xsd.status in ('OLD', 'PROCESSING')
