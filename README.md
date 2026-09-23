# Currency Exchange Service

An application for the practical **"Currency Exchange"** project on the [Skillbox](https://skillbox.ru) educational platform.

## Technologies Used

- Spring Boot 2.7
- Maven 3
- Lombok
- MapStruct
- Liquibase
- PostgreSQL

## Requirements

### JDK 17

The project uses Java 17 syntax. To run the application locally, you need to have **JDK 17** installed.

### Docker

Docker must be installed and running.

The PostgreSQL database is run in a Docker container.

### Internet Connection

An internet connection is required to retrieve current exchange rates.

## Useful Commands

### Start the PostgreSQL Database Container

```bash
docker run -p 5432:5432 --name postgres -e POSTGRES_PASSWORD=postgres -d postgres
````

The username for connecting to the PostgreSQL container is: `postgres`.

### IntelliJ IDEA

Run the main method in the Application class.

### API Requests

Create new currency

```bash
curl --request POST \
  --url http://localhost:8080/api/currency/create \
  --header 'Content-Type: application/json' \
  --data '{
  "name": "Gotham City Dollar",
  "nominal": 3,
  "value": 32.2,
  "isoNumCode": 1337
}'
```

Get currency by ID

```bash
curl --request GET \
  --url http://localhost:8080/api/currency/1333
```

Convert Currency by Numeric Code

```bash
curl --request GET \
--url http://localhost:8080/api/currency/convert?value=100&numCode=840
```