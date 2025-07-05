### `backend/README.md`

````markdown
# Jira Dashboard Backend

This is the backend for the Jira Dashboard application built with Spring Boot and MongoDB. It provides user authentication using JWT and CRUD operations for projects and tasks.

## 🛠 Tech Stack

- Java 17
- Spring Boot
- Spring Security (JWT Auth)
- MongoDB
- Maven

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven
- MongoDB running locally or remotely

### Run the application

```bash
cd backend
mvn spring-boot:run
````

The backend will be available at: `http://localhost:8080`

### API Endpoints

#### Authentication

* `POST /api/users/register` – Register a new user
* `POST /api/users/login` – Login and receive JWT
* `GET /api/users/me` – Get current user (requires JWT)

#### Projects

* `GET /api/projects` – Get all projects for current user
* `POST /api/projects` – Create a project
* `GET /api/projects/{id}` – Get project by ID
* `PUT /api/projects/{id}` – Update a project
* `DELETE /api/projects/{id}` – Delete a project

> All project routes require JWT Authorization in the `Authorization` header.

### 💡 Environment Variables (Optional)

You can configure the MongoDB connection in `application.properties`:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/jira_dashboard
```

## 📂 Project Structure

```
├── config/
├── controller/
├── model/
├── repository/
├── security/
├── util/
└── JiraDashboardBackendApplication.java
```

## 📘 License

This project is for educational/demo purposes.

````
