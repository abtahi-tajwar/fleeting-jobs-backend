# Fleeting Jobs Backend — Application Architecture Context

This file is the **application-level architecture context** for the Fleeting Jobs backend. Any AI agent working on this codebase must read this file first, then read the relevant module context files before making changes.

**Last updated:** 2026-09-22 (demo profile seeder)

---

## How to use agent context

### Before any work

1. Read this file: `agent-context/architecture.md`
2. If the task targets a specific module, also read that module's context if it exists:
   - `agent-context/<module>/context.md`
   - Example: `agent-context/jobs/context.md`
3. If a submodule exists (currently only `profile`), also check:
   - `agent-context/profile/<submodule>/context.md`
   - Example: `agent-context/profile/education/context.md`
4. Use the existing module implementation as the reference. Do not invent new patterns.

### After any work

1. Update this architecture file if the change affected:
   - module boundaries
   - shared infrastructure (`common`)
   - worker / RabbitMQ / WebClient communication
   - auth / security
   - API response conventions
   - persistence / entity relationships
   - technology choices
2. Update the affected module's `agent-context/<module>/context.md` so architectural decisions stay current.
3. If a module context file does not exist yet, create it when that module is first meaningfully changed.

Module context files should describe: purpose, package layout, key types, API endpoints, dependencies on other modules, worker interactions, and any module-specific rules.

---

## Background

The Fleeting Jobs backend is a Java Spring Boot application that provides the core API and business logic for the Fleeting Jobs platform.

The application manages users, administrators, job postings, job sources (companies), parser templates, user profiles, and document generation. It exposes REST APIs for the frontend and coordinates business operations, data persistence, authentication, and communication with a separate worker service.

The worker service is **not** part of this Spring Boot app. It is a separate Python service that performs specialized work: scraping company job listings, talking to LLMs, and building PDFs.

---

## Repository location

```
fleeting-jobs-backend/fleeting-jobs-backend/
├── backend/            # This Spring Boot application
└── worker-service/     # Separate Python worker (scraping, LLM, PDF)
```

Backend root:

```
backend/
├── agent-context/      # Reusable AI agent context (this folder)
├── src/main/java/com/fleetingtrails/fleetingjobsbackend/
├── src/main/resources/
├── pom.xml
└── docker-compose.yml  # Local Postgres + RabbitMQ
```

Base Java package:

```
com.fleetingtrails.fleetingjobsbackend
```

Entry point: `FleetingJobsBackendApplication`. It loads `.env` via dotenv-java (`ignoreIfMissing`) into system properties before starting Spring.

---

## Technology stack

| Concern | Choice |
|---|---|
| Language / runtime | Java 25 |
| Framework | Spring Boot 4.1.0 |
| HTTP API | Spring Web MVC (`spring-boot-starter-webmvc`) |
| Security | Spring Security + JWT (`jjwt` 0.12.5) |
| Persistence | Spring Data JPA + PostgreSQL |
| Schema | Hibernate `ddl-auto=update` currently. Flyway is on the classpath but no migration files exist yet. |
| Mapping | MapStruct 1.6.3 + Lombok 1.18.46 + `lombok-mapstruct-binding` 0.2.0 |
| Validation | `jakarta.validation` / Spring Validation |
| Worker HTTP | Spring WebFlux `WebClient` |
| Worker async | RabbitMQ via Spring AMQP |
| API docs | springdoc-openapi 2.8.9 |
| CSV seeding | Apache Commons CSV 1.11.0 |
| Local infra | Docker Compose: Postgres 17 + RabbitMQ 4 (management) |

Local defaults from `application.properties`:

- App name: `fleeting-jobs-backend`
- Postgres: `jdbc:postgresql://localhost:5432/fleeting_jobs` / `postgres` / `postgres`
- Worker HTTP: `worker.base-url=http://localhost:8000`
- RabbitMQ: `localhost:5672`, credentials from env (`RABBIT_USERNAME`, `RABBIT_PASSWORD`)
- JWT: `jwt.secret` from env, expiration `86400000` ms (24h)
- CORS: `http://localhost:5173` and `http://127.0.0.1:5173`

Secrets (`JWT_SECRET`, RabbitMQ credentials) come from `.env`. Do not commit secrets.

---

## System overview

