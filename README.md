# Todo App

A full-stack task management application for organizing personal to-dos and their subtasks. Users can create an account, sign in, and manage a private task list from a responsive web interface.

## What it does

- Registers users and authenticates returning users
- Protects user data with JSON Web Token (JWT) authentication
- Creates, displays, edits, completes, and deletes to-dos
- Adds subtasks to individual to-dos
- Edits, completes, and deletes subtasks
- Persists application data in a local SQLite database
- Keeps users signed in across browser refreshes until they log out or their token expires

## Built with

- **Frontend:** Angular 22, TypeScript, RxJS, and CSS
- **Backend:** Java 21, Spring Boot, Spring MVC, and Spring Data JPA
- **Database:** SQLite
- **Authentication:** JWT
- **Testing:** JUnit, REST Assured, Cucumber, and Selenium
- **Deployment:** Docker and Docker Compose

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

### 1. Configure the frontend

Copy `todoapp/frontend/src/environments/environment.example.ts` to both of the following files:

```text
todoapp/frontend/src/environments/environment.ts
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

After creating the two frontend environment files described above, run this command from the repository root:

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
| `PUT` | `/todo` | Update a to-do | Required |
| `DELETE` | `/todo` | Delete a to-do | Required |
| `GET` | `/subtask?todoId={id}` | Get a to-do's subtasks | Required |
| `POST` | `/subtask` | Create a subtask | Required |
| `PUT` | `/subtask` | Update a subtask | Required |
| `DELETE` | `/subtask` | Delete a subtask | Required |

Authenticated requests use a bearer token in the `Authorization` header.
