# Jenkins CI/CD Demo — Spring Boot

A simple Spring Boot REST API that demonstrates a complete CI/CD pipeline using Jenkins, Docker, and Maven.

---

## Tech Stack

| Layer       | Technology              |
|-------------|-------------------------|
| Language    | Java 17                 |
| Framework   | Spring Boot 3.2         |
| Build       | Maven 3.9               |
| CI/CD       | Jenkins (Declarative Pipeline) |
| Container   | Docker + Docker Compose |
| Testing     | JUnit 5, MockMvc        |

---

## Project Structure

```
jenkins-cicd-demo/
├── src/
│   ├── main/java/com/demo/
│   │   ├── DemoApplication.java      # Entry point
│   │   ├── controller/
│   │   │   └── HelloController.java  # REST endpoints
│   │   ├── service/
│   │   │   └── HelloService.java     # Business logic
│   │   └── model/
│   │       └── ApiResponse.java      # Response model
│   └── test/java/com/demo/
│       └── controller/
│           └── HelloControllerTest.java
├── Dockerfile                        # Multi-stage Docker build
├── docker-compose.yml                # App + Jenkins setup
├── Jenkinsfile                       # CI/CD pipeline definition
└── pom.xml
```

---

## REST API Endpoints

| Method | Endpoint       | Description                        |
|--------|----------------|------------------------------------|
| GET    | `/api/hello`   | Returns "Hello, World!"            |
| GET    | `/api/hello?name=Parth` | Returns "Hello, Parth!"  |
| GET    | `/api/health`  | Application health check           |
| GET    | `/api/info`    | App version/info                   |
| GET    | `/actuator/health` | Spring Boot actuator health    |

---

## Running Locally

### With Maven
```bash
mvn spring-boot:run
```
App starts at `http://localhost:8080`

### With Docker
```bash
docker build -t jenkins-cicd-demo .
docker run -p 8080:8080 jenkins-cicd-demo
```

### With Docker Compose (App + Jenkins together)
```bash
docker compose up -d
```
- App: `http://localhost:8080`
- Jenkins: `http://localhost:8081`

---

## Running Tests

```bash
mvn test
```

---

## CI/CD Pipeline — Jenkins

### Pipeline Stages

```
Checkout → Build → Test → Code Coverage → Package → Docker Build → Docker Push → Deploy
```

| Stage          | What it does                                              |
|----------------|-----------------------------------------------------------|
| **Checkout**   | Pulls source code from SCM (GitHub)                       |
| **Build**      | Compiles the Java source with `mvn compile`               |
| **Test**       | Runs JUnit tests, publishes results in Jenkins            |
| **Code Coverage** | Generates JaCoCo coverage report                       |
| **Package**    | Creates the executable JAR, archives it as artifact       |
| **Docker Build** | Builds Docker image tagged with the build number        |
| **Docker Push** | Pushes image to Docker Hub (runs on `main` branch only) |
| **Deploy**     | Runs the new container via Docker Compose                 |

### Setting Up Jenkins

1. **Start Jenkins** via Docker Compose:
   ```bash
   docker compose up -d jenkins
   ```

2. **Unlock Jenkins** — Get the initial admin password:
   ```bash
   docker exec cicd-jenkins cat /var/jenkins_home/secrets/initialAdminPassword
   ```
   Open `http://localhost:8081` and paste the password.

3. **Install Plugins** — Install the suggested plugins, plus:
   - Docker Pipeline
   - JaCoCo
   - Git

4. **Configure Tools** in *Manage Jenkins → Tools*:
   - Add JDK named `JDK-17`
   - Add Maven named `Maven-3.9`

5. **Add Docker Hub credentials** in *Manage Jenkins → Credentials*:
   - Kind: Username with password
   - ID: `docker-hub-credentials`

6. **Create a Pipeline Job**:
   - New Item → Pipeline
   - Under Pipeline, set Definition to *Pipeline script from SCM*
   - SCM: Git → paste your repo URL
   - Script Path: `Jenkinsfile`
   - Save → Build Now

---

## How CI/CD Works — Flow Diagram

```
Developer pushes code
        │
        ▼
   GitHub / SCM
        │  (webhook trigger)
        ▼
     Jenkins
        │
   ┌────┴────────────────────────────────┐
   │  Stage 1: Checkout                  │
   │  Stage 2: Build (mvn compile)       │
   │  Stage 3: Test (JUnit)              │
   │  Stage 4: Code Coverage (JaCoCo)   │
   │  Stage 5: Package (JAR)             │
   │  Stage 6: Docker Build              │
   └────────────────────────────────────┘
        │  (main branch only)
        ▼
   Docker Hub  ←── Docker Push
        │
        ▼
   Docker Compose Deploy
        │
        ▼
   App running at :8080
```

---

## Environment Variables

| Variable                  | Default | Description              |
|---------------------------|---------|--------------------------|
| `SERVER_PORT`             | `8080`  | Application port         |
| `SPRING_PROFILES_ACTIVE`  | —       | Active Spring profile    |

---

## Multi-Stage Docker Build

The `Dockerfile` uses a two-stage build to keep the final image small:

1. **Builder stage** — uses the full Maven + JDK image to compile and package.
2. **Runtime stage** — copies only the JAR into a lightweight JRE Alpine image (~80MB).

---

## Author

**Parth Kumar**  
Spring Boot + Jenkins CI/CD Demo Project
