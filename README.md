
# Timesheet Management System

A full-stack application for managing employee hierarchies with Spring Boot backend and Angular frontend.

## Project Structure

- `backend/`: Spring Boot application
- `frontend/`: Angular application
- `docker-compose.yml`: Docker configuration for PostgreSQL database

## Prerequisites

- Java 11 or higher
- Node.js and npm
- Docker and Docker Compose
- Maven

## Running the Application

### 1. Start the PostgreSQL Database

```bash
docker-compose up -d
```

This will start a PostgreSQL database with:
- Database: `employee_db`
- Username: `postgres`
- Password: `postgres`
- Port: 5432

### 2. Run the Spring Boot Backend

```bash
cd backend
mvn spring-boot:run
```

The backend will start on http://localhost:8080

### 3. Run the Angular Frontend

```bash
cd frontend
npm install
npm start
```

The frontend will start on http://localhost:4200

## Features

- User registration and login with BCrypt password hashing
- Employee hierarchy visualization
- Manager-subordinate relationship management
- Responsive UI with Angular Material

## API Endpoints

### Authentication

- `POST /api/auth/register`: Register a new user/employee
- `POST /api/auth/login`: Login with username and password

### Employee Hierarchy

- `GET /api/employees/hierarchy/{id}`: Get employee hierarchy information

## Application Flow

1. Register a new account (optionally specify a manager ID)
2. Login with your credentials
3. View your profile, manager details, and subordinates on the dashboard
4. Navigate between dashboard and profile pages

## Technologies Used

### Backend
- Spring Boot
- Spring Data JPA/Hibernate
- PostgreSQL
- Spring Security with BCrypt

### Frontend
- Angular 17
- Angular Material
- TypeScript
- RxJS

### DevOps
- Docker & Docker Compose
