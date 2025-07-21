# Hitachi Solusindo Platform

## Project Description

This is ONLY MOCK UP PROJECT, DOES NOT CORELATED TO ANYONE

This project is the backend for the "Hitachi Solusindo Platform", built from scratch following a comprehensive Business Requirements Document (BRD). It provides a robust and scalable API for managing various business processes, including user authentication, product management, customer data, tax calculations, and transaction processing.

## Features
- User Authentication and Authorization (Spring Security, JWT)
- Role-Based Access Control (RBAC)
- Product Management (CRUD operations)
- Customer Management (CRUD operations)
- Tax Management (CRUD operations)
- Transaction Management
- Centralized Exception Handling
- API Documentation with Swagger/OpenAPI
- Layered Architecture (Controller, Service, Repository, Entity)
- DTO Pattern for Request and Response Payloads
- UUID as Primary Keys for Entities

## Technologies Used
- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security
- PostgreSQL (or other relational database)
- Lombok
- JJWT (JSON Web Token)
- Swagger/OpenAPI (for API documentation)
- Maven

## Setup Instructions

### Prerequisites
- Java Development Kit (JDK) 17 or higher
- Maven
- PostgreSQL (or your preferred relational database)

### 1. Clone the Repository
```bash
git clone <repository_url>
cd hcs-idn
```

### 2. Database Configuration
Update the `src/main/resources/application.properties` file with your database connection details:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/hcs_idn_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### 3. Build the Project
```bash
mvn clean install
```

### 4. Run the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080` by default.

## API Endpoints

Once the application is running, you can access the API documentation via Swagger UI:

`http://localhost:8080/api/swagger-ui/index.html`

Key API categories include:
- `/api/auth`: User registration, login, token refresh.
- `/api/products`: Product management.
- `/api/customers`: Customer management.
- `/api/taxes`: Tax management.
- `/api/transactions`: Transaction processing.
- `/api/users`: User management (admin only).

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── raaflahar/
│   │           └── hcs_idn/
│   │               ├── HcsIdnApplication.java
│   │               ├── config/             # Spring configurations (Security, OpenAPI, Data Initializer)
│   │               ├── constant/           # Enums and constants
│   │               ├── controller/         # REST API controllers
│   │               ├── dto/                # Data Transfer Objects (request and response)
│   │               ├── entity/             # JPA Entities (database models)
│   │               ├── exception/          # Custom exceptions
│   │               ├── repository/         # Spring Data JPA repositories
│   │               ├── security/           # Spring Security configurations, JWT filters, UserDetails
│   │               ├── service/            # Business logic interfaces
│   │               │   └── impl/           # Business logic implementations
│   │               ├── specification/      # JPA Specifications for dynamic queries
│   │               └── util/               # Utility classes (e.g., JWTUtil)
│   └── resources/
│       ├── application.properties  # Application configuration
│       ├── static/
│       └── templates/
└── test/
    └── java/
        └── com/
            └── raaflahar/
                └── hcs_idn/
                    └── HcsIdnApplicationTests.java
```

## Contributing

Contributions are welcome! Please follow the existing code style and architectural patterns. For major changes, please open an issue first to discuss what you would like to change.

### Coding Guidelines
- Adhere to the Layered Architecture: `Controller` -> `Service` (Interface) -> `ServiceImpl` -> `Repository` -> `Entity`.
- Use DTOs for all API requests and responses.
- Wrap successful API responses with `CommonResponse<T>`.
- Use custom exceptions for error handling.
- Ensure all sensitive endpoints are secured with Spring Security.
- Utilize Lombok for boilerplate reduction.
- Use constructor injection for dependencies.

## License

MIT License