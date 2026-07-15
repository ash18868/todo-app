# Phase 1 Deliverables

## Team Name

Team Tetra

## User Stories

Account Creation: As a new user, I can register an account to start tracking my todo tasks.

* Navigate to sign up page from log in screen

  * Stretch Goal: Make homepage
* Create Username & Password

  * Stretch Goal: Password requirements
* Click on sign up button
* Check if username already exists

  * Warn if duplicate is found
* If username doesn’t exist create user
* Automatically return to login screen

Authentication: As a new user, I can log in and out to securely access my todo items.

* Enter Username & Password
* Click login button
* Check if credentials match DB records

  * Warn if invalid
* If match is found, return the authentication token
* Take user to todo screen
* Frontend request todo list from backend
* Click logout button

  * Deletes token and returns to login page

Task Management: As a user, I can create, edit, and delete todo items to keep track of my work.

* Create

  * Fill out Todo name field
  * Click add todo
  * Backend will insert Todo into Todo table
* Edit (Change name/mark complete)

  * Click edit button
  * Edit name field
  * Click save edit
  * Send an update to backend
  * Click Todo checkmark box
  * Send an update to backend
* Delete

  * Click trashcan
  * Prompt warning message
  * Send delete to backend, which also deletes subtasks

Subtask Organization: As a user, I can create, edit, and delete subtask items to better organize my primary tasks.

* Create

  * Fill out subtask name field
  * Click add todo
  * Backend will insert subtask into subtask table
* Edit (Change name/mark complete)

  * Click edit button
  * Edit name field
  * Click save edit
  * Send an update to backend
  * Click subtask checkmark box
  * Send an update to backend
* Delete

  * Click trashcan
  * Prompt warning message
  * Send delete to backend

## Database Model

### Todos

* todo_ID(PK), Name(alt_PK), user_Id(FK), Completed(boolean)
* If Todo is deleted, it also needs to remove its child subtasks

### Subtasks

* subtask_id(PK) todo_ID(Parent)(FK), Title, Completed(boolean)

### Users

* user_Id(PK), Username, password

<img width="250" height="659" alt="ERD" src="https://github.com/user-attachments/assets/e8e44af6-eda8-4050-a8fd-11d836b2e7ba" />

## API Contract

### Register Account

#### Request

* Method: `POST`
* URL: `/api/auth/register`
* Body:

```json
{
  "username": "string",
  "password": "string"
}
```

#### Response

##### Success

* Status: `200`
* Response body:

```text
Java Web Token
```

##### Fail

* Status: `400`
* Response body:

```json
{
  "message": "generic error message"
}
```

---

### Logging In

#### Request

* Method: `POST`
* URL: `/api/auth/login`
* Body:{username, password}

```json
{
  "username": "string",
  "password": "string"
}
```

#### Response

##### Success

* Status: `200`

##### Fail

* Status: `400`
```json
{
  "message": "Fail to log in message"
}
```
---

### Logging Out

#### Request

* Method: `POST`
* URL: `/logout`
* Body:

```json
{
  "user_id": "number"
}
```

#### Response

##### Success

* Status: `200`
* Response body:

```json
{
  "message": "log out successful"
}
```

##### Fail

* Status: `500`
* Response body:

```json
{
  "message": "error message"
}
```

---

### Load Todo Page

#### Request

* Method: `GET`
* URL: `/todo`
* Body:

```json
{
  "user_id": "number"
}
```

#### Response

##### Success

* Status: `200`
* Response body:

```json
[
  {
    "todo_id": "number",
    "title": "string",
    "user_id": "number",
    "completed": "boolean"
  }
]
```

##### Fail

* Status: `500`
* Response body:

```json
{
  "message": "error message"
}
```

---

### Creating Task

#### Request

* Method: `POST`
* URL: `/todo`
* Body:

```json
{
  "user_id": "number",
  "title": "string",
  "completed": false
}
```

#### Response

##### Success

* Status: `200`
* Response body:

```json
[
  {
    "todo_id": "number",
    "title": "string",
    "user_id": "number",
    "completed": "boolean"
  }
]
```

##### Fail

* Status: `500`
* Response body:

```json
{
  "message": "Failed to create task"
}
```

---

### Editing Task

#### Request

* Method: `PUT`
* URL: `/todo`
* Body:

```json
{
  "task_id": "number",
  "title": "string",
  "completed": "boolean"
}
```

#### Response

##### Success

* Status: `200`

##### Fail

* Status: `400` or `500`

---

### Deleting Task

Auto deletes subtasks.

#### Request

* Method: `DELETE` (Auto deletes subtasks)
* URL: `/api/todo`
* Body:

```json
{
  "task_id": "number"
}
```

#### Response

##### Success

* Status: `200`

##### Fail

* Status: `400` or `500`

---

### Creating Subtask

#### Request

* Method: `POST`
* URL: `/todo`
* Body:

```json
{
  "todo_id": "number",
  "title": "string",
  "user_id": "number",
  "completed": false
}
```

#### Response

##### Success

* Status: `200`

##### Fail

* Status: `400` or `500`

---

### Editing Subtask

#### Request

* Method: `PUT`
* URL: `/api/subtask`
* Body:

```json
{
  "subtask_id": "number",
  "title": "string",
  "completed": "boolean"
}
```

#### Response

##### Success

* Status: `200`

##### Fail

* Status: `400` or `500`

---

### Deleting Subtask

#### Request

* Method: `DELETE`
* URL: `/api/subtask`
* Body:

```json
{
  "subtask_id": "number"
}
```

#### Response

##### Success

* Status: `200`

##### Fail

* Status: `400` or `500`

## Definition of Done

* [ ] Successfully create a new user

  * Blocks duplicate
  * New account reflected in database

* [ ] Successfully login/logout to created account

  * Checks credential validity

* [ ] Successfully retrieves all todos with proper status’ and names

  * Doesn’t show deleted todos

* [ ] Can edit Todo and subtask name and changes persist

* [ ] Can update Todo/Subtask completion status and change persists

* [ ] Can successfully delete Todo/subtask and deletion persists

* [ ] Database changes are verified

## Task Backlog
https://github.com/mtreiley/Team4Repository/issues