```
Frontend (Vite, :5173)
        │  REST + JWT
        ▼
Spring Boot Backend
        │
        ├── PostgreSQL (entities / repositories)
        │
        ├── WebClient  ──────────► Worker Service (:8000)
        │     sync HTTP            scrape lists, generate PDFs
        │
        └── RabbitMQ
              produce: request.job_details
              consume: receive.job_details
              consume: receive.new_job_listing
```

### When to use WebClient vs RabbitMQ

- **WebClient (synchronous HTTP to worker):** kick off a scrape of a company listing page; generate resume PDFs. The backend waits (`.block()`) for the HTTP call.
- **RabbitMQ (asynchronous):** request job-detail scraping for individual job URLs; receive scraped job descriptions; receive newly discovered job listings from the worker.

Do not replace one with the other unless explicitly requested.

---

## Module architecture

The backend follows a modular layered architecture.

Each business module is self-contained and follows this structure:

```
<module>/
├── entity/
├── repository/
├── service/
├── controller/
├── mapper/
└── dto/
```

Some modules also have extra packages when needed (`config`, `filter`, `seeder`, `enums`, `constants`, `specification`). That is allowed. Do not add new architectural layers unless explicitly requested.

### Layer responsibilities

- **Entity:** Persistence / domain models (`@Entity`).
- **Repository:** Database access via Spring Data JPA. Persistence only.
- **Service:** Business logic and orchestration.
- **Controller:** REST API endpoints. No business logic.
- **Mapper:** MapStruct conversions between entities, DTOs, and API models.
- **DTO:** Request/response payloads. Not persistence models.

### Rules

1. Keep module-specific code inside its module.
2. Controllers call services, never repositories directly.
3. Services use repositories and mappers.
4. Repositories handle persistence only.
5. Mappers handle object/DTO conversions only.
6. Do not introduce new architectural patterns unless explicitly requested.
7. Follow existing modules as the implementation reference.
8. Shared, cross-module infrastructure belongs in `common`, not in a business module.
9. Cross-module reads currently often go through the other module's **repository** (for example `JobService` uses `CompanyRepository`). Prefer that existing pattern unless asked to refactor to service-to-service calls.
10. Profile is the only module that uses `_submodules`. Do not introduce `_submodules` elsewhere unless asked.

### Typical request flow

```
Controller
  → Service
      → Repository (load / save entities)
      → Mapper (entity ↔ DTO)
      → WorkerService / RabbitProducerService (only when talking to worker)
  → Controller wraps result in API*Response
```

---

## Modules

### `common` — shared infrastructure (not a business module)

Package: `com.fleetingtrails.fleetingjobsbackend.common`

Contains cross-cutting code used by all modules:

- `config/`
  - `WebConfig` — CORS
  - `WebClientConfig` / `WorkerWebClientConfig` — worker `WebClient` bean (`workerWebClient`)
  - `RabbitConfig` — queue names and JSON converter
- `exception/`
  - `ResourceNotFoundException`
  - `GlobalExceptionHandler` (`@RestControllerAdvice`)
- `response/` — standard API envelopes
- `services/WorkerService` — thin holder of the worker `WebClient`
- `services/rabbit/` — producer, listeners, message DTOs
- `seeder/` + `SeedRunner` — startup seed orchestration

Do not put business rules here.

### `auth` — authentication and authorization

Purpose: login, first-login password setup (OTP), JWT issue/validation, Spring Security filter chain, RBAC role/permission catalog, admin user seeding.

See `agent-context/auth/context.md` for OTP / set-password and the roles/permissions seed.

Notable packages (users still live in `user`; roles/permissions live here):

```
auth/
├── config/SecurityConfig.java, PasswordConfig.java
├── controller/AuthController.java
├── dto/
├── entity/RoleEntity.java, PermissionEntity.java
├── enums/AppModule.java
├── filter/JwtAuthenticationFilter.java
├── repository/RoleRepository.java, PermissionRepository.java
├── seeder/AuthSeeder.java, PermissionSeeder.java
└── service/AuthService.java, JwtService.java
```

- Public endpoints: `/auth/**`, `POST /users`
- All other HTTP requests require a valid JWT (`Authorization: Bearer ...`)
- Stateless sessions (`SessionCreationPolicy.STATELESS`)
- CSRF disabled
- Permanent password and OTP are both hashed with BCrypt on `UserEntity`
- Seeded users receive OTP only (`password` is null) and must call set-password before they can use protected APIs
- Login with OTP returns `requiresPasswordSetup: true` and **no JWT**
- `POST /auth/set-password` takes `{ email, otp, password }`, clears OTP, stores the new password, and issues a JWT
- `AuthService` implements `UserDetailsService` and authenticates by email
- `UserEntity.role` is still the old enum (`USER`, `ADMIN`) used by login responses. Endpoint authorization is still authenticated-vs-public, not yet enforced from the `roles` / `permissions` tables
- RBAC catalog: `roles` + `permissions`, seeded from `src/main/resources/seeds/auth/roles.json` and `src/main/resources/seeds/auth/permissions.json`
- Module/submodule names come from `AppModule` / `AppModule.Submodule`

