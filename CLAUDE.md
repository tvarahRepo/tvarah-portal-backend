# Objective
You are a senior backend architect. Your task is to generate a production-grade Spring Boot microservice with clean architecture, strong security integration, and schema visualization support.

# Tech Stack
- Java 17+
- Spring Boot (latest stable)
- Spring Web
- Spring Data JPA
- PostgreSQL
- Lombok
- Maven
- MapStruct
- Liquibase (for DB migrations)
- Keycloak (for authentication & authorization)
- Docker support
- PlantUML (for schema visualization)

# Architecture Guidelines
- Follow clean architecture (Controller → Service → Repository)
- Use a unified "model" package instead of separate entity and DTO layers
- Apply SOLID principles
- Use constructor-based dependency injection
- Ensure modular and scalable package structure:

  - controller
  - service
  - repository
  - model
  - mapper
  - exception
  - config
  - security

# API Design
- RESTful endpoints
- Use proper HTTP status codes
- Standard response wrapper:
  {
    "status": "success|error",
    "message": "...",
    "data": {}
  }

# Database
- PostgreSQL
- Use UUID as primary key
- Integrate Liquibase for schema versioning and change management
- Maintain changelog files in a structured and scalable format

# Security (Keycloak Integration)
- Integrate Keycloak for authentication and authorization
- Configure:
  - Realm
  - Client (confidential/public as needed)
- Use Spring Security with Keycloak adapter
- Secure APIs using role-based access control (RBAC)
- Extract user details (JWT claims) in request context
- Provide a basic security configuration that can be extended

# Liquibase Guidelines
- Use XML or YAML-based changelogs
- Maintain:
  - master changelog file
  - versioned incremental changesets
- Follow naming conventions for changesets
- Ensure idempotency and rollback support where possible

# PlantUML (PUML) Requirements
- Generate PUML files to visualize:
  - Data model (class diagram for models)
  - Relationships between tables/entities
- Place PUML files under:
  /docs/puml/
- Ensure diagrams are clean and readable
- Include instructions to render diagrams

# Validation
- Use javax.validation annotations
- Validate request payloads at controller level

# Exception Handling
- Global exception handler using @ControllerAdvice
- Standardized error response format
- Custom exceptions:
  - ResourceNotFoundException
  - BadRequestException
  - UnauthorizedException

# Logging
- Use SLF4J
- Log at appropriate levels (INFO, DEBUG, ERROR)
- Avoid logging sensitive data

# Documentation
- Add OpenAPI/Swagger configuration for API documentation

# DevOps
- Provide:
  - Dockerfile
  - docker-compose (PostgreSQL + Keycloak + application)
- Ensure services are properly networked
- Externalize configurations via environment variables

# Output Format
- Provide complete project structure
- Include all necessary files with code
- Ensure the project builds and runs without modification

# Constraints
- Avoid unnecessary boilerplate
- Keep code clean, readable, and production-ready
- Follow enterprise backend best practices