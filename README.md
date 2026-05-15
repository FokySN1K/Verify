
# Verify

Сервис автоматизированной валидации XML-файлов по XSD-схемам.

## 🚀 Быстрый старт

```bash
# 1. Настройка окружения
cp .env.example .env

# 2. Запуск всех сервисов
docker compose up -d
```

После запуска сервисы доступны по адресам:
- 🌐 **Frontend**: http://localhost:13000
- 🔌 **Backend API**: http://localhost:8080
- 📚 **Swagger UI**: http://localhost:8080/swagger-ui.html
- 🐘 **PostgreSQL**: localhost:5432

## 🔌 API Endpoints

| Метод | Эндпоинт | Описание |
|-------|----------|----------|
| `POST` | `/validate_with_xsd_data` | Валидация: XML и XSD передаются в теле запроса (JSON) |
| `POST` | `/validate_with_xsd_files` | Валидация: загрузка файлов XML и XSD (multipart/form-data) |
| `POST` | `/create_client` | Регистрация нового клиента |
| `POST` | `/get_xsd_light_list` | Получение списка доступных XSD-схем |

### Пример запроса `/validate_with_xsd_data`

```json
{
  "xml": "<root><field>value</field></root>",
  "xsd": "<xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\">...</xs:schema>"
}
```

### Пример ответа

```json
{
  "isValid": false,
  "errors": [
    {
      "line": 3,
      "column": 12,
      "message": "cvc-type.3.1.3: The value 'value' of element 'field' is not valid."
    }
  ]
}
```

## ⚙️ Переменные окружения

Файл `.env` (на основе `.env.example`):

```env
# Database
POSTGRES_DB=verify
POSTGRES_USER=verify
POSTGRES_PASSWORD=verify
POSTGRES_HOST=db
POSTGRES_PORT=5432

# Backend
APP_PORT=8080
JAVA_VERSION=25

# Frontend
FRONTEND_PORT=13000
VITE_API_BASE_URL=http://localhost:8080
```

## 🛠️ Разработка и отладка

```bash
# Пересборка и перезапуск контейнеров
docker compose up -d --build

# Просмотр логов
docker compose logs -f backend
docker compose logs -f frontend

# Остановка проекта
docker compose down

# Полная очистка (с удалением томов)
docker compose down -v
```

## 📦 Технологический стек

| Компонент | Технологии |
|-----------|-----------|
| **Backend** | Spring Boot 4.0.5, Java 25, PostgreSQL, Liquibase |
| **Frontend** | React 18, Vite, TailwindCSS 4 |
| **Валидация** | `javax.xml.validation` (W3C XML Schema) |
| **Инфраструктура** | Docker Compose, Maven |

## ⚠️ Важно

- Валидатор поддерживает только стандарт **W3C XML Schema**.
- Ошибки валидации возвращаются с указанием строки, колонки и описания проблемы.
- Для корректной работы фронтенда убедитесь, что `VITE_API_BASE_URL` в `.env` указывает на доступный адрес бэкенда.
