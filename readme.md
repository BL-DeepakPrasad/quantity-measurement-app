# Quantity Measurement App — Backend Microservices

A production-ready backend system built using Spring Boot and Spring Cloud Microservices Architecture.  
The application demonstrates scalable service-oriented architecture with secure authentication, API Gateway routing, service discovery, centralized configuration, and cloud-native deployment practices.

---

## Overview

The backend is designed using independently deployable microservices that communicate through REST APIs and are dynamically managed using Eureka Service Discovery.

Key highlights include:

- Microservices Architecture
- API Gateway Routing
- JWT-based Authentication
- Google OAuth2 Login
- Service Discovery with Eureka
- Centralized Database Management
- Dockerized Deployment
- Cloud Hosting on Render

---

## Live Services

| Service | Endpoint |
|----------|----------|
| API Gateway | https://quantity-measurement-app-api-gateway.onrender.com |
| Auth Service | https://quantity-measurement-app-auth-service.onrender.com |
| Quantity Service | https://quantity-measurement-app-quantity-service.onrender.com |
| Eureka Server | https://quantity-measurement-app-44gd.onrender.com |

---

## API Documentation

### Auth Service Swagger
https://quantity-measurement-app-auth-service.onrender.com/swagger-ui/index.html

### Quantity Service Swagger
https://quantity-measurement-app-quantity-service.onrender.com/swagger-ui/index.html

---

## Technology Stack

### Backend
- Java 21
- Spring Boot 3.x
- Spring Cloud Gateway
- Spring Cloud Netflix Eureka
- Spring Security
- OAuth2 Authentication
- JWT Authentication
- Spring Data JPA
- Hibernate
- MySQL

### DevOps & Deployment
- Docker
- Render Cloud Platform
- GitHub Actions (optional CI/CD)

---

## Architecture Flow

```text
Client Request
      │
      ▼
API Gateway
      │
      ▼
Eureka Service Discovery
      │
 ┌───────────────┬────────────────┐
 ▼               ▼                ▼
Auth Service   Quantity Service   Other Services
      │             |
      ▼             |
MySQL Database   <--|
```

---

## Core Features

### Authentication & Authorization
- Google OAuth2 Integration
- JWT Token Generation & Validation
- Stateless Authentication
- Secure Route Protection

### Microservices Infrastructure
- Dynamic Service Discovery
- API Gateway-based Routing
- Load-balanced Service Communication

### Database Management
- MySQL Integration
- JPA/Hibernate ORM
- Transaction Management

### Deployment
- Fully Dockerized Services
- Cloud Deployment using Render
- Independent Service Scalability

---

## Local Development Setup

### Prerequisites

Ensure the following are installed:

- Java 21
- Maven
- MySQL
- Docker (Optional)

---

## Environment Configuration

Create a `.env` file inside the project root.

```env
# Eureka
EUREKA_URL=http://localhost:8761/eureka/

# Frontend URL
FRONTEND_URL=http://localhost:5173

# Database
DB_URL=jdbc:mysql://localhost:3306/qma_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=your_password

# JWT
JWT_SECRET=your_jwt_secret

# Google OAuth2
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
GOOGLE_REDIRECT_URI=http://localhost:8080/login/oauth2/code/google
```

---

## Running the Services

### Eureka Server

```bash
mvn spring-boot:run
```

### API Gateway

```bash
mvn spring-boot:run
```

### Auth Service

```bash
mvn spring-boot:run
```

### Quantity Service

```bash
mvn spring-boot:run
```

---

## Security Implementation

The backend follows secure authentication and authorization practices:

- JWT-based stateless authentication
- OAuth2 login using Google
- Protected API routes
- Token validation filters
- Secure API Gateway routing

---

## Deployment Strategy

All backend services are containerized using Docker and deployed independently on Render Cloud Platform.

The architecture supports:
- Independent scaling
- Fault isolation
- Cloud-native deployment
- Simplified maintenance

---

## Repository Structure

```text
quantity-measurement-app/
│
├── api-gateway/
├── auth-service/
├── quantity-service/
├── eureka-server/
│
├── docker-compose.yml
├── README.md
└── .env
```

---

## Author

**Deepak Prasad**

Backend Developer | Java & Spring Boot Enthusiast