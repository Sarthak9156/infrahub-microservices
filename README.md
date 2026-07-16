# InfraHub - Infrastructure Management System

A Spring Boot based Infrastructure Management System built using a Microservices Architecture. This project provides secure authentication using JWT, Refresh Tokens, and Role-Based Authorization.

---

## Features

### Authentication
- User Registration
- User Login
- JWT Authentication
- Refresh Token Authentication
- Logout API
- BCrypt Password Encryption

### Security
- Spring Security
- JWT Authentication Filter
- Role-Based Authorization
- Stateless Authentication

### User Management
- Create User
- Get User
- Update User
- Delete User

### Exception Handling
- Global Exception Handling
- Custom Exceptions
- Validation Handling

---

## Tech Stack

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- PostgreSQL
- JWT (JSON Web Token)
- Maven
- Postman
- Git & GitHub

---

## Project Structure

```
src
├── config
├── controller
├── dto
├── entity
├── exception
├── repository
├── service
```

---

## API Endpoints

| Method | Endpoint | Description |
|---------|----------|-------------|
| POST | /users | Register User |
| POST | /login | Login User |
| POST | /refresh | Generate New Access Token |
| POST | /logout | Logout User |
| GET | /users/{id} | Get User By Id |
| PUT | /users/{id} | Update User |
| DELETE | /users/{id} | Delete User |
| GET | /Allusers | Admin Only |

---

## Authentication Flow

```
User
   │
   ▼
Login
   │
   ▼
Access Token + Refresh Token
   │
   ▼
Access Protected APIs
   │
   ▼
Access Token Expired
   │
   ▼
Refresh Token API
   │
   ▼
New Access Token
   │
   ▼
Logout
   │
   ▼
Refresh Token Deleted
```

---

## Architecture

Controller

↓

Service

↓

Repository

↓

Database

---

## Security Features

- Password Encryption using BCrypt
- JWT Access Token
- Refresh Token
- Role-Based Authorization
- Stateless Authentication
- Custom Authentication Filter

---

## Future Enhancements

- API Gateway
- Service Registry (Eureka)
- Config Server
- Employee Service
- Asset Service
- Department Service
- Docker
- Kubernetes
- CI/CD Pipeline

---

## Author

**Sarthak Kayastha**

GitHub:
https://github.com/Sarthak9156
