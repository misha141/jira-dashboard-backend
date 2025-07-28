# Jira Dashboard Backend

This is the backend for the Jira Dashboard application built with Spring Boot and MongoDB. It provides user authentication using JWT, CRUD operations for projects and tasks, and integrates with GitHub via webhooks to automate task updates.

## 🛠 Tech Stack

- Java 17
- Spring Boot
- Spring Security (JWT Auth)
- MongoDB
- Maven
- **GitHub Webhooks**

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven
- MongoDB running locally or remotely

### Run the application

```bash
cd backend
mvn spring-boot:run
```

### The backend will be available at: http://localhost:8080

## API Endpoints
### Authentication
- POST /api/users/register – Register a new user

- POST /api/users/login – Login and receive JWT

- GET /api/users/me – Get current user (requires JWT)

### Projects
- GET /api/projects – Get all projects for current user

- POST /api/projects – Create a project

- GET /api/projects/{id} – Get project by ID

- PUT /api/projects/{id} – Update a project

- DELETE /api/projects/{id} – Delete a project

All project routes require JWT Authorization in the Authorization header.

### Tasks
- GET /api/tasks/project/{projectId} - Get all tasks for a specific project

- POST /api/tasks - Create a new task

- PUT /api/tasks/{id} - Update a task

- DELETE /api/tasks/{id} - Delete a task

### GitHub Webhooks
This endpoint is used by GitHub to automatically update a task's status based on a Git commit.

- POST /api/github/webhook – Receives a webhook from GitHub's push event.

- No authentication is required. This endpoint is public.

- The commit message must contain the task's short ID (e.g., Closes task #123).

### 💡 Commit Message Convention
For a commit to update a task, its message must follow this format:

``` Closes task #<TASK_ID>
Example: git commit -m "Fixing login bug. Closes task #12"
```

This tells the backend to find the task with the sequential taskId of 12 and move its status to "Done."

### 📝 Environment Variables (Optional)
You can configure the MongoDB connection in application.properties:

### Properties

- spring.data.mongodb.uri=mongodb://localhost:27017/jira_dashboard

### 📂 Project Structure
```├── config/
├── controller/
├── model/
├── repository/
├── security/
├── service/
├── util/
└── JiraDashboardBackendApplication.java
```
The service/ directory was added to support the sequential task ID generation.

### 📘 License
This project is for educational/demo purposes.