API:

- `POST /auth/login` → `{ token, userId, email, role, requiresPasswordSetup }`
- `POST /auth/set-password` → `{ token, userId, email, role, requiresPasswordSetup: false }`

### `user` — platform users regardless of role

Purpose: CRUD for users (job seekers and admins). Identity and contact/profile header fields live here.

- Entity: `UserEntity` → table `users`
- Roles: `Role.USER`, `Role.ADMIN` (default `USER`)
- Unique email
- `password` (nullable until first set) and `otp` (cleared after first password set); both BCrypt hashed
- Create hashes password with BCrypt and leaves `otp` null
- MapStruct ignores password on response DTOs and ignores otp on create/update

API (`/users`):

- `POST /users` — public registration
- `GET /users`
- `GET /users/{id}`
- `PUT /users/{id}`
- `DELETE /users/{id}`

### `profile` — user profile used to build CVs / cover letters

Purpose: structured profile data used later for document generation.

There is **no parent Profile entity**. Profile is a family of user-owned submodules:

```
profile/_submodules/
├── education/     → table educations        → /users/{userId}/education
├── workexperience → table work_experiences  → /users/{userId}/experiences
├── skill/         → table skills            → /users/{userId}/skills
├── certification/ → table certifications    → /users/{userId}/certifications
└── award/         → table awards            → /users/{userId}/awards
```

Each submodule has the full layered stack: `entity`, `repository`, `service`, `controller`, `mapper`, `dto`.

`profile/seeder/ProfileSeeder` loads `seeds/profile/demo-profile.json` onto the seeded SUBSCRIBER user. See `agent-context/profile/context.md`.

Each profile entity has `@ManyToOne` to `UserEntity` (`user_id`).

Typical submodule API:

- `POST /users/{userId}/<resource>`
- `GET /users/{userId}/<resource>`
- `PUT /users/{userId}/<resource>/{id}`
- `DELETE /users/{userId}/<resource>/{id}`

### `company` — companies whose sites are scraped and used as job filters

Purpose: companies are both scrape sources and filter dimensions for jobs.

- Entity: `CompanyEntity` → table `companies`
- Fields include `name`, `listingUrl` (unique), `singlePageUrlTemplate`, `enabled`, `lastScrapedAt`
- `OneToMany` jobs, `OneToOne` parser template (`mappedBy = "company"`)

API (`/companies`):

- `GET /companies/list`
- `GET /companies/get/{id}`
- `POST /companies/create`
- `PUT /companies/update/{id}`
- `DELETE /companies/delete/{id}`

Startup seeding: `CompanySeeder` reads `classpath:seeds/companies/companies_seed.csv` and matching JSON parser templates under `seeds/companies/templates/`.

### `parser` — scrape strategy per company website

Purpose: parser configuration tells the worker how to scrape a given company site successfully.

- Entity: `ParserTemplateEntity` → table `parser_templates`
- `config` is JSONB mapped to `ParserTemplateType`
- `OneToOne` with `CompanyEntity` (`company_id` unique). One template per company.
- Filtering uses Spring Data JPA `Specification` (`ParserTemplateSpecification`)
- Create/reassign rejects a second template for the same company (`409 CONFLICT`)

API (`/parser`):

- `GET /parser/list` (optional filters: companyId, companyName)
- `GET /parser/get/{id}`
- `POST /parser/create`
- `PUT /parser/update/{id}`
- `DELETE /parser/delete/{id}`

`ParserTemplateType` is a nested JSON model (listing selectors, job details, pagination). JSON property names are snake_case.

### `jobs` — scraped / stored job postings

Purpose: persist jobs discovered from company listings and enrich them with descriptions.

- Entity: `JobEntity` → table `jobs`
- Fields: `title`, `url`, `description`, `ManyToOne` company
- Constants: scrape interval 2 days; description-fetch batch limit 3

API (`/jobs`):

