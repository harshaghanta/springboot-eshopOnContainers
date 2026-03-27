# GitHub Copilot Instructions

## Project Overview

This is **springboot-eshopOnContainers**, a Spring Boot microservices reference application demonstrating modern distributed system architecture with containerization, event-driven communication, and a full observability stack.

## Architecture

### Microservices

| Service | Path | Description |
|---------|------|-------------|
| Catalog API | `src/Services/catalogapi` | Product catalog management backed by SQL Server |
| Basket API | `src/Services/basketapi` | Shopping cart backed by Redis |
| Ordering API | `src/Services/Ordering/orderapi` | Order management with DDD/CQRS patterns |
| Ordering Background Tasks | `src/Services/Ordering/ordering-backgroundtasks` | Async order processing |
| Ordering Notification | `src/Services/Ordering/ordering-notification` | Event-driven notifications |
| Payment API | `src/Services/Payment/paymentapi` | Payment processing |
| Webhooks API | `src/Services/Webhooks/webhooks-api` | Webhook management |
| Webhooks Client | `src/Services/Webhooks/webhooks-client` | Webhook client |
| Web SPA | `src/Web/WebSpa` | Angular 11 frontend + Spring Boot server |

### Shared Building Blocks (`src/BuildingBlocks/`)

- **eventbus** – Abstract event bus interface
- **eventbus-rabbitmq** – RabbitMQ implementation of the event bus
- **integration-eventlog** – Outbox/Inbox pattern for reliable event delivery

## Technology Stack

- **Java 25**, **Spring Boot 4.0.1**, **Maven 3.9**
- **Spring Data JPA / Hibernate** – ORM for SQL Server
- **SQL Server** – Primary relational database (CatalogDB, OrderingDB)
- **Redis** – Basket service cache (via Jedis)
- **RabbitMQ** – Asynchronous message broker / event bus
- **Keycloak** – OAuth2 / OpenID Connect identity provider
- **Envoy** – API Gateway (`src/Gateways/Envoy/`)
- **Angular 11 + TypeScript** – Web SPA frontend
- **Docker / Docker Compose** – Local orchestration
- **Kubernetes + Helm** – Production orchestration (`deploy/k8s/`)
- **Istio** – Optional service mesh
- **Prometheus + Grafana + Loki + Tempo** – Metrics, logs, and traces
- **OpenTelemetry** – Distributed tracing via Java agent

## Key Architectural Patterns

### Event-Driven Communication
- Services communicate asynchronously via **integration events** published to RabbitMQ.
- **Outbox Pattern**: Events are first saved to an `OutboxEntity` table within the same transaction as the business operation, then published asynchronously by a background processor. This guarantees at-least-once delivery.
- **Inbox Pattern**: Incoming events are recorded in `InboxMessage` to ensure idempotent processing.
- Integration event handlers are named `*IntegrationEventHandler`.

### Domain-Driven Design (Ordering Service)
- The ordering service is split into `ordering-domain`, `ordering-infrastructure`, and `orderapi` modules.
- Use domain entities, value objects, and domain events within `ordering-domain`.
- Keep infrastructure concerns (JPA repositories, SQL queries) in `ordering-infrastructure`.
- Use the **Pipelinr** library for command/query dispatch (CQRS-style).

### API Design
- REST APIs follow standard Spring MVC conventions.
- All services expose **SpringDoc / OpenAPI** documentation (Swagger UI).
- JWT tokens from Keycloak are validated using `spring-security-oauth2-resource-server`.

## Coding Conventions

### Java / Spring Boot
- Use **constructor injection** (not field injection) for Spring beans.
- Annotate REST controllers with `@RestController` and map routes with `@RequestMapping` / `@GetMapping` / `@PostMapping` etc.
- Use `@Service` for business logic, `@Repository` for data access.
- Prefer Spring Data JPA repositories; write JPQL or native queries only when necessary.
- Use `@Transactional` on service methods that perform write operations.
- Log using SLF4J (`private static final Logger log = LoggerFactory.getLogger(MyClass.class);`). Structured JSON logging is configured via `logback-spring.xml`.
- Configuration values come from `application.yml` (and profile variants); inject them via `@Value` or `@ConfigurationProperties`.

### Event Bus
- Define integration events as plain Java records or classes extending a common base.
- Register event handlers in the Spring context; they are auto-discovered by the event bus.
- Always save events to the outbox within the same database transaction as the triggering operation.

### Maven / Build
- Every service inherits from `src/BuildingBlocks/parent-pom/pom.xml` which manages common dependency versions.
- Use the Spring Boot Maven Plugin with Paketo Buildpacks to produce OCI images: `mvn spring-boot:build-image`.
- Shared libraries (eventbus, integration-eventlog) are published to GitHub Packages; provide a `settings.xml` with credentials when building locally.
- The canonical `settings.xml` for GitHub Packages is at `src/settings.xml`.

### Frontend (Web SPA)
- Angular 11 source lives in `src/Web/WebSpa/WebSpa-Angular/`.
- Use **Bootstrap 4** for layout and styling; avoid custom CSS where Bootstrap utilities suffice.
- HTTP calls to backend services go through the Envoy API gateway.
- Follow the Angular style guide: feature modules, services for API calls, interfaces for models.

## Local Development

### Running with Docker Compose
```bash
cd src/
docker-compose build
docker-compose up
```
- Web SPA: `http://host.docker.internal:8080`
- Default credentials: `alice@gmail.com` / `Pass@word`
- Grafana: `http://host.docker.internal:3000` (admin / admin)

### Running a Single Service
```bash
cd src/Services/catalogapi
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### Building Docker Images
```bash
mvn spring-boot:build-image -s ../../settings.xml
```

## Testing

- Unit and integration tests live in `src/test/java/` within each service module.
- Run tests with `mvn test`.
- Integration tests may require a running SQL Server or Redis; use the `local` Spring profile and Docker Compose to spin up dependencies.

## CI/CD

- GitHub Actions workflows are in `.github/workflows/`.
- Each service has a dedicated workflow triggered by path-based pushes to `main`.
- All workflows use the reusable composite action at `.github/workflows/composite/build-push/action.yml` to build and push Docker images.
- Shared libraries are published to GitHub Packages via the `eventbus-publish.yml` workflow (manual trigger).
- Helm charts are released via `chart-release.yml` (manual trigger).

## Observability

- **Metrics**: Micrometer exports to Prometheus; view in Grafana.
- **Logs**: Logback (JSON) → Promtail → Loki → Grafana.
- **Traces**: OpenTelemetry Java agent → OTel Collector → Tempo → Grafana.
- Actuator endpoints (`/actuator/health`, `/actuator/metrics`, `/actuator/prometheus`) are enabled on all services.

## Security

- Authentication is centralised in **Keycloak**; services act as OAuth2 resource servers.
- Secrets (DB passwords, RabbitMQ credentials) are mounted from `src/Secrets/` in Docker Compose and from Kubernetes Secrets in production.
- Never hardcode credentials or secrets in source code.

## Deployment (Kubernetes)

```bash
cd deploy/k8s/
helm install eshop ./eshop
```

- Kubernetes manifests and Helm charts are in `deploy/k8s/`.
- Istio `VirtualService` and `DestinationRule` resources are in `deploy/k8s/istio/`.
- The Catalog API has a Horizontal Pod Autoscaler (HPA) configured as an example.
