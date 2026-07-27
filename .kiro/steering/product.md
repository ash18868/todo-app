# Product Overview

Todo App is a full-stack task management demo built with a Spring Boot REST API
and an Angular single-page application. Users can register, log in, and manage a
private collection of todos and nested subtasks.

## Current Status

- Public demo: http://18.220.129.235/
- Hosted on a single Amazon EC2 instance with Docker Compose.
- The Angular production bundle is served by nginx, which forwards `/api`
  requests to the Spring Boot container.
- The deployment currently uses HTTP rather than HTTPS. Visitors must use a
  unique temporary password that they do not use anywhere else.
- SQLite data is intentionally ephemeral. Rebuilding or replacing the backend
  container may reset accounts and todos.
- HTTPS and Jenkins-based CI/CD are planned improvements, but are intentionally
  deferred.

## Core Features

- Registration and login with BCrypt password hashing and JWT authentication
- Todos scoped to the authenticated user
- Nested subtask creation, viewing, editing, completion, and deletion
- Angular route guards and an HTTP interceptor for authenticated requests
- DTO-based API input/output with Jakarta Bean Validation
- Ownership-aware repository queries that prevent cross-user data access

## Authentication

- Registration and login return an `AuthResponse` containing a signed JWT.
- The frontend stores the JWT and sends it as `Authorization: Bearer <token>`.
- The signing key is supplied through the uncommitted `JWT_SECRET` environment
  variable.
- All application endpoints except `/register`, `/login`, `/error`, and health
  checks require a valid token.
- Tokens contain the user ID as the subject and the username as a claim and
  expire after 24 hours.

## API Endpoints

### Public

| Method | Path | Description |
| --- | --- | --- |
| POST | `/register` | Register and return an authentication token |
| POST | `/login` | Log in and return an authentication token |
| GET | `/actuator/health` | Application health check |

### Todos

| Method | Path | Description |
| --- | --- | --- |
| POST | `/todo` | Create a todo for the current user |
| GET | `/todo` | Get the current user's todos |
| PUT | `/todo/{todoId}` | Update an owned todo |
| DELETE | `/todo/{todoId}` | Delete an owned todo |

### Subtasks

| Method | Path | Description |
| --- | --- | --- |
| POST | `/todo/{todoId}/subtask` | Create a subtask under an owned todo |
| GET | `/todo/{todoId}/subtask` | Get an owned todo's subtasks |
| PUT | `/todo/{todoId}/subtask/{subtaskId}` | Update an owned subtask |
| DELETE | `/todo/{todoId}/subtask/{subtaskId}` | Delete an owned subtask |

## Business Rules

- Usernames are unique and validated by the registration DTO/service.
- Passwords are validated and stored only as BCrypt hashes.
- A todo title is unique per user.
- A subtask title is unique within its parent todo.
- Protected operations derive the user ID from the validated JWT and verify
  ownership; client-provided resource IDs alone never authorize access.

## Roadmap

1. Add a domain and HTTPS termination.
2. Stop presenting the HTTP URL as production-ready once HTTPS is available.
3. Add a Jenkins pipeline for tests, Docker builds, deployment, and health
   verification.
4. Add deployment monitoring and a simple rollback procedure.
