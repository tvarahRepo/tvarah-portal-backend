# Tvarah — Talent Intelligence Platform

> A production-grade Spring Boot microservice that powers a full-cycle recruitment intelligence platform — from candidate sourcing and job matching to interview scheduling and offer tracking. Built with clean architecture, JWT-based security via Keycloak, schema-version-controlled PostgreSQL via Liquibase, and full OpenAPI documentation.

---

## Table of Contents

- [Business Overview](#business-overview)
- [Domain Model](#domain-model)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Security & Authorization](#security--authorization)
- [Database & Liquibase](#database--liquibase)
- [API Documentation](#api-documentation)
- [Schema Diagrams (PlantUML)](#schema-diagrams-plantuml)
- [Environment Variables](#environment-variables)
- [Getting Started](#getting-started)
- [Running with Docker](#running-with-docker)
- [Running Locally](#running-locally)
- [Actuator & Observability](#actuator--observability)
- [Exception Handling](#exception-handling)
- [Contributing](#contributing)

---

## Business Overview

Tvarah is the backend engine for a **talent intelligence and recruitment operations platform**. It serves HR teams, recruiters, and hiring managers to:

- **Manage clients** — companies that raise hiring requirements, segmented by industry and size.
- **Post and score job descriptions** — structured JDs with skill requirements, experience bands, salary ranges, and non-negotiable rules.
- **Build a candidate pool** — rich candidate profiles covering identity, work history, education, skills, social presence, documents, and preferences.
- **Score and rank candidates** — multi-dimensional scoring across education quality, experience depth, skill breadth, domain alignment, and fraud risk.
- **Track applications** — link candidates to job openings, manage pipeline stages, and produce per-application JD-fit evaluations.
- **Drive interview cycles** — schedule multi-round interviews, capture questions, focus areas, feedback, and round-level scores.
- **Reference data backbone** — centralized master tables for industry, department, job title, designation, skill, institution, degree, and specialization, ensuring consistency across all records.

---

## Domain Model

The platform is organized into the following logical domains:

### Core Operational Entities

| Entity | Purpose |
|---|---|
| `user` | Platform users (recruiters, admins) linked to Keycloak identities |
| `client` | Hiring companies with industry and recruiter assignments |
| `job_description` | Structured job postings with skill arrays, salary range, rounds, and scoring |
| `candidate` | Central candidate record with current designation, company, and experience summary |
| `candidate_job` | Many-to-many bridge linking candidates to job descriptions, with pipeline status |

### Candidate Profile Extensions (1:1)

| Entity | Purpose |
|---|---|
| `candidate_preference` | Location, CTC expectations, relocation flag, work mode |
| `candidate_notice_period` | Resignation date, last working day, earliest joining date |
| `candidate_score` | Multi-dimension ML/rule-based scores (education, experience, skills, domain) + fraud flags |
| `candidate_resume_summary` | Qualitative labels and tags across skill depth, experience quality, academic background, domain depth |

### Candidate History

| Entity | Purpose |
|---|---|
| `candidate_experience` | Work history entries with company, designation, industry, and tenure |
| `candidate_education` | Academic history with institution, degree, specialization, and years |
| `candidate_skill` | Skill inventory with proficiency level, years of use, and validation status |
| `candidate_document` | Uploaded documents (resume, certificate, etc.) with typed URLs |
| `candidate_social_media` | Social/professional profile links (LinkedIn, GitHub, etc.) |

### Evaluation & Interview

| Entity | Purpose |
|---|---|
| `candidate_job_evaluation` | Per-application JD-fit scores (skill match, domain match, experience relevance) |
| `candidate_job_interview` | Interview round records with scheduling, mode, questions, feedback, and score |

### Reference / Master Data

| Entity | Purpose |
|---|---|
| `industry` | Industry taxonomy with NAICS references and sector classification |
| `department` | Hierarchical department tree supporting sub-departments |
| `job_title` | Role titles linked to departments and seniority levels |
| `designation` | Formal designation catalog with category and level metadata |
| `skill` | Verified skill catalog by category, tier, and technical flag |
| `institution` | Educational institutions with tier, ranking, city/state, and parent institution |
| `degree` | Degree types with level and category |
| `specialization` | Academic specializations with degree mapping via `specialization_degree_map` |
| `company` | Company master with industry, headcount, MCA verification, and ratings |
| `status` | Polymorphic status lookup table (name + type) used across all entities |

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2.x |
| Web | Spring Web (REST) |
| Persistence | Spring Data JPA + Hibernate |
| Database | PostgreSQL 16 |
| Schema Management | Liquibase |
| Security | Spring Security + OAuth2 Resource Server (JWT) |
| Identity Provider | Keycloak 24 |
| Object Mapping | MapStruct 1.5.5 |
| Boilerplate Reduction | Lombok 1.18.30 |
| Build Tool | Maven |
| API Docs | SpringDoc OpenAPI 2.3 (Swagger UI) |
| Containerization | Docker + Docker Compose |
| Schema Visualization | PlantUML |

---

## Architecture

```
Client (HR / Recruiter)
        │
        │  HTTP + Bearer JWT
        ▼
┌─────────────────────────────────────────────────┐
│              Tvarah Microservice                 │
│                                                 │
│  ┌──────────────────────────────────────────┐   │
│  │         Security Filter Chain            │   │
│  │  BearerTokenAuthenticationFilter         │◀──── Keycloak JWKS
│  │  JwtAuthConverter                        │   │
│  │  (realm_access + resource_access roles)  │   │
│  └─────────────────────┬────────────────────┘   │
│                        │ authenticated token     │
│  ┌─────────────────────▼────────────────────┐   │
│  │       Authorization Interceptor          │   │
│  │  - Claim presence validation             │   │
│  │  - Token expiry double-check             │   │
│  │  - User context population               │   │
│  │  - Request attribute injection           │   │
│  └─────────────────────┬────────────────────┘   │
│                        │ user context injected   │
│  ┌─────────────────────▼────────────────────┐   │
│  │  controller  (@PreAuthorize RBAC)         │   │
│  │  REST endpoints + request validation      │   │
│  └─────────────────────┬────────────────────┘   │
│                        │                        │
│  ┌─────────────────────▼────────────────────┐   │
│  │  service  (interface + impl)              │   │
│  │  Business logic + orchestration           │   │
│  └─────────────────────┬────────────────────┘   │
│                        │                        │
│  ┌─────────────────────▼────────────────────┐   │
│  │  repository  (Spring Data JPA)            │   │
│  │  PostgreSQL — schema via Liquibase        │   │
│  └──────────────────────────────────────────┘   │
│                                                 │
│  ┌──────────┐ ┌────────────┐ ┌───────────────┐ │
│  │  model   │ │  mapper    │ │  exception    │ │
│  │  entity  │ │  MapStruct │ │  GlobalHandler│ │
│  │  request │ │            │ │  Custom Excs  │ │
│  │  response│ └────────────┘ └───────────────┘ │
│  └──────────┘                                   │
└─────────────────────────────────────────────────┘
```

### Architectural Principles

- **Clean layered architecture** — Security Filter Chain → Authorization Interceptor → Controller → Service → Repository. No layer skipping.
- **Authorization Interceptor** — runs between the security filter chain and the controller. Validates claim presence, double-checks token expiry, and injects user context into request attributes before any business logic executes.
- **Unified `model` package** — entities, request DTOs, and response DTOs colocated under sub-packages: `model/` (JPA entities), `model/request/`, `model/response/`.
- **Constructor injection** throughout — no field injection, making dependencies explicit and testable.
- **`ApiResponse<T>` wrapper** — all endpoints return a consistent envelope:
  ```json
  {
    "status": "success",
    "message": "...",
    "data": { }
  }
  ```
- **UUID primary keys** on every table.
- **SOLID principles** — interfaces for all services, single responsibility per class.

---

## Project Structure

```
tvarah/
├── src/
│   ├── main/
│   │   ├── java/com/tvarah/
│   │   │   ├── TvarahApplication.java
│   │   │   ├── config/
│   │   │   │   ├── DatabaseConfig.java       # JPA / datasource config
│   │   │   │   └── OpenApiConfig.java        # Swagger / OpenAPI config
│   │   │   ├── controller/                   # REST controllers
│   │   │   ├── service/                      # Service interfaces
│   │   │   │   └── impl/                     # Service implementations
│   │   │   ├── repository/                   # Spring Data JPA repositories
│   │   │   ├── model/
│   │   │   │   ├── BaseEntity.java           # Common id / audit fields
│   │   │   │   ├── request/                  # Inbound DTOs
│   │   │   │   └── response/
│   │   │   │       └── ApiResponse.java      # Unified response wrapper
│   │   │   ├── mapper/                       # MapStruct mappers
│   │   │   ├── security/
│   │   │   │   ├── SecurityConfig.java       # HTTP security + CORS
│   │   │   │   ├── JwtAuthConverter.java     # Keycloak roles → GrantedAuthority
│   │   │   │   └── SecurityUtils.java        # JWT claim helpers
│   │   │   └── exception/
│   │   │       ├── GlobalExceptionHandler.java
│   │   │       ├── ResourceNotFoundException.java
│   │   │       ├── BadRequestException.java
│   │   │       └── UnauthorizedException.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── db/changelog/
│   │           ├── db.changelog-master.xml   # Liquibase master
│   │           └── changes/                  # Versioned changesets
│   └── test/
│       ├── java/com/tvarah/
│       │   └── TvarahApplicationTests.java
│       └── resources/application.yml
├── docs/
│   └── puml/
│       ├── architecture.puml
│       ├── er-diagram-business.puml
│       └── er-diagram-technical.puml
├── Dockerfile
├── docker-compose.yml
├── .env.example
└── pom.xml
```

---

## Security & Authorization

Tvarah uses **Keycloak as an external identity and authorization server**. The service acts as an **OAuth2 Resource Server** — it never issues tokens, only validates them.

### Flow

```
User  ──login──▶  Keycloak  ──JWT──▶  User
User  ──HTTP + Bearer JWT──▶  Tvarah API
Tvarah  ──JWKS fetch──▶  Keycloak  (token validation)
```

### Key Components

| Component | Role |
|---|---|
| `SecurityConfig` | Configures HTTP security rules, CSRF, stateless session, and method-level security |
| `JwtAuthConverter` | Extracts roles from the `resource_access.<client-id>.roles` JWT claim and maps them to Spring `GrantedAuthority` |
| `SecurityUtils` | Convenience methods to read the current principal's subject, email, and roles from `SecurityContextHolder` |

### Keycloak Setup

1. Create a **realm** named `tvarah` (or your custom value via `KEYCLOAK_REALM`).
2. Create a **client** named `tvarah-client` (confidential or public depending on your frontend).
3. Define roles under the client (e.g., `ROLE_ADMIN`, `ROLE_RECRUITER`, `ROLE_VIEWER`).
4. Assign roles to users or groups within the realm.
5. The `JwtAuthConverter` maps `resource_access.tvarah-client.roles` → Spring Security roles.

### RBAC

Secure endpoints using Spring's `@PreAuthorize`:

```java
@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/admin/users")
public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() { ... }

@PreAuthorize("hasAnyRole('ADMIN', 'RECRUITER')")
@PostMapping("/candidates")
public ResponseEntity<ApiResponse<CandidateResponse>> createCandidate(...) { ... }
```

---

## Database & Liquibase

### PostgreSQL

- Connection pooled via **HikariCP** (max 10 connections, min idle 2).
- `ddl-auto: validate` — Hibernate validates schema against entities; Liquibase owns all DDL.
- All tables use **UUID** primary keys.
- Audit columns (`created_on`, `updated_on`) use `TIMESTAMPTZ DEFAULT now()`.

### Liquibase

Schema versioning is managed through Liquibase changelogs:

```
src/main/resources/db/changelog/
├── db.changelog-master.xml          # Master file — includes all changesets
└── changes/
    ├── 001-create-status.xml
    ├── 002-create-industry.xml
    ├── 003-create-department.xml
    └── ...
```

**Changeset naming convention:** `{sequence}-{description}.xml`

**Guidelines:**
- Each changeset must have a unique `id` and `author`.
- Use `rollback` tags wherever possible for safe reversals.
- Changesets are idempotent by design (`runOnChange: false` by default).
- Master file uses `<include file="..."/>` to pull in individual changesets in order.

---

## API Documentation

Swagger UI is available at runtime:

| URL | Description |
|---|---|
| `http://localhost:8080/api/swagger-ui.html` | Interactive Swagger UI |
| `http://localhost:8080/api/v3/api-docs` | Raw OpenAPI JSON spec |

The API is documented via SpringDoc OpenAPI. Controllers are annotated with `@Tag`, `@Operation`, and `@ApiResponse` for rich documentation.

All endpoints return:

```json
{
  "status": "success | error",
  "message": "Human-readable message",
  "data": { }
}
```

Error responses include:

```json
{
  "status": "error",
  "message": "Resource not found with id: abc-123",
  "data": null
}
```

---

## Schema Diagrams (PlantUML)

All schema diagrams live in `docs/puml/`. Three diagrams are provided:

| File | Description |
|---|---|
| `architecture.puml` | System architecture — components, security layer, infrastructure |
| `er-diagram-business.puml` | Business-level ER — entity names and business relationships in plain language |
| `er-diagram-technical.puml` | Technical ER — full column definitions with types, constraints, indexes, and FK references |

### Rendering Diagrams

**Option 1 — VS Code**
Install the [PlantUML extension](https://marketplace.visualstudio.com/items?itemName=jebbs.plantuml), then open any `.puml` file and press `Alt+D` to preview.

**Option 2 — IntelliJ IDEA**
Install the PlantUML Integration plugin. Right-click any `.puml` file → `PlantUML`.

**Option 3 — CLI**
```bash
# Install PlantUML (requires Java)
brew install plantuml

# Render to PNG
plantuml docs/puml/er-diagram-technical.puml
plantuml docs/puml/er-diagram-business.puml
plantuml docs/puml/architecture.puml

# Output files will appear alongside the .puml files
```

**Option 4 — Online**
Paste the contents of any `.puml` file into [plantuml.com/plantuml](http://www.plantuml.com/plantuml/uml/).

Pre-rendered PNGs are committed under `out/docs/puml/` for quick reference without a local renderer.

---

## Environment Variables

Copy `.env.example` to `.env` and update values before running:

```bash
cp .env.example .env
```

| Variable | Default | Description |
|---|---|---|
| `DB_HOST` | `localhost` | PostgreSQL host |
| `DB_PORT` | `5432` | PostgreSQL port |
| `DB_NAME` | `tvarah_db` | Database name |
| `DB_USER` | `tvarah_user` | Database username |
| `DB_PASSWORD` | `tvarah_pass` | Database password |
| `KEYCLOAK_REALM` | `tvarah` | Keycloak realm name |
| `KEYCLOAK_CLIENT_ID` | `tvarah-client` | Keycloak client ID |
| `KEYCLOAK_ISSUER_URI` | `http://localhost:8180/realms/tvarah` | JWT issuer URI |
| `KEYCLOAK_JWK_URI` | `http://localhost:8180/realms/tvarah/protocol/openid-connect/certs` | JWKS endpoint |
| `KC_ADMIN` | `admin` | Keycloak admin username (Docker only) |
| `KC_ADMIN_PASSWORD` | `admin` | Keycloak admin password (Docker only) |

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- Docker & Docker Compose (for containerized setup)
- PostgreSQL 16 (for local-only setup)

---

## Running with Docker

The fastest way to get the full stack running (PostgreSQL + Keycloak + Tvarah app):

```bash
# 1. Clone the repository
git clone <repo-url>
cd tvarah

# 2. Create your .env file
cp .env.example .env

# 3. Start all services
docker compose up --build

# 4. Wait for all services to be healthy, then access:
#    App:      http://localhost:8080/api/swagger-ui.html
#    Keycloak: http://localhost:8180  (admin / admin)
```

### Service Ports

| Service | Port | Description |
|---|---|---|
| Tvarah App | `8080` | REST API + Swagger UI |
| PostgreSQL | `5432` | Database |
| Keycloak | `8180` | Auth server admin console |

### Stopping

```bash
docker compose down          # stop containers
docker compose down -v       # stop and remove volumes (wipes DB data)
```

---

## Running Locally

For local development without Docker:

### 1. Start PostgreSQL

```bash
docker run -d \
  --name tvarah-postgres \
  -e POSTGRES_DB=tvarah_db \
  -e POSTGRES_USER=tvarah_user \
  -e POSTGRES_PASSWORD=tvarah_pass \
  -p 5432:5432 \
  postgres:16-alpine
```

### 2. Start Keycloak

```bash
docker run -d \
  --name tvarah-keycloak \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  -e KC_HTTP_PORT=8180 \
  -p 8180:8180 \
  quay.io/keycloak/keycloak:24.0 start-dev
```

Configure the `tvarah` realm and `tvarah-client` via the Keycloak admin console at `http://localhost:8180`.

### 3. Build and Run

```bash
# Build
mvn clean package -DskipTests

# Run
java -jar target/tvarah-1.0.0-SNAPSHOT.jar
```

Or run directly via Maven:

```bash
mvn spring-boot:run
```

### 4. Verify

```
GET http://localhost:8080/api/actuator/health
```

Expected response:
```json
{ "status": "UP" }
```

---

## Actuator & Observability

Spring Boot Actuator is enabled with the following endpoints exposed:

| Endpoint | URL | Description |
|---|---|---|
| Health | `GET /api/actuator/health` | Liveness and readiness check |
| Info | `GET /api/actuator/info` | Application metadata |
| Metrics | `GET /api/actuator/metrics` | JVM, HTTP, HikariCP metrics |

Health details are shown only to authorized users (`show-details: when-authorized`).

### Logging

- SLF4J is used throughout. No sensitive data (passwords, tokens) is logged.
- Log levels configurable via `application.yml`:
  - `com.tvarah` → `DEBUG`
  - `org.springframework.security` → `INFO`
  - `org.hibernate.SQL` → `DEBUG`

---

## Exception Handling

Global exception handling is centralized in `GlobalExceptionHandler` (`@ControllerAdvice`).

| Exception | HTTP Status | Use Case |
|---|---|---|
| `ResourceNotFoundException` | `404 Not Found` | Entity lookup by ID fails |
| `BadRequestException` | `400 Bad Request` | Invalid input or business rule violation |
| `UnauthorizedException` | `401 Unauthorized` | Missing or insufficient permissions |
| `MethodArgumentNotValidException` | `400 Bad Request` | Bean Validation (`@Valid`) failures |
| Unhandled `Exception` | `500 Internal Server Error` | Catch-all for unexpected errors |

All errors return the standard `ApiResponse` envelope with `"status": "error"`.

---

## Contributing

1. Follow the clean architecture layer boundaries — no repository calls from controllers.
2. All new database changes must be added as versioned Liquibase changesets — never modify existing changelogs.
3. New endpoints must be annotated for Swagger (`@Operation`, `@Tag`).
4. Secure all endpoints with appropriate `@PreAuthorize` role checks.
5. Use `ApiResponse<T>` as the return type for all controller methods.
6. All entities must extend `BaseEntity` and use UUID PKs.
7. Update the relevant PlantUML diagrams in `docs/puml/` when adding or modifying entities.
