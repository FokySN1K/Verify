select c.id -- Плохо, что приходится вытаскивать внутренние идентификаторы
     , c.client_id
     , c.name
     , c.surname
     , c.email
     , c.role
from client c
where c.client_id = :client_id