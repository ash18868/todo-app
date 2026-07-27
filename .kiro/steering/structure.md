# Project Structure

```text
.
|-- .kiro/steering/                 # Product, structure, and technology guidance
|-- docker-compose.yml              # Local/EC2 frontend and backend orchestration
|-- README.md
`-- todoapp/
    |-- Dockerfile                  # Spring Boot multi-stage image
    |-- build.gradle.kts
    |-- src/
    |   |-- main/
    |   |   |-- java/com/teamtetra/todoapp/
    |   |   |   |-- controller/    # Auth, todo, and nested subtask REST endpoints
    |   |   |   |-- dto/           # Validated request and response records
    |   |   |   |-- entity/        # User, Todo, and Subtask JPA entities
    |   |   |   |-- exception/     # Domain and global exception handling
    |   |   |   |-- repo/          # Ownership-aware Spring Data repositories
    |   |   |   |-- service/       # Authentication and business logic
    |   |   |   `-- utility/       # JWT, interceptor, CORS, and password encoder
    |   |   `-- resources/
    |   |       `-- application.properties
    |   `-- test/                   # JUnit, REST Assured, Cucumber, and test config
    `-- frontend/
        |-- Dockerfile              # Node build and nginx runtime image
        |-- nginx.conf.template     # SPA fallback and /api reverse proxy
        |-- src/
        |   |-- app/
        |   |   |-- auth/           # Auth service, guard, and HTTP interceptor
        |   |   |-- components/     # Login, register, dashboard, todo, subtask
        |   |   |-- guards/
        |   |   |-- models/
        |   |   `-- services/
        |   `-- environments/
        |       |-- environment.ts  # Committed public production config (/api)
        |       `-- environment.development.ts
        |-- angular.json
        |-- package.json
        `-- package-lock.json
```

## Backend Architecture

The backend follows a layered flow:

```text
Controller -> Service -> Repository -> Entity
```

- Controllers map HTTP requests, validate DTOs, and delegate business rules.
- Services obtain the authenticated user from `AuthService`, enforce ownership,
  and map entities to response DTOs.
- Repositories extend `JpaRepository` and use derived ownership-aware queries,
  such as todo ID plus owning user ID.
- Entities use `@ManyToOne` relationships: todos belong to users, and subtasks
  belong to todos.
- `GlobalExceptionHandler` handles validation failures, while domain handlers
  return application-specific failures.

## API Conventions

- Public authentication routes are `/register` and `/login`.
- Todo collection routes use `/todo`.
- Resource mutation routes identify the resource in the path:
  `/todo/{todoId}`.
- Subtasks are nested beneath their parent:
  `/todo/{todoId}/subtask/{subtaskId}`.
- Request bodies use DTOs and never determine resource ownership.
- JWT-derived identity is the source of authorization.

## Frontend Architecture

- Angular uses standalone components and signals where appropriate.
- HTTP behavior is encapsulated in services.
- The auth interceptor adds bearer tokens.
- The production API URL is the same-origin `/api` path.
- nginx serves `index.html` for Angular client-side routes.
- nginx removes the `/api` prefix and forwards API traffic to Spring Boot.

## Deployment Structure

Docker Compose runs:

1. `frontend`: nginx serving Angular and proxying `/api`.
2. `backend`: Spring Boot with an ephemeral SQLite database.

The public EC2 deployment is available at http://18.220.129.235/. It currently
uses HTTP, so visitors must not submit passwords reused on other services.

Planned deployment work:

- Add a stable domain and HTTPS.
- Bind internal application ports so only the TLS reverse proxy is public.
- Add Jenkins later for test/build/deploy automation.

## Testing Conventions

- Backend integration tests use a random Spring Boot port and H2.
- Cucumber feature files live under `src/test/resources/features`.
- Selenium-backed browser scenarios require an available browser environment.
- Frontend unit tests run once in CI-style mode with
  `npm test -- --watch=false`.
- Deployment verification should check `/api/actuator/health` and one
  authentication flow after containers start.
