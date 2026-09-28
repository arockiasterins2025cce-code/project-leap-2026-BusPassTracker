# BusPass — Student Bus Pass Application Tracker

Spring Boot backend for the Project Leap Java + DBMS assessment.

## Technology
- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- MySQL
- Bean Validation
- REST API
- Layered architecture: Controller → Service → Repository → Database

## 1. Create the database

MySQL:

```sql
CREATE DATABASE buspass_db;
```

The application can also create the database because `createDatabaseIfNotExist=true` is present.

## 2. Configure MySQL password

Open:

`src/main/resources/application.properties`

Change:

```properties
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

to your actual MySQL root password.

## 3. Run

From the project folder:

```bash
mvn spring-boot:run
```

Or open the project in IntelliJ / VS Code and run `BusPassApplication.java`.

Server:

`http://localhost:8090`

## 4. Main API endpoints

### Students

POST `/api/students`

```json
{
  "name": "Test Student",
  "registerNumber": "25CC010",
  "department": "CCE",
  "year": "2",
  "phone": "9876543210"
}
```

GET `/api/students`

GET `/api/students/1`

### Bus routes

POST `/api/routes`

```json
{
  "routeNumber": "R04",
  "routeName": "Tiruppur - College",
  "boardingPoint": "Tiruppur Bus Stand"
}
```

GET `/api/routes`

### Apply for bus pass

POST `/api/applications`

```json
{
  "studentId": 1,
  "routeId": 1,
  "boardingPoint": "Gandhipuram",
  "photoReference": "student_photo_001.jpg"
}
```

### View application

GET `/api/applications/1`

### Student application history

GET `/api/applications/student/1`

### Admin: list all applications

GET `/api/applications`

### Admin: approve

PUT `/api/applications/1/approve`

```json
{
  "remark": "Documents verified",
  "validityDays": 180
}
```

The system automatically generates a pass number such as `BP-000001`.

### Admin: reject

PUT `/api/applications/1/reject`

```json
{
  "remark": "Document verification failed",
  "rejectionReason": "Invalid student ID"
}
```

A rejection without `rejectionReason` is rejected by the service layer.

### Admin: passes expiring in next 30 days

GET `/api/applications/expiring/30-days`

## Business rules implemented

1. A student cannot have more than one active APPROVED pass.
2. A rejected application must contain a rejection reason.
3. Only PENDING applications can be approved/rejected.
4. Approval requires a positive validity period.
5. Missing Student/Route/Application returns a clear 404 response.
6. Invalid request fields return a clear 400 response.

## Database relationships

Student 1 ---- * PassApplication * ---- 1 BusRoute

A student can submit multiple applications over time, but only one APPROVED/active pass is allowed.

## Suggested demo order

1. Start MySQL.
2. Start Spring Boot.
3. Show students in MySQL.
4. Show bus routes.
5. Create a pass application in Postman.
6. Show PENDING status.
7. Approve it and show pass number + validity.
8. Try another application for the same student and show the business-rule error.
9. Create another application and reject it with a reason.
10. Try rejection without a reason and show the validation/business-rule error.
11. Show the 30-day expiry endpoint.

## Architecture

Client/Postman
      ↓
REST Controller
      ↓
Service Layer
      ↓
Spring Data JPA Repository
      ↓
MySQL

The Service Layer contains the important business rules.