- `GET /jobs/list`
- `POST /jobs/process/fetch/jobs` — for companies due to scrape, POST listing URL + parser config to worker `/jobs/scrape-list/`
- `POST /jobs/process/fetch/description` — enqueue RabbitMQ `request.job_details` for jobs with null description

Inbound from worker (via `RabbitListenerService` → `JobService`):

- `receive.new_job_listing` → create `JobEntity`
- `receive.job_details` → set job description

### `document` — document generation (resume / cover letter)

Purpose: generate application documents (currently resume PDFs) from a job URL or a job description.

This module currently has **no entity / repository / mapper**. It is an HTTP proxy to the worker:

- `POST /documents/generate/resume/from-url` → worker `/documents/generate/from_url/resume.pdf`
- `POST /documents/generate/resume/from-description` → worker `/documents/generate/from_description/resume.pdf`

Responses are raw `application/pdf` bytes, not `API*Response` envelopes.

---

## Shared API conventions

### Response envelopes (`common.response`)

Most JSON endpoints wrap payloads:

- `APIListResponse<T>` — `{ success, data: T[] }`
- `APIGetResponse<T>` — `{ success, data, message }`
- `APIPostResponse<T>` — `{ success, data }` (also used for some GET-by-id responses)
- `APIErrorResponse` — `{ success: false, message, errors? }`

Use the static factories: `APIListResponse.success(...)`, `APIPostResponse.success(...)`, `APIPostResponse.failed(...)`.

Exceptions:

- Auth login returns `ResponseEntity<AuthResponseDto>` directly.
- Document generation returns PDF bytes.
- User delete / profile deletes often return `204 No Content`.

### Errors

- Throw `ResourceNotFoundException` for missing records. `GlobalExceptionHandler` maps it to `404` + `APIErrorResponse`.
- Validation failures (`@Valid`) become `400` with field error map.
- Parser uniqueness uses `ResponseStatusException` (`409`).

### DTO naming

Prefer the target module's existing names:

- Create: `*CreateDto`
- Update: `*UpdateDto`
- Detail: `*GetDto` or `*ResponseDto`
- List item: `*ListItemDto` or `*ListItemResponse`

Controllers should use `@Valid` on write bodies where the module already does.

### REST path styles currently in use

Two styles exist. Match the module you are editing:

1. Action paths (company, parser): `/resource/list`, `/resource/get/{id}`, `/resource/create`, `/resource/update/{id}`, `/resource/delete/{id}`
2. Resource paths (user, profile submodules): `/users`, `/users/{id}`, nested `/users/{userId}/education`

Do not "fix" one style to match the other unless asked.

---

## Mapping (MapStruct)

- Mapper interfaces live in the module `mapper/` package.
- Always: `@Mapper(componentModel = "spring")`
- Typical methods: `toEntity(CreateDto)`, `to*Dto(Entity)`, `updateEntityFromDto(UpdateDto, @MappingTarget Entity)`
- Ignore generated / sensitive fields (`id`, timestamps, `password`, `otp`, `role`) as the existing mapper does.
- Implementations are generated at compile time. Do not hand-write `*MapperImpl`.

Lombok and MapStruct both run as annotation processors. Keep `lombok-mapstruct-binding` in `pom.xml`.

---

## Persistence

- PostgreSQL database `fleeting_jobs`
- Entities use identity IDs (`GenerationType.IDENTITY`)
- Timestamps typically use `@CreationTimestamp` / `@UpdateTimestamp`
- JSONB is used for parser config (`@JdbcTypeCode(SqlTypes.JSON)`)
- Schema is currently evolved by Hibernate (`spring.jpa.hibernate.ddl-auto=update`)
- Flyway starter is present; there is no `db/migration` directory yet. Do not assume Flyway is the source of truth until migrations are introduced.

### Current tables / relationships

```
users 1──* educations
users 1──* work_experiences
users 1──* skills
users 1──* certifications
users 1──* awards

companies 1──* jobs
companies 1──1 parser_templates

roles 1──* permissions
```

`users.role` is still the `USER`/`ADMIN` enum. Users are not yet foreign-keyed to `roles`.

---

## Worker communication details

### HTTP (`WorkerService` + `workerWebClient`)

`WorkerService` exposes `webClient`. Callers use it directly:

```
workerService.webClient.post()
    .uri(...)
    .contentType(MediaType.APPLICATION_JSON)
    .bodyValue(...)
    .retrieve()
    ...
    .block();
```

Known worker routes used by backend:

