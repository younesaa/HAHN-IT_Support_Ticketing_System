# IT Support Ticketing System

An IT support ticketing system that allows:
- Employees to create and view their own support tickets.
- IT Support staff to view all tickets, update statuses, and add comments.
- Secure user management with JWT authentication and Spring Security.
- Audit logs to track ticket status changes and comments.

---

## Getting Started

Follow these steps to set up the project on your local machine:

### 1- Navigate to the Backend Directory and build with maven

```bash
git clone url
cd ./backend/sysyemsupport
mvn clean package  # or ./mvnw clean package for windows
```
### 2- Navigate to the root project Directory

Ensure you are in the root directory (where the repository was cloned), then run:

```bash
cd ../..
docker-compose up
```
All environment variables are pre-configured on .env (sould be removed from repository after)
This setup facilitates a smooth local build without extra configurations.

### 3- Access API Documentation (Swagger UI)

Ensure you are in the root directory (where the repository was cloned), then run:
Once the application and services are running, access the Swagger API documentation here: http://localhost:8080/swagger-ui/index.html

API Documentation Preview

![APIs](https://github.com/younesaa/HAHN-IT_Support_Ticketing_System/blob/main/swagger-preview1.JPG?raw=true)
![DTOs](https://github.com/younesaa/HAHN-IT_Support_Ticketing_System/blob/main/swagger-preview2.JPG?raw=true)

### 4- Build and access app UI

Tech Stack

- Backend: Java 17, Spring Boot, Spring Security, JWT, JUnit, Mockito
- Containerization: Docker, Docker Compose
- Database: Oracle SQL
- Frontend: swagger, Java Swing (MigLayout)