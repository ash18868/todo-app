# Tech Stack

## Backend

- Java 21
- Spring Boot 4.1.0
- Spring Web MVC
- Spring Data JPA and Hibernate
- SQLite through `sqlite-jdbc`
- Hibernate Community Dialects for `SQLiteDialect`
- Spring Validation and Jakarta Bean Validation
- Spring Security Crypto with BCrypt password hashing
- JJWT for signed authentication tokens
- Spring Boot Actuator for health checks
- Lombok
- Gradle with Kotlin DSL

### Backend Security

- `AuthService` handles registration, BCrypt hashing, login, and user extraction.
- `JwtUtility` signs and validates JWTs using `JWT_SECRET`, which must never be
  committed.
- `AuthInterceptor` validates bearer tokens and attaches authenticated identity
  to each protected request.
- Todo and subtask services use ownership-aware repository methods.
- Controllers accept validated request DTOs rather than binding request bodies
  directly to persistence entities.
- CORS origins can be configured using `CORS_ALLOWED_ORIGINS`.

### Database

- Local and demo deployments use SQLite.
- The datasource defaults to `jdbc:sqlite:todo.db?foreign_keys=on`.
- Hibernate currently uses `ddl-auto=update`.
- The EC2 Docker deployment intentionally has no database volume, so data may be
  erased when the backend container is replaced.
- SQLite is an intentional choice for this small, single-instance portfolio
  demo; migration to a managed database is not currently planned.

## Frontend

- Angular 22 standalone components
- TypeScript 6
- RxJS
- Angular HttpClient, reactive forms, router guards, and interceptors
- Vitest through Angular's unit-test builder
- npm lockfile installs via `npm ci` in Docker
- Production API base URL: `/api`
- Development API base URL: `http://localhost:8080`

The production Angular environment file is committed because `/api` is public
browser configuration, not a secret. Secrets must never be placed in Angular
environment files because their values are compiled into browser JavaScript.

## Testing

- JUnit 5
- Spring Boot JPA and MVC test starters
- H2 for isolated tests
- REST Assured integration tests
- Cucumber acceptance tests
- Selenium browser tests
- Angular/Vitest unit tests

Current validation baseline:

- Backend suite passes after a forced Gradle rebuild.
- Frontend suite passes all 12 tests.
- Angular production build passes.
- Both Docker images build.
- Docker smoke test verifies the frontend, proxied health endpoint, and
  registration/JWT flow.
- Production npm dependencies have no known audit findings.

## Deployment

- AWS EC2, currently using a small burstable instance
- Docker Engine and Docker Compose
- Backend image: multi-stage Eclipse Temurin Java 21 build/runtime
- Frontend image: multi-stage Node build and nginx runtime
- nginx serves the Angular SPA and proxies `/api` to `backend:8080`
- Public demo: http://18.220.129.235/
- Transport is currently HTTP; HTTPS is planned.
- Jenkins is planned but not installed yet.

## Configuration

Required EC2/repository-root `.env` value:

```dotenv
JWT_SECRET=<strong-random-secret>
```

The `.env` file is ignored and must never be committed. The committed Angular
`environment.ts` contains only the public relative API path.

## Common Commands

### Backend (`todoapp/`)

```bash
./gradlew test
./gradlew bootRun
./gradlew bootJar
```

### Frontend (`todoapp/frontend/`)

```bash
npm ci
npm test -- --watch=false
npm run build
npm start
```

### Docker (repository root)

```bash
docker compose up -d --build
docker compose ps
docker compose logs --tail=100 backend frontend
docker compose down
```

Health check through the frontend proxy:

```bash
curl http://127.0.0.1/api/actuator/health
```

If the frontend is mapped to host port 4200 locally, use
`http://127.0.0.1:4200/api/actuator/health`.