- `POST /jobs/scrape-list/` — body: company_id, listing_url, parser_template
- `POST /documents/generate/from_url/resume.pdf`
- `POST /documents/generate/from_description/resume.pdf`

### RabbitMQ (`RabbitConfig`)

Queue names (durable):

| Constant | Queue | Direction |
|---|---|---|
| `REQUEST_JOB_DETAILS_QUEUE` | `request.job_details` | Backend → Worker |
| `RECEIVE_JOB_DETAILS_QUEUE` | `receive.job_details` | Worker → Backend |
| `RECEIVE_NEW_JOB_LISTING` | `receive.new_job_listing` | Worker → Backend |

- Producer: `RabbitProducerService.requestJobDetails(RequestJobDetailsMessageDto)` (`id`, `url`)
- Listeners: `RabbitListenerService` delegates to `JobService`
- Message DTOs live in `common.services.rabbit.dto`
- JSON converter: `JacksonJsonMessageConverter`

Messages from worker for new listings use `company_id` (snake_case field). Preserve that contract.

---

## Security

- JWT secret and expiration from configuration
- Login compares the submitted secret against hashed `password`, then hashed `otp`, using `PasswordEncoder`
- OTP login does not issue a JWT. `JwtAuthenticationFilter` also refuses to authenticate a user who still has an OTP set
- `JwtAuthenticationFilter` skips `/auth/**` and `POST /users`, otherwise reads Bearer token, loads user by email, sets `SecurityContext`
- CORS allows the local Vite origins only
- `spring.main.allow-circular-references=true` remains in config from the previous AuthenticationManager cycle; login no longer uses `AuthenticationManager`

Do not weaken public-endpoint rules or JWT validation without an explicit request.

---

## Seeding

On application startup, `CommandLineRunner` beans seed data:

- `DatabaseSeeder` → `AuthSeeder.seedRoles()` from `src/main/resources/seeds/auth/roles.json`
- `DatabaseSeeder` → `PermissionSeeder.seedPermissions()` from `src/main/resources/seeds/auth/permissions.json` (roles must already exist)
- `DatabaseSeeder` → `AuthSeeder.seedUser("admin@test.com", "0000")` seeds SUPER_ADMIN if missing
- `DatabaseSeeder` → `AuthSeeder.seedUser(..., "SUBSCRIBER", ...)` seeds demo subscriber `abtahitajwar@gmail.com` if missing
- `DatabaseSeeder` → `ProfileSeeder.seedDemoProfile()` from `src/main/resources/seeds/profile/demo-profile.json` (subscriber must already exist)
- `SeedRunner` → `CompanySeeder` upserts companies + parser templates from CSV/JSON

Keep seed data under `src/main/resources/seeds/`. Idempotent skip-if-exists inserts are required.

---

## Coding conventions for agents

1. Constructor injection. Existing code mixes explicit constructors and `@RequiredArgsConstructor`; either is fine if consistent within the file.
2. Controllers stay thin: validate input, call service, wrap response.
3. Services own transactions (`@Transactional` where the module already uses it, especially writes).
4. Do not call other modules' controllers.
5. Do not add business logic to mappers, repositories, or `common`.
6. Prefer existing exception types over new ones.
7. Match formatting and naming of the surrounding module.
8. Do not refactor unrelated code, rename public API paths, or change worker/Rabbit contracts unless asked.
9. After the change, update `agent-context` as described at the top of this file.

---

## Current architectural snapshot (as of 2026-09-21)

Seeded admin first-login uses OTP (`users.otp`) and `POST /auth/set-password` before a JWT is issued.

RBAC catalog tables (`roles`, `permissions`) are seeded from `seeds/auth/roles.json` and `seeds/auth/permissions.json`. Endpoint checks against those tables are not wired yet.

Implemented business modules: `auth`, `user`, `profile` (5 submodules), `company`, `parser`, `jobs`, `document`.

Not yet present:

- Cover-letter generation endpoints (document module currently does resume PDF only)
- A parent `ProfileEntity` / `/profiles` resource
- Role-based authorization on endpoints (tables exist; not enforced on requests yet)
- Flyway migration files
- Pagination on list endpoints (lists currently return full collections)

Known coupling to preserve until a dedicated refactor is requested:

- `JobService` depends on `CompanyRepository` and worker/Rabbit helpers
- `ParserTemplateService` depends on `CompanyRepository`
- Profile submodule services depend on `UserRepository`
- `AuthService` depends on `UserRepository` and `PasswordEncoder`
