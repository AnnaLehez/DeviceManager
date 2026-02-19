# 📱 Device Manager API

An enterprise-grade Spring Boot REST API for managing electronic device inventories. Built with a focus on concurrency safety, automated database migrations, and a "zero-config" containerized setup.

## 🛠 Tech Stack

- **Java 21** & **Spring Boot 3.4.2**
- **PostgreSQL 16** (Alpine)
- **Flyway** (Database Migrations)
- **MapStruct** (Entity-DTO Mapping)
- **Docker & Docker Compose** (Containerization)
- **Testcontainers** (E2E Test Isolation)

---

## 📋 Prerequisites

Ensure Docker is installed and the following host ports are available:

- **8080** (API)
- **5432** (PostgreSQL)

> **Security Note:** For the purpose of this technical evaluation, an `.env` file containing demo credentials has been intentionally committed. In a production environment, this file is git-ignored and secrets are managed via a secure vault.
---
## 🚀 Quick Start & Run Modes

The application uses Spring Profiles (`APP_PROFILE`) to manage its data state.

### 1. Demo Mode (Default)

Starts the app and populates the database with sample data using Flyway

```
docker compose up --build -d
```

- **API Endpoints:** `http://localhost:8080/api/v1/devices`

- **Swagger UI:** `http://localhost:8080/swagger-ui.html`

### 2. Clean / Production Mode

Starts the application with the full schema but **zero data**.

```
APP_PROFILE=prod docker compose up --build -d
```
---
## 🏗 Key Architectural Features

- **Concurrency Control:** Implements **Optimistic Locking** (`@Version`) to prevent "Lost Updates" when multiple users edit the same device simultaneously.

- **Database-as-Code:** Hibernate is restricted to `validate` mode. All schema creation and modification are strictly managed by **Flyway**.

- **Ephemeral Infrastructure:** No Docker Volumes are mapped. Destroying the containers (`docker compose down -v`) wipes the state, guaranteeing a reproducible testing environment.
---
## 🧪 Testing Strategy

The project utilizes a **dedicated test database** (`devices_test_db`) for End-to-End (E2E) and integration testing to ensure absolute test isolation.

* **Data Safety:** Tests do **not** interact with the primary development or demo database (`devices_db`).
* **Configuration:** The test suite connects to `jdbc:postgresql://localhost:5432/devices_test_db` (typically managed via the `test` Spring profile).
* **Execution:** This guarantees that local development data is never mutated or dropped by automated tests.
> **Note for Evaluator:** Before executing `mvn verify` or running E2E tests locally, ensure that a PostgreSQL instance is running on port 5432 and the `devices_test_db` has been created.
---

## 🔮 Roadmap & Technical Debt

To transition this project to a production-ready state, the following improvements are planned:

1. **Enhanced Search & Sanitization:** Implement case-insensitive filtering for text searches and automatic whitespace trimming on incoming API requests to improve data consistency and user experience

2. **Filtering:** Implement multi-field search combinations (e.g., Name + Brand)

3. **Security:** Implement Spring Security with JWT to restrict write operations (`POST/PUT/DELETE`).

4. **Normalization:** Analyze data to understand if we can extract `Brand`and `Model` into a dedicated table to reduce data redundancy.

5. **Data Persistence:** Transition from ephemeral containers to persistent Docker Volumes for local development, or attach a managed database service (e.g., AWS RDS, PostgreSQL) for production workloads.

6. **Pagination:** Add `Pageable` support to the `GET` endpoints to prevent memory strain on large datasets.

7. **Soft Deletes:** Implement a `deleted_at` timestamp rather than hard-deleting rows for audit compliance.

8. **Data Auditing:** Integrate Spring Data JPA Auditing (`@LastModifiedDate`) to automatically track entity lifecycles, ensuring a reliable timeline for when records are created or modified.

9. **Caching:** Implement a caching layer (e.g., **Redis** or Spring `@Cacheable` with Caffeine) for high-traffic, read-heavy endpoints like the device catalog to reduce database load.

10. **Observability & Telemetry:** Integrate Micrometer with Prometheus and Grafana to expose JVM metrics and implement structured logging with distributed tracing (e.g., OpenTelemetry) to monitor API latency and system health.
