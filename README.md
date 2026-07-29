# Todo App

A full-stack task management application for organizing personal to-dos and their subtasks. Users can create an account, sign in, and manage a private task list from a responsive web interface.

The application is backed by automated Cucumber acceptance tests and REST Assured API integration tests.

## Live demo

Visit **[https://todo.webweaver.dev](https://todo.webweaver.dev)**.

The demo runs in Docker on Amazon EC2, with Caddy providing HTTPS and proxying
traffic to the containerized application. Its SQLite database is intentionally
ephemeral, so accounts and tasks may be reset when the application is redeployed.

## What it does

- Registers users and authenticates returning users
- Protects user data with JSON Web Token (JWT) authentication
- Creates, displays, edits, completes, and deletes to-dos
- Adds subtasks to individual to-dos
- Edits, completes, and deletes subtasks
- Stores application data in SQLite
- Keeps users signed in across browser refreshes until they log out or their token expires

## Built with

- **Frontend:** Angular 22, TypeScript, RxJS, and CSS
- **Backend:** Java 21, Spring Boot, Spring MVC, and Spring Data JPA
- **Database:** SQLite
- **Authentication:** JWT
- **Testing:** JUnit, REST Assured, Cucumber, and Selenium
- **Deployment:** Docker Compose on Amazon EC2 with Caddy-managed HTTPS

## Project structure

```text
.
|-- docker-compose.yml       # Runs the frontend and backend containers
|-- README.md
`-- todoapp/
    |-- src/                 # Spring Boot application and backend tests
    |-- frontend/            # Angular application and frontend tests
    |-- build.gradle.kts     # Backend dependencies and build configuration
    `-- Dockerfile           # Backend container image
```

## Run locally

### Prerequisites

- Java 21
- Node.js and npm

### 1. Configure the frontend for development

The production `environment.ts` is committed and uses the same-origin `/api`
path. For local Angular development, copy the example to:

```text
todoapp/frontend/src/environments/environment.development.ts
```

The example configuration points the frontend to the backend at `http://localhost:8080`.

### 2. Start the backend

From the `todoapp` directory:

```bash
./gradlew bootRun
```

On Windows PowerShell, use:

```powershell
.\gradlew.bat bootRun
```

The API starts at `http://localhost:8080`. On first run, it creates `todo.db` in the `todoapp` directory and initializes the required tables.

### 3. Start the frontend

In a second terminal:

```bash
cd todoapp/frontend
npm install
npm start
```

Open `http://localhost:4200` in a browser.

## Run with Docker

For Docker Compose, create `.env` beside `docker-compose.yml` in the repository
root:

```dotenv
JWT_SECRET=replace-with-a-long-random-secret
CORS_ALLOWED_ORIGINS=http://localhost:4200
```

Docker Compose reads this file and passes the values into the backend container.
The `.env` file is ignored and must never be committed. Then run this command
from the repository root:

```bash
docker compose up --build
```

Then open `http://localhost:4200`. The backend is available at `http://localhost:8080`.

Stop the containers with:

```bash
docker compose down
```

## Tests

Run the backend test suite from `todoapp`:

```bash
./gradlew test
```

Run the frontend unit tests from `todoapp/frontend`:

```bash
npm test
```

The backend suite includes unit, API, and Cucumber-based browser tests. Browser scenarios require the application and their configured browser environment to be available.

## API overview

| Method | Endpoint | Purpose | Authentication |
| --- | --- | --- | --- |
| `POST` | `/register` | Create an account | Public |
| `POST` | `/login` | Sign in and receive a JWT | Public |
| `GET` | `/todo` | Get the signed-in user's to-dos | Required |
| `POST` | `/todo` | Create a to-do | Required |
| `PUT` | `/todo/{todoId}` | Update a to-do | Required |
| `DELETE` | `/todo/{todoId}` | Delete a to-do | Required |
| `GET` | `/todo/{todoId}/subtask` | Get a to-do's subtasks | Required |
| `POST` | `/todo/{todoId}/subtask` | Create a subtask | Required |
| `PUT` | `/todo/{todoId}/subtask/{subtaskId}` | Update a subtask | Required |
| `DELETE` | `/todo/{todoId}/subtask/{subtaskId}` | Delete a subtask | Required |

Authenticated requests use a bearer token in the `Authorization` header.
