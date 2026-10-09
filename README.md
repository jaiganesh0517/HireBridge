# HireBridge — Backend

A full-stack placement/internship portal connecting students and recruiters, built as a portfolio project while preparing for campus placements.

**Live demo:** https://hire-bridge-frontend.vercel.app
**Frontend repo:** https://github.com/jaiganesh0517/HireBridge-frontend

## What it does

- Students browse and search jobs, apply, and track their application status.
- Eligibility is checked before an application is saved: job status, deadline, duplicate application, minimum CGPA, and eligible branch.
- Recruiters post jobs, set eligible branches, review applicants (including contact email), and update status (shortlisted, selected, rejected).
- Users can view and edit their own profile.
- Expired job postings are closed automatically by a scheduled task.
- Role-based access is enforced end to end with JWT authentication.

## Tech stack

- **Java 21 / Spring Boot** — REST API
- **Spring Security + JWT** — stateless authentication, role-based authorization
- **Spring Data JPA / Hibernate** — persistence
- **MySQL** — database
- **JUnit 5 + Mockito** — unit tests
- **Maven** — build tool

## Key design decisions

- `StudentProfile` and `RecruiterProfile` share their primary key with `Users` via `@OneToOne @MapsId`.
- Passwords are hashed with BCrypt and must be at least 6 characters including a special character, validated on the server. Login returns a unified error message to prevent user enumeration.
- A unique constraint on `(studentId, jobId)` in `Application` prevents duplicate applications.
- Custom exceptions (`ResourceNotFoundException`, `DuplicateResourceException`, `UnauthorizedException`, `BusinessRuleException`) are mapped to proper HTTP status codes via a `@RestControllerAdvice` global handler.
- Identity is always derived from the JWT on the server side, never trusted from client-supplied IDs.
- Recruiters can only view and update applicants for jobs they posted (ownership checked in the service layer).
- Job lists are paginated (`page`, `size`; default 9, max 50, newest first) and returned in a `PageResponse<T>` wrapper.
- A scheduled task runs hourly and closes OPEN jobs whose deadline has passed, using a single bulk `UPDATE`. `applyToJob` also rejects expired jobs directly, so applications are blocked between scheduler runs.

## API overview

| Method | Path | Access | Purpose |
|---|---|---|---|
| POST | `/api/auth/register` | Public | Register user |
| POST | `/api/auth/login` | Public | Login, returns JWT |
| GET | `/api/jobs?page=&size=` | Public | List jobs (paginated, newest first) |
| GET | `/api/jobs/search?title=&skill=&page=&size=` | Public | Search jobs by title/skill (paginated) |
| GET | `/api/myJobs` | Recruiter | List jobs posted by the logged-in recruiter |
| POST | `/api/jobs` | Recruiter | Create a job posting |
| POST | `/api/jobs/{jobId}/branches` | Recruiter | Set eligible branches |
| GET | `/api/application/{jobId}/applicants` | Recruiter | View applicants for own job |
| PATCH | `/api/application/{applicationId}/status/{status}` | Recruiter | Update an applicant's status |
| POST | `/api/students/profile` | Student | Create student profile |
| GET | `/api/students/profile` | Student | View own profile |
| PUT | `/api/students/profile` | Student | Edit own profile |
| POST | `/api/recruiter/profile` | Recruiter | Create recruiter profile |
| GET | `/api/recruiter/profile` | Recruiter | View own profile |
| PUT | `/api/recruiter/profile` | Recruiter | Edit own profile |
| POST | `/api/application/{jobId}/apply` | Student | Apply to a job |
| GET | `/api/application/myApplication` | Student | View own applications |

## Testing

Unit tests use JUnit 5 and Mockito with mocked repositories, so no database is needed.

- `applyToJob`: job not found, closed job, deadline passed, duplicate application, missing profile, CGPA too low, branch not eligible, happy path.
- `updateApplicationStatus`: application not found, job not found, recruiter who doesn't own the job, happy path.


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

- [x] Unit tests (JUnit, Mockito)
- [x] Pagination on job listings
- [x] Scheduled job to auto-close postings past their deadline
- [ ] GitHub Actions CI running tests
- [ ] Dockerfile and docker-compose
- [ ] Phone number and resume link on student profiles
- [ ] Email notification on status change
- [ ] Admin role endpoints
