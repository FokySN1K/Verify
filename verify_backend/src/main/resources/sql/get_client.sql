select c.name
     , c.surname
     , c.email
     , c.role
from client c
where c.client_id = :client_id