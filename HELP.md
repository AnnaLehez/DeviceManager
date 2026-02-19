### 🚀 Quick Start & Demo Mode

#### Prerequisites:
Before starting the application, ensure that the Docker daemon is running and the following host ports are available:
* **8080**: Used by the Spring Boot API `devices-api`.
* **5432**: Used by the PostgreSQL Database `postgres-db`.

By default, the project starts in a **Stateless Demo Mode** for zero-config evaluation.

- **Command (Run from project root):** `docker compose up --build -d`
- **Behavior:** Spins up API + PostgreSQL, creates schema, and populates sample data.
- **Persistence:** **None.** No Docker volumes are mapped; data is wiped on container destruction for a clean test state.



**⏳ Startup Time Note:** The initial command may take a few minutes (approx. 3-5 mins) as Docker downloads the base images, Maven resolves dependencies, and the application compiles. Please wait until your terminal displays a success message indicating the database is `Healthy` and the API has started:

```text
 ✔ Image devicemanager-api  Built                                    
 ✔ Container devices-db     Healthy                                  
 ✔ Container devices-api    Started                                    
```

---

### ⌨️ Command Cheat Sheet

| **Goal**             | **Command**                                         |
|----------------------|-----------------------------------------------------|
| **Start Demo Mode**  | `docker compose up --build -d`                      |
| **Start Clean Mode** | `APP_PROFILE=prod docker compose up --build -d`     |
| **Total Reset**      | `docker compose down -v`                            |
| **Check Logs**       | `docker logs -f devices-api`                        |
| **Run Unit Tests**   | `mvn clean test`                                    |
| **Start DB**         | `docker compose up postgres-db -d`                  |
| **Run E2E Tests**    | `mvn verify -Dsurefire.skip=true`(only if DB is up) |

---
## 📖 Useful Endpoints


- **API Directory (Start Page):** [`http://localhost:8080/`](http://localhost:8080/)
- **API Endpoints:** [`http://localhost:8080/api/v1/devices`](http://localhost:8080/api/v1/devices)
- **Swagger UI:** [`http://localhost:8080/swagger-ui.html`](http://localhost:8080/swagger-ui.html)

#### Monitoring

- **Application Health (with DB status):** [`http://localhost:8080/actuator/health`](http://localhost:8080/actuator/health)
- **Kubernetes Liveness Probe:** [`http://localhost:8080/actuator/health/liveness`](http://localhost:8080/actuator/health/liveness)
- **Kubernetes Readiness Probe:** [`http://localhost:8080/actuator/health/readiness`](http://localhost:8080/actuator/health/readiness)
- **Prometheus Metrics Export:** [`http://localhost:8080/actuator/prometheus`](http://localhost:8080/actuator/prometheus)
---
### ⚙️ Architecture Highlights

- **Database-as-Code:** Flyway manages all SQL in `src/resources/db/migration`. Hibernate is set to `validate` only.

- **Concurrency:** **Optimistic Locking** via `@Version` prevents "Lost Updates" during parallel edits.

- **Environment:** Managed via `.env`. To connect to an **external DB**, override `DB_HOST` in the environment or `.env` file.

---
## 🔮 Known Issues & Limitations

For a detailed breakdown of architectural decisions, technical debt, and future improvements, please refer to the [Future Roadmap section in the README.md](./README.md).