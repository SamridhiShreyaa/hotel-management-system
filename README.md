# Hotel Management System

Spring Boot mini project for hotel operations covering guest auth, room search, reservation lifecycle, payments, and admin management.

## Tech Stack

- Java 17
- Spring Boot 3
- Spring MVC + Spring Security
- Spring Data JPA + Hibernate
- MySQL
- Thymeleaf
- Maven

## Project Modules

- auth: registration, login, room search
- reservation: create, modify, cancel, history
- payment: payment flow and logs
- admin: dashboard, room and reservation management, reports

## Team Ownership

- Samridhi: auth and room search
- Risu: reservation lifecycle
- Sanjana: payment processing
- Shreehari: admin panel and reports

## Local Setup

1. Create MySQL database named hotel_db.
2. Execute schema.sql to create tables and seed sample data.
3. Configure src/main/resources/application.properties locally.
4. Run using Maven:

```bash
mvn spring-boot:run
```

## Notes

- application.properties is environment-specific and should not be committed with credentials.
- This commit initializes repository scaffolding only; feature implementation is pending.
