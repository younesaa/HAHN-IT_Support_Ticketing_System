# IT Support Ticketing System

An IT support ticketing system that allows:
- Employees to create and view their own support tickets.
- IT Support staff to view all tickets, update statuses, and add comments.
- Secure user management with JWT authentication and Spring Security.
- Audit logs to track ticket status changes and comments.

---

## Getting Started

Follow these steps to set up the project on your local machine:

Please note that the app has configured with 
- an admin user with credentials username=Admin and password=Admin123  ( for creating users)
    username=Admin and password=Admin123
- Employee & IT support users to make testing more easier with credentials ( for creation/viewing & managing/commenting/changing status tickets )
    username=userEmployee1 and password=userEmployee1   
    username=userEmployee2 and password=userEmployee2
    username=userSupport1 and password=userSupport1  

### 1- Navigate to the Backend Directory and build with maven

```bash
git clone url
cd ./backend/sysyemsupport
mvn clean package  # or ./mvnw clean package for windows
```
### 2- Navigate to the root project Directory

Ensure you are in the root directory (where the repository was cloned), for that run:

```bash
cd ../..
docker-compose up
```
All environment variables are pre-configured on .env (sould be removed from repository after)
This setup facilitates a smooth local build without extra configurations.

### 3- Access API Documentation (Swagger UI)

Once the application and services are running, access the Swagger API documentation here: http://localhost:8080/swagger-ui/index.html

API Documentation Preview

![APIs](https://github.com/younesaa/HAHN-IT_Support_Ticketing_System/blob/main/swagger-preview1.JPG?raw=true)
![DTOs](https://github.com/younesaa/HAHN-IT_Support_Ticketing_System/blob/main/swagger-preview2.JPG?raw=true)

### 4- Build and access app UI

Ensure you are in the root directory (where the repository was cloned), then run:

```bash
java -jar it_support_swing_ui-1.0-SNAPSHOT.java 
```
UI Preview

![Employe Dashboard](https://github.com/younesaa/HAHN-IT_Support_Ticketing_System/blob/main/ui_preview.JPG?raw=true)
![Ticket Creation Dashboard](https://github.com/younesaa/HAHN-IT_Support_Ticketing_System/blob/main/ui_preview_1.JPG?raw=true)
![Employee Ticket List Dashboard](https://github.com/younesaa/HAHN-IT_Support_Ticketing_System/blob/main/ui_preview_2.JPG?raw=true)
![Tickets View Dashboard](https://github.com/younesaa/HAHN-IT_Support_Ticketing_System/blob/main/support_preview_1.JPG?raw=true)
![Audit Logs Dashboard](https://github.com/younesaa/HAHN-IT_Support_Ticketing_System/blob/main/support_preview_2.JPG?raw=true)

Notes

- Admin Dashboard Complete (add & delete users)
- Employee Dashhboard Complete ( ticket creation & ticket listing with status filter )
- Support IT Dashhboard Complete ( ticket status Update and Comments & Aufit Log Actions listing )

Tech Stack

- Backend: Java 17, Spring Boot, Spring Security, JWT, JUnit, Mockito
- Containerization: Docker, Docker Compose
- Database: Oracle SQL
- Frontend: swagger, Java Swing (MigLayout)
