# Phase 2 Deliverables

## API Specification

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

* Method: `GET`
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
* Response body:

```text
Java Web Token
```

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

## Wireframes
<img width="2716" height="1095" alt="Visual Mockup" src="https://github.com/user-attachments/assets/57282588-9848-40d0-b604-d58753bdcc12" />

## Database Schema
<img width="250" height="659" alt="ERD" src="https://github.com/user-attachments/assets/55798956-73b3-4fcd-b472-576975dd0507" />
