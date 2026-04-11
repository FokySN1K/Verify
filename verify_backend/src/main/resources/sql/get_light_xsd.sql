select xsd.id,
       xsd.stage,
       xsd.name,
       xsd.begin_date,
       xsd.end_date,
       null as data,
       xsd.status,
       xsd.link,
       xsd.version
from xsd