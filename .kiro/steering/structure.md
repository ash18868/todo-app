# Project Structure

```
Team4Repository/
├── todoapp/                        # Main application module
│   ├── build.gradle.kts            # Gradle build config (Kotlin DSL)
│   ├── todo.db                     # SQLite database file (auto-created at runtime)
│   ├── frontend/                   # Angular frontend application
│   │   ├── src/
│   │   │   ├── app/
│   │   │   │   ├── auth/           # Auth guard, interceptor, and auth service
│   │   │   │   │   ├── auth.guard.ts
│   │   │   │   │   ├── auth.interceptor.ts
│   │   │   │   │   └── auth.service.ts
│   │   │   │   ├── components/     # Feature components
│   │   │   │   │   ├── dashboard/
│   │   │   │   │   ├── login/
│   │   │   │   │   ├── register/
│   │   │   │   │   ├── todo/
│   │   │   │   │   └── subtask/
│   │   │   │   ├── guards/         # Route guards
│   │   │   │   ├── models/         # TypeScript interfaces (User, Todo, Subtask)
│   │   │   │   ├── services/       # Angular services for HTTP calls
│   │   │   │   ├── app.routes.ts   # Route definitions
│   │   │   │   ├── app.config.ts   # Application config (providers, etc.)
│   │   │   │   └── app.ts          # Root app component
│   │   │   └── index.html
│   │   ├── angular.json
│   │   ├── package.json
│   │   └── tsconfig.json
│   └── src/                        # Spring Boot backend source
│       ├── main/
│       │   ├── java/com/teamtetra/todoapp/
│       │   │   ├── TodoappApplication.java     # Spring Boot entry point
│       │   │   ├── controller/                 # REST controllers
│       │   │   │   ├── UserController.java
│       │   │   │   ├── TodoController.java
│       │   │   │   └── SubtaskController.java
│       │   │   ├── service/                    # Business logic
│       │   │   │   ├── UserService.java
│       │   │   │   ├── TodoService.java
│       │   │   │   └── SubtaskService.java
│       │   │   ├── repo/                       # Spring Data JPA repositories
│       │   │   │   ├── UserRepo.java
│       │   │   │   ├── TodoRepo.java
│       │   │   │   └── SubtaskRepo.java
│       │   │   ├── entity/                     # JPA entity classes
│       │   │   │   ├── User.java
│       │   │   │   ├── Todo.java
│       │   │   │   └── Subtask.java
│       │   │   ├── exception/                  # Custom RuntimeException subclasses
│       │   │   │   ├── RegistrationFailure.java
│       │   │   │   ├── LoginFailure.java
│       │   │   │   ├── AddTodoFailure.java
│       │   │   │   └── AddSubtaskFailure.java
│       │   │   └── utility/                    # Cross-cutting concerns
│       │   │       ├── JwtUtility.java         # JWT generation and validation
│       │   │       ├── AuthInterceptor.java    # Token validation on all protected routes
│       │   │       └── WebConfig.java          # CORS config and interceptor registration
│       │   └── resources/
│       │       └── application.properties
│       └── test/
│           ├── java/com/teamtetra/todoapp/
│           │   ├── TodoappApplicationTests.java
│           │   ├── cucumber/
│           │   │   ├── CucumberRunner.java     # JUnit Platform Suite entry point for Cucumber
│           │   │   └── steps/
│           │   │       └── Steps.java          # Step definitions for .feature files
│           │   └── rest/
│           │       └── RestAssuredTests.java   # Integration tests using REST Assured
│           └── resources/
│               ├── features/
│               │   └── registration.feature   # Cucumber feature files
│               └── test.properties            # Overrides production config (H2 in-memory DB)
```

## Architectural Pattern

Standard Spring layered architecture:

**Controller → Service → Repository → Entity**

- **Controllers** handle HTTP mapping and `@ExceptionHandler` methods for their domain exceptions. They read `userId` from the request attributes set by `AuthInterceptor` (not from the request body). They delegate all logic to the service layer.
- **Services** contain business logic and validation. They are annotated `@Service` and use `@RequiredArgsConstructor` for dependency injection.
- **Repositories** are Spring Data JPA interfaces extending `JpaRepository`. Custom finders use Spring's derived query naming (e.g., `findByUserId`).
- **Entities** are plain JPA classes annotated with `@Entity`, `@Data`, and `@NoArgsConstructor` (Lombok). Primary keys are `Long` fields named `{entity}Id` (e.g., `userId`, `todoId`).
- **Exceptions** are `RuntimeException` subclasses, one per failure domain. They are thrown in services and caught by `@ExceptionHandler` in controllers.
- **Utility** classes handle cross-cutting concerns: JWT logic lives in `JwtUtility`, request interception in `AuthInterceptor`, and MVC configuration (CORS + interceptor registration) in `WebConfig`.

## Backend Conventions

- Package root: `com.teamtetra.todoapp`
- New features follow the same four-layer structure: entity → repo → service → controller
- Each controller handles its own exceptions via `@ExceptionHandler` (no global `@ControllerAdvice` currently)
- Foreign keys between entities are stored as raw `Long` IDs (e.g., `userId` on `Todo`), not as JPA `@ManyToOne` object references
- Lombok `@Data` provides getters, setters, `equals`, `hashCode`, and `toString` on all entities
- `userId` is extracted from the JWT via request attributes (`request.getAttribute("userId")`) — never trust `userId` from the request body on protected endpoints

## Testing Conventions

- **REST Assured tests** live in `test/java/.../rest/` and spin up a real Spring Boot context on a random port (`@SpringBootTest(webEnvironment = RANDOM_PORT)`). They use `test.properties` to swap SQLite for H2.
- **Cucumber tests** use `CucumberRunner` as the JUnit Platform Suite entry point. Feature files live in `test/resources/features/`. Step definitions live in `test/java/.../cucumber/steps/`. HTML reports are generated at `reports/cucumber-report.html`.
- Test properties (`test.properties`) override the production datasource with an H2 in-memory DB using `create-drop` to ensure isolation.

## Frontend Conventions

- Angular app lives in `todoapp/frontend/` — kept separate from the Spring Boot `src/` directory
- Uses **Angular 22 standalone component API** — no NgModules
- Auth-related files (guard, interceptor, service) are co-located in `app/auth/`
- TypeScript interfaces in `models/` mirror backend entity shapes (`User`, `Todo`, `Subtask`)
- HTTP calls are encapsulated in Angular services under `services/` — components never call `HttpClient` directly
- The auth interceptor in `auth/auth.interceptor.ts` attaches the JWT from storage to outgoing requests
- Backend runs on port 8080 during development; configure the API base URL via Angular's `environment.ts` files
