# Product Overview

A full-stack todo management application built by Team Tetra. It consists of a Spring Boot REST API backend and an Angular single-page application frontend. Users can register and log in, then create and manage todo items with optional subtasks.

## Core Features

- User registration and login with JWT-based authentication
- CRUD operations for todo items, scoped to the authenticated user
- Subtask management nested under todos
- Angular SPA with route-guarded dashboard, login, and registration pages

## Authentication

- Login returns a signed JWT; the frontend stores it and sends it as `Authorization: Bearer <token>` on subsequent requests
- All endpoints except `/register` and `/login` require a valid token
- Token contains `userId` (subject) and `username` claims; expires after 24 hours

## API Endpoints

### Auth (public — no token required)

| Method | Path        | Description         |
|--------|-------------|---------------------|
| POST   | /register   | Register a new user |
| POST   | /login      | Log in, returns JWT |

### Todos (protected — requires valid JWT)

| Method | Path    | Description                         |
|--------|---------|-------------------------------------|
| POST   | /todo   | Create a todo for the current user  |
| GET    | /todo   | Get all todos for the current user  |
| PUT    | /todo   | Update a todo                       |
| DELETE | /todo   | Delete a todo                       |

### Subtasks (protected — requires valid JWT)

| Method | Path       | Description                              |
|--------|------------|------------------------------------------|
| POST   | /subtask   | Create a subtask under a todo            |
| GET    | /subtask   | Get all subtasks for a given `todoId`    |
| PUT    | /subtask   | Update a subtask                         |
| DELETE | /subtask   | Delete a subtask                         |

## Business Rules

- Usernames must be 5–15 characters and unique
- Passwords must be 5–15 characters and contain at least one uppercase letter, one lowercase letter, and one digit
- Todos must be associated with a valid existing user (enforced via JWT — `userId` is read from the token, not the request body)
- Todo titles must be unique
