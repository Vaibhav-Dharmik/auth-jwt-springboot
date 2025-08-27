# Auth JWT Spring Boot Starter

Starter template for a Spring Boot (3.x) REST API with JWT authentication, role-based authorization, modular architecture, multi-database support (JPA + Mongo), and OpenAPI documentation.

## Features

- User registration & login (JWT)
- OAuth2 social login (Google & GitHub) mapped into local user store
- Role-based access control (ADMIN / USER) with `@PreAuthorize`
- Stateless security, BCrypt password hashing
- Global exception handling
- DTO validation (Jakarta Validation)
- Swagger UI (springdoc-openapi)
- Profiles: `dev` and `prod`
- Config via environment variables / `.env`
- Supports SQL (MySQL/PostgreSQL/H2) & MongoDB
- Layered architecture (Controller, Service, Repository, Entity, DTO, Mapper)
- Unit test dependencies (JUnit, Mockito, Spring Security Test)

## Quick Start

```bash
mvn clean install
SPRING_PROFILES_ACTIVE=dev mvn spring-boot:run
```

Windows PowerShell:

```powershell
$env:SPRING_PROFILES_ACTIVE="dev"; mvn spring-boot:run
```

Swagger UI: http://localhost:8080/swagger-ui/index.html

## Environment Variables

| Variable    | Description               | Default (dev)                    |
| ----------- | ------------------------- | -------------------------------- |
| DB_URL      | JDBC URL                  | jdbc:h2:mem:testdb               |
| DB_USERNAME | DB user                   | sa                               |
| DB_PASSWORD | DB password               | (empty)                          |
| MONGO_URI   | Mongo connection          | mongodb://localhost:27017/authdb |
| JWT_SECRET  | Base64 secret (>=256-bit) | dev sample provided              |
| PORT        | Server port (prod)        | 8080                             |

Generate a secure secret:

```bash
openssl rand -base64 48
```

## API Endpoints

Public:

- POST `/api/auth/register`
- POST `/api/auth/login`
- GET `/oauth2/authorization/google` (Browser redirect)
- GET `/oauth2/authorization/github` (Browser redirect)

Protected:

- GET `/api/dashboard`
- GET `/api/profile`
- GET `/api/admin/metrics` (ADMIN only)
- POST `/api/auth/logout` (stateless hint: client just discards token)

## Adding a New Role

1. Insert new role row in `roles` table or via repository.
2. Assign to user (update service / admin endpoint).
3. Use `@PreAuthorize("hasRole('NEWROLE')")` on methods.

## Testing

```bash
mvn test
```

## Project Structure

```
entity/      # JPA entities
repository/  # Spring Data repositories
service/     # Business logic
controller/  # REST controllers
security/    # JWT filter & user details
config/      # Security & OpenAPI configs
dto/         # Request/response DTOs
mapper/      # Mapping utilities
exception/   # Global exception handler
```

## Extensibility

- Add new entity + repository
- Create DTO + mapper if needed
- Add service interface & implementation
- Expose via controller
- Secure with annotations / config
