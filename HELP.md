### 🚀 Quick Start & Demo Mode

#### Prerequisites:
Before running the application, ensure the following ports are available on your host machine:
*8080*: Used by the `devices-api`.
*5432*: Used by the `postgres-db`.

By default, the project starts in a **Stateless Demo Mode** for zero-config evaluation.

- **Command (Run from project root):** `docker compose up --build -d`
- **Behavior:** Spins up API + PostgreSQL, creates schema, and populates sample data.
- **Persistence:** **None.** No Docker volumes are mapped; data is wiped on container destruction for a clean test state.

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

- **API Base:** `http://localhost:8080/api/v1/devices`
- **Documentation:** `http://localhost:8080/swagger-ui.html`
- **Health Check:** `http://localhost:8080/actuator/health`
---
### ⚙️ Architecture Highlights

- **Database-as-Code:** Flyway manages all SQL in `src/resources/db/migration`. Hibernate is set to `validate` only.

- **Concurrency:** **Optimistic Locking** via `@Version` prevents "Lost Updates" during parallel edits.

- **Environment:** Managed via `.env`. To connect to an **external DB**, override `DB_HOST` in the environment or `.env` file.

---
## 🔮 Known Issues & Limitations

For a detailed breakdown of architectural decisions, technical debt, and future improvements, please refer to the [Future Roadmap section in the README.md](./README.md).