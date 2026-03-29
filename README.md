
# Документация к API

## Общая информация

API предназначено для управления проектами, заказами, XML-документами, XSD-схемами и пользователями (customer / contractor / admin).  
Базовый адрес API: /api  
WebSocket для событий и обновлений: /ws  

Конфигурация клиента:
```js
const API_CONFIG = {
  baseUrl: 'http://localhost:8080/api',
  endpoints: {
    projects: '/projects',
    orders: '/orders',
    documents: '/documents',
    employees: '/employees',
    auditLogs: '/audit-logs',
    upload: '/documents/upload'
  },
  wsUrl: 'ws://localhost:8080/ws'
};
````

Роли:

* **ADMIN** — управление XSD схемами
* **CUSTOMER** — создание и управление заказами
* **CONTRACTOR** — работа с XML и участие в тендерах

---

## Основные API группы

### Проекты и системные данные

* `GET /api/projects` — получить список проектов
* `POST /api/projects` — создать проект
* `GET /api/employees` — получить список сотрудников
* `GET /api/audit-logs` — получить аудит-лог

---

### Документы

* `GET /api/documents` — получить список документов (возможны фильтры)
* `GET /api/documents/{id}` — получить документ по ID
* `POST /api/documents/upload` — загрузить новый документ
* `POST /api/documents/{id}/versions` — создать новую версию документа
* `GET /api/documents/{id}/download` — скачать документ

---

### Клиенты и заказы

* `POST /create_client` — создание клиента (client_id, role, name, surname, email; проверяется уникальность email)
* `GET /get_orders` — получить все заказы клиента (client_id)
* `POST /customer/order/add_new_order` — создать заказ (client_id, name, description)
* `POST /customer/change_order_status` — изменить статус заказа (client_id, order_id, status)
* `POST /customer/order/send_to_tender` — отправить заказ в тендер (client_id, order_id)
* `GET /customer/order/get_tender_contractor` — получить список подрядчиков по заказу (client_id, order_id)
* `POST /customer/order/choose_contractor` — выбрать подрядчика (customer_id, order_id, contractor_id; заказ переходит в PROCESSING)

Статусы заказов:

* PROCESSING
* DONE
* FAILED
* TENDER

---

### XML документы

* `GET /contract/get_xml` — получить список XML по заказу (client_id, order_id)
* `GET /contract/xml/download` — скачать XML (client_id, order_id, xml_id)
* `GET /contract/xml/get_xml_info` — получить информацию о XML (client_id, order_id, xml_id)
* `POST /contract/xml/change_xml_status` — изменить статус XML (client_id, order_id, xml_id, next_status, reason optional)

Работа подрядчика:

* `POST /contractor/order/add_new_xml_list` — добавить список XML (client_id, order_id, xsd_id_list, xml_name_list)
* `POST /contractor/order/xml/add_new_version` — добавить новую версию XML (client_id, order_id, xml_id, data; выполняется валидация по XSD)

Статусы XML:

* PROCESSING
* CHECKING
* DONE
* REFUSED

---

### XSD схемы (администратор)

* `GET /get_light_xsd_info` — получить список XSD (имя, версия, описание)
* `POST /admin/add_new_xsd` — добавить XSD (name, begin_date, end_date, data)
* `POST /admin/delete_new_xsd` — удалить XSD (xsd_id; только если статус NEW)
* `POST /admin/activate_xsd` — активировать XSD (xsd_id; перевод в PROCESSING, предыдущие версии становятся OLD)
* `POST /admin/add_new_version_xsd` — создать новую версию XSD (xsd_id, data, begin_date)

Статусы XSD:

* NEW
* PROCESSING
* OLD

---

### Валидация

* `POST /validate_wth_xsd_id` — валидация XML по XSD ID (xml, xsd_id; проверяется существование схемы)
* `POST /validate_with_xsd_data` — валидация XML по переданной схеме (xml_data, xsd_data)

---

### Тендеры

* `GET /tender/get_orders` — получить заказы в статусе TENDER
* `POST /tender/subscribe_to_order` — подписаться на заказ (contractor_id, order_id; доступно только для CONTRACTOR)

---

## Жизненные циклы

* Заказ: `NEW → TENDER → PROCESSING → DONE → FAILED`
* XML: `PROCESSING → CHECKING → DONE → REFUSED`
