# Tech Stack

## Terminal

- **Shell: Git Bash** — all commands should use Bash syntax (e.g., `./gradlew`, not `gradlew.bat`)

---

## Backend

### Language & Runtime
- Java 21
- Spring Boot 4.1.0

### Frameworks & Libraries
- **Spring Data JPA** — ORM and repository layer
- **Spring Web MVC** — REST controllers
- **Hibernate** — JPA provider; uses `update` DDL strategy in production (schema is migrated, data persists between runs) and `create-drop` in tests
- **SQLite** via `org.xerial:sqlite-jdbc:3.53.2.0` — file-based database (`todo.db` in project root)
- **Hibernate Community Dialects** `8.0.0.Alpha1` — provides `SQLiteDialect`
- **Lombok** — reduces boilerplate (`@Data`, `@NoArgsConstructor`, `@RequiredArgsConstructor`)
- **JJWT** `0.13.0` — JWT generation, validation, and claim extraction (fully implemented)

### Security
- **`JwtUtility`** — generates and validates signed JWTs (HMAC-SHA); tokens include `userId` (subject) and `username` claim; expire after 24 hours
- **`AuthInterceptor`** — `HandlerInterceptor` that validates the `Authorization: Bearer <token>` header on every request; attaches `userId` and `username` as request attributes for controllers to use
- **`WebConfig`** — registers the interceptor (excludes `/register` and `/login`) and configures CORS to allow `http://localhost:4200`

### Test Dependencies
- **JUnit 5** (via `junit-platform-launcher` and `junit-platform-suite`)
- **Spring Boot test starters** for JPA and Web MVC
- **H2** `2.4.240` — in-memory database used in tests (overrides SQLite via `test.properties`)
- **Cucumber** `7.33.0` — BDD acceptance tests (`cucumber-java`, `cucumber-junit-platform-engine`)
- **REST Assured** `6.0.0` — integration tests against the running Spring Boot server

### Build System
- **Gradle** with Kotlin DSL (`build.gradle.kts`)
- Group: `com.teamtetra`, artifact: `todoapp`

### Database
- SQLite file at `todoapp/todo.db`
- `spring.jpa.hibernate.ddl-auto=update` in production — schema is migrated on startup, data persists between runs
- `spring.jpa.hibernate.ddl-auto=create-drop` in tests — tables are rebuilt fresh for each test run
- SQL logging is enabled (`show-sql=true`, `format_sql=true`)

---

## Frontend

### Language & Runtime
- TypeScript (`~6.0.2`)
- Node.js with npm `10.9.0`

### Frameworks & Libraries
- **Angular 22** — component-based SPA framework (standalone component API)
- **Angular CLI 22** — project scaffolding, build, and dev server
- **RxJS** `~7.8.0` — reactive HTTP and state management
- **Angular HttpClient** — HTTP calls to the Spring Boot API
- **Vitest** `^4.0.8` — frontend unit test runner
- **Prettier** `^3.8.1` — code formatter

### Frontend Location
- Source lives in `todoapp/frontend/` — sibling to the backend `src/` directory

---

## Common Commands

> **Run all commands in Git Bash.**

### Backend — run from `todoapp/`

```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun

# Run backend tests
./gradlew test

# Clean build outputs
./gradlew clean
```

### Frontend — run from `todoapp/frontend/`

```bash
# Install dependencies
npm install

# Start dev server (default: http://localhost:4200)
ng serve

# Build for production
ng build

# Run frontend tests (single run, no watch)
npx vitest run
```

> During development the Angular dev server runs on port 4200 and the Spring Boot API runs on port 8080. Configure the API base URL in `src/environments/environment.ts`.
