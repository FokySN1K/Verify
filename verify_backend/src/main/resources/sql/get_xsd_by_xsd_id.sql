select xsd.id
     , xsd.name
     , xsd.data
     , xsd.stage
     , xsd.begin_date
     , xsd.end_date
     , xsd.status
     , xsd.link
     , xsd.version
from xsd
where xsd.id = :xsd_id
