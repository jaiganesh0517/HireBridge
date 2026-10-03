# HireBridge — Backend

A full-stack placement/internship portal connecting students and recruiters, built as a portfolio project while preparing for campus placements.

**Live demo:** https://hire-bridge-frontend.vercel.app
**Frontend repo:** https://github.com/jaiganesh0517/HireBridge-frontend

## What it does

- Students browse and search open roles, apply, and track their application status.
- Recruiters post jobs, set eligible branches, review applicants, and update their status (shortlisted, selected, rejected).
- Role-based access is enforced end to end with JWT authentication.

## Tech stack

- **Java 21 / Spring Boot** — REST API
- **Spring Security + JWT** — stateless authentication, role-based authorization
- **Spring Data JPA / Hibernate** — persistence
- **MySQL** — database
- **Maven** — build tool

## Key design decisions

- `StudentProfile` and `RecruiterProfile` share their primary key with `Users` via `@OneToOne @MapsId`.
- Passwords are hashed with BCrypt; login returns a unified error message to prevent user enumeration.
- A unique constraint on `(studentId, jobId)` in `Application` prevents duplicate applications.
- Custom exceptions (`ResourceNotFoundException`, `DuplicateResourceException`, `UnauthorizedException`, `BusinessRuleException`) are mapped to proper HTTP status codes via a `@RestControllerAdvice` global handler.
- Identity is always derived from the JWT on the server side, never trusted from client-supplied IDs.

## API overview

| Method | Path | Access | Purpose |
|---|---|---|---|
| POST | `/api/auth/register` | Public | Register user |
| POST | `/api/auth/login` | Public | Login, returns JWT |
| GET | `/api/jobs` | Public | List open jobs |
| GET | `/api/jobs/search` | Public | Search jobs by title/skill |
| POST | `/api/jobs` | Recruiter | Create a job posting |
| POST | `/api/jobs/{jobId}/branches` | Recruiter | Set eligible branches |
| GET | `/api/jobs/myJobs` | Recruiter | List jobs posted by the logged-in recruiter |
| GET | `/api/application/{jobId}/applicants` | Recruiter | View applicants for a job |
| PATCH | `/api/application/{applicationId}/status/{status}` | Recruiter | Update an applicant's status |
| POST | `/api/students/profile` | Student | Create student profile |
| POST | `/api/recruiter/profile` | Recruiter | Create recruiter profile |
| POST | `/api/application/{jobId}/apply` | Student | Apply to a job |
| GET | `/api/application/myApplication` | Student | View own applications |

## Running locally

1. Create a MySQL database and a local `application.properties` (gitignored) with:
```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/hirebridge
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   jwt.secret=a_long_random_secret_at_least_32_characters
```
2. Run `./mvnw spring-boot:run` (or run `HireBridgeApplication` from your IDE).
3. The API is available at `http://localhost:8080`.

## Deployment

Deployed on [Railway](https://railway.app), with environment-based configuration (`SPRING_DATASOURCE_URL`, `JWT_SECRET`, etc.) instead of a committed properties file.

## Roadmap

- [ ] Unit/integration tests (JUnit, Mockito)
- [ ] Pagination on job listings
- [ ] Scheduled job to auto-close postings past their deadline
- [ ] Admin role endpoints
