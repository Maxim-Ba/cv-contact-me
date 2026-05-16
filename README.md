# cv-contact-me

Микросервис "Написать мне" для CV-сайта. Spring Boot 2.7 / Java 8.

## Что делает

- `POST /api/contact` — принимает имя, email и сообщение
- Отправляет уведомление в **Telegram** (long polling бот)
- При ошибке Telegram — **SMTP fallback** (Яндекс Почта или другой провайдер)
- Сохраняет каждую заявку в **PostgreSQL** (таблица `feedback`)

## Стек

- Java 8 + Spring Boot 2.7.18
- Spring Data JPA + Flyway + PostgreSQL
- Spring Mail (SMTP)
- [TelegramBots 6.9.7.1](https://github.com/rubenlagus/TelegramBots) (long polling)

## Быстрый старт

### 1. Получить Telegram credentials

1. Создать бота через `@BotFather` → получить `TELEGRAM_BOT_TOKEN`
2. Написать боту `/chatid` или любое сообщение, затем проверить:  
   `https://api.telegram.org/bot<TOKEN>/getUpdates`  
   Найти `result[0].message.chat.id` — это твой `TELEGRAM_CHAT_ID`

### 2. Настроить env

```bash
cp .env.example .env
# Заполнить переменные в .env
```

### 3. Запуск через Docker

```bash
docker-compose up -d
```

Сервис доступен на `http://localhost:8090`.

### 4. Локальный запуск

```bash
# Нужна PostgreSQL, создать базу cv_contact
mvn spring-boot:run
```

## API

### POST /api/contact

```json
{
  "name": "Иван",
  "email": "ivan@example.com",
  "message": "Привет!"
}
```

**Ответ 200:**
```json
{ "ok": true }
```

**Ответ 400 (невалидные данные):**
```json
{ "ok": false, "error": "Email обязателен" }
```

**Ответ 500 (оба канала недоступны):**
```json
{ "ok": false, "error": "Не удалось отправить сообщение. Попробуйте позже." }
```

## Env vars

| Переменная | Описание | По умолчанию |
|---|---|---|
| `TELEGRAM_BOT_TOKEN` | Токен от @BotFather | — |
| `TELEGRAM_BOT_USERNAME` | Username бота | `CvContactBot` |
| `TELEGRAM_CHAT_ID` | Твой chat_id | — |
| `SMTP_HOST` | SMTP хост | `smtp.yandex.ru` |
| `SMTP_PORT` | SMTP порт | `587` |
| `SMTP_USER` | SMTP логин | — |
| `SMTP_PASSWORD` | Пароль приложения | — |
| `SMTP_TO` | Получатель письма | — |
| `DB_URL` | JDBC URL | `jdbc:postgresql://localhost:5432/cv_contact` |
| `DB_USER` | Пользователь БД | `postgres` |
| `DB_PASSWORD` | Пароль БД | `secret` |
| `CORS_ALLOWED_ORIGINS` | Разрешённые origins (через запятую) | `http://localhost:4200` |
| `SERVER_PORT` | Порт сервиса | `8090` |